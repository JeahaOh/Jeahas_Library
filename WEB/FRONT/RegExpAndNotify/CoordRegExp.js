/**
 * @JEAHA
 * 정규표현식 등의 유효성 검사를 위한 JS 모음.
 */

//	모든 html 테그 삭제 <style>,<script>.
const htmlTag = /[<][^>]*[>]/gi;

//	모든 특수문자.
const specialChar     = /[\{\}\[\]\/?.,;:|\)*~`!^\-+<>@\#$%&\\\=\(\'\"]/g;
const notAllowSpclCharNoti = "Special Character is not allowed."

//	공백, ., -를 제외한 특수문자 검출.
const someSpecialChar = /[\{\}\[\]\/?;:|\)*~`!^\-+<>@\#$%&\\\=\(\'\"]/g;
const someSpecialCharNoti = "Allowed Special Characters are , And _";

//	입력 받은 문자열의 HTML 태그를 삭제한다.
const replaceHTML = function( str ) {
	return str.replace( htmlTag, '' ).trim();
};

//	입력 받은 문자열의 특수문자를 제거한다. 2번째 인자가 있을 경우 그 문자열로 대체한다.
const rplcSpclChar = function( str, alt ) {
  if( str.match(specialChar) != null && str.match(specialChar).length > 0 ){
    $.notify(notAllowSpclCharNoti, {position: "bottom right"});
  } 
  if( alt != null && typeof str === 'string' ) return str.replace( specialChar, alt );
  else return str.replace( specialChar, '' );
}

//	입력 받은 문자열에서 일부 특수문자를 제거한다. 2번째 인자가 있을 경우 그 문자열로 대체한다.
const rplcSpclCharLsn = function( str, alt ) {
  if( str.match(someSpecialChar) != null
      && str.match(someSpecialChar).length > 0 ) {
    // console.log( str.match(someSpecialChar), str.match(someSpecialChar).length );
    $.notify(someSpecialCharNoti, {position: "bottom right"});
    if( alt != null && typeof str === 'string' ) return str.replace( someSpecialChar, alt );
    else return str.replace( someSpecialChar, '' );
  }
  return str;
}

/**
 * @Jeaha
 * 좌표를 확인,
 * 1. /^((\d{1,3}(\.)\d{2,12}))/가 맞는지 확인한다.
 * 2. 범위 안에 있는지 확인한다.
 *   +- 180.0000
 *   +- 90.0000
 * 3. 0이 반복 되었는지 확인한다.
*/
const isLat = function( coord ) {
  //  포멧을 확인한다.
  if( !/^((\d{1,3})(\.)(\d{2,12}))/.test(coord) ) {
    $.notify("This coord is not match on EPSG:4326.", {position: "bottom right"});
    return false;
  }
  //  범위를 확인한다.
  if( coord <= -180 || coord >= 180 ) {
    $.notify("This coord should in -180 ~ 180.", {position: "bottom right"});
    return false;
  }
  //  0이 3번 반복되는지 확인한다
  let zeroCntr = coord.match(/(0){3}/g);
  if( zeroCntr != null && zeroCntr.length > 0 ) {
    $.notify("'0' should not repeat over 2 times", {position: "bottom right"});
    return false;
  }
  return true;
}
const isLon = function( coord ) {
  //  포멧을 확인한다.
  if( !/^((\d{1,2})(\.)(\d{2,12}))/.test(coord) ) {
    $.notify("This coord is not match on EPSG:4326.", {position: "bottom right"});
    return false;
  }
  //  범위를 확인한다.
  if( coord <= -90 || coord >= 90 ) {
    $.notify("This coord should in -90 ~ 90.", {position: "bottom right"});
    return false;
  }
  //  0이 3번 반복되는지 확인한다
  let zeroCntr = coord.match(/(0){3}/g);
  if( zeroCntr != null && zeroCntr.length > 0 ) {
    $.notify("'0' should not repeat over 2 times", {position: "bottom right"});
    return false;
  }
  return true;
}

/**
 * @JEAHA
 * 받은 좌표가 lat lon 쌍의 좌표인지 확인
 * 4326 체계 좌표가 맞다면 true, 아니면 false.
 * @param {*} str 
 */
const isCoord = function( str ) {
	let result = str.match(/(\d{1,3})(\.)(\d{2,12})/g);
	if(result != null) return result.length % 2 == 0;
	else return false;
}

/**
 * @JEAHA
 * 받은 문자열이 WKT 형식인지 유효성 검사를 한다.
 * @param {} str 
 */
const isFeature = function( str ) {
	str = str.toUpperCase()
	let valid = true;
  //  POINT || LINESTRING || POLYGON 으로 시작하는지 확인한다.
	valid = str.startsWith("POINT(") ?
    true : (str.startsWith("LINESTRING(") ?
        true : (str.startsWith("POLYGON(") ? true : false));
	//  )로 끝나는지 확인한다.
	valid = valid ? str.endsWith(")") : valid;

	let openCnt = str.match(/\(/g);
	let closeCnt = str.match(/\)/g);

  //  (와 )의 갯수가 일치하는지 확인한다.
	valid = ( openCnt != null && closeCnt != null ) ? openCnt.length == closeCnt.length : valid;
	
	return valid ? isCoord(str) : valid;
}

$('.filtering').on('blur', function(evt) {
  let filterAt = evt.target.dataset.filter_at;
  let reFocus = false;
  if( filterAt.indexOf("onlyText") >= 0) evt.target.value = rplcSpclChar(evt.target.value, '_');
  if( filterAt.indexOf("spclCharLsn") >= 0) evt.target.value = rplcSpclCharLsn(evt.target.value, '_');
  if( filterAt.indexOf("lat4326") >= 0 ) reFocus = isLat( evt.target.value );
  if( filterAt.indexOf("lon4326") >= 0 ) reFocus = isLon( evt.target.value );

  //  입력값이 유효하지 않을 경우 포커스를 다시 준다?
  //  유효하지 않으면 무한 포커스?
  //  if( !reFocus ) evt.target.focus();
});