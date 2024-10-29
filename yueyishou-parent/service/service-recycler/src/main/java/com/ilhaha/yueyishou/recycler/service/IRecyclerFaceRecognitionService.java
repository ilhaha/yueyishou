package com.ilhaha.yueyishou.recycler.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerFaceRecognition;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerPersonalization;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerFaceModelForm;

public interface IRecyclerFaceRecognitionService extends IService<RecyclerFaceRecognition> {


    /**
     * 判断回收员是否已进行了每日人脸签到
     * @param recyclerId
     * @return
     */
    Boolean isSign(Long recyclerId);

}
