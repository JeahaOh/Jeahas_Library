package org.nmnw.apac.amsa;

import java.util.ArrayList;
import java.util.List;
import org.nmnw.apac.message.Message;
import org.nmnw.apac.util.MsgParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FragmentPaser {

  private static final Logger logger = LoggerFactory.getLogger(FragmentPaser.class);

  /**
   * Safty Message를 처리.
   * 
   * @param target
   */
  public static List<Message> progressPart1(String target) {
    logger.info("\t:: START ::");
    // logger.info("target : [\n{}\n]", target);
    if (!Crawler.PROGRESS)
      return null;
    if (!target.contains("SECURITE") && !target.contains("NNNN")) {
      logger.info("progressPart1 return null");
      return null;
    }

    List<Message> list = new ArrayList<Message>();
    for (String msg : (target.replace("Part 1. Safety Messages:", "").trim().split("NNNN"))) {
      if (msg.trim().length() >= 10) {
        list.add(MsgParser.MsgParse(msg.trim(), null));
      }
    }
    logger.info("\t:: END : {} ::\n", list.size());
    return list;
  }

  /**
   * navarea X warnings 를 처리하기 위한 method. 형식이 일정치 않음으로 예외처리를 더 해줘야 함.
   * 
   * @param target
   */
  public static List<Message> progressPart2(String target) {
    if (!Crawler.PROGRESS)
      return null;
    if (target.length() < 10) {
      logger.info("\n\t!! SOMETHING IS WRONG AT PROGRESS NAVAREA X WARNINGS !!");
      return null;
    } else {
      logger.info("\t:: START ::");
    }

    target = target.replace("Part 2. NAVAREA X warnings:", "").trim();

    // 안필요한 값??
    // Pattern UTCDatePtn = Pattern.compile("\\d{2}.\\d{2}\\d{2}\\s(UTC)\\s\\S{3}\\s\\d{2}");

    // -> navarea X warning
    List<Message> list = new ArrayList<Message>();

    for (String str : target.trim().split("NNNN")) {
      // logger.info("\ntemp : [\n" + temp.trim() + "\n]\n\n");
      if (str.contains("WWW.AMSA.GOV.AU.")) {
        // logger.info("no list : [{}]", str);
        MsgParser.noListParser(str);

      } else {
        if (str.trim().length() >= 10) {
          list.add(MsgParser.MsgParse(str.trim(), null));
        }
      }
    }

    // <- navarea X warning
    logger.info("\t:: END : {} ::\n", list.size());
    return list;
  }

  /**
   * Part 3 Coastal Warnings 를 처리하기 위한 method.
   * 
   * @param target
   */
  public static List<Message> progressPart3(String target) {
    if (!Crawler.PROGRESS)
      return null;
    if (target.length() < 10) {
      logger.info("\t!! SOMETHING IS WRONG AT PROGRESS COASTAL WARNINGS !!");
      Crawler.PROGRESS = false;
      return null;
    }
    logger.info("\t:: START ::");
    // -> coastalWarnings part를 구역을 나눈 뒤, 구역별 NMNW를 가져옴.
    List<Message> list = new ArrayList<>();
    String[] coastalWarnArr = target.split("AUSCOAST coastal warning - area ");
    logger.info("Count of CW AREA : " + coastalWarnArr.length);

    String region;
    for (String warnArea : coastalWarnArr) { // 데이터가 없다면 작업.
      if (warnArea.length() >= 1 && !warnArea.contains("nil")) { // 첫번째 문자는 AREA임.
        region = warnArea.substring(0, 1);
        warnArea = warnArea.substring(warnArea.indexOf(":") + 1);

        // AREA별로 다시 warning 들을 쪼갬.
        String[] warnArr = warnArea.split("NNNN");
        for (String msg : warnArr) {
          if (msg.trim().length() >= 10) {
            list.add(MsgParser.MsgParse(msg.trim(), region));
          }
        }
      }
    } // <- coastalWarnings
    logger.info("\t:: END : {} ::\n", list.size());
    return list;
  }

}
