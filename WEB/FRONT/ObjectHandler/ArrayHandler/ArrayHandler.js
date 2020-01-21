/**
 * 대상이 되는 배열에서 해당 요소를 찾아 삭제한다.
 * @param {*} array 
 * @param {*} element 
 */
const removeArrElement = function( array, element ) {
  const idx = array.indexOf(element);
  if( idx !== -1 ) {
    array.splice(idx, 1);
  }
}