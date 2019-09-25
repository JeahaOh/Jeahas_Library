/**
 * url에서 특정 param의 value를 바꾸고 싶을 때 사용.
 * ex)
 * url   : http://localhost:8080/cop/bbs/SelectBBSMasterInfs.do?siteId=2&pageIndex=2
 * param : 'pageIndex'
 * value : 3
 * 
 * result = http://localhost:8080/cop/bbs/SelectBBSMasterInfs.do?siteId=2&pageIndex=3
 */

function replaceQueryString( url, param, value ) {
  var re = new RegExp("([?|&])" + param + "=.*?(&|$)","i");
  if ( url.match(re) )
      return url.replace(re,'$1' + param + "=" + value + '$2');
  else
      return url + '&' + param + "=" + value;
}