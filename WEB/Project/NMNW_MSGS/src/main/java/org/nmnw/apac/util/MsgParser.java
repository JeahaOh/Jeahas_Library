package org.nmnw.apac.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.nmnw.apac.amsa.Crawler;
import org.nmnw.apac.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsgParser {

  private static final Logger logger = LoggerFactory.getLogger(MsgParser.class);
  private static String[] vals;
  private static Message msg;
  private static Pattern ptn;
  private static Matcher mtchr;
  private static final String CNTRY = "AUS";

  private static final Const consts = new Const();

  private static final String[] NM_KEYWORDS =
      {"RACON"/* 레이더 */, "BUOY" /* 부표 */, "LIGHTHOUSE"/* 등대 */, "SAFETY MESSAGE", "BEACON"};

  public static Message MsgParse(String detail, String region) {
    detail = detail.replaceAll("` ", "");
    msg = new Message();
    // msg.setDetail(detail.concat(Crawler.TIME_FORMAT_DESC).replace("\n", "</br>"));
    msg.setDetail(detail);

    // -> msg를 parse해서 fromDate를 가져옴.
    ptn = Pattern.compile("\\d{6}(Z) \\w{3} \\d{2}");
    mtchr = ptn.matcher(detail);
    if (mtchr.find()) {
      msg.setFromDate(
          TimeHandle.unixTimeConvert(TimeHandle.dateFormatConvert(mtchr.group().replace("Z", ""),
              Crawler.TIME_FORMAT, TimeHandle.getDATETIME_form())) + "");
    }
    // <- fromDate

    // -> subject msgId, title
    String ID_TITL_SBJCT = detail.split("\n")[2];
    msg.setSubject(ID_TITL_SBJCT);
    msg.setMessageId(ID_TITL_SBJCT);
    msg.setTitle(ID_TITL_SBJCT);
    // <- subject msgId, title
    msg.setCountry(CNTRY);



    // -> position south east
    String position = "";
    ptn = Pattern.compile(consts.getRegExpPointCoordinate());
    mtchr = ptn.matcher(detail);
    if (mtchr.find()) {
      position = mtchr.group().trim();
    }
    msg.setPosition(position);

    // 만약 position이 점이 아니라 면이라면?? 면적이 된다면 부분은 점일때 사용하는 것으로...
    if (position != null && position.length() > 5) {
      ptn = Pattern.compile(consts.getRegExpLat());
      mtchr = ptn.matcher(position);
      if (mtchr.find()) {
        String lat = PosiConvert(mtchr.group().trim());
        msg.setLat(lat);
      }
      ptn = Pattern.compile(consts.getRegExpLon());
      mtchr = ptn.matcher(position);
      if (mtchr.find()) {
        String lon = PosiConvert(mtchr.group().trim());
        msg.setLon(lon);
      }
    }

    if (position.equals("")) {
      ptn = Pattern.compile(consts.getRegExpLineWithoutDegreeCoordinate());
      mtchr = ptn.matcher(detail);
      if (mtchr.find()) {
        position = mtchr.group().trim();
      }
      msg.setPosition(position);
    }
    // <- position south east



    // -> type
    String t = "NW";
    for (String keyword : NM_KEYWORDS) {
      if (detail.contains(keyword)) {
        t = "NM";
        break;
      }
    }
    msg.setType(t);
    msg.setMainType(t);

    // <- type

    // -> shortId
    ptn = Pattern.compile("\\d{1,3}(\\/)\\d{2}");
    mtchr = ptn.matcher(detail);
    if (mtchr.find()) {
      msg.setShortId(CNTRY + " " + t + " " + mtchr.group());
      msg.setId(mtchr.group());
    }
    // <- shortId

    // region
    if (region != null && region.length() > 0) {
      msg.setRegion(region);
    }

    // scrapDate & toDate
    msg.setScrapDate(Crawler.SCRAPE_DATE);
    msg.setToDate((Long.parseLong(Crawler.SCRAPE_DATE) + (12 * 60 * 60 * 1000)) + "");



    return msg;
  }

  public static void noListParser(String target) {
    // logger.info("target : [\n{}\n]\n\n", target);
    ptn = Pattern.compile("(\\d{6})\\s(UTC)\\s(\\w{3})\\s(\\d{2})");
    String navxForceTime = "";
    String auCstForceTime = "";
    String cnclTime = "";

    // 1. 2. 3. 으로 쪼갠다.
    for (String s : target.split("(\\d{1}(\\.\\ ))")) {

      if (s.contains("NAVAREA X WARNINGS")) {
        // navarea x warnings의 force time을 먼저 구한다.
        mtchr = ptn.matcher(s);
        if (mtchr.find()) {
          navxForceTime = String.valueOf(TimeHandle.unixTimeConvert(
              TimeHandle.dateFormatConvert(mtchr.group().replace("UTC ", "").trim(),
                  Crawler.TIME_FORMAT, TimeHandle.getDATETIME_form())));
        }
        s = s.substring(s.indexOf("\n"), s.length()).trim();
        // no list 정리
        // logger.info("NAVAREA X WARNINGS : \t{}", s.trim());


      } else if (s.contains("AUSCOAST WARNINGS")) {
        // auscoast warnings의 force time을 먼저 구한다.
        mtchr = ptn.matcher(s);
        if (mtchr.find()) {
          auCstForceTime = String.valueOf(TimeHandle.unixTimeConvert(
              TimeHandle.dateFormatConvert(mtchr.group().replace("UTC ", "").trim(),
                  Crawler.TIME_FORMAT, TimeHandle.getDATETIME_form())));
        }
        s = s.substring(s.indexOf("\n"), s.length()).trim();

        // no list 정리
        // logger.info("AUSCOAST WARNINGS : ");

        // area 당 msg no
        for (String list : s.trim().split("\n")) {
          if (!list.contains("NIL")) {
            list = list.replace("AUSCOAST WARNINGS IN FORCE AT", "").replace(" AND", ",").trim();
            // logger.info(list);
          }
        }


      } else if (s.contains("CANCEL THIS MESSAGE")) {
        cnclTime = String.valueOf(TimeHandle.unixTimeConvert(TimeHandle.dateFormatConvert(
            s.replace("CANCEL THIS MESSAGE", "").replace("UTC ", "").trim(), Crawler.TIME_FORMAT,
            TimeHandle.getDATETIME_form())));
      } else {
        continue;
      }
    }

    logger.info("navxaForceTime : {}", navxForceTime);
    logger.info("auCstForceTime : {}", auCstForceTime);
    logger.info("CANCEL TIME : {}", cnclTime);
  }



  public static final String PosiConvert(String target) {
    vals = target.replace("\\W", "").trim().split("\\D");

    if (vals.length != 3) {
      logger.warn("\n\n\t!! Posi Converter get WRONG VALUE -> {} !!\n", target);
      return null;
    }
    if (target.contains("S") || target.contains("N")) {
      if (Integer.parseInt((vals[0]).replace("-", "").trim()) > 90) {
        logger.warn("\n\n\t!! Posi Converter get WRONG VALUE !!\n\tLat Can Not Over 90 -> {}\n",
            target);
        return null;
      }
      return "-" + String.format("%.8f", (Integer.parseInt(vals[0]))
          + (Integer.parseInt(vals[1]) / 60.0) + (Integer.parseInt(vals[2]) / 3600.0));

    } else if (target.contains("E") || target.contains("W")) {
      if (Integer.parseInt((vals[0]).replace("-", "").trim()) > 180) {
        logger.warn("\n\n\t!! Posi Converter get WRONG VALUE !!\n\tLon Can Not Over 180 -> {}\n",
            target);
        return null;
      }
      return String.format("%.8f", (Integer.parseInt(vals[0])) + (Integer.parseInt(vals[1]) / 60.0)
          + (Integer.parseInt(vals[2]) / 3600.0));
    } else {
      return null;
    }
  }

  public static void main(String[] args) {
    // System.out.println(PosiConvert("23-26.49S"));
    String target = "SECURITE\n" + "FM JRCC AUSTRALIA 072353Z AUG 19\n" + "NAVAREA X 058/19\n"
        + "1. NAVAREA X WARNINGS IN FORCE AT 080000 UTC AUG 19\n" + "052/19, 057/19, 058/19\n"
        + "2. AUSCOAST WARNINGS IN FORCE AT 080000 UTC AUG 19\n"
        + "AREA A 197/19, 209/19, 210/19, 213/19, 217/19\n" + "AREA B NIL\n" + "AREA C NIL\n"
        + "AREA D 224/19\n" + "AREA E NIL\n" + "AREA F 191/19\n"
        + "AREA G 122/19, 132/19, 135/19, 136/19, 137/19, 140/19, 141/19, 142/19, 149/19, 150/19, 185/19, 212/19, 219/19, 222/19, 226/19\n"
        + "AREA H NIL\n"
        + "3. COMPLETE TEXTS OF ALL WARNINGS IN FORCE ARE AVAILABLE FROM WEBSITE WWW.AMSA.GOV.AU.\n"
        + "4. CANCEL THIS MESSAGE 150000 UTC AUG 19";
    noListParser(target);
  }
}

/*
 * SECURITE FM JRCC AUSTRALIA 070549Z AUG 19 NAVAREA X 057/19 SPECIAL PURPOSE VESSEL ILE DE
 * BREHAT/FOUC CONDUCTING CABLE LAYING OPERATIONS FROM 31-30` S 156-40` E TO 34-30` S 153-00` E
 * 2.0NM CLEARANCE REQUESTED. NNNN
 */
