export const timeBehavior = Behavior({
  methods: {
    /**
     * 计算传入的时间与当前时间相差多少分钟
     * @param {*} time 要计算的时间
     */
    calculateTimeDifference(time) {
      // 将传入的时间字符串转换为 Date 对象
      const inputTime = new Date(time.replace(/-/g, '/')); // 确保兼容 iOS
      // 获取当前时间
      const currentTime = new Date();

      // 计算时间差，单位为毫秒
      const timeDifference = currentTime - inputTime;

      // 将时间差转换为分钟数
      const differenceInMinutes = Math.floor(timeDifference / 1000 / 60);

      return differenceInMinutes;
    },

    /**
     * 计算传入时间后几分钟的时间点
     * @param {*} time 要计算的时间
     * @param {*} min 要计算的时间后几分钟的时间点
     */
    calculateFutureTime(time, min) {

      // 将接受时间转为 Date 对象
      let acceptDate = new Date(time.replace(/-/g, '/')); // iOS 不支持 YYYY-MM-DD 格式，需替换为 YYYY/MM/DD

      // 加上 min 分钟
      acceptDate.setMinutes(acceptDate.getMinutes() + min);

      // 只保留时分，格式化为 HH:MM
      return this.formatTimeToHM(acceptDate);
    },

    /**
     * 计算传入时间前几分钟的时间点
     * @param {*} time 要计算的时间
     * @param {*} min 要计算的时间前几分钟的时间点
     */
    calculatePastTime(time, min) {

      // 将接受时间转为 Date 对象
      let acceptDate = new Date(time.replace(/-/g, '/')); // iOS 不支持 YYYY-MM-DD 格式，需替换为 YYYY/MM/DD

      // 减去 min 分钟
      acceptDate.setMinutes(acceptDate.getMinutes() - min);

      // 只保留时分，格式化为 HH:MM
      return this.formatTimeToHM(acceptDate);
    },

    /**
     *  格式化时间为 HH:MM
     * @param {*} date 需要格式的时间
     */
    formatTimeToHM(date) {
      let hours = date.getHours();
      let minutes = date.getMinutes();

      // 补零
      if (hours < 10) hours = '0' + hours;
      if (minutes < 10) minutes = '0' + minutes;

      return hours + ':' + minutes;
    },
  }
});