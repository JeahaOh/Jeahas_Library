# GRADLE

## Gradle로 자바 프로젝트 생성하기
- 터미널에서 대상 폴더로 들어가기
- `gradle init --type java-application`
  - gradle init : Gradle 초기화를 위한 명령어
  - --type java-application : java 응용 프로그램 프로젝트를 의미.
- ```
  Select build script DSL:
    1: groovy
    2: kotlin
  Enter selection (default: groovy) [1..2] 1
  ```
- ```
  Select test framework:
    1: junit
    2: testng
    3: spock
  Enter selection (default: junit) [1..3] 1
  ```
- buile.gradle(gradle 설정파일)
  - 플러그인 추가 :  id eclipse
  - repository 변경 : mavenCentral()
- 프로젝트에 맞게 .gitignore 파일 수정
- 터미널에서 이클립스 설정 파일 생성 `gradle eclipse` 
- 이클립스에서 임포트

## WEB Project로 만들기
- 출처: `https://mikelim.mintocean.com/entry/Gradle-Eclipse-Gradle-Spring-웹-프로젝트`
- 경로 만들기
  - src/main/resouces
  - src/main/webapp
  - src/main/webapp/WEB-INF
  - src/test/resouces
- build.gradle에 플러그인 넣기
  - apply plugin: 'java'
  - apply plugin: 'eclipse-wtp'
  - apply plugin: 'war'