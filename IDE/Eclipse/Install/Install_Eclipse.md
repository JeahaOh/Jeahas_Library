# Windows Eclipse 설치하기
## 설치
  1. 이클립스 설치 프로그램 [다운로드](https://www.eclipse.org/downloads/).
  2. `eclipse-inst-win64.exe` 실행.
  3. `Eclipse IDE for Enterprise Java Developers` 선택.  
    ![](./img/01.png)
  4. JVM과 설치 경로 지정  
    - `JVM` 선택에서 JRE가 아닌 JDK 선택.    
    - `Installation Folder`에서 적절한 경로 지정.  
    ![](./img/02.png)
  5. 동의 할게 뜨면 동의 하고 설치 진행.  
    ![](./img/03.png)
  6. 설치 완료 후, 실행.
    ![](./img/04.png)
  7. JAVA로 작업할 파일들이 저장될 경로 (Workspace) 지정.
    ![](./img/05.png)  
    
## 설치 확인을 위한 테스트
  1. 새 프로젝트 생성.  
    `(ctrl + n) -> Java Project`  
    ![](./img/06.png)
  2. 프로젝트 이름 지정.  
    ![](./img/07.png)
  3. finish.  
    ![](./img/08.png)
  4. 테스트 프로젝트의 패키지 생성.  
    `(ctrl + n) -> package`  
    ![](./img/09.png)
  5. `test`로 패키지 이름 지정.  
    ![](./img/10.png)
  6. `Test.class` 파일 생성.
    `(ctrl + n) -> class`  
    ![](./img/11-1.png)
  7. `public void main(String[] args)` 체크 후 생성.
    ![](./img/11-2.png)
  8. 간단한 출력문 추가.  
      ```
        System.out.println("HI");
      ```
  9. `ctrl + f11`으로 실행.  
    ![](./img/12.png)