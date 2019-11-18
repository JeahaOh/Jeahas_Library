//  sessionStorage 사용 법

//  Create
sessionStorage.setItem( 'key', new Date() );

//  Read
data = sessionStorage.getItem( 'key' );
console.log( data );

//  Delete
sessionStorage.removeItem( 'key' );
console.log( sessionStorage.getItem('key') );

//  전체 삭제
sessionStorage.clear();