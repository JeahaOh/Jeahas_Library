$.ajaxSetup({
  error: function( xhr, status, err ) {
    switch ( xhr.status ) {
      case ( 0 ) :
        console.error( 'Not Connected Or Verify Network Or Interupted By Function.' ); break;
      case ( 200 ) : 
        console.error( 'OK' ); break;
      case ( 202 ) : 
        console.error( 'Acepted But Not Progressed.' ); break;
      case ( 204 ) : 
        console.error( 'No Content. (Void)' ); break;
      case ( 400 ) :
        console.error( 'Request Content Was Invalid.' ); break;
      case ( 401 ) :
        console.error( 'Unauthorized 인증 필요. (실제로는 Unauthenticated 의 의미)' ); break;
      case ( 403 ) :
        console.error( 'Forbidden 권한 부족. (실제로는 Unauthorized 의 의미)' ); break;
      case ( 405 ) :
        console.error( 'Method Not Allowed 메소드가 일치하지 않음.' ); break;
      case ( 406 ) :
        console.error( 'Not Acceptable 헤더 또는 내용이 서버에서 받아들일 수 없는 요청.' ); break;
      case ( 408 ) :
        console.error( 'Request Timeout 요청시간 초과.' ); break;
      case ( 413 ) :
        console.error( 'Requested Entity Too Large 내용이 너무 큼 (첨부파일)' ); break;
      case ( 429 ) : 
        console.error( 'Too Many Requests 요청 횟수 제한' ); break;
      case ( 500 ) :
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