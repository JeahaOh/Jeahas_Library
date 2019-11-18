//  localStorage 사용 법

//  Create
localStorage.key = 'value';
localStorage.setItem(1, new Date() );

//  Read
data = localStorage.key;
console.log( data );

data = localStorage.getItem( 1 );
console.log( data );

//  key가 없다면 가져오지 않는다.
//  localStorage.getItem();

//  Delete
localStorage.removeItem( 1 );
console.log( localStorage.getItem(1) );

//  전체 삭제
localStorage.clear();