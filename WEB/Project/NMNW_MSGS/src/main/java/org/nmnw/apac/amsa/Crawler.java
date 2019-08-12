package org.nmnw.apac.amsa;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.nmnw.apac.message.Message;
import org.nmnw.apac.request.Communicater;
import org.nmnw.apac.util.TimeHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("unused")
public class Crawler {

  private static final Logger logger = LoggerFactory.getLogger(Crawler.class);
  // Crawling 할 URL
  private static final String TARGET_URL =
      "https://www.operations.amsa.gov.au/AMSA.Web.MSIPublication/";

  private static Pattern PTN = null;
  private static Matcher MTCHR = null;

  public static Boolean PROGRESS = false;

  private static Document DOC = null;
  private static String NAVAREAxWARNINGS = "";

  private static String COASTAL_WARNINGS = "";
  private static String SAFETY_MSGS = "";

  private static String START_TIME = "";
  private static String SCRAPE_DATE = "";
  private static String TIME_FORMAT = "";


  private static int CRAWL_CNT = 0;
  private static final int CRAWL_MAX = 5;

  @Autowired
  private static Communicater cmmnctr;

  private static List<Message> SFT_MSG_LIST = null;
  private static List<Message> NAVxW_LIST = null;
  private static List<Message> CW_LIST = null;

  // 파일로 저장하기 위해 사용하는 변수들.
  private static BufferedOutputStream bs = null;
  private static final String HEAD = "AMSA_NMNW_";
  private static final String TAG = ".html.txt";
  private static final String SAVE_DIR = "C:/Users/GMTC_JH/eGov/workspace/Scrap.test/";

  public static void main(String[] args) {

    crawl();
  } // main

  public static void crawl() {


    // -> URL에서 HTML 파일 긁어오기
    while (!PROGRESS) {
      DOC = getDoc();
      // // logger.info(doc.toString());
      if (CRAWL_CNT == CRAWL_MAX)
        return;
    }

    // -> Part x. 별로 다시 나눔
    splitParts(DOC);
    // scrapDate

    // Part 1. Safety Messages: 처리?
    // progressPart1(SAFETY_MSGS);

    // Part 2. NAVAREA X warnings: 처리
    // progressPart2(NAVAREAxWARNINGS);

    // // Part 3. Coastal warnings: 처리
    progressPart3(COASTAL_WARNINGS);


    cmmnctr.deleteRequest();
    cmmnctr.sendListAsObject(SFT_MSG_LIST);
    cmmnctr.sendListAsObject(NAVxW_LIST);
    cmmnctr.sendListAsObject(CW_LIST);

    // System.out.println(CW_LIST.toString());
    initVals();



  }

  /**
   * Target URL 에서 HTML 파일을 통째로 반환.
   * 
   * @return HTML document
   */
  private static Document getDoc() {
    logger.info("TARGET URL : " + TARGET_URL);
    // Crawler를 실행하는 시점의 시간을 얻어옴.
    START_TIME = new SimpleDateFormat("yyy.MM.dd_HHmmss", Locale.KOREA).format(new Date());
    logger.info("========== " + START_TIME + " ========== cnt : " + CRAWL_CNT);
    CRAWL_CNT++;
    try {
      DOC = Jsoup.connect(TARGET_URL).timeout(600000).get();
      PROGRESS = true;
      logger.info("Getting documennt Success \t"
          + new SimpleDateFormat("HH.mm.ss", Locale.KOREA).format(new Date()));
      // -> 긁어온 HTML파일 원본 저장.
      // fileSave(START_TIME, DOC.toString());
      // <- 긁어온 HTML파일 원본 저장.
      return DOC;
    } catch (IOException e) {
      logger.info("Getting document Failed \t"
          + new SimpleDateFormat("HH.mm.ss", Locale.KOREA).format(new Date()));
      logger.info(e.getMessage());
      PROGRESS = false;
      return null;
    }
  }

  /**
   * HTML 파일에서 불필요한 태그들을 제거 한 뒤,
   * <hr>
   * tag를 기준으로 part별로 분할한 List 를 반환
   * 
   * @param doc
   * @return
   */
  private static List<String> getTrimedList(Document doc) {
    if (!PROGRESS)
      return null;
    // -> 불 필요한 태그 지우기
    doc.select("fieldset").remove();
    for (Element input : doc.select("input")) {
      input.remove();
    }
    // <- 불 필요한 태그 지우기

    List<String> parts = new ArrayList<>();
    // <hr> 태그로 나눠서 List로 뽑음.
    for (String part : doc.select("form").toString().split("<hr>")) {
      if (part.contains("Part 4") || part.contains("Part 5"))
        continue;
      parts.add(part);
    }
    logger.info("Trimed Valid Fragments : " + parts.size());
    return parts;
  }

  /**
   * HTML 파일을 getTrimedList로 List로 반환 받은 뒤, 필요한 전역 변수를 설정, Part 별로 전역 변수 할당.
   * 
   * @param doc
   */
  private static void splitParts(Document doc) {
    if (!PROGRESS)
      return;
    for (String part : getTrimedList(doc)) {
      part = part.replaceAll(" <[^>]*>", "").replaceAll("<[^>]*>", "").trim();

      // logger.info("\n\n"+ part + "\n\n");
      if (part.contains("current at")) {
        // Maritime Safety Information current at 070444 UTC Aug 19
        // 시간 정규 표현식 210205 UTC Jun 19
        PTN = Pattern.compile("\\d{2}\\d{2}\\d{2}\\s(UTC)\\s\\S{3}\\s\\d{2}");
        MTCHR = PTN.matcher(part);
        while (MTCHR.find()) {
          SCRAPE_DATE = MTCHR.group().replace("UTC", "").replace("  ", " ").trim();
        }
      } else if (part.contains("Part 1.")) {
        if (part.contains("SECURITE") && part.contains("NNNN"))
          SAFETY_MSGS = part;
      } else if (part.contains("Part 2.")) {
        NAVAREAxWARNINGS = part;
      } else if (part.contains("Part 3.")) {
        COASTAL_WARNINGS = part.replaceFirst("Part 3. Coastal warnings:", "").trim();
      } else if (part.contains("date-time format")) {
        TIME_FORMAT = getTimeFormat(part);
        SCRAPE_DATE = TimeHandle.unixTimeConvert(
            TimeHandle.dateFormatConvert(SCRAPE_DATE, TIME_FORMAT, TimeHandle.getDATETIME_form()))
            + "";
      }
    }
  }

  /**
   * HTML 문서의 제일 마지막에 `The date-time format used is "ddhhmmZ mon yy", where "Z" indicates UTC.` 으로
   * 선언한 시간 포멧을 가져온다.
   * 
   * @param target
   * @return
   */
  private static String getTimeFormat(String target) {
    if (!PROGRESS)
      return null;
    for (String a : target.trim().split("</p>")) {
      if (a.contains("date-time format")) {
        target = a;
      }
    }
    PTN = Pattern.compile("(\")\\w+\\s\\w+\\s\\w+(\")");
    MTCHR = PTN.matcher(target);
    if (MTCHR.find()) {
      target = MTCHR.group().replaceAll("\"", "").replace("Z", "").replace("mon", "MM").trim();
    }
    logger.info("Time Format : " + target);
    return target;
  }

  /**
   * Safty Message를 처리.
   * 
   * @param target
   */
  private static void progressPart1(String target) {
    if (!PROGRESS)
      return;
    if (!target.contains("SECURITE") && !target.contains("NNNN"))
      return;

    SFT_MSG_LIST = new ArrayList<Message>();
    Message sm;
    for (String msg : (target.replace("Part 1. Safety Messages:", "").trim().split("NNNN"))) {
      if (msg.trim().length() >= 10) {
        sm = new Message(msg.trim(), TIME_FORMAT);
        sm.setScrapDate(SCRAPE_DATE);
        SFT_MSG_LIST.add(sm);
      }

    }
    logger.info("");
  }

  /**
   * navarea X warnings 를 처리하기 위한 method. 형식이 일정치 않음으로 예외처리를 더 해줘야 함.
   * 
   * @param target
   */
  private static void progressPart2(String target) {
    if (!PROGRESS)
      return;
    if (target.length() < 10) {
      logger.info("\n\t!! SOMETHING IS WRONG AT PROGRESS NAVAREA X WARNINGS !!");
      return;
    } else {
      logger.info("\t:: progressPart2 ::");
    }

    target = target.replace("Part 2. NAVAREA X warnings:", "").trim();

    // 안필요한 값??
    // Pattern UTCDatePtn = Pattern.compile("\\d{2}.\\d{2}\\d{2}\\s(UTC)\\s\\S{3}\\s\\d{2}");

    // -> navarea X warning
    List<String> navareaXwarningList = new ArrayList<String>();

    for (String str : target.trim().split("NNNN")) {
      // logger.info("\ntemp : [\n" + temp.trim() + "\n]\n\n");
      if (str.toUpperCase().contains("WWW.AMSA.GOV.AU.")) {
        logger.info("no list : [{}]", str);

      } else {
        // Message nvaxW;
        // if (str.trim().length() >= 10) {
        // nvaxW = new Message(str.trim(), TIME_FORMAT);
        // nvaxW.setScrapDate(SCRAPE_DATE);
        // NAVxW_LIST.add(nvaxW);
        // }
        logger.info(str);
      }



    }
    // area 당 msg no
    /*
     * for (String area : temp.split("AREA ")) { if (!area.contains("NIL")) { area =
     * area.replace("AUSCOAST WARNINGS IN FORCE AT", "").replace(" AND", ",").trim();
     * logger.info(area); } }
     */
    // <- navarea X warning
    logger.info("\t:: progressPart2 ::\n\n");

  }

  /**
   * Part 3 Coastal Warnings 를 처리하기 위한 method.
   * 
   * @param target
   */
  private static List<Message> progressPart3(String target) {
    if (!PROGRESS)
      return null;
    if (target.length() < 10) {
      logger.info("\t!! SOMETHING IS WRONG AT PROGRESS COASTAL WARNINGS !!");
      PROGRESS = false;
      return null;
    }
    logger.info("\t:: progressPart3 ::");
    // -> coastalWarnings part를 구역을 나눈 뒤, 구역별 NMNW를 가져옴.
    CW_LIST = new ArrayList<>();
    String[] coastalWarnArr = target.split("AUSCOAST coastal warning - area ");
    logger.info("Count of CW AREA : " + coastalWarnArr.length);

    String region;
    Message cw;
    for (String warnArea : coastalWarnArr) { // 데이터가 없다면 작업.
      if (warnArea.length() >= 1 && !warnArea.contains("nil")) { // 첫번째 문자는 AREA임.
        region = warnArea.substring(0, 1);
        warnArea = warnArea.substring(warnArea.indexOf(":") + 1);

        // AREA별로 다시 warning 들을 쪼갬.
        String[] warnArr = warnArea.split("NNNN");
        for (String coastalWarn : warnArr) {
          if (coastalWarn.trim().length() >= 10) {
            cw = new Message(coastalWarn.trim(), TIME_FORMAT);
            cw.setRegion(region);
            cw.setScrapDate(SCRAPE_DATE);
            logger.info(cw.toString());
            CW_LIST.add(cw);
          }
        }
      }
    } // <- coastalWarnings
    logger.info("\t:: progressPart3 ::\n\n");
    return CW_LIST;
  }


  /**
   * 제목과 내용을 받아서 정수로 선언한 위치에 저장한다.
   * 
   * @param title
   * @param cont
   * @return
   */
  private static Boolean fileSave(String title, String cont) {
    if (!PROGRESS)
      return false;
    title = SAVE_DIR + HEAD + title + TAG;
    try {
      bs = new BufferedOutputStream(new FileOutputStream(title)); // 경로를 포함한 파일 이름.
      bs.write(cont.getBytes());
      logger.info("\nWriting File at " + title);
      return true;
    } catch (Exception e) {
      logger.info("\nWriting File Failed");
      logger.info(e.getMessage());
      return false;
    } finally {
      try {
        bs.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } // finally
  }


  private static void initVals() {
    PTN = null;
    MTCHR = null;
    PROGRESS = false;
    // connection = true;
    DOC = null;
    NAVAREAxWARNINGS = "";
    COASTAL_WARNINGS = "";
    SAFETY_MSGS = "";
    START_TIME = "";
    SCRAPE_DATE = "";
    TIME_FORMAT = "";
    CRAWL_CNT = 0;
    Communicater.SEND_CNT = 0;
    CW_LIST = null;
    logger.info("");
  }

} // class

// part 별로 메소드 분리 하자...
