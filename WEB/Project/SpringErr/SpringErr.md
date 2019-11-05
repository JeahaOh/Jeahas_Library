# Spring 실행 에러
```
심각: Error configuring application listener of class org.springframework.web.context.ContextLoaderListener
java.lang.ClassNotFoundException: org.springframework.web.context.ContextLoaderListener
	at org.apache.catalina.loader.WebappClassLoaderBase.loadClass(WebappClassLoaderBase.java:1854)
	at org.apache.catalina.loader.WebappClassLoaderBase.loadClass(WebappClassLoaderBase.java:1703)
	at org.apache.catalina.core.DefaultInstanceManager.loadClass(DefaultInstanceManager.java:506)
	at org.apache.catalina.core.DefaultInstanceManager.loadClassMaybePrivileged(DefaultInstanceManager.java:488)
	at org.apache.catalina.core.DefaultInstanceManager.newInstance(DefaultInstanceManager.java:115)
	at org.apache.catalina.core.StandardContext.listenerStart(StandardContext.java:4919)
	at org.apache.catalina.core.StandardContext.startInternal(StandardContext.java:5517)
	at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:150)
	at org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1574)
	at org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1564)
	at java.util.concurrent.FutureTask.run(Unknown Source)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(Unknown Source)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(Unknown Source)
	at java.lang.Thread.run(Unknown Source)
```
위와 같은 에러가 발생 했을 경우.  
다음 경로 에서, 설정을 해 준다.  
- Project 우클릭 > Properties > Deployment Assembly   
- Add 버튼 > Java Build Path Entries > Maven Dependencies 선택 > Apply 적용.