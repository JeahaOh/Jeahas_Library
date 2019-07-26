/**
 * 2019.07.26
 * Date & Time을 간단한 문자열로 반환하기 위한 객체.
 */
const TimeStamp = {
  ZeroLeader: function (n, d) {
    var z = '';
    n = n.toString();
    if (n.length < d) {
      for (i = 0; i < d - n.length; i++)
        z += '0';
    }
    return z + n;
  },
  // 날짜와 시간을 "20190726_114854" 형식으로 반환.
  getDateTime: function () {
    var d = new Date();
    var s = this.ZeroLeader(d.getFullYear(), 4)
      + this.ZeroLeader(d.getMonth() + 1, 2)
      + this.ZeroLeader(d.getDate(), 2) + "_"
      + this.ZeroLeader(d.getHours(), 2)
      + this.ZeroLeader(d.getMinutes(), 2)
      + this.ZeroLeader(d.getSeconds(), 2);
    return s;
  },
  // 날짜를 "20190726" 형식으로 반환.
  getDate: function () {
    var d = new Date();
    var s = this.ZeroLeader(d.getFullYear(), 4)
      + this.ZeroLeader(d.getMonth() + 1, 2)
      + this.ZeroLeader(d.getDate(), 2)
    return s;
  },
  // 시간을 "114902" 형식으로 반환.
  getTime: function () {
    var d = new Date();
    var s = this.ZeroLeader(d.getHours(), 2)
      + this.ZeroLeader(d.getMinutes(), 2)
      + this.ZeroLeader(d.getSeconds(), 2);
    return s;
  }
}