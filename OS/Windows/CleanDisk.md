# Clean Disk
- 윈도우 불필요한 파일 정리하는 PowerShell 명령어  
  - PowerShell 관리자 권한으로 실행  
  - dism /online /cleanup-image /analyzecomponentstore
  - dism /online /cleanup-image /startcomponentcleanup
- 두개 명령 내려주면 진행
```
PS C:\WINDOWS\system32> dism /online /cleanup-image /analyzecomponentstore

배포 이미지 서비스 및 관리 도구
버전: 10.0.18362.1

이미지 버전: 10.0.18362.535

[==========================100.0%==========================]

구성 요소 저장소(WinSxS) 정보:

Windows 탐색기에서 보고된 구성 요소 저장소의 크기 : 5.80 GB

구성 요소 저장소의 실제 크기 : 5.68 GB

    Windows와 공유됨 : 4.10 GB
    백업 및 기능 사용 안 함 : 1.58 GB
    캐시 및 임시 데이터 :  0 bytes

마지막 정리 날짜 : 2020-01-02 09:38:48

다시 사용 가능한 패키지 수 : 2
구성 요소 저장소 정리 권장 : 예

작업을 완료했습니다.
PS C:\WINDOWS\system32> dism /online /cleanup-image /startcomponentcleanup

배포 이미지 서비스 및 관리 도구
버전: 10.0.18362.1

이미지 버전: 10.0.18362.535

[=====                      10.0%                          ]
[==========================100.0%==========================]
작업을 완료했습니다.
PS C:\WINDOWS\system32>
```