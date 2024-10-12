export const permissionBehavior = Behavior({

  methods: {
    userPermission(permissionName) {
      return new Promise((resolve) => {
        // 获取授权设置
        wx.getSetting({
          success: (res) => {
            const authStatus = res.authSetting[permissionName];

            // 如果从未请求过该权限
            if (authStatus === undefined) {
              wx.authorize({
                scope: permissionName,
                success: () => {
                  resolve(true); // 授权成功返回 true
                },
                fail: () => {
                  wx.showToast({
                    title: '授权被拒绝',
                    icon: 'none',
                    duration: 2000
                  });
                  resolve(false); // 授权被拒绝返回 false
                }
              });
            }
            // 如果用户拒绝了该权限
            else if (authStatus === false) {
              wx.showModal({
                title: "授权提示",
                content: "需要您的授权才能继续使用功能，请确认授权",
                success: (modalRes) => {
                  if (modalRes.confirm) {
                    wx.openSetting({
                      success: (settingRes) => {
                        if (settingRes.authSetting[permissionName]) {
                          resolve(true); // 重新授权成功返回 true
                        } else {
                          wx.showToast({
                            title: '授权被拒绝',
                            icon: 'none',
                            duration: 2000
                          });
                          resolve(false); // 授权被拒绝返回 false
                        }
                      },
                      fail: () => {
                        wx.showToast({
                          title: '打开设置页面失败',
                          icon: 'none',
                          duration: 2000
                        });
                        resolve(false); // 打开设置页面失败返回 false
                      }
                    });
                  } else {
                    wx.showToast({
                      title: '您拒绝了授权',
                      icon: 'none',
                      duration: 2000
                    });
                    resolve(false); // 用户取消授权返回 false
                  }
                }
              });
            }
            // 用户已经授权
            else {
              resolve(true); // 已经授权返回 true
            }
          },
          fail: () => {
            wx.showToast({
              title: '获取设置失败',
              icon: 'none',
              duration: 2000
            });
            resolve(false); // 获取设置失败返回 false
          }
        });
      });
    }
  }
})