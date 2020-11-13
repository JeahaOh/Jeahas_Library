# Linux 에서 Tomcat 재시작 하는 스크립트

테스트 서버에서 구성해 둔 Tomcat을 재기동 할때마다 일일히 shutdown 시키고, startup 하는게 귀찮아서 한방에 재시작할 수 있도록 restart.sh를 만들어 봤다.  
기존의 startup.sh와 shutdown.sh가 있는 상태에서 작동한다.

```shell
    #!/bin/bash
    clear
    export LANG=ko_KR.UTF-8

    echo -e "Shutdown tomcat now...\n"

    ## shutdown.sh 의 경로 알맞게 설정 해야함.
    bash /usr/local/tomcat/bin/shutdown.sh

    echo -e "tomcat is now terminated...\n"

    sleep 10

    if [ -z "`ps -eaf | grep java | grep /usr/local/tomcat/bin`" ]; then
        echo -e "Tomcat was terminated successfully.\n\n"
    else
        ps -eaf | grep java | grep /usr/local/tomcat/bin | awk '{print $2}' |
        while read PID
        do
            echo "Killing $PID... "
            kill -9 $PID
            echo "Tomcat is being shutdowned"
        done
    fi

    echo -e "tomcat restart now\n\n"

    ## startup.sh 의 경로 알맞게 설정 해야함.
    bash /usr/local/tomcat/bin/startup.sh

    if [ -z "`ps -eaf | grep java | grep /usr/local/tomcat/bin`" ]; then
        echo -e "ERROR!!! Cannot start tomcat server.\n\n"
    else
        echo -e "Tomcat restart successfully.\n\n"
    fi

    echo "restart finished"
```
프로세스 확인하는 함수를 따로 빼 두거나,  
startup이나 shutdown의 경로를 따로 변수로 잡아주면 좋을것 같지만 일단 킵해두도록 함.  
  
  
추가적으로 서버 기동 후 로그를 보는 명령어도 귀찮아서 shell 파일로 만들어 봤다.


```shell
    #!/bin/bash
    export LANG=ko_KR.UTF-8

    tail -f ../logs/catalina.out
```