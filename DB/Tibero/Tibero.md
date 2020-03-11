# 거지같은 티베로 사용하는 프로젝트 셋팅하기.
티베로 DB를 사용 할 경우 LOG4JDBC로 로그를 보기 위해서는  
[Servers] - [Tomcat] - [Open Launch Configuration] - [Arguments]에 다음과 같은 설정을 넣어 줘야 함.  
```
  -Dlog4jdbc.drivers=com.tmax.tibero.jdbc.TbDriver
```