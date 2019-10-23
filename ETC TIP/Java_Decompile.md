# Class File Decompile 하는 방법
소스가 없는 파일을 Decompile 해야 하는 일이 생김.  
대체.... **소스 파일 백업의 중요성**...  
어쨌든 Jad Decompiler를 사용하면 class 파일을 java파일로 변환 할 수 있다.  
`jad.exe`를 독립적으로 사용하는 방법과, eclipse plugin을 설치하는 방법이 있다.
윈도우 기준으로 기록을 남기자면.  
  
## jad.exe
- `https://varaneckas.com/jad/`에서 `jad.exe`를 다운 받는다.  
- CMD에서 바로 실행할 수 있도록 `jad.exe`를 `JAVA` 환경변수가 잡혀있는 `bin` 폴더에 넣는다.
- 설치 확인을 위해 CMD에서 `jad`를 실행해 본다.  
  ```
    >>> jad
    Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov (jad@kpdus.com).
    Usage:    jad [option(s)] <filename(s)>
    Options: -a       - generate JVM instructions as comments (annotate)
            -af      - output fully qualified names when annotating
            -b       - generate redundant braces (braces)
            -clear   - clear all prefixes, including the default ones
            -d <dir> - directory for output files
            -dead    - try to decompile dead parts of code (if there are any)
            -dis     - disassembler only (disassembler)
            -f       - generate fully qualified names (fullnames)
            -ff      - output fields before methods (fieldsfirst)
            -i       - print default initializers for fields (definits)
            -l<num>  - split strings into pieces of max <num> chars (splitstr)
            -lnc     - output original line numbers as comments (lnc)
            -lradix<num>- display long integers using the specified radix
            -nl      - split strings on newline characters (splitstr)
            -noconv  - don't convert Java identifiers into valid ones (noconv)
            -nocast  - don't generate auxiliary casts
            -noclass - don't convert .class operators
            -nocode  - don't generate the source code for methods
            -noctor  - suppress the empty constructors
            -nodos   - turn off check for class files written in DOS mode
            -nofd    - don't disambiguate fields with the same names (nofldis)
            -noinner - turn off the support of inner classes
            -nolvt   - ignore Local Variable Table entries (nolvt)
            -nonlb   - don't insert a newline before opening brace (nonlb)
            -o       - overwrite output files without confirmation
            -p       - send all output to STDOUT (for piping)
            -pa <pfx>- prefix for all packages in generated source files
            -pc <pfx>- prefix for classes with numerical names (default: _cls)
            -pe <pfx>- prefix for unused exception names (default: _ex)
            -pf <pfx>- prefix for fields with numerical names (default: _fld)
            -pi<num> - pack imports into one line using .* (packimports)
            -pl <pfx>- prefix for locals with numerical names (default: _lcl)
            -pm <pfx>- prefix for methods with numerical names (default: _mth)
            -pp <pfx>- prefix for method parms with numerical names (default:_prm)
            -pv<num> - pack fields with the same types into one line (packfields)
            -r       - restore package directory structure
            -radix<num>- display integers using the specified radix (8, 10, or 16)
            -s <ext> - output file extension (default: .jad)
            -safe    - generate additional casts to disambiguate methods/fields
            -space   - output space between keyword (if, while, etc) and expression
            -stat    - show the total number of processed classes/methods/fields
            -t<num>  - use <num> spaces for indentation (default: 4)
            -t       - use tabs instead of spaces for indentation
            -v       - show method names while decompiling
            -8       - convert Unicode strings into ANSI strings (ansi)
            -&       - redirect STDERR to STDOUT
    >>>>
  ```
- 사용 방법은 README나 구글링을 해보도록 한다.
  
  

## 실제 사용
Spring Project의 실행 파일만 갖고 있어서 사용하게 되었고 프로젝트 디컴파일을 하게 되었다.  
프로젝트 단위의 디컴파일을 위해서는 다음과 같은 명령을 실행 하면 되는듯 하고,  
소스 코드로 만들기 위해서는 디컴파일 후 경로를 직접 다 다시 잡아줘야 하는 듯 하다.  
  
```
PS C:\Users\GMTC_JH> cd .\Desktop\dir\
PS C:\Users\GMTC_JH\Desktop\dir> ls

    디렉터리: C:\Users\GMTC_JH\Desktop\dir

Mode                LastWriteTime         Length Name
----                -------------         ------ ----
d-----     2019-10-23  오후 12:20                cab
d-----     2019-10-23  오후 12:20                css
d-----     2019-10-23  오후 12:20                html
d-----     2019-10-23  오후 12:20                images
d-----     2019-10-23  오후 12:20                js
d-----     2019-10-23  오후 12:20                META-INF
d-----     2019-10-23  오후 12:21                WEB-INF
-a----     2016-06-28   오후 2:30           2798 code404.jsp
-a----     2016-06-28   오후 2:30           2637 code500.jsp
-a----     2016-02-26   오후 1:37            321 index - 복사본.jsp
-a----     2019-10-23   오전 9:33            321 index.jsp
-a----     2016-06-28   오후 2:30           7984 map.jsp


PS C:\Users\GMTC_JH\Desktop\dir> jad -o -r -sjava **/*.class


Parsing WEB-INF\classes\egovframework\com\cmm/AltibaseClobStringTypeHandler.class... Generating egovframework\com\cmm\AltibaseClobStringTypeHandler.java
Couldn't fully decompile method getResultInternal
Couldn't resolve all exception handlers in method getResultInternal
Parsing WEB-INF\classes\egovframework\com\cmm/ComDefaultCodeVO.class... Generating egovframework\com\cmm\ComDefaultCodeVO.java
Parsing WEB-INF\classes\egovframework\com\cmm/ComDefaultVO.class... Generating egovframework\com\cmm\ComDefaultVO.java
Parsing WEB-INF\classes\egovframework\com\cmm/EgovComCrossSiteHndlr.class... Generating egovframework\com\cmm\EgovComCrossSiteHndlr.java
Couldn't resolve all exception handlers in method doStartTag
Couldn't resolve all exception handlers in method doEndTag
"""
생략
"""
Parsing WEB-INF\classes\egovframework\com\cmm\service/EgovFileMngService.class... Generating egovframework\com\cmm\service\EgovFileMngService.java
Parsing WEB-INF\classes\egovframework\com\cmm\service/EgovFileMngUtil.class... Generating egovframework\com\cmm\service\EgovFileMngUtil.java
Couldn't fully decompile method writeUploadedFile
Couldn't resolve all exception handlers in method writeUploadedFile
Couldn't fully decompile method downFile
Couldn't resolve all exception handlers in method downFile
Couldn't fully decompile method writeFile
Couldn't resolve all exception handlers in method writeFile
Couldn't fully decompile method downFile
Couldn't resolve all exception handlers in method downFile
Parsing WEB-INF\classes\egovframework\com\cmm\service/EgovProperties.class... Generating egovframework\com\cmm\service\EgovProperties.java
Couldn't fully decompile method getPathProperty
Couldn't resolve all exception handlers in method getPathProperty
Couldn't fully decompile method getProperty
Couldn't resolve all exception handlers in method getProperty
Couldn't fully decompile method getPathProperty
Couldn't resolve all exception handlers in method getPathProperty
Couldn't fully decompile method getProperty
Couldn't resolve all exception handlers in method getProperty
Couldn't fully decompile method loadPropertyFile
Couldn't resolve all exception handlers in method loadPropertyFile
Parsing WEB-INF\classes\egovframework\com\cmm\service/EgovUserDetailsService.class... Generating egovframework\com\cmm\service\EgovUserDetailsService.java
Parsing WEB-INF\classes\egovframework\com\cmm\service/FileVO.class... Generating egovframework\com\cmm\service\FileVO.java
Parsing WEB-INF\classes\egovframework\com\cmm\service/Globals.class... Generating egovframework\com\cmm\service\Globals.java
Parsing WEB-INF\classes\egovframework\com\cmm\service\impl/CmmUseDAO.class... Generating egovframework\com\cmm\service\impl\CmmUseDAO.java
"""
생략
"""
```
  
마지막 publish 년도가 2001년으로 최신 버전은 지원 안되지 않을까?  
다음과 같은 log가 뜬다...
```
Couldn't fully decompile method XXX
Couldn't resolve all exception handlers in method XXX
```
