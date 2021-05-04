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
  [root@ASDF ~]# ls
  WAS-2.csv  anaconda-ks.cfg  install.log  install.log.syslog  watch.sh  공개  다운로드  문서  바탕화면  비디오  사진  음악  템플릿

  [root@ASDF ~]# find watch.sh
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


  
---


## 예제

### 현재 디렉토리에 있는 파일 및 디렉토리 리스트 표시

옵션 없이 find 명령만 사용하면 현재 디렉토리에 있는 모든 파일과 디렉토리(하위 디렉토리를 포함) 표시.  
주로 디렉토리의 파일 리스트를 다른 명령으로 전달, 처리할 때 사용함.  

```shell
  # 현재 디렉토리의 파일 및 디렉토리 출력.
  find

  # 현재 디렉토리의 모든 내용을 줄 바꿈 없이 출력.
  find -print0
```

### 대상 디렉토리에 있는 파일 및 디렉토리 리스트 표시

대상 디렉토리에 있응 파일 및 디렉토리 리스트 표시.

```shell
  find [PATH]
```

### 현재 디렉토리 아래 모든 파일 및 하위 디렉토리에서 파일 검색

현재 디렉토리에 포함 된 모든 하위 디렉토리 및 파일에서 지정된 파일을 검색.

```shell
  find . -name [NAME]
```

### 전체 파일 경로에서 검색

디렉토리 경로를 루트(/)로 지정하여 전체 경로에서 검색.

```shell
  find / -name "NAME"
```

### 파일 이름이 특정 문자열로 시작하는 파일 검색

특정 문자열로 시작하는 파일 검색.

```shell
  find . -name "STR*"
```

### 파일 이름에 특정 문자열을 포함하는 파일 검색

```shell
  find . -name "*STR*"
```

### 파일 이름이 특정 문자열로 끝나는 파일 검색

```shell
  find . -name "*STR"
```

### 빈 디렉토리 또는 크기가 0인 파일 검색

**-empty** 옵션을 넣으면 빈 디렉토리 또는 크기가 0인 파일 검색 가능.

```shell
  # 빈 디렉토리 또는 크기가 0인 파일 검색.
  find . -empty

  # STR 이라는 이름의 빈 디렉토리 또는 크기가 0인 파일 검색.
  find . -name STR -empty
```

### 특정 파일을 검색 후 삭제

**-delete** 옵션을 사용하면 검색된 파일 및 디렉토리를 삭제함.  
검색 결과의 디렉토리가 비어있지 않으면 삭제되지 않음.

```shell
  find . -empty -delete
```

### 검색된 결과를 줄 바꿈 없이 이어서 출력

find의 기본 옵션이 -print인데, -print0 옵션을 주게 되면 줄 바꿈 없이 출력 가능.  

```shell
  # 검색 결과를 줄 바꿈으로 구분하여 출력
  find .
  find . -print

  # 검색 결과를 줄 바꿈 없이 출력.
  find . -print0
```

### 파일 또는 디렉토리만 검색하기

**-type** 옵션을 사용하여, 파일의 종류를 지정할 수 있음.  

- d : directory special
- f : regular file
- l : symbolick link

```shell
  # 이름에 A가 포함되는 일반 파일 검색
  find . -name "*A*" -type f

  # 이름에 A가 포함되는 디렉토리 검색
  find . -name "*A*" -type d
```

### 파일의 크기를 이용하여 검색

**-size** 옵션을 사용하면 파일의 크기로 찾을 수 있음.  
기본적으로 block 단위르 사용함. 파일 크기 뒤에 b, c, w, k, M, G 를 붙임으로 단위를 바꿔 조회 가능.  

- b : block
- c : bytes
- w : 2bytes
- k : kb
- M : MB
- G : GB

크기 앞에 +(초과), -(미만)으로 범위 조건을 부여 가능하며, **-size** 옵션을 조합하여 크기의 범위를 사용 가능.

```shell
  # 파일 크기가 1024 bytes인 파일 검색.
  find . -size 1024c
  # 파일 크기가 1024 bytes를 초과하는 파일 검색.
  find . -size +1024c
  # 미만인 파일 검색.
  find . -size -1024c
  # 1024 초과, 2048 미만
  find . -size +1024c -size -2048c
```

### 검색된 파일에 대한 상세 정보 출력

**-exec** 옵션으로 검색된 결과를 이용해 다른 명령 실행 가능.

```shell
  ls
  BbsAuthorBindingInterceptor.class   LoginAuthorBindingInterceptor.class            ProgrmAuthorBindingInterceptor.class           TemplateBindingInterceptor.class              TemplateMainBindingInterceptor.class-bk_20210209
  ControllerInterceptor.class         NeoAuthorBindingInterceptor.class              ProgrmAuthorBindingInterceptor.class-20210320  TemplateBindingInterceptor.class-bk_20210203
  ControllerInterceptorAdapter.class  NeoAuthorBindingInterceptor.class-bk_20210407  StaffAuthorInterceptor.class                   TemplateMainBindingInterceptor.class

  # 현재 디렉토리에서 *.class 파일들의 상세정보 출력
  find . -name "*.class" -exec ls -al {} \;
  -rw-rw-r-- 1 user user 6475 2021-03-20 16:17 ./ProgrmAuthorBindingInterceptor.class
  -rw-r--r-- 1 user user 2273 2019-12-07 22:58 ./StaffAuthorInterceptor.class
  -rw-rw-r-- 1 user user 5819 2016-03-17 18:00 ./BbsAuthorBindingInterceptor.class
  -rw-rw-r-- 1 user user 1323 2015-08-24 16:54 ./ControllerInterceptor.class
  -rw-r--r-- 1 user user 4948 2021-01-15 12:33 ./LoginAuthorBindingInterceptor.class
  -rw-rw-r-- 1 user user 8673 2021-04-07 09:41 ./NeoAuthorBindingInterceptor.class
  -rw-r--r-- 1 user user 5455 2019-12-08 14:22 ./ControllerInterceptorAdapter.class
  -rw-rw-r-- 1 user user 3728 2021-02-09 15:56 ./TemplateMainBindingInterceptor.class
  -rw-r--r-- 1 root root 13309 2021-02-09 15:56 ./TemplateBindingInterceptor.class
```

### 검색된 파일의 라인 수 출력

find 명령과 wc 명령을 조합하여 검색된 파일의 문자 수 또는 라인수를 계산하여 출력 가능.

```shell
  ls -al
  합계 416
  -rw-r--r--.  1 root root   1462 2020-12-07 13:08 addrlink.js
  -rw-r--r--.  1 root root   1086 2015-08-06 15:41 admin.js
  -rw-r--r--.  1 root root  11208 2021-01-09 02:13 calendar.js
  -rw-r--r--.  1 root root   3027 2021-01-21 13:31 default.js
  -rw-r--r--.  1 root root   3027 2021-02-02 16:50 default.js-remote-20210202.js
  -rw-r--r--.  1 root root  97162 2020-12-07 13:08 jquery-1.12.4.min.js
  -rw-r--r--.  1 root root   2049 2020-02-18 14:28 lay_pop.js
  -rw-r--r--.  1 root root   2975 2017-09-28 10:59 map.js
  -rw-r--r--.  1 root root 136334 2020-03-23 11:10 program.js
  -rw-r--r--.  1 root root  82602 2020-03-23 11:10 program.min.js
  -rw-r--r--.  1 root root  10587 2015-08-06 15:41 tariff.js

  find . -name "*.js" -exec wc -l {} \;
  63 ./addrlink.js
  44 ./admin.js
  385 ./calendar.js
  146 ./default.js
  146 ./default.js-remote-20210202.js
  4 ./jquery-1.12.4.min.js
  73 ./lay_pop.js
  51 ./map.js
  2234 ./program.js
  0 ./program.min.js
  353 ./tariff.js
```

### 검색 된 파일에서 문자열 검색

find 과 grep을 조합해서, 검색된 파일안의 특정 문자열 찾기 가능.

```shell
  # .js 파일에 문자열 function이 있는지 검색
  find . -name "*.js" -exec grep "function" {} \;

  # .js 파일에 문자열 "reduce"가 있는 라인 표시
  find . -name "*.js" -exec grep -n "reduce" {} \;
```

### 검색중 에러 메세지 출력하지 않기

find 명령 실행중 접근 권한으로 에러가 나는 경우가 있음.  
표준 에러 2가 발생할 경우 `/dev/null`로 redirection 시켜 출력을 하지 않을 수 있음.

```shell
  find / -name "*a*" 2> /dev/null
```


### 하위 디렉토리 검색하지 않기

**-maxdepth** 옵션을 사용하면 검색할 하위 디렉토리의 깊이 제한 가능함.  
-maxdepth 옵션은 다른 옵션보다 먼저 사용해야 함.

```shell
  find / -maxdepth 1 -name "sys"
```

### 검색 된 파일 복사

cp 명령과 조합하여 검색 된 파일을 지정된 디렉토리에 복사 가능.

```shell
  find . -name "*.class" -exec sp {} /bk/ \;
```



