/**
 * 2019.07.26
 * Date & Time을 간단한 문자열로 반환하기 위한 객체.
 * GMT Time 인듯?
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
  },
  getGMTTimeZone: function () {
    return new Date().toString().match(/([A-Z]+[\+-][0-9]+)/)[1];
  },


  // UTC 날짜와 시간을 "20190726_114854" 형식으로 반환.
  getUTCDateTime: function () {
    var d = new Date();
    var s = this.ZeroLeader(d.getUTCFullYear(), 4)
      + this.ZeroLeader(d.getUTCMonth() + 1, 2)
      + this.ZeroLeader(d.getUTCDate(), 2) + "_"
      + this.ZeroLeader(d.getUTCHours(), 2)
      + this.ZeroLeader(d.getUTCMinutes(), 2)
      + this.ZeroLeader(d.getUTCSeconds(), 2);
    return s;
  },
  // UTC 날짜를 "20190726" 형식으로 반환.
  getUTCDate: function () {
    var d = new Date();
    var s = this.ZeroLeader(d.getUTCFullYear(), 4)
      + this.ZeroLeader(d.getUTCMonth() + 1, 2)
      + this.ZeroLeader(d.getUTCDate(), 2)
    return s;
  },
  // UTC 시간을 "114902" 형식으로 반환.
  getUTCTime: function () {
    var d = new Date();
    var s = this.ZeroLeader(d.getUTCHours(), 2)
      + this.ZeroLeader(d.getUTCMinutes(), 2)
      + this.ZeroLeader(d.getUTCSeconds(), 2);
    return s;
  },
  getMiliTime : function() {
    return Math.floor(new Date().getTime())
  },
  //  UNIXTIME을 리턴
  //  Unix time : 1970 -> 지금까지의 초 10자리의 숫자임.
  getUnixTime : function() {
    return Math.floor(new Date().getTime() / 1000)
  },
  /* 
   * millisecond 값을 인자로 받아서 다음 코드 실행 시간을 늦춘다.
   * AJAX 요청시 서버 부하를 막기 위해 사용한다.
   */
  timeDelay = function( gap ){
    var then, now; 
    then = new Date().getTime(); 
    now = then; 
    while( (now - then ) < gap ) { 
      // 현재시간을 읽어 함수를 불러들인 시간과의 차를 이용하여 처리 
      now = new Date().getTime();
    }
    console.log( now - then );
  } 
}