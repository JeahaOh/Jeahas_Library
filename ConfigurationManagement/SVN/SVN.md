# SVN

## SVN lock 해제시키기
1. SQLite 설치
  - `http://sqlitebrowser.org/`
2. DB Browser for SQLite 실행
3. 데이터베이스 열기
  - 프로젝트 내부의 .svn파일에 위치하는 wc.db 
4. SQL 실행 탭에서 두가지 명령 실행
  - DELETE FROM WC_LOCK;
  - DELETE FROM WORK_QUEUE;
5. Eclipse에서 cleanup 실행.