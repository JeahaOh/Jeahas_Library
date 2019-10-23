# CFR Decompiler
`http://www.benf.org/other/cfr/`  
최신 버전이고 지속적으로 업데이트 하는 듯 함.  
`https://slothink.tistory.com/134` 블로그에 따르면,  
`JAD Decompiler`는 1.5버전 이후는 지원이 안되고, 
`JD core`를 이용한 `JD_Gui`를 주로 사용해서 decompile 한 후,  
컴파일 오류가 나는 파일에 대해서 `CFR Decompiler` 를 사용한다고 함.  
  
```
java -jar cfr-0.147.jar "디컴파일 할 jar 경로" --outputdir "디텀파일 소스 파일이 등록될 경로"
```
```
PS dir >> java -jar .\cfr-0.147.jar snp.jar --outputdir d:/src
Processing snp.jar (use silent to silence)
Processing WEB-INF.classes.egovframework.com.cmm.AltibaseClobStringTypeHandler
Processing WEB-INF.classes.egovframework.com.cmm.annotation.IncludedInfo
Processing WEB-INF.classes.egovframework.com.cmm.ComDefaultCodeVO
Processing WEB-INF.classes.egovframework.com.cmm.ComDefaultVO
Processing WEB-INF.classes.egovframework.com.cmm.EgovComCrossSiteHndlr
"""
생략
"""
```
