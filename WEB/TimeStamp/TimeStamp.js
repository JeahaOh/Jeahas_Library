/**
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <jeaha1217@gmail.com> wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy me a beer.
 * @description : Date & Time을 간단하게 사용하기 위한 객체.
 * @author  : Jeaha Oh (jeaha.dev, jeaha1217@gmail.com)
 * @since   : 2020.07.26
 * ----------------------------------------------------------------------------
 */
const TimeStamp = {
  zerofill: function (n, d) {
    let z = '';
    n = n.toString();
    if (n.length < d) {
      z = '0'.repeat(d - n.length);
    }
    return z + n;
  },
  datetime: function( seed ) {
    try {
      let root = new Date( seed );
      if( root == 'Invalid Date')
        return new Date();
      else return root;
    } catch ( e ) {
      return new Date();
    }
  },
  // 날짜와 시간을 "20190726_114854" 형식으로 반환.
  getDateTime: function () {
    var d = new Date();
    var s = this.zerofill(d.getFullYear(), 4)
      + this.zerofill(d.getMonth() + 1, 2)
      + this.zerofill(d.getDate(), 2) + "_"
      + this.zerofill(d.getHours(), 2)
      + this.zerofill(d.getMinutes(), 2)
      + this.zerofill(d.getSeconds(), 2);
    return s;
  },
  // 날짜를 "20190726" 형식으로 반환.
  getDate: function () {
    var d = new Date();
    var s = this.zerofill(d.getFullYear(), 4)
      + this.zerofill(d.getMonth() + 1, 2)
      + this.zerofill(d.getDate(), 2)
    return s;
  },
  // 시간을 "114902" 형식으로 반환.
  getTime: function () {
    var d = new Date();
    var s = this.zerofill(d.getHours(), 2)
      + this.zerofill(d.getMinutes(), 2)
      + this.zerofill(d.getSeconds(), 2);
    return s;
  },
  get: function( ... args ) {
    console.log( args );
  },



  getGMTTimeZone: function () {
    return new Date().toString().match(/([A-Z]+[\+-][0-9]+)/)[1];
  },


  // UTC 날짜와 시간을 "20190726_114854" 형식으로 반환.
  getUTCDateTime: function () {
    var d = new Date();
    var s = this.zerofill(d.getUTCFullYear(), 4)
      + this.zerofill(d.getUTCMonth() + 1, 2)
      + this.zerofill(d.getUTCDate(), 2) + "_"
      + this.zerofill(d.getUTCHours(), 2)
      + this.zerofill(d.getUTCMinutes(), 2)
      + this.zerofill(d.getUTCSeconds(), 2);
    return s;
  },
  // UTC 날짜를 "20190726" 형식으로 반환.
  getUTCDate: function () {
    var d = new Date();
    var s = this.zerofill(d.getUTCFullYear(), 4)
      + this.zerofill(d.getUTCMonth() + 1, 2)
      + this.zerofill(d.getUTCDate(), 2)
    return s;
  },
  // UTC 시간을 "114902" 형식으로 반환.
  getUTCTime: function () {
    var d = new Date();
    var s = this.zerofill(d.getUTCHours(), 2)
      + this.zerofill(d.getUTCMinutes(), 2)
      + this.zerofill(d.getUTCSeconds(), 2);
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
  timeDelay : function( gap ){
    var then, now; 
    then = new Date().getTime(); 
    now = then; 
    while( (now - then ) < gap ) { 
      // 현재시간을 읽어 함수를 불러들인 시간과의 차를 이용하여 처리 
      now = new Date().getTime();
    }
    console.log( now - then );
  },

  /**
   * 두개의 밀리초를 받아 두 시간의 차이를 리턴한다.
   * raw가 true라면 milliseconds로,
   *      false라면 문자열로 리턴한다.
   * @param {*} prv 
   * @param {time} now 
   * @param {boolean} raw
   */
  getTimeGap : function ( prv, now, raw ) { 
    if( typeof now == undefined || now == null ) now = this.getMiliTime();

    let timming = now - prv;
    if( isNaN(Number(timming)) ) return "Incomputable Time";
    if( raw ) return timming;
    if (timming < 1000) return timming + " ms";
    else return (timming / 1000) + " secs";
  }
}