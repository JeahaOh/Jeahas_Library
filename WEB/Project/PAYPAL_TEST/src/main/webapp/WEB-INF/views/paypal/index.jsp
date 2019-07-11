<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="kr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>TEST</title>
</head>
<% String siteDomain = request.getRequestURL().toString().replace(request.getRequestURI(), ""); %>
<body>
  <%-- paypal form start --%>
  <form id="paypal_form" action="https://www.sandbox.paypal.com/cgi-bin/webscr" method="post">
    <input type="text" name="business" value="sorrowkissed-facilitator@naver.com"/></br>
    <input type="text" name="amount" value="1"/></br>
    <input type="text" name="item_name" value="PAYPAL_TEST"/></br>
    <input type="text" name="item_number" value="0000"/></br>
    <input type="text" name="currency_code" value="USD"/></br>
    <input type="text" name="custom" value=""/></br>
    <input type="text" name="cmd" value="_xclick"/></br>
    <input type="text" name="charset" value="UTF-8"/></br>
    <%-- IPN 메세지 받을 url :
        /* IPN  (비동기 응답 메세지)
         * - 결제가 완료되면 결제 결과를 페이팔 서버네 보관해 두었다가 상점 서비스로 전송해 준다.
         * - 비동기 방식이므로, 구매자가 결제 완료 후에 바로 페이팔서버에서 상점 서버로 데이터를 전송해 주지 않음.
         * - 구매자가 결제후, 페이팔 창을 닫아버려도 페이팔 서버는 상점 서버에서 수신확인을 해 줄때까지 결제 결과를 알려주므로,
         */  결제 결과를 무조건 알기 위해서는 필수 값이다.
       --%>
    <input type="text" name="notify_url"     value="<%=siteDomain%>/paypal/ipn_return" size="50" /></br>
    <!-- 결제 취소 URL :  -->
    <input type="text" name="cancel_return" value="<%=siteDomain%>/paypal/cncl_return" size="50" />
    <input type="submit"/>
  </form>
  <%-- paypal form end --%>
</body>
</html>