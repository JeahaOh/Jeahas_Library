# TIP

## 포토샾 없이 이미지 편집하기
- `https://pixlr.com/`

## 투명 favicon
- 동위 dir에 favicon.ico

## 이미지 파일 svg로 변환 하기
- potrace 설치 : `http://potrace.sourceforge.net/`
  - mac, win, linux 모두 호환.
- potrace 환경변수 잡아주기.
- bitmap 이미지로 변환.
  - `mkbitmap [파일 이름 ex) ASDF.png] [파일이 많을 경우 *.확장자]`
- svg 이미지로 변환.
  - `potrace -s [파일 이름 ex) ASDF.pbm] [파일이 많을 경우 *.pbm]`