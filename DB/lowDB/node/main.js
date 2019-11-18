//  lowDB 모듈
const low = require('lowdb')
//  lowDB를 통해 데이터를 저장할 때, 파일로 동기방식으로 저장하기 위한 모듈
const FileSync = require('lowdb/adapters/FileSync')

//  데이터를 db.json 파일에 json형식으로 저장하겠다.
const adapter = new FileSync('db.json')

const db = low(adapter)

// Set some defaults (required if your JSON file is empty)
db.defaults({ posts: [], user: {}, count: 0 }).write()