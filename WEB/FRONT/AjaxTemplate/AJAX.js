$.ajaxSetup({
  error: function( xhr, status, err ) {
    console.warn( 'JEAHA.DEV' );
    switch ( xhr.status ) {
      case ( 0 ) :
        console.error( 'Not Connected Or Verify Network Or Interupted By Function.\n대충 인터넷이 끊겼거나 다른 요청에 의해서 중단 되었단 뜻.' ); break;
      case ( 200 ) : 
        console.error( 'OK\n200 응답받은 데이터 타입이 요청한 타입과 맞지 않음.\n혹은 세션 만료, 로그인 필요함.' ); break;
      case ( 202 ) : 
        console.error( 'Acepted But Not Progressed.' ); break;
      case ( 204 ) : 
        console.error( 'No Content. (Void)' ); break;
      case ( 400 ) :
        console.error( 'Request Content Was Invalid.\n컨트롤러가 받는 데이터 타입과 맞지 않음.' ); break;
      case ( 401 ) :
        console.error( 'Unauthorized.\n인증 필요. (실제로는 Unauthenticated 의 의미)' ); break;
      case ( 403 ) :
        console.error( 'Forbidden.\n권한 부족. (실제로는 Unauthorized 의 의미)' ); break;
      case ( 405 ) :
        console.error( 'Method Not Allowed\nGET\\POST가 일치하지 않음.' ); break;
      case ( 406 ) :
        console.error( 'Not Acceptable\n헤더 또는 내용이 서버에서 받아들일 수 없는 요청.' ); break;
      case ( 408 ) :
        console.error( 'Request Timeout\n요청시간 초과.' ); break;
      case ( 413 ) :
        console.error( 'Requested Entity Too Large.\n내용이 너무 큼 (첨부파일).' ); break;
      case ( 415 ) :
        console.error( 'Unsupported Media Type.\n요청한 데이터 타입을 서버에서 만들줄 모름.' ); break;
      case ( 429 ) : 
        console.error( 'Too Many Requests.\n요청 횟수 제한' ); break;
      case ( 500 ) :
        alert( '서버 트래픽 과부하.\n요청이 너무 많음.' );
        console.error( 'Internal Server Error.' );  break;
      case ( 0 ) : 
        console.error( '' ); break;
    }
    console.warn( 'xhr' );
    console.log( xhr );
    console.warn( 'status' );
    console.log( status );
    console.warn( 'err' );
    console.log( err );
  }
});

$.ajax({
  type : "POST",
  url : "your url",
  dataType : "json",
  data : "your Data",
  traditional : true,    // or false, your choice
  async : false,    // or true, your choice
  beforeSend : function(xhr, opts) {
    // when validation is false
    if (false) {
      xhr.abort();
    }
  }
})
.done(function() {
  // success code
})
.fail(function() {
  // error code
});