import {
  reqCosUpload
} from '../../../api/customer/cos';
import {
  idCardOcr
} from '../../../api/customer/ocr';
import {
  reqCreateFaceModel,
  reqDeleteFaceModel
} from '../../../api/customer/facemodel'
import {
  reqRecyclerAuth,
  reqGetAuthImages
} from '../../../api/customer/customer'
import {
  toast
} from '../../../utils/extendApi'
Component({
  data: {
    tip: '',
    authStatus: 0,
    idFrontList: [], // 身份证正面文件列表
    idBackList: [], // 身份证反面文件列表
    idcardHandList: [], // 手持身份证文件列表
    fullFaceList: [], // 正脸照文件列表
    faceRecognitionList: [], // 人脸识别文件列表
    recyclerApplyForm: { // 保存认证信息
      idcardFrontUrl: '',
      idcardBackUrl: '',
      idcardHandUrl: '',
      fullFaceUrl: '',
      faceRecognitionUrl: '',
      faceModelId: '',
      name: '',
      gender: '',
      birthday: '',
      idcardNo: '',
      idcardAddress: '',
      idcardExpire: ''
    }
  },
  lifetimes: {
    attached() {
      this.getAuthImages()
    },
  },
  methods: {
    // 查询提交
    reSubmit() {
      this.deleteIdFront();
      this.deleteIdBack();
      this.deleteIdcardHand();
      this.deleteFaceRecognition();
      this.deleteFullFace();
      this.setData({
        authStatus: 0,
        tip: '请按照要求上传身份证照片及人脸识别信息，以完成认证。'
      })
    },
    // 获取认证信息
    async getAuthImages() {
      const res = await reqGetAuthImages();
      const data = res.data
      if (data) {
        this.setData({
          authStatus: data.authStatus ? data.authStatus : 0,
          tip: data.authStatus == 1 ? '正在审核，请耐心等待。' : data.authStatus == -1 ? "未通过审核，请重新上传相关信息。" : "请按照要求上传身份证照片及人脸识别信息，以完成认证。",
          idFrontList: data.idcardFrontUrl ? [{
            url: data.idcardFrontUrl
          }] : [],
          idBackList: data.idcardBackUrl ? [{
            url: data.idcardBackUrl
          }] : [],
          idcardHandList: data.idcardHandUrl ? [{
            url: data.idcardHandUrl
          }] : [],
          fullFaceList: data.fullFaceUrl ? [{
            url: data.fullFaceUrl
          }] : [],
          faceRecognitionList: data.faceRecognitionUrl ? [{
            url: data.faceRecognitionUrl
          }] : [],
        });
        if (this.data.authStatus == 2) {
          this.triggerEvent('gotoRecyclerIndex', this.data.authStatus);
        }
      }

    },
    // 删除人脸模型
    async deleteFaceRecognition(event) {
      this.setData({
        faceRecognitionList: [],
        'recyclerApplyForm.faceRecognitionUrl': '',
        'recyclerApplyForm.faceModelId': '',
      })
      const res = await reqDeleteFaceModel();
    },
    // 删除正脸照
    deleteFullFace(event) {
      this.setData({
        fullFaceList: [],
        'recyclerApplyForm.fullFaceUrl': ''
      })
    },
    // 删除手持身份证照
    deleteIdcardHand(event) {
      this.setData({
        idcardHandList: [],
        'recyclerApplyForm.idcardHandUrl': ''
      })
    },
    // 删除反面身份证
    deleteIdBack(event) {
      this.setData({
        idBackList: [],
        'recyclerApplyForm.idcardBackUrl': '',
        'recyclerApplyForm.idcardExpire': '',
      })
    },
    // 删除正面身份证
    deleteIdFront(event) {
      this.setData({
        idFrontList: [],
        'recyclerApplyForm.name': '',
        'recyclerApplyForm.birthday': '',
        'recyclerApplyForm.gender': '',
        'recyclerApplyForm.idcardAddress': '',
        'recyclerApplyForm.idcardFrontUrl': '',
        'recyclerApplyForm.idcardNo': '',
      })
    },
    // 更新图片列表
    updateFileList(name, file) {
      const key = `${name}List`; // 动态生成对应的文件列表 key
      this.setData({
        [key]: [...this.data[key], file] // 更新相应的文件列表
      });
    },
    // 更新表单数据
    updateRecyclerApplyForm(data) {
      const updatedForm = {
        ...this.data.recyclerApplyForm
      }; // 复制现有的 recyclerApplyForm

      // 遍历 data 中的属性，并检查是否为空或 null
      Object.keys(data).forEach(key => {
        if (data[key] !== null && data[key] !== '') {
          updatedForm[key] = data[key]; // 仅在值不为 null 或空字符串时赋值
        }
      });

      // 使用 setData 更新 recyclerApplyForm
      this.setData({
        recyclerApplyForm: updatedForm
      });
    },

    // 删除图片时更新列表
    removeFileList(name, index) {
      const key = `${name}List`;
      const fileList = [...this.data[key]];
      fileList.splice(index, 1); // 删除对应索引的文件
      this.setData({
        [key]: fileList
      });
    },
    // 处理身份证上传后的回调
    async uploadIDCard(event) {
      const res = await idCardOcr(event);
      // 识别失败
      if (res.code != 200) {
        wx.showToast({
          title: res.message,
          icon: 'error',
          duration: 1000
        });
        return;
      }
      // 识别成功
      const data = res.data;
      const file = {
        url: data.idcardFrontUrl == null ? data.idcardBackUrl : data.idcardFrontUrl
      }; // 构造新的文件对象
      const {
        name
      } = event.detail;
      // 更新文件列表
      this.updateFileList(name, file);

      // 更新表单数据
      this.updateRecyclerApplyForm(data);
    },
    // 处理普通照片上传的回调
    async upload(event) {
      const res = await reqCosUpload(event, 'recycler');
      // 上传失败
      if (res.code != 200) {
        wx.showToast({
          title: res.message,
          icon: 'error',
          duration: 1000
        });
        return;
      }
      // 上传成功
      // 识别成功
      const data = res.data;
      const file = {
        url: data.url
      }; // 构造新的文件对象
      const {
        name
      } = event.detail;

      // 更新文件列表
      this.updateFileList(name, file);

      console.log(data);
      // 更新表单数据
      // 显式更新 recyclerApplyForm 中的手持身份证或正脸照字段
      if (name === 'idcardHand') {
        this.updateRecyclerApplyForm({
          idcardHandUrl: file.url
        });
      } else if (name === 'fullFace') {
        this.updateRecyclerApplyForm({
          fullFaceUrl: file.url
        });
      }
    },
    // 处理创建人脸模型的回调
    async uploadFaceModel(event) {
      let faceModelForm = {
        name: this.data.recyclerApplyForm.name,
        gender: this.data.recyclerApplyForm.gender
      }
      const res = await reqCreateFaceModel(event, faceModelForm);
      if (res.code != 200) {
        wx.showToast({
          title: res.message,
          icon: 'error',
          duration: 1000
        });
        return;
      }
      if (res.data.faceModelId == null) {
        toast({
          title: '人脸创建失败',
          icon: 'error'
        });
      }

      const data = res.data;
      if (data.faceRecognitionUrl == null) {
        return;
      }
      const file = {
        url: data.faceRecognitionUrl
      }; // 构造新的文件对象
      const {
        name
      } = event.detail;
      // 更新文件列表
      this.updateFileList(name, file);

      // 更新表单数据
      this.updateRecyclerApplyForm(data);
    },
    // 处理图片删除操作
    onDelete(event) {
      const {
        name,
        index
      } = event.detail; // 获取上传框的name和要删除的文件索引
      this.removeFileList(name, index); // 更新文件列表，删除指定图片
    },
    // 提交表单处理函数
    async onSubmit(event) {
      const {
        idFrontList,
        idBackList,
        idcardHandList,
        fullFaceList,
        faceRecognitionList
      } = this.data;

      // 检查是否有未上传的必需图片
      if ([idFrontList, idBackList, idcardHandList, fullFaceList, faceRecognitionList].some(list => list.length === 0)) {
        wx.showToast({
          title: '请上传所有必需的照片',
          icon: 'error',
          duration: 1000
        });
        return;
      }

      const res = await reqRecyclerAuth(this.data.recyclerApplyForm);
      if (res.data) {
        wx.showToast({
          title: '提交成功',
          icon: 'success',
          duration: 1000
        });
        this.setData({
          authStatus: 1,
          tip: '正在审核中，请耐心等待。'
        })
      }

    }
  }
});