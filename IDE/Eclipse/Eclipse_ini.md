# eclipse.ini 주요 설정 값
Eclipse 실행시 eclipse.ini 파일에 설정된 옵션으로 실행됨.  
이 설정 파일은 windows 에서는 eclipse.exe 파일이 있는 설치 폴더에, MacOS 에서는 Eclipse.app > Contents > MacOS 폴더 안에 있다.  
주요 설정 값들을 알아보자.  

- vm
  * jdk의 경로를 직접 지정. 보통은 신경 쓸 필요는 없으나 jdk를 여러개 설치하고 작업한다면 직접 위치를 지정하여 사용할 수 있음.
  * vmargs 라인 이전에 설정.
- -Dosgi.requiredJavaVersion=1.8
  * 사용할 자바 버전.
- -Xverify:none
  * 초기 실행시 클래스의 유효성 검사의 여부.
- -XX:UseParallelOldGC
  * 병렬 GC 사용
- -XX:+AggressiveOpts
  * 컴파일러의 소수점 최적화 기능 작동 설정.
- -XX:-UseConcMarkSweepGC 
  * 병행 Mark-Sweep GC를 수행하여 이클립스 GUI의 응답 최적화 설정.
- -XX:PermSize=1024m 
  * JVM 클래스와 메서드를 위한 공간.
  * OutOfMemoryError:PermGenspace 발생시 이 설정값을 늘려야 한다. 
- -XX:MaxPermSize=1024m
  * 최대값은 8번의 최소값 이상이여야 함.
- -XX:NewSize=256m
  * 새로 생성된 객체들을 위한 공간
- -XX:MaxNewSize=256m
  * 상기 동일
- Xms2048m
  * 최소 Heap 메모리 크기.
- Xmx4096m 
  * 이클립스가 사용하는 최대 Heap 메모리 크기, 보통 내장된 메모리의 4분의 1을 최대 Heap 메모리로 설정하여 사용.

항상 설정 파일을 수정할 땐 **원본 백업**을 해 두도록 하자.
## eclipse.ini
```
-startup
plugins/org.eclipse.equinox.launcher_1.5.600.v20191014-2022.jar
--launcher.library
C:\..\.p2\pool\plugins\org.eclipse.equinox.launcher.win32.win32.x86_64_1.1.1100.v20190907-0426
-product
org.eclipse.epp.package.jee.product
-showsplash
C:\..\.p2\pool\plugins\org.eclipse.epp.package.common_4.14.0.20191212-1200
--launcher.defaultAction
openFile
--launcher.appendVmargs
-vm
C:/Program Files/Java/jdk1.8.0_241/bin
-vmargs
-Dosgi.requiredJavaVersion=1.8
-Dosgi.instance.area.default=@user.home/eclipse-workspace
-XX:+UseG1GC
-XX:+UseStringDeduplication
--add-modules=ALL-SYSTEM
-Dosgi.dataAreaRequiresExplicitInit=true
-Xverify:none
-XX:UseParallelOldGC
-XX:+AggressiveOpts
-XX:-UseConcMarkSweepGC 
-XX:MaxPermSize=1024m
-XX:MaxNewSize=1024m
-Xms2048m
-Xmx4096m
--add-modules=ALL-SYSTEM
-Declipse.p2.max.threads=10
-Doomph.update.url=http://download.eclipse.org/oomph/updates/milestone/latest
-Doomph.redirection.index.redirection=index:/->http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/

```

## WAS 메모리 설정
실행할 서버 > Open Launch Configuration > Arguments > VM arguments 설정
```
-XX:MaxPermSize=256M
-Xms1024m
-Xmx1024m
```