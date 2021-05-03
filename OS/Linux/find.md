# [Linux] find 명령어

## find

**find**는 리눅스에서 파일 또는 디렉토리를 계층구족로 검색할 때 사용함.  
원하는 파일을 찾는데 오래 걸리긴 하지만 생각보다 활용도나 활용 빈도가 높아서 정리해 보기로 함.

## 명령어 옵션

일반적인 리눅스 명령어와는 다르게 find 명령은 옵션보다 표현식이 더 많이 사용 됨.  
검색 결과가 지정된 표현식의 조합에 따라 많이 달라짐.

```shell
  [root@ASDF ~]# find --help
  Usage: find [-H] [-L] [-P] [-Olevel] [-D help|tree|search|stat|rates|opt|exec] [path...] [expression]

  기본 경로로는 현재 디렉토리; 기본 표현식은 -print.
  표현식은 연산자, 옵션, 테스트 및 조치로 구성됨:

  연산자 (우선순위 감소; -다른 항목이 지정되지 않은 경우 암시적) :
        ( EXPR )   ! EXPR   -not EXPR   EXPR1 -a EXPR2   EXPR1 -and EXPR2
        EXPR1 -o EXPR2   EXPR1 -or EXPR2   EXPR1 , EXPR2

  위치 옵션 : -daystart -follow -regextype

  일반 옵션 (always true, 다른 표현식 앞에 지정됨):
        -depth --help -maxdepth LEVELS -mindepth LEVELS -mount -noleaf
        --version -xdev -ignore_readdir_race -noignore_readdir_race

  테스트 (N can be +N or -N or N): -amin N -anewer FILE -atime N -cmin N
        -cnewer FILE -ctime N -empty -false -fstype TYPE -gid N -group NAME
        -ilname PATTERN -iname PATTERN -inum N -iwholename PATTERN -iregex PATTERN
        -links N -lname PATTERN -mmin N -mtime N -name PATTERN -newer FILE
        -nouser -nogroup -path PATTERN -perm [+-]MODE -regex PATTERN
        -readable -writable -executable
        -wholename PATTERN -size N[bcwkMG] -true -type [bcdpflsD] -uid N
        -used N -user NAME -xtype [bcdpfls]
        -context CONTEXT


  작업 : -delete -print0 -printf FORMAT -fprintf FILE FORMAT -print 
        -fprint0 FILE -fprint FILE -ls -fls FILE -prune -quit
        -exec COMMAND ; -exec COMMAND {} + -ok COMMAND ;
        -execdir COMMAND ; -execdir COMMAND {} + -okdir COMMAND ;

  Report (and track progress on fixing) bugs via the findutils bug-reporting
  page at http://savannah.gnu.org/ or, if you have no web access, by sending
  email to <bug-findutils@gnu.org>.
```
  
| OPTION | DESC |
| ---: | :--- |
| -P | 심볼릭 링크를 따라가지 않고, 심볼릭 링크 자체 정보 사용. |
| -L | 심볼릭 링크에 연결된 파일 정보 사용. |
| -H | 심볼릭 링크를 따라가지 않으나, Command Line Argument를 처리할 땐 예외. |
| -D | 디버그 메세지 출력 |
  
| EXPRESSION | DESC |
| :---: | :--- |
| -name | 지정된 문자열 패턴에 해당하는 파일 검색. |
| -empty | 빈 디렉토리 또는 크기가 0인 파일 검색. |
| -delete | 검색된 파일 또는 디렉토리 삭제. |
| -exec | 검색된 파일에 대해 지정된 명령 실행. |
| -path | 지정된 문자열 패턴에 해당하는 경로에서 검색. |
| -print | 검색 결과를 출력. 검색 항목은 newline으로 구분(기본값). |
| -print0 | 검색 결과를 출력. 검색 항목은 null로 구분. |
| -size | 파일 크기를 사용하여 파일 탐색. |
| -type | 지정된 파일 타입에 해당하는 파일 검색. |
| -mindepth | 검색할 하위 디렉토리 최소 깊이 지정. |
| -maxdepth | 검색할 하위 디렉토리 최대 깊이 지정. |
| -atime | 파일 접근(access) 시각을 기준으로 파일 검색. |
| -ctime | 파일 내용 및 속성 변경(change) 시각을 기준으로 검색. |
| -mtime | 파일의 데이터 수정(modify) 시각을 기준으로 검색. |
  

표현식 사용시 연산자를 이용하면 두 개 이상의 표현식 조합이 사용가능.  
find 명령에서 두 개 이상의 표현식을 사용할 경우 연산자를 지정하지 않으면 기본적으로 AND 연산자가 적용됨.  

| EXPR | DESC |
| :--- | :--- |
| (expression) | expression 우선순위 지정. |
| !expression <br/> -not expression | expression 결과에 NOT 연산 |
| expression -a expression <br/> expression -and expression <br/> expression expression | expression 간 AND 연산 |
| expression -o expression <br/> expression -or expression | expression 간 OR 연산 |

  
---

## find 명령 사용 예제

현재 디렉토리 내에서 지정된 이름의 파일을 찾는 것은 간단함.  
find 명령 뒤에 파일 이름을 지정하면 됨.

```shell
  [root@WAS2 ~]# ls
  WAS-2.csv  anaconda-ks.cfg  install.log  install.log.syslog  watch.sh  공개  다운로드  문서  바탕화면  비디오  사진  음악  템플릿

  [root@WAS2 ~]# find watch.sh
  watch.sh
```

보통 현재 디렉토리에서 파일을 찾을 땐 `ls | grep 이거`로 찾기 때문에 현재 디렉토리와 하위 디렉토리에 위치한 파일을 찾을 경우에는 `find . -name "이거"`를 사용함.  

## 요약

| command | ex |
| :--- | :--- |
| find | 현재 디렉토리에 있는 파일 또는 디렉토리 리스트 표시. |
| find [PATH] | 대상 디렉토리에 있는 파일 또는 디렉토리 표시. |
| find . -name [FILE] | 현재 디렉토리 및 하위 디렉토리에서 파일 또는 디렉토리 검색. |
| find / -name [FILE] | 전체 파일 시스템에서 파일 검색. |
| find . -name "STR*" | 이름이 특정 문자열로 시작하는 파일 검색. |
| find . -name "*STR" | 이름이 특정 문자열로 끝나는 파일 검색. |
| find . -name "*STR*" | 이름에 특정 문자열이 포함된 파일 검색. |
| find . -empty | 크기가 0인 파일 또는 디렉토리 검색. |
| find . -name "*.EXT" -delete | 특정 확장자를 가진 모든 파일 검색 후 삭제. |
| find . -name [FILE] -print0 | 검색된 리스트를 줄 바꿈 없이 이어서 출력 |
| find . -name [FILE] -type f | 파일 또는 디렉토리만 검색하기 |
| find . -size +[N]c -and -size -[M]c | 파일의 크기를 사용하여 파일 검색. |
| find . -name [FILE] -exec ls -l {} \; | 검색됱 파일에 대한 상세 정보 출력. |
| find . -name [FILE] -exec grep "STR" {} \; | 검색된 파일에서 문자열 찾기 |
| find . -name [FILE] > [SAVE_FILE] | 검색 결과를 파일로 저장. |
| find . -name [FILE] 2> /dev/null | 검색 중 에러 출력하지 않기. |
| find . -maxdepth 1 -name [FILE] | 하위 디렉토리 검색하지 않기. |
| find . -name [FILE] -exec cp {} [PATH] \; | 검색된 파일 복사. |
