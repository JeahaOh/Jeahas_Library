//  lowDB 모듈
const low = require('lowdb')
//  lowDB를 통해 데이터를 저장할 때, 파일로 동기방식으로 저장하기 위한 모듈
const FileSync = require('lowdb/adapters/FileSync')

//  데이터를 db.json 파일에 json형식으로 저장하겠다.
const adapter = new FileSync('db.json')

const db = low(adapter)

// Set some defaults (required if your JSON file is empty)
db.defaults({ posts: [], user: [], count: 0 }).write()

// Create
//  white()를 해주지 않으면 데이터가 들어가지 않음.
// db.get('user').push({
//   id: 1,
//   name: 'Jeaha',
//   profile: 'dev'
// }).write();

// db.get('posts').push({
//   id: 1,
//   title: 'lowDB is...',
//   description: 'lowDB is awsome',
//   user: 1
// }).write();

// db.get('posts').push({
//   id: 2,
//   title: 'js...',
//   description: 'js is wierd..',
//   user: 1
// }).write();

//  READ
// console.log( db.get('posts').value() );
console.log(
  db.get('posts')
  //  원하는 데이터만 가져오고 싶을 때
  .find({title: 'lowDB is...'})
  .value()
);