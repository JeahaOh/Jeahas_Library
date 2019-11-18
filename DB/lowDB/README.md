# lowDB
javascript를 통해서 작동하는 DB.  

## nodejs에서 lowDB 설치
CMD에서 npm을 통해 설치한다.  
```
PS C:...\lowDB> npm install -s lowdb
+ lowdb@1.0.0
added 6 packages from 6 contributors and audited 7 packages in 1.418s
found 0 vulnerabilities
```
  
main.js를 작성 한 뒤 실행을 해 주면 db.json 파일이 생성 된다.  
db.json은 {} 비어있는 객체가 생성된다.

## browser에서 lowDB 설치
lowDB.html  
페이지를 크롬에서 실행, LocalStorage 확인해 보면 node에서와 같은 빈 객체가 생성 됨.