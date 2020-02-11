// (function (){
$(window).onload = function() {


  // console.clear();
  console.group('on load');
  
  let isGetable = localStorage.getItem('img') !== null && localStorage.getItem('img').length > 0;
  console.log( 'isGetable : ' + isGetable );

  if( isGetable ) {
    let img = localStorage.getItem('img')
    // console.log( img );
    let imgTag = document.createElement('img');
    imgTag.src = img;
    // imgTag = $(imgTag);

    try {
      console.log( imgTag );
      //  이미지 띄울 코드
    } catch( err ) {
      console.log( err );
    }
    
  }

    console.groupEnd('on load');
  // console.clear();
// })();
}
//  onload

const IMGLYR_ABLE_TYPES = ['image/png', 'image/jpeg', 'image/bmp'];
const IMGLYR_LIMIT_BYTES = 1000000;

const imgLyrFileUpload = function( target ) {
  // console.log( target );

  if( target.files.length > 1 ) return alert('파일은 1개만 올릴 수 있습니다.');
  let file = target.files[0];
  if( !imglyrFileValidator( file ) ) return;

  let imgToBase64 = function( img ) {
    let reader = new FileReader();
    reader.readAsDataURL( img );
    reader.onloadend = function() {
      // console.log( 'RESULT : ', reader.result );
      // return result = reader.result;
      localStorage.setItem("img", reader.result);
    }
  }
  imgToBase64( file );
}
/**
 * 파일의 유효성 검사를 한다.
 * true false를 리턴한다.
 * jpg, png
 * 10mb
 * @param {*} file 
 */
const imglyrFileValidator = function( file ) {
  // console.log( file );
  let type = file.type;
  let bytes = file.size;
  console.log( 'type : ' + type + '\nsize : ' + bytes + 'bytes');
  
  let isAbleType = (IMGLYR_ABLE_TYPES.indexOf(type.toLowerCase()) >= 0);
  if( !isAbleType ) {
    
    let text = '허용 되지 않는 형식의 파일입니다.';
    text += '\n사용 가능 파일 형식 : ' + IMGLYR_ABLE_TYPES;
    text += '\n파일 형식 : ' + (type.length === 0 ? "알수없음" : type);

    alert( text ); 
    return false;
  }

  let isAbleSize = (bytes > 0 && bytes < IMGLYR_LIMIT_BYTES);
  if( !isAbleSize ){
    let text = '허용 되지 않는 크기의 파일입니다.';
    text += '\n사용 가능 파일 크기 : ' + IMGLYR_LIMIT_BYTES + ' bytes.';
    text += '\n파일 크기 : ' + bytes + ' bytes.'
    alert( text );
  }
  
  console.log( "isAbleType : " + isAbleType + "\nisAbleSize : " + isAbleSize );
  return isAbleType && isAbleSize;
}


