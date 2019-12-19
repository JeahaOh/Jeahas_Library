# pom.xml
pom.xml에 다음과 같은 Dependency를 추가 해 준다.  
```
<!-- JSON을 위한 Dependency -->
<dependency>
  <groupId>net.sf.json-lib</groupId>
  <artifactId>json-lib</artifactId>
  <version>2.4</version>
  <classifier>jdk15</classifier>
</dependency>
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
  <version>2.9.5</version>
</dependency>
<dependency>
  <groupId>org.codehaus.jackson</groupId>
  <artifactId>jackson-mapper-asl</artifactId>
  <version>1.9.13</version>
</dependency>
<dependency>
  <groupId>org.codehaus.jackson</groupId>
  <artifactId>jackson-mapper-asl</artifactId>
  <version>1.9.13</version>
</dependency>
<!-- JSON을 위한 Dependency -->
```

# servlet.xml
servlet.xml 혹은 dispatcher-servlet.xml
```
<mvc:annotation-driven>
  <mvc:message-converters>
  <!-- 
    이 부분은 Controller에서 일반적인 HTML을 리턴하기 위한 설정이다.
    JSON을 리턴하지 않을 경우는 Default 값으로 지정 되어 있기 때문에 설정 할 필요 없지만,
    JSON 리턴과 HTML 리턴을 모두 하려면은 명시적으로 설정 해 줘야 한다.
   -->
    <bean class="org.springframework.http.converter.StringHttpMessageConverter">
      <property name="supportedMediaTypes">
        <list>
          <value>text/html; charset=UTF-8</value>
        </list>
      </property>
    </bean>
    <!--
      Controller에서 JSON 리턴시 객체를 변환 해주기 위해서 MessageConverter가 필요하다. 
    -->
    <bean class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
      <property name="supportedMediaTypes">
        <list>
          <value>application/json; charset=UTF-8</value>
        </list>
      </property>
    </bean>
  </mvc:message-converters>

</mvc:annotation-driven>
```

# Controller
Controller는  
필수적으로 `@ResponceBody`  
권장 사항으로  `produces = "application/json; charset=UTF-8"` 를 명시 해 준다.  
produces 속성은 Response의 Content-Type을 제어한다.
```
@RequestMapping(
    value = "/reqWaterDepth.do",
    method = RequestMethod.POST,
    produces = "application/json; charset=UTF-8")
@ResponseBody
public Map<String, List<WaterDepthDTO>> reqWaterDepth (
    HttpServletRequest req,
    HttpServletResponse res,
    @RequestParam Map<String, String> area
      ) throws Exception {
  logger.info("\n\tREQ Water Depth . do \n" + area.get("area") + "\n");
  return service.getWaterDepthInRange(area.get("area"));
}
```

# Ajax Request
Request 부분에서는 딱히 설정할 것이 없다.
```
function getData(area) {
  return new Promise(function(resolve, reject) {
    console.group('drawSurvArea getData');
    area = {
      'area' : area
    };
    //  console.log( area );
    $.ajax({
      type : "POST",
      url : "/reqWaterDepth.do",
      data : area,
      dataType : 'json',
      beforeSend : function(xhr, opts) {
        console.log("before send");
        // when validation is false
        if (false) {
          xhr.abort();
        }
      },
      success : function(res) {
        console.log("SUCCESS");
        console.log(res);
        if (Object.keys(res).length === 0
            && JSON.stringify(res) === JSON.stringify({})) {
          alert('해당 영역에 수심데이터가 없습니다.');
          return;
        } else {
          resolve(res);
        }
      },
      error : function(err) {
        console.log("ERR");
        console.error(err.statusText);
        alert('데이터를 처리 하던중 에러가 발생했습니다.\n' + err.statusText + ' : ' + err.status);
        reject(err);
      }
    });
    console.groupEnd('drawSurvArea getData');
  })
}
getData(area).then(function(data) {
  console.log('THEN')
  console.log(data);
});
```