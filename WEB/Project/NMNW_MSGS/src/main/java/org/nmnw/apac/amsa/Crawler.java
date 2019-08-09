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
import org.nmnw.apac.message.CoastalWarning;
import org.nmnw.apac.request.Request;
import org.nmnw.apac.request.RetrofitClient;
import org.nmnw.apac.util.TimeHandleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressWarnings("unused")
public class Crawler {

  private static final Logger logger = LoggerFactory.getLogger(Crawler.class);
  // Crawling 할 URL
  private static final String TARGET_URL =
      "https://www.operations.amsa.gov.au/AMSA.Web.MSIPublication/";

  private static Pattern ptn = null;
  private static Matcher mtchr = null;

  private static Boolean progress = false;

  private static Document DOC = null;
  private static String NAVAREAxWARNINGS = "";

  private static String COASTAL_WARNINGS = "";
  private static String SAFTY_MSGS = "";

  private static String START_TIME = "";
  private static String scrapDate = "";
  private static String timeFormat = "";


  private static int CRAWL_CNT = 0;
  private static final int CRAWL_MAX = 5;

  private static int SEND_CNT = 0;

  private static List<CoastalWarning> list = null;

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
    while (!progress) {
      DOC = getDoc();
      if (CRAWL_CNT == CRAWL_MAX)
        return;
    }
    // logger.info(doc.toString());

    // -> Part x. 별로 다시 나눔
    splitParts(DOC);
    // scrapDate

    // Part 1. Safety Messages: 처리?
    progressPart1(SAFTY_MSGS);

    // Part 2. NAVAREA X warnings: 처리
    progressPart2(NAVAREAxWARNINGS);

    // Part 3. Coastal warnings: 처리
    progressPart3(COASTAL_WARNINGS);
    //
    sendListAsObject();

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
      progress = true;
      logger.info("Getting documennt Success \t"
          + new SimpleDateFormat("HH.mm.ss", Locale.KOREA).format(new Date()));
      // -> 긁어온 HTML파일 원본 저장.
      fileSave(START_TIME, DOC.toString());
      // <- 긁어온 HTML파일 원본 저장.
      return DOC;
    } catch (IOException e) {
      logger.info("Getting document Failed \t"
          + new SimpleDateFormat("HH.mm.ss", Locale.KOREA).format(new Date()));
      logger.info(e.getMessage());
      progress = false;
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
    if (!progress)
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
    if (!progress)
      return;
    for (String part : getTrimedList(doc)) {
      part = part.replaceAll(" <[^>]*>", "").replaceAll("<[^>]*>", "").trim();

      // logger.info("\n\n"+ part + "\n\n");
      if (part.contains("current at")) {
        // Maritime Safety Information current at 070444 UTC Aug 19
        // 시간 정규 표현식 210205 UTC Jun 19
        ptn = Pattern.compile("\\d{2}\\d{2}\\d{2}\\s(UTC)\\s\\S{3}\\s\\d{2}");
        mtchr = ptn.matcher(part);
        while (mtchr.find()) {
          scrapDate = mtchr.group().replace("UTC", "").replace("  ", " ").trim();
        }
      } else if (part.contains("Part 1.")) {
        if (part.contains("SECURITE") && part.contains("NNNN"))
          SAFTY_MSGS = part;
      } else if (part.contains("Part 2.")) {
        NAVAREAxWARNINGS = part.replace("Part 2. NAVAREA X warnings:", "").replace("NNNN", "")
            .replace("\n\n\n\n", "\n").trim();
      } else if (part.contains("Part 3.")) {
        COASTAL_WARNINGS = part.replaceFirst("Part 3. Coastal warnings:", "").trim();
      } else if (part.contains("date-time format")) {
        timeFormat = getTimeFormat(part);
        scrapDate = TimeHandleUtil.unixTimeConvert(TimeHandleUtil.dateFormatConvert(scrapDate,
            timeFormat, TimeHandleUtil.getDATETIME_form())) + "";
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
    if (!progress)
      return null;
    for (String a : target.trim().split("</p>")) {
      if (a.contains("date-time format")) {
        target = a;
      }
    }
    ptn = Pattern.compile("(\")\\w+\\s\\w+\\s\\w+(\")");
    mtchr = ptn.matcher(target);
    if (mtchr.find()) {
      target = mtchr.group().replaceAll("\"", "").replace("Z", "").replace("mon", "MM").trim();
    }
    logger.info("Time Format : " + target);
    return target;
  }

  /**
   * Safty Message를 처리하게 된다면 사용할 메소드.
   * 
   * @param target
   */
  private static void progressPart1(String target) {
    if (!progress)
      return;
    logger.info("\n\n\t:: progressPart1 ::\n" + target + "\t:: progressPart1 ::\n\n");
  }

  /**
   * navarea X warnings 를 처리하기 위한 method. 형식이 일정치 않음으로 예외처리를 더 해줘야 함.
   * 
   * @param target
   */
  private static void progressPart2(String target) {
    if (!progress)
      return;
    if (target.length() < 10) {
      logger.info("\n\t!! SOMETHING IS WRONG AT PROGRESS NAVAREA X WARNINGS !!");
      return;
    }

    logger.info("\t:: progressPart2 ::");
    // ptn을 바꿔 줘야 할거 같은데..
    Pattern UTCDatePtn = Pattern.compile("\\d{2}.\\d{2}\\d{2}\\s(UTC)\\s\\S{3}\\s\\d{2}");
    // -> navarea X warning
    List<String> navareaXwarningList = new ArrayList<String>();
    String[] tempArr = target.trim().split("\\d{1}(\\.)");

    int idx = 0;
    for (String temp : tempArr) {
      logger.info(temp);
      navareaXwarningList.add(idx + " : " + temp.trim());
      if (temp.contains("AUSCOAST WARNINGS")) {
        // FORCE DATE?
        mtchr = UTCDatePtn.matcher(temp);
        if (mtchr.find()) {
          logger.info("AUSCOAST WARNINGS IN FORCE AT : " + mtchr.group());
        }
        for (String area : temp.split("AREA ")) {
          if (!area.contains("NIL")) {
            area = area.replace("AUSCOAST WARNINGS IN FORCE AT", "").replace(" AND", ",").trim();
            // logger.info(area);
          }
        }
      } else if (temp.contains("CANCEL THIS MESSAGE ")) {
        // Message Cancel Date
        mtchr = UTCDatePtn.matcher(temp);
        while (mtchr.find()) {
          // END DATE?
          logger.info("CANCEL THIS MESSAGE : " + mtchr.group());
        }
      }
      idx++;
    }
    // <- navarea X warning
    logger.info("\t:: progressPart2 ::\n\n");
  }

  /**
   * Part 3 Coastal Warnings 를 처리하기 위한 method.
   * 
   * @param target
   */
  private static List<CoastalWarning> progressPart3(String target) {
    list = new ArrayList<>();
    if (!progress)
      return null;
    if (target.length() < 10) {
      logger.info("\t!! SOMETHING IS WRONG AT PROGRESS COASTAL WARNINGS !!");
      progress = false;
      return null;
    }
    logger.info("\t:: progressPart3 ::");
    // logger.info(target);
    // -> coastalWarnings part를 구역을 나눈 뒤, 구역별 NMNW를 가져옴.
    String[] coastalWarnArr = target.split("AUSCOAST coastal warning - area ");
    logger.info("Count of CW AREA : " + coastalWarnArr.length);

    String region;
    CoastalWarning cw;
    for (String warnArea : coastalWarnArr) { // 데이터가 없다면 작업.
      if (warnArea.length() >= 1 && !warnArea.contains("nil")) { // 첫번째 문자는 AREA임.
        region = warnArea.substring(0, 1);
        // logger.info("\nAREA : " + region);
        warnArea = warnArea.substring(warnArea.indexOf(":") + 1);

        // AREA별로 다시 warning 들을 쪼갬.
        String[] warnArr = warnArea.split("NNNN");
        for (String coastalWarn : warnArr) {
          if (coastalWarn.trim().length() >= 10) {
            cw = new CoastalWarning(coastalWarn.trim(), timeFormat);
            cw.setRegion(region);
            cw.setScrapDate(scrapDate);
            logger.info(cw.toString());
            list.add(cw);
          }
        }
      }
    } // <- coastalWarnings
    logger.info("\t:: progressPart3 ::\n\n");
    return list;
  }

  private static void sendListAsObject() {
    if (!progress && list.size() < 0)
      return;
    for (CoastalWarning cw : list) {
      sender(cw);
      matrixTime(1000);
    }
    matrixTime(5000);
    logger.info("::::\tNMNW_MSGS SEND END {}/{}\t\t::::", SEND_CNT, list.size());
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static void sender(CoastalWarning cw) {
    logger.info("\t:: Send Coastal Warning ::\n{}\n", cw.toString());

    Retrofit retrofit = RetrofitClient.getClient(false);
    Request request = retrofit.create(Request.class);
    Call<?> call = request.postMsg(cw);

    call.enqueue(new Callback() {
      @Override
      public void onResponse(Call call, Response response) {
        logger.info("CONNECTION SUCCESS ->");
        logger.info("RESPONSE        : {}", response);
        logger.info("RESPONSE CODE   : {}", response.code());
        logger.info("RESPONSE MSG    : {}", response.message());
        logger.info("RESPONSE BODY   : {}", response.body());
        logger.info("RESPONSE HEADER : [\n" + response.headers() + "]\n\n");
        logger.info("STATUS COUNT    : {}/{}", SEND_CNT, list.size());
        if (response.code() == 200 && !(response.body() + "").contains("ERROR"))
          SEND_CNT++;
      }

      @Override
      public void onFailure(Call call, Throwable t) {
        logger.info("Connection FAIL");
        logger.info("FAIL CAUSE : {}", t.getMessage());
      }
    });
  }

  /**
   * 제목과 내용을 받아서 정수로 선언한 위치에 저장한다.
   * 
   * @param title
   * @param cont
   * @return
   */
  private static Boolean fileSave(String title, String cont) {
    if (!progress)
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

  /**
   * 인자 값으로 주는 시간만큼 시간 delay.
   * 
   * @param delayTime
   */
  private static void matrixTime(int delayTime) {
    long startTime = System.currentTimeMillis();
    long endTime = 0;
    while (endTime - startTime < delayTime) {
      endTime = System.currentTimeMillis();
    }
  }

  private static void initVals() {
    ptn = null;
    mtchr = null;
    progress = false;
    // connection = true;
    DOC = null;
    NAVAREAxWARNINGS = "";
    COASTAL_WARNINGS = "";
    SAFTY_MSGS = "";
    START_TIME = "";
    scrapDate = "";
    timeFormat = "";
    CRAWL_CNT = 0;
    SEND_CNT = 0;
    list = null;
    logger.info("");
  }

} // class

// part 별로 메소드 분리 하자...
