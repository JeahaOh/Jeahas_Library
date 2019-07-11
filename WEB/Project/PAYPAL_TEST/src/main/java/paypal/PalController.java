//package paypal;
//
//import java.io.BufferedOutputStream;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLDecoder;
//import java.net.URLEncoder;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Enumeration;
//import java.util.HashMap;
//import java.util.Locale;
//import java.util.Map;
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//@Controller
//public class PalController {
//  private static final String STATUS = EgovProperties.getProperty("Globals.status");
//  private static String PARAM_AT_VALUE;
//  private static String URL_PAYPAL_VALIDATE;
//  private static String LOG_DIR;
//  
//  static {
//    switch(STATUS){
//      case "TEST" :
//        System.out.println("\t:: PalController " + STATUS + " Mode ::");
//        PARAM_AT_VALUE = EgovProperties.getProperty("Globals.paypal.tkn.test");
//        URL_PAYPAL_VALIDATE = EgovProperties.getProperty("Globals.paypal.url.test");
//        LOG_DIR = EgovProperties.getProperty("Globals.log.dir.test");
//        break;
//      case "REAL" :
//        System.out.println("\t:: PalController " + STATUS + " Mode ::");
//        PARAM_AT_VALUE = EgovProperties.getProperty("Globals.paypal.tkn.real");
//        URL_PAYPAL_VALIDATE = EgovProperties.getProperty("Globals.paypal.url.real");
//        LOG_DIR = EgovProperties.getProperty("Globals.log.dir.real");
//        break;
//    }
//    if(PARAM_AT_VALUE.length() < 5) System.out.println(":::::::::: ERROR PARAM_AT_VALUE IS NOT DETECTED :::::::::");
//    if(URL_PAYPAL_VALIDATE.length() < 5) System.out.println(":::::::::: ERROR URL_PAYPAL_VALIDATE IS NOT DETECTED :::::::::");
//    if(LOG_DIR.length() < 5) System.out.println(":::::::::: ERROR LOG_DIR IS NOT DETECTED :::::::::");
//  }
//  
//  // PDT 첫번째 응답 변수
//  private static final String PARAM_TX = "tx";
//  private static final String PARAM_CMD = "cmd";
//  private static final String PARAM_CMD_VALUE = "_notify-synch";
//  private static final String PARAM_AT = "at";
//  private static final String RESPONSE_SUCCESS = "SUCCESS";
//  private static final String RESPONSE_FAIL = "FAIL";
//  
////  private static final String PARAM_ITEM_NAME = "item_name";    // 상품이름
////  private static final String PARAM_ITEM_NUMBER = "item_number";   // 상품번호
////  private static final String PARAM_PAYMENT_STATUS = "payment_status";       // 결제 상태
////  private static final String PARAM_MC_GROSS = "mc_gross";    // 페이팔 결제금액
////  private static final String PARAM_MC_FEE = "mc_fee";     // 페이팔 수수료금액
////  private static final String PARAM_MC_CURRENCY = "mc_currency";   // 화폐
////  private static final String PARAM_TXN_ID = "txn_id";     // 거래번호
////  private static final String PARAM_RECEIVER_EMAIL = "receiver_email";    // 페이팔 판매자계정 이메일
////  private static final String PARAM_PAYER_EMAIL = "payer_email";          // 페이팔 구매자계정 이메일
////  private static final String PARAM_CUSTOM = "custom";     // 상점회원번호
//  
//  private static final String FLAG_PDT = "PDT";
//  private static final String FLAG_CNCL = "CNCL";
//  private static final String FLAG_IPN = "IPN";
//  
//  StringBuffer sb = null; // 결과값 넣을 StringBuilder
//  
//
//  @Resource(name = "PayManageDAO")
//  private PayManageDAO payManageDAO;
//  
//  @Resource(name = "EgovCmmUseService")
//  private EgovCmmUseService cmmUseService;
//  
//  @Resource(name = "cmmUseDAO")
//  private CmmUseDAO cmmUseDAO;
//  
//  /**
//   * 일단 사용하지 않는 메소드
//   */
//  @RequestMapping(value = "/com/gmtc/paypal/return.do")
//  public @ResponseBody String Rturn(HttpServletRequest request, HttpSession session) throws Exception {
//    System.out.println("\n\tPalController.Rtrun() START"  + new Date());
//    System.out.println("\n\tPalController.Rtrun() END");
//    return new Date() + "";
//  }
//  
//  /**
//   * paypal에서 동기식으로 결제 결과를 보내줄 때 실행 되는 메소드.
//   * @param request
//   * @param model
//   * @param session
//   * @return
//   * @throws Exception
//   */
//  @RequestMapping(value = "/com/gmtc/paypal/pdt_return.do")
//  public String pdt_return(HttpServletRequest request, ModelMap model, HttpSession session) throws Exception {
//    System.out.println("\n\tPalController.pdt_return() START :: " + new Date());
//    String returnUrl = "/error";
//    Map<String, Object> resultMap = palCmnct(request, FLAG_PDT);
//    sb = new StringBuffer(); // 결과값 넣을 StringBuilder
//    
//    /**  resultMap에 저장되어 있는 정보
//     * resultMsg : 화면에 보여줄 결제 완료 메세지 StringBuilder
//     * payManage : payManage
//     * returnUrl : 컨트롤러에서 리턴할 값
//     */
//    
//    PayManage payManage = (PayManage) resultMap.get("payManage");
//    model.addAttribute("payManage", payManage);
//    
//    sb = (StringBuffer) resultMap.get("resultMsg");
//    model.addAttribute("appresult", sb);
//    
//    returnUrl = (String) resultMap.get("returnUrl");
//    
//    System.out.println("\n\tPalController.pdt_return() END :: " + new Date());
//    return returnUrl;
//  }
//  
//  /**
//   * paypal에서 비동기 식으로 결제 결과를 보내줄 때 실행 되는 메소드.
//   */
//  @RequestMapping(value = "/com/gmtc/paypal/ipn_return.do")
//  public void ipn_return(HttpServletRequest request, HttpSession session) throws Exception {
//    System.out.println("\n\tPalController.ipn_return() START :: " + new Date());
//    palCmnct(request, FLAG_IPN);
//    System.out.println("\n\tPalController.ipn_return() END :: " + new Date());
//  }
//  
//  /**
//   * 여기는 환불할때 호출 할 거 같은데....
//   */
//  @RequestMapping(value = "/com/gmtc/paypal/cncl_return.do")
//  public @ResponseBody String cncl_return(HttpServletRequest request, HttpSession session) throws Exception {
//    System.out.println("\n\tPalController.cncl_return() START :: " + new Date());
//    palCmnct(request, FLAG_CNCL);
//    System.out.println("\n\tPalController.cncl_return() END :: " + new Date());
//    return "";
//  }
//  
//  /**
//   * paypal에서 결제 시 동기, 비동기 식 두가지 방법으로 보내주고, 환불값이 오므로 결제 처리를 하는 실직적인 method.
//   * 
//   * resultMap에 저장되어 있는 정보
//   * resultMsg : 화면에 보여줄 결제 완료 메세지 StringBuilder
//   * payManage : payManage
//   * returnUrl : 컨트롤러에서 리턴할 값
//   */
//  @SuppressWarnings({"rawtypes", "unchecked"})
//  private Map<String, Object> palCmnct(HttpServletRequest request, String methodFlag) throws Exception {
//    System.out.println("\n\tPalController.palCmnt() Called From : " + methodFlag);
//    
//    Map<String, Object> resultMap = new HashMap<>();;
//    String master_no = "";
//    String log = "";//  LOG 저장을 위한 String
//    sb = new StringBuffer(); // 결과값 넣을 StringBuilder
//    
//    //  환불처리의 경우는 어떻게 하지? 아직 환불은 안하니까... 표시만 해두자..
//    if(methodFlag.equals(FLAG_CNCL)){
//      log += "\\n\n!!!! IMPORTANT :: PAYPAL CANCEL REQUEST IS COME !!!!\n\n";
//      //  logger.info("\\n\n!!!! IMPORTANT :: PAYPAL CANCEL REQUEST IS COME !!!!\n\n");
//    } else {
//      log += ":::::::::: PAYPAL REQUEST IS COME TO " + methodFlag + " AT " + new Date() + " ::::::::::\n";
//    }
//    
//    // -> 유효성 검사를 하기 위해 파라미터(str)를 구성한다.
//    Enumeration en = request.getParameterNames();
//    String str = PARAM_CMD + "=" + PARAM_CMD_VALUE;
//    log += "\n "+ methodFlag + " :: REQUEST PARAM LIST ->\n";
//    log += PARAM_CMD + " : " + PARAM_CMD_VALUE;
//    en = request.getParameterNames();
//    while (en.hasMoreElements()) {
//      String paramName = (String) en.nextElement();
//      String paramValue = request.getParameter(paramName);
//      str += "&" + paramName + "=" + URLEncoder.encode(paramValue, "UTF-8");
//      log += "\n" + paramName + " : " + URLEncoder.encode(paramValue, "UTF-8");
//      
//      System.out.println(paramName + " : " + paramValue);
//      //  이미 DB에 결제 정보가 있는지 확인하기 위한 Index
//      if(paramName.equals("cm")){
//        master_no = paramValue;
//      } else if(paramName.equals("custom")){
//        master_no = paramValue;
//      }
//    }
//    str += "&" + PARAM_AT + "=" + PARAM_AT_VALUE;
//    log += "\n" + PARAM_AT + " : " + PARAM_AT_VALUE;
//    System.out.println( methodFlag + "\nSending to PayPal:" + str);
//    log += "\n<- REQUEST PARAM LIST ::  " + methodFlag  + "\n\n";
//    // <- 유효성 검사를 위한 str
//    
//    // -> 중복 검사, ORDR_APPROVAL_AP table의 MASTER_NO에 값이 있다면 이미 결제가 된 것 이니 리턴하자.
//    System.out.println("Customer NO : " + master_no);
//    Boolean alreadyPaid = payManageDAO.payCheck( Integer.parseInt(master_no) );
//    log += "Customer NO : " + master_no + " is duplication " + alreadyPaid;
//    
//    if(alreadyPaid != null && alreadyPaid == true){
//      System.out.println("\tis already PAID");
//      
//      //  이미 결제 됬다고 화면에 보여줄 sb
//      sb.append("<tr><th class='td01'><p>Result</p></th>");
//      sb.append("<td class='td02'><p> The payment has already been completed. </p></td></tr>");
//      sb.append("<td class='td02'><p> If you didn't get the 'Payment Complete Mail'. </p></td></tr>");
//      sb.append("<td class='td02'><p> Please contact secretariat@e-navap.org. </p></td></tr>");
//      
//      resultMap.put("resultMsg", sb );
//      
//      PayManage payManage = new PayManage();
//      payManage.setMaster_no(master_no);
//      payManage = payManageDAO.selectPayInfoByMasterNo(payManage);
//      resultMap.put("payManage", payManage);
//      resultMap.put("returnUrl", "/asiapacific/asiaregistration_step04");
//      
//      //  LOG를 파일로 저장하자.
//      log += "\n:::::::::: PAYPAL REQUEST PROCESSIN END AT " + new Date() + " ::::::::::\n " + methodFlag + "\n";
//      palLogSaver(log, master_no);
//      return resultMap;
//    } else {
//      System.out.println("\tis not paid\n\n");
//    }
//    // <- 중복 검사
//
//    // -> 유효성을 검사하기 위해 PayPal로 다시 전송시작.
//    log += "\n\n" +  methodFlag + "\n:: VALIDITI TEST 1 ::\n";
//    URL url = new URL(URL_PAYPAL_VALIDATE);
//    System.out.println(URL_PAYPAL_VALIDATE);
//    //  logger.info(URL_PAYPAL_VALIDATE);
//    HttpURLConnection rulConn = (HttpURLConnection) url.openConnection();
//    rulConn.setDoOutput(true);
//    rulConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//    PrintWriter pw  = null;
//    try{
//      pw = new PrintWriter(rulConn.getOutputStream());
//      pw.println(str);
//    } catch(Exception e){
//      System.out.println("\n\t:::::ERROR:::::" + new Date());
//      System.out.println("\tCAUSE -> " + e.getCause());
//      System.out.println("\tMESSAGE -> " + e.getMessage());
//      e.printStackTrace();
//    } finally {
//      try{ if(pw != null) { pw.close(); } } catch(Exception e) { System.out.println("\tERR MESSAGE -> " + e.getMessage()); }
//    }
//    // <- 유효성 검사.
//    System.out.println("\n" + methodFlag + "\n::VALIDITY TEST 1 URL::\n\t" + url.toString() + "\t" + str);
//    log += "\n" + methodFlag + " \tURL :\n" + url.toString() + str;
//    //  -> 유효성 검사 결과.
//    String res = "";
//    BufferedReader in = new BufferedReader(new InputStreamReader(rulConn.getInputStream()));
//    try {
//      res = in.readLine();
//      System.out.println("\n" + methodFlag + "\n:: VALIDITY TEST 1 RETURN RES : \n" + res + "\n");
//      log += "\n" + methodFlag + "\nVALIDITY TEST 1 :" + res + "\n\n";
//    } catch(Exception e) {
//      System.out.println("\n\t:::::ERROR:::::" + new Date());
//      System.out.println("\tCAUSE -> " + e.getCause());
//      System.out.println("\tMESSAGE -> " + e.getMessage());
//      e.printStackTrace();
//    }
//
//    //  <- 유효성 검사 결과.
//    Boolean isSuccessed = false;
//    HashMap vars = new HashMap();
//    String resultCode = "";
//    
//    //  -> 결제가 유효하다면.
//    if (res.equals(RESPONSE_SUCCESS)) {
//      isSuccessed = true;
//      System.out.println("\n" + methodFlag + "\n\n:::PAYPAL VALIDITY TEST 2 :::\nSTATUS : " + res);
//      log += "\n" + methodFlag + "\n:::PAYPAL VALIDITY TEST 2 ->\nSTATUS : " + res;
//      resultCode = res;
//      
//      String[] temp;
//      while ((res = in.readLine()) != null) {
//        temp = res.split("=");
//        if (temp.length == 2) {
//          vars.put(temp[0], URLDecoder.decode(temp[1], "UTF-8"));
//          System.out.println(temp[0] + " : " + URLDecoder.decode(temp[1], "UTF-8"));
//          log += "\n" + temp[0] + " : " + URLDecoder.decode(temp[1], "UTF-8");
//        } else {
//          vars.put(temp[0], "");
//          log += "\n" + temp[0];
//        }
//        //res.split(" ");
//      }
//      //  <-> 결제가 유효하지 않다면.
//    } else if (res.equals(RESPONSE_FAIL)) {
//      System.out.println("\n" + methodFlag + "\n::: PAYPAL VALIDITY TEST :::\nSTATUS : " + res);
//      log += "\n" + methodFlag + "\n:::PAYPAL VALIDITY TEST 2 ->\nSTATUS : " + res + "\n\n";
//    } else {
//      System.out.println("\n" + methodFlag + "\n!!! PAYPAL VALIDITY TEST !!!\nSTATUS : " + res);
//      log += "\n" + methodFlag + "\n:::PAYPAL VALIDITY TEST 2 ->\nSTATUS : " + res  + "\nSERVER RESPONSE VALUE IS NULL\n\n";
//    }
//    System.out.println("\n" + methodFlag + "\n\n:::PAYPAL VALIDITY TEST 2 END:::\n\n");
//    log += "\n<- PAYPAL VALIDITY TEST 2 END:::"+"\n" + methodFlag + "\n\n";
//    //  <- 결제 유효성
//    
//    System.out.println("\n" + methodFlag + ":: PAYMENT VALIDITY TEST END SAVING START ::");
//
//    //  log를 파일에 저장할 때 사용할 변수   결제자 이름, 결제 완료 시간
//    String customerMasterNo = "Customer Master No : ";
//    
//    //  -> 결제 데이터가 없고 결제 정보가 유효하다면 객체로 받자. 다만 예외가 잘 나니 예외처리 하자.
//    log += "\n" + methodFlag + "\n:: SAVE RESULT TO DB ::";
//    try{
//      System.out.println(isSuccessed);
//      PayManage payManage = new PayManage();
//      if(isSuccessed){
//        payManage.setMaster_no( (String)vars.get("custom"));
//        customerMasterNo += (String)vars.get("custom");
//        payManage = payManageDAO.selectPayInfoByMasterNo(payManage);
//        System.out.println("\tPayManaget.getMaster_no() : " + payManage.getMaster_no());
//        payManage.setResultcode(  resultCode);
//        payManage.setResultmsg(   resultCode);
//        payManage.setTid(       (String)vars.get("txn_id"));
//        payManage.setTotprice(  (String)vars.get("mc_gross"));
//        payManage.setMoid(      (String)vars.get("txn_id"));
//        payManage.setAppldate(  ((String)vars.get("payment_date")).substring(9));
//        
//        payManage.setAppltime( ((String)vars.get("payment_date")).substring(0, 9).trim());
//        payManage.setGoodname(  (String)vars.get("item_name"));
//        payManage.setFlag("P");
//        
//        // 이전 데이터 삭제      할 필요가 있나?
//        payManageDAO.deleteApprovalAP(payManage);
//        
//        //  데이터 입력
//        payManageDAO.insertApprovalAP(payManage);
//        
//        //  결제 완료 등록
//        payManage.setOrdr_pass("2");
//        payManageDAO.updateORDRMaster(payManage);
//        
//        //  payManage의 데이터가 성공적으로 저장 됬다면 저장
//        resultMap.put("payManage", payManage);
//        resultMap.put("returnUrl", "/asiapacific/asiaregistration_step04");
//        
//        //  등록 완료 메일 보내기.
//        EgovMultiPartEmail eem = new EgovMultiPartEmail();
//        eem.sendApprovalMail(payManage);
//
//        //  화면에 보여줄 결제 완료 메세지.
//        sb.append(eem.getApprovalMessage(payManage));
//        resultMap.put("resultMsg", sb );
//      }  else  {
//        sb.append("<p>Result Faild</p>");
//        sb.append("<p>Result Code : " + resultCode +"</p>");
//        resultMap.put("resultMsg", sb );
//      }
//    } catch(Exception e) {
//      System.out.println("\n\t:::::ERROR:::::" + new Date());
//      System.out.println("\tCAUSE -> " + e.getCause());
//      System.out.println("\tMESSAGE -> " + e.getMessage());
//      e.printStackTrace();
//    }
//    
//    if(methodFlag.equals(FLAG_CNCL)){
//      log += "\n" + methodFlag + "\n\n!!!!!!!!!!!!!!! IMPORTANT :: PAYPAL CANCEL REQUEST IS END !!!!!!!!!!!!!!!\n\n";
//    }
//    
//    log += "\n" + methodFlag + "\n:::::::::: PAYPAL REQUEST PROCESSIN END AT " + new Date() + " ::::::::::\n";
//    //  LOG를 파일로 저장하자.
//    palLogSaver(log, customerMasterNo);
//    
//    System.out.println("\n\tPalController.palCmnt() END!!");
//    //  메소드 출처에 따라 리턴 값을 다르게 하자.
//    if(methodFlag.equals(FLAG_PDT)){
//      return resultMap;
//    } else {
//      return null;
//    }
//  }
//  /**
//   * 19.06.07 paypal 결제 정보 log를 txt 파일로 저장하기 위한 method
//   * 결제자 이름과 결제자 이메일
//   * jeaha
//   * @param log
//   * @return
//   * @throws Exception
//   */
//  private void palLogSaver(String log, String master_no) throws Exception {
//    System.out.println("\n::palLogSaver CALLED::");
//    //  로그 파일을 저장하기 위해 사용하는 변수
//    String log_time = new SimpleDateFormat ( "yyyy_MM_dd", Locale.KOREA ).format( new Date() );
//    String file_name = LOG_DIR + log_time + ".log";
//    
//    File file = new File(file_name);
//    FileWriter writer = null;
//    
//    // -> 파일 저장
//    try {
//      writer = new FileWriter(file, true);
//      writer.write(log);
//      writer.flush();
//      
//      System.out.println(":: " + master_no + " customer paypal log save::\n");
//    } catch (Exception e) {
//      System.out.println("!!! paypal log saving fail at " + log_time +" !!!");
//      System.out.println("\tCAUSE -> " + e.getCause());
//      System.out.println("\tMESSAGE -> " + e.getMessage());
//      e.printStackTrace();
//    } finally {
//      try{ if(writer != null) { writer.close(); } } catch(Exception e){ System.out.println("\t BufferdOutputStream Close Error MESSAGE -> " + e.getMessage()); }
//    }
//    //  <- 파일 저장
//  }
//}