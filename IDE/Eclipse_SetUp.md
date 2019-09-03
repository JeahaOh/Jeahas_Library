# Eclipse 설치시 설정 나에게 최적화.

## LOG.
- Console에 출력 되는 LOG 파일로 저장(다른 프로그램으로 로그를 보면 편함.)
  - [Run] - [Run Configurations] (- [Java Application] - [Main이 있는 Class]) - [Common tab]
  - Standard Input and Output 창의 `Output File`에서 설정
- Console에 출력 되는 Server LOG 파일로 저장
  - [Run] - [Run Configurations] (- [Apache Tomcat] - [Server가 돌아가는 Project]) - [Common tab]
  - Standard Input and Output 창의 `Output File`에서 설정

## Console 창 로그 한줄 길이 제한
- [Console 우클릭] - [Preference] - Fixed width console 체크, 원하는 글자수 지정.


## fomatter 설정 import
- fomat 설정 xml 파일 준비
- [Java | JavaScript] - [Code Style] - [Fomatter]
- import

## 한 라인 최대 문자 입력 가이드라인 보이기.
- [Window] - [Preference] - [General] - [Editor] - [Text Editors tab]
  - Show print margin
  - print margin column : 원하는 글자수.

## javascript code assist(자동완성) 기능 설정
- [preferences] - [JavaScript] - [Content Assist] - [Auto-Activation 활성화]
- [Help] - [Install New Software] - [http://oss.opensagres.fr/tern.repository/1.2.0/] 설치 - [install anyway] - [Eclipse 재시작]
  - 적용 방법
    - [프로젝트 우클릭] - [Configure] - [Convert to Tern Project] - 사용할 Module 선택해 주고 apply
    - [프로젝트 우클릭] - [Propertis] - [JavaScript] - [Tern] - [Modules] - 사용할 기능들 선택해주고 apply
- 단점. jsp 태그를 에러로 인식함.

## Class Diagram Generator ObjectAid
- [Help] - [Install Nes Software] - [Add] - [아래 name과 url 입력] - [Object Class Diagram 선택(나머지는 유료)]
- 설명 : https://www.objectaid.com/download
    - Name: ObjectAid UML Explorer 1.1.x
    - URL: http://www.objectaid.com/update/1.1
- 사용법
    - [ctrl + n] - [Class Diagram] - [저장 경로와 이름 지정] - [생성] - [원하는 java 파일을 드래그 & 드롭으로 .ucls 안에 넣어준다.]
    - public static 으로 사용하는 메소드는 불러오지 못함.

# Market Place Plugin
## Darkest Dark Theme
- Dark Grey Theme
- Pastell
## AnyEdit
- 소문자 -> 대문자 (ctrl + alt + l)
- 대문자 -> 소문자 (ctrl + alt + u)
- 카멜 <-> 스네이크 (ctrl + alt + k)

