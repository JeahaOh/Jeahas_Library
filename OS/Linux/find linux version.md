# [Linux] 리눅스 버전 확인하기
  
리눅스에서 버젼이나 사양을 확인하는 방법은 여러가지 있다.  
가끔 필요하니, 리눅스 버젼 확인 명령어와 커널을 확인하는 명령어를 기록 해 두자.
  
---
  
## OS 버젼 확인 1
  
가장 대표적인 방법이다.
  
```shell
  cat /etc/*release*

  CentOS release 6.5 (Final)
  LSB_VERSION=base-4.0-amd64:base-4.0-noarch:core-4.0-amd64:core-4.0-noarch:graphics-4.0-amd64:graphics-4.0-noarch:printing-4.0-amd64:printing-4.0-noarch
  cat: /etc/lsb-release.d: 디렉터리입니다
  CentOS release 6.5 (Final)
  CentOS release 6.5 (Final)
  cpe:/o:centos:linux:6:GA
```
  
## OS 버젼 확인 2
  
```shell
  cat /etc/issue

  CentOS release 6.5 (Final)
  Kernel \r on an \m
```
  
---
  
## 일반적인 커널 정보 확인
  
```shell
  uname -a
  Linux WAS2 2.6.32-431.el6.x86_64 #1 SMP Fri Nov 22 03:15:09 UTC 2013 x86_64 x86_64 x86_64 GNU/Linux
```
  
---
  
## OS bit 확인하기

```shell
  getconf LONG_BIT
  64
```