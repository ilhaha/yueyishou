Page({
  data: {
    categoryList: [{
        name: "可回收物",
        icon: "../../assets/images/category-knowledge/khsw/khsw.webp",
        desc: "适宜回收、可循环利用的废品，如：纸张、塑料、易拉罐、织物、轮胎等。",
        examples: [{
            name: "纸箱",
            icon: "../../assets/images/category-knowledge/khsw/paper_box.png"
          },
          {
            name: "塑料瓶",
            icon: "../../assets/images/category-knowledge/khsw/plastic_bottle.jpeg"
          },
          {
            name: "易拉罐",
            icon: "../../assets/images/category-knowledge/khsw/metal_can.jpeg"
          },
          {
            name: "织物",
            icon: "../../assets/images/category-knowledge/khsw/fabric.jpeg"
          }
        ],
        handling: "将这些废品投入可回收物垃圾桶中，经过分类、清洗、压缩等工序处理后送往专业回收机构进行回收利用。",
        precautions: "注意：沾水、油污的纸张不属于可回收物，请将其投放至其他垃圾桶。",
        showDetails: false // 控制详细信息的显示
      },
      {
        name: "厨余垃圾",
        icon: "../../assets/images/category-knowledge/cylj/cylj.jpeg",
        desc: "容易腐烂的有机物，如：剩菜剩饭、果皮果核、茶叶渣等。",
        examples: [{
            name: "剩菜剩饭",
            icon: "../../assets/images/category-knowledge/cylj/leftover.jpeg"
          },
          {
            name: "果皮",
            icon: "../../assets/images/category-knowledge/cylj/fruit_peel.jpeg"
          },
          {
            name: "菜叶",
            icon: "../../assets/images/category-knowledge/cylj/vegetable_leaf.jpeg"
          }
        ],
        handling: "将厨余垃圾投入专用的厨余垃圾桶中，尽量沥干水分，避免使用塑料袋。厨余垃圾可以经过堆肥处理转化为有机肥料。",
        precautions: "注意：厨余垃圾不应混入塑料袋或其他杂质，否则会影响堆肥质量。",
        showDetails: false
      },
      {
        name: "有害垃圾",
        icon: "../../assets/images/category-knowledge/yhlj/yhlj.png",
        desc: "对环境和人体健康有害的废弃物，如：电池、灯管、过期药品等。",
        examples: [{
            name: "废旧电池",
            icon: "../../assets/images/category-knowledge/yhlj/battery.jpeg"
          },
          {
            name: "废旧灯管",
            icon: "../../assets/images/category-knowledge/yhlj/lamp.jpeg"
          },
          {
            name: "过期药品",
            icon: "../../assets/images/category-knowledge/yhlj/medicine.jpeg"
          }
        ],
        handling: "将有害垃圾投入专门的有害垃圾桶中，由专业部门进行无害化处理，避免有毒有害物质泄露污染环境。",
        precautions: "注意：避免将有害垃圾破损泄露，请密封包装后投放。",
        showDetails: false
      },
      {
        name: "其他垃圾",
        icon: "../../assets/images/category-knowledge/qtlj/qtlj.png",
        desc: "难以回收利用，不能自然降解的垃圾，如：污染纸张、烟蒂等。",
        examples: [{
            name: "烟蒂",
            icon: "../../assets/images/category-knowledge/qtlj/broken_ceramic.jpeg"
          },
          {
            name: "破碎陶瓷",
            icon: "../../assets/images/category-knowledge/qtlj/cigarette_butt.jpeg"
          }
        ],
        handling: "将这些废品投入其他垃圾桶中，一般会被送往垃圾焚烧厂或填埋场进行处理。",
        precautions: "注意：请将垃圾尽量压实，减少体积，方便后续处理。",
        showDetails: false
      }
    ]
  },

  // 切换详情显示状态
  toggleDetails(event) {
    const index = event.currentTarget.dataset.index;
    let categoryList = this.data.categoryList;
    categoryList[index].showDetails = !categoryList[index].showDetails;
    this.setData({
      categoryList: categoryList
    });
  }
});