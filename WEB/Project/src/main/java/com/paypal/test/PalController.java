package com.paypal.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/paypal")
public class PalController {
  
  //  PAYPAL 공통 변수
  private static final String STATUS = "TEST";
  private static String PARAM_AT_VALUE = "AkgQb6Ohw5xH4skCbbZZeyFBRJNDA5mKE3Nw6i8DEQ8UQgX9Vie8P0n1";
  private static String URL_PAYPAL_VALIDATE = "https://ipnpb.sandbox.paypal.com/cgi-bin/webscr";
  private static String LOG_DIR ="/www/e-navap_org/www/paypal_log/";
  
  //  PAYPAL 공통 정수
  private static final String PARAM_TX = "tx";
  private static final String PARAM_CMD = "cmd";
  private static final String PARAM_AT = "at";

  // PDT 응답 정수
  private static final String PARAM_CMD_VALUE_PDT = "_notify-synch";
  private static final String RESPONSE_SUCCESS = "SUCCESS";
  private static final String RESPONSE_FAIL = "FAIL";
  
  // IPN 응답 정수
  private static final String PARAM_CMD_VALUE_IPN = "_notify-validate";
  private static final String IPN_RESPONSE_SUCCESS = "VERIFIED";
  private static final String IPN_RESPONSE_FAIL = "FAIL";
  private static final String IPN_RESPONSE_INVALID = "INVALID";
  
  //private static final String PARAM_ITEM_NAME = "item_name";    // 상품이름
  //private static final String PARAM_ITEM_NUMBER = "item_number";   // 상품번호
  //private static final String PARAM_PAYMENT_STATUS = "payment_status";       // 결제 상태
  //private static final String PARAM_MC_GROSS = "mc_gross";    // 페이팔 결제금액
  //private static final String PARAM_MC_FEE = "mc_fee";     // 페이팔 수수료금액
  //private static final String PARAM_MC_CURRENCY = "mc_currency";   // 화폐
  //private static final String PARAM_TXN_ID = "txn_id";     // 거래번호
  //private static final String PARAM_RECEIVER_EMAIL = "receiver_email";    // 페이팔 판매자계정 이메일
  //private static final String PARAM_PAYER_EMAIL = "payer_email";          // 페이팔 구매자계정 이메일
  //private static final String PARAM_CUSTOM = "custom";     // 상점회원번호
  
  private static final String FLAG_PDT = "PDT";
  private static final String FLAG_CNCL = "CNCL";
  private static final String FLAG_IPN = "IPN";
  
  StringBuffer sb = null; // 결과값 넣을 StringBuilder
  
  @RequestMapping("/payment.do")
  public String index(HttpServletRequest request, HttpServletResponse response) throws Exception {
    System.out.println("/paypal/index");
    return "paypal/payment";
  }
  
  /**
   * paypal에서 동기식으로 결제 결과를 보내줄 때 실행 되는 메소드.
   * @param request
   * @param model
   * @param session
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/pdt_return.do")
  public String pdt_return(HttpServletRequest request, ModelMap model, HttpSession session) throws Exception {
    System.out.println("\n\tPalController.pdt_return() START :: " + new Date());
    String returnUrl = "/error";
    Map<String, Object> resultMap = palCmnct(request, FLAG_PDT);
    sb = new StringBuffer(); // 결과값 넣을 StringBuilder
    
    /**  resultMap에 저장되어 있는 정보
     * resultMsg : 화면에 보여줄 결제 완료 메세지 StringBuilder
     * payManage : payManage
     * returnUrl : 컨트롤러에서 리턴할 값
     */
//    PayManage payManage = (PayManage) resultMap.get("payManage");
//    model.addAttribute("payManage", payManage);
    
    sb = (StringBuffer) resultMap.get("resultMsg");
    model.addAttribute("appresult", sb);
    
    returnUrl = (String) resultMap.get("returnUrl");
    
    System.out.println("\n\tPalController.pdt_return() END :: " + new Date());
    return returnUrl;
  }
  
  /**
   * paypal에서 비동기 식으로 결제 결과를 보내줄 때 실행 되는 메소드.
   */
  @RequestMapping(value = "/ipn_return.do")
  public void ipn_return(HttpServletRequest request, HttpSession session) throws Exception {
    System.out.println("\n\tPalController.ipn_return() START :: " + new Date());
    palCmnct(request, FLAG_IPN);
    System.out.println("\n\tPalController.ipn_return() END :: " + new Date());
  }
  
  /**
   * 결제 도중에 취소 되면 처리 하는 곳.?
   */
  @RequestMapping(value = "/cncl_return.do")
  public @ResponseBody String cncl_return(HttpServletRequest request, HttpSession session) throws Exception {
    System.out.println("\n\tPalController.cncl_return() START :: " + new Date());
    palCmnct(request, FLAG_CNCL);
    System.out.println("\n\tPalController.cncl_return() END :: " + new Date());
    return "";
  }
  
  

  /**
   * paypal에서 결제 시 동기, 비동기 식 두가지 방법으로 보내주고, 환불값이 오므로 결제 처리를 하는 실직적인 method.
   * 
   * resultMap에 저장되어 있는 정보
   * resultMsg : 화면에 보여줄 결제 완료 메세지 StringBuilder
   * payManage : payManage
   * returnUrl : 컨트롤러에서 리턴할 값
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  private Map<String, Object> palCmnct(HttpServletRequest request, String methodFlag) throws Exception {
    LogSaver ls = new LogSaver();     //  LOG 저장을 위한 String
    ls.log("\n\tPalController.palCmnt() Called From : " + methodFlag);
    Map<String, Object> resultMap = new HashMap<>();;
    String master_no = "";            //  결제자 index
    Boolean isRefund = false;         //  요청인 환불인지 아닌지
    sb = new StringBuffer(); // 결과값 넣을 StringBuilder
    
    
    
    
    /*  paypal과 통신 -----------> */
    // -> 유효성 검사를 하기 위해 파라미터(str)를 구성한다.
    ls.log("\n:: " + methodFlag + " REQUEST PARAM LIST ->\n");
    Enumeration en = request.getParameterNames();
    String str = "";
    switch(methodFlag){
      case FLAG_PDT : str = "?" + PARAM_CMD + "=" + PARAM_CMD_VALUE_PDT;
        break;
      case FLAG_IPN : str = "?" + PARAM_CMD + "=" + PARAM_CMD_VALUE_IPN;
        break;
      default : str = "?" + PARAM_CMD + "=" + PARAM_CMD_VALUE_PDT;
    }
    ls.log(str);
    en = request.getParameterNames();
    while (en.hasMoreElements()) {
      String paramName = (String) en.nextElement();
      String paramValue = request.getParameter(paramName);
      //  이미 DB에 결제 정보가 있는지 확인하기 위한 Index
      if(paramName.equals("cm")){
        master_no = paramValue;
      } else if(paramName.equals("custom")){
        master_no = paramValue;
      }
      str += "&" + paramName + "=" + URLEncoder.encode(paramValue, "UTF-8");
      ls.log("\n" + paramName + " : " + URLEncoder.encode(paramValue, "UTF-8"));
      if(methodFlag.equals(FLAG_IPN)) resultMap.put(paramName, URLEncoder.encode(paramValue, "UTF-8"));
    }
    if( methodFlag.equals(FLAG_IPN) ){
      ls.log("\n is it refund ?");
      try {
        //  요청이 환불인지 확인
        if( ((String) resultMap.get("payment_status")).toLowerCase().contains("rufund") ||
            ((String) resultMap.get("reason_code")).toLowerCase().contains("refund")  ){
          isRefund = true;
        }
      } catch(Exception e) {
        ls.log("\n\t:::::ERROR:::::" + new Date());
        ls.log("\tCAUSE -> " + e.getCause());
        ls.log("\tMESSAGE -> " + e.getMessage());
        e.printStackTrace();
        ls.save(master_no);
      }
      ls.log("\t" + isRefund);
    }
    str += "&" + PARAM_AT + "=" + PARAM_AT_VALUE;
    ls.log("\n" + PARAM_AT + " : " + PARAM_AT_VALUE);
    ls.log("\n<- REQUEST PARAM LIST ::\n");
    ls.log("\nSending to PayPal :\n" + str);
    // <- 유효성 검사를 위한 str
    
    
    // -> 유효성을 검사하기 위해 PayPal로 다시 전송시작.
    ls.log("\n:: " +  methodFlag + " VALIDITY CHECK 1 ::\t " + URL_PAYPAL_VALIDATE + "\n");
    URL url = new URL(URL_PAYPAL_VALIDATE);
    HttpURLConnection rulConn = (HttpURLConnection) url.openConnection();
    rulConn.setDoOutput(true);
    rulConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    PrintWriter pw  = null;
    try{
      pw = new PrintWriter(rulConn.getOutputStream());
      pw.println(str);
    } catch(Exception e){
      ls.log("\n\t:::::ERROR:::::" + new Date());
      ls.log("\tCAUSE -> " + e.getCause());
      ls.log("\tMESSAGE -> " + e.getMessage());
      e.printStackTrace();
      ls.save(master_no);
    } finally {
      try{ if(pw != null) { pw.close(); } } catch(Exception e) { ls.log("\tERR MESSAGE -> " + e.getMessage()); ls.save(master_no);}
    }
    // <- 유효성 검사.
    ls.log("\n" + methodFlag + "\n::VALIDITY CHECK 1 END URL::\n\t" + url.toString() + "\t" + str);
    //  -> 유효성 검사 결과.
    String res = "";
    BufferedReader in = new BufferedReader(new InputStreamReader(rulConn.getInputStream()));
    try {
      res = in.readLine();
      ls.log("\n" + methodFlag + "\n:: VALIDITY CHECK 1 RETURN RES : " + res + "\n");
    } catch(Exception e) {
      ls.log("\n\t:::::ERROR:::::" + new Date());
      ls.log("\tCAUSE -> " + e.getCause());
      ls.log("\tMESSAGE -> " + e.getMessage());
      ls.save(master_no);
      e.printStackTrace();
    }
    //  <- 유효성 검사 결과.
    
    String resultCode = "";
    
    //  --> 통신 방식에 따른 결제 유효성 결과 실패시 처리.
    ls.log("\n\n::: " + methodFlag + " PAYPAL VALIDITY CHECK 2 :::\nSTATUS : " + res);
    if( methodFlag.equals(FLAG_IPN) ){
      if( !res.equals(IPN_RESPONSE_SUCCESS) ){
        //  IPN에서 유효성이 안 맞다면 로그만 남겨두자.
        ls.log("/n IPN RESPONSE INVERIFIED at" + new Date() );
        ls.save(master_no);
        return null;
      }
    } else if ( methodFlag.equals(FLAG_PDT) ){
      if ( !res.equals(RESPONSE_SUCCESS) ){
        //  PDT에서 결제가 유효하지 않다면.
        resultMap.clear();
        resultCode = res;
        sb.append("<p>Result Faild</p>");
        sb.append("<p>Result Code : " + resultCode +"</p>");
        resultMap.put("resultMsg", sb );
        resultMap.put("returnUrl", "/asiapacific/asiaregistration_step04");
        ls.log("/n PDT RESPONSE NOT SUCCESS at" + new Date() );
        ls.save(master_no);
        return resultMap;
      }
    }
    ls.log("\n::: " + methodFlag + "PAYPAL Process END:::\n\n");
    //  <- 결제 유효성 실패 처리
    /*  <-----------  paypal과 통신    */
    //  사실상, 결과 로그 저장은 여기까지만 해도 됨.

    
    
    /*
    // -> 중복 검사, ORDR_APPROVAL_AP table의 MASTER_NO에 값이 있다면 이미 결제가 된 것 이니 리턴하자.
    Boolean alreadyPaid = payManageDAO.payCheck( Integer.parseInt(master_no) );
    ls.log("\nCustomer NO : " + master_no + " is already exist? " + alreadyPaid);
    
    //  IPN으로 결과를 받았을 때 이미 결제가 된 계정이면 아무것도 하지 않고 리턴,
    //  if( methodFlag.equals(FLAG_IPN) ) return null;
    //  환불이 아니지만 이미 결제가 된 계정이라면 결제가 이미 완료 되었다고 리턴?
    if(  !isRefund && (alreadyPaid != null && alreadyPaid == true)    ){
      
      System.out.println("\tis already PAID");
      
      //  이미 결제 됬다고 화면에 보여줄 sb
      sb.append("<tr><th class='td01'><p>Result</p></th>");
      sb.append("<td class='td02'><p> The payment has been completed. </p></td></tr>");
      sb.append("<td class='td02'><p> If you didn't get the 'Payment Complete Mail'. </p></td></tr>");
      sb.append("<td class='td02'><p> Please contact secretariat@e-navap.org. </p></td></tr>");
      
      resultMap.put("resultMsg", sb );
      
      PayManage payManage = new PayManage();
      payManage.setMaster_no(master_no);
      payManage = payManageDAO.selectPayInfoByMasterNo(payManage);
      resultMap.put("payManage", payManage);
      resultMap.put("returnUrl", "/asiapacific/asiaregistration_step04");
      
      //  LOG를 파일로 저장하자.
      ls.log("\n:::::::::: PAYPAL REQUEST PROCESS END AT " + new Date() + " ::::::::::\n " + methodFlag + "\n");
      ls.save(master_no);
      return resultMap;
    }
    // <- 중복 검사
    */
    
    /**
     * 여기서부터 경우의 수
     * ipn || pdt
     * 결제 || 환불
     */
    
    
    
    
    //  IPN PDT 둘다 결제 성공, 결제 값이 아직 DB에 저장되지 않은 경우
    //  결과를 PayManage로 변환 시킨 뒤, DB에 저장한다?
    
    
    //  IPN에서 성공시.
    //  어? 처음 온 값 저장하는 거 잖아 이건...
    //  IPN처리를 위해 resultMap에 저장
    //  결제 성공일 경우
    //  이미 값이 있다면 저장하지 않아야 함.
    //  값이 없다면 저장.
    
    //////  IPN으로 결과가 들어 왔는데, 결제 상태가 환불 이라면?
    if( isRefund )
    
    //  PDT에서
    resultCode = res;
    HashMap result = new HashMap();
    
    //  res를 배열로 조개서 hashmap에 담는다.
    String[] temp;
    while ((res = in.readLine()) != null) {
      temp = res.split("=");
      if (temp.length == 2) {
        result.put(temp[0], URLDecoder.decode(temp[1], "UTF-8"));
        ls.log(temp[0] + " : " + URLDecoder.decode(temp[1], "UTF-8"));
      } else {
        result.put(temp[0], "");
        ls.log("\n" + temp[0]);
      }
    }
    
    
    
    
    /*
    ls.log("\n" + methodFlag + ":: PAYMENT VALIDITY CHECK END SAVING START ::");
    //  log를 파일에 저장할 때 사용할 변수   결제자 이름, 결제 완료 시간
    String customerMasterNo = "Customer Master No : ";
    
    //  -> 결제 데이터가 없고 결제 정보가 유효하다면 객체로 받자. 다만 예외가 잘 나니 예외처리 하자.
    ls.log("\n" + methodFlag + "\n:: SAVE RESULT TO DB ::");
    try{
      PayManage payManage = new PayManage();
      payManage.setMaster_no( (String)result.get("custom"));
      customerMasterNo += (String)result.get("custom");
      payManage = payManageDAO.selectPayInfoByMasterNo(payManage);
      ls.log("\tPayManaget.getMaster_no() : " + payManage.getMaster_no());
      payManage.setResultcode(  resultCode);
      payManage.setResultmsg(   resultCode);
      payManage.setTid(       (String)result.get("txn_id"));
      payManage.setTotprice(  (String)result.get("mc_gross"));
      payManage.setMoid(      (String)result.get("txn_id"));
      payManage.setAppldate(  ((String)result.get("payment_date")).substring(9));
      
      payManage.setAppltime( ((String)result.get("payment_date")).substring(0, 9).trim());
      payManage.setGoodname(  (String)result.get("item_name"));
      payManage.setFlag("P");
      
      // 이전 데이터 삭제
      payManageDAO.deleteApprovalAP(payManage);
      
      //  데이터 입력
      payManageDAO.insertApprovalAP(payManage);
      
      //  결제 완료 등록
      payManage.setOrdr_pass("2");
      payManageDAO.updateORDRMaster(payManage);
      
      resultMap.clear();
      
      //  payManage의 데이터가 성공적으로 저장 됬다면 저장
      resultMap.put("payManage", payManage);
      resultMap.put("returnUrl", "/asiapacific/asiaregistration_step04");
      
      //  등록 완료 메일 보내기.
      //EgovMultiPartEmail eem = new EgovMultiPartEmail();
      //eem.sendApprovalMail(payManage);

      //  화면에 보여줄 결제 완료 메세지.
      //sb.append(eem.getApprovalMessage(payManage));
      resultMap.put("resultMsg", sb );
    }  catch(Exception e) {
       ls.log("\n\t:::::ERROR:::::" + new Date());
       ls.log("\tCAUSE -> " + e.getCause());
       ls.log("\tMESSAGE -> " + e.getMessage());
       e.printStackTrace();
       ls.save(master_no);
    }
    */
    
    ls.log("\n" + methodFlag + "\n:::::::::: PAYPAL PROCESS END AT " + new Date() + " ::::::::::\n");
    //  LOG를 파일로 저장하자.
    ls.log("\n\tPalController.palCmnt() END!!");
    ls.save(master_no);
    //  메소드 출처에 따라 리턴 값을 다르게 하자.
    if(methodFlag.equals(FLAG_PDT)){
      return resultMap;
    } else {
      return null;
    }
  }
  
  //  결과를 DB에 저장하기 위한 method
  //  결과를 payManage로 변환하는 method
}