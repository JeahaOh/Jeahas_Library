/**
 * js 객체의 value 값이 비어있는 key를 비워준다.
 * ajax나 session 저장 하기 전 js 객체의 사이즈를 줄이기 위해 사용한다.
 * @param {} obj 
 */
const removeObjKeyEmpty = function(obj) {
  console.group( 'Remove Empty At Object' );

  Object.keys(obj).forEach(function(key) {
    (obj[key] && typeof obj[key] === 'object') && removeObjKeyEmpty(obj[key]) ||
    (obj[key] === undefined || obj[key] === null) && delete obj[key]
  });

  console.groupEnd( 'Remove Empty At Object' );
  return obj;
};