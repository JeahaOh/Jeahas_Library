package org.nmnw.apac.message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.nmnw.apac.util.MsgConvertUtil;
import org.nmnw.apac.util.TimeHandleUtil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Message {
  /**
   * H: (area) SECURITE FM JRCC AUSTRALIA 172259Z JUN 19 (경보를 준 기관명 + 시간, 날짜) AUSCOAST WARNING
   * 194/19 (뭘까) DARWIN DGPS STATION ALRS VOL 2 IN POSITION 12-26.72S 130-57.51E (이유 위경도)
   * INOPERATIVE (뭔가 있음) NNNN
   * 
   * type : NM인지 NW인지 area : A - H 구역 publishDate : 시작일 cancelDate : 종료일 msg : 전체 내용 position : 위도
   * 경도 ( 남위, 동경인듯 ) south : 남위 east : 동경
   * 
   * RCC : Rescue Coordination Centre JRCC : Joint Rescue Coordination Centre
   * 
   * TIME -> UTC MILI TIME으로 하라.
   */
  @Expose
  @SerializedName("id")
  String id;
  @Expose
  @SerializedName("country")
  String country;
  @Expose
  @SerializedName("shortId")
  String shortId;
  @Expose
  @SerializedName("messageId")
  String messageId;
  @Expose
  @SerializedName("subject")
  String subject;
  @Expose
  @SerializedName("title")
  String title;
  @Expose
  @SerializedName("type")
  String type;
  @Expose
  @SerializedName("mainType")
  String mainType;
  @Expose
  @SerializedName("region")
  String region;
  @Expose
  @SerializedName("fromDate")
  String fromDate;
  @Expose
  @SerializedName("scrapDate")
  String scrapDate;
  @Expose
  @SerializedName("toDate")
  String toDate; // 임의 값
  @Expose
  @SerializedName("position")
  String position;
  // 위도 lat : N, S / 북위, 남위
  @Expose
  @SerializedName("lat")
  String lat;
  // 경도 lon : E, W / 동경, 서경
  @Expose
  @SerializedName("lon")
  String lon;
  @Expose
  @SerializedName("detail")
  String detail;

  private static String[] NM_KEYWORDS =
      {"RACON"/* 레이더 */, "BUOY" /* 부표 */, "LIGHTHOUSE"/* 등대 */, "SAFETY MESSAGE", "BEACON"};

  /* Constructor */
  public Message() {}

  public Message(String detail, String timeForm) {
    super();
    detail = detail.replaceAll("` ", "");
    this.detail = detail;

    // -> msg를 parse해서 fromDate를 가져옴.
    Pattern ptn = Pattern.compile("\\d{6}(Z) \\w{3} \\d{2}");
    Matcher mtchr = ptn.matcher(detail);
    if (mtchr.find()) {
      this.fromDate = TimeHandleUtil.unixTimeConvert(TimeHandleUtil.dateFormatConvert(
          mtchr.group().replace("Z", ""), timeForm, TimeHandleUtil.getDATETIME_form())) + "";
    }
    // <- fromDate



    // -> subject
    subject = detail.split("\n")[2];
    messageId = subject;
    title = subject;
    // <- subject
    this.country = "AUS";


    // -> position south east
    ptn = Pattern.compile(
        "\\d{0,3}\\W\\d{0,3}\\W\\d{0,3}.\\d{0,3}(.|)(!?S|!?N)\\d{0,3}\\W\\d{0,3}\\W\\d{0,3}.\\d{0,3}(.|)(!?E|!?W)");
    mtchr = ptn.matcher(detail);
    if (mtchr.find()) {
      this.position = mtchr.group().trim();
    }
    // 만약 position이 점이 아니라 면이라면?? 면적이 된다면 부분은 점일때 사용하는 것으로...
    if (this.getPosition() != null && this.getPosition().length() > 5) {
      ptn = Pattern.compile("\\d{0,3}\\W\\d{0,3}\\W\\d{0,3}.\\d{0,3}(.|)(!?S|!?N)");
      mtchr = ptn.matcher(this.getPosition());
      if (mtchr.find()) {
        this.lat = MsgConvertUtil.PosiConvert(mtchr.group().trim());
      }
      ptn = Pattern.compile("\\d{0,3}\\W\\d{0,3}\\W\\d{0,3}.\\d{0,3}(.|)(!?E|!?W)");
      mtchr = ptn.matcher(this.getPosition());
      if (mtchr.find()) {
        this.lon = MsgConvertUtil.PosiConvert(mtchr.group().trim());
      }
    }
    // <- position south east


    // -> type
    for (String keyword : NM_KEYWORDS) {
      if (detail.toUpperCase().contains(keyword)) {
        this.setType("NM");
        this.setMainType("NM");
        System.out.println(keyword + this.type);
      } else {
        this.setType("NW");
        this.setMainType("NW");
      }
    }
    // <- type

    // -> shortId
    ptn = Pattern.compile("\\d{1,3}(\\/)\\d{2}");
    mtchr = ptn.matcher(detail);
    if (mtchr.find()) {
      this.shortId = this.type + " " + mtchr.group();
    }
    // <- shortId
    System.out.println("THIS : " + this.toString());
  }

  /* getter & setter */
  public void setScrapDate(String scrapDate) {
    this.scrapDate = scrapDate;
    // 12시간 뒤.
    this.toDate = (Long.parseLong(scrapDate) + (12 * 60 * 60 * 1000)) + "";
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getShortId() {
    return shortId;
  }

  public void setShortId(String shortId) {
    this.shortId = shortId;
  }

  public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getMainType() {
    return mainType;
  }

  public void setMainType(String mainType) {
    this.mainType = mainType;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getFromDate() {
    return fromDate;
  }

  public void setFromDate(String fromDate) {
    this.fromDate = fromDate;
  }

  public String getToDate() {
    return toDate;
  }

  public void setToDate(String toDate) {
    this.toDate = toDate;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getLat() {
    return lat;
  }

  public void setLat(String lat) {
    this.lat = lat;
  }

  public String getLon() {
    return lon;
  }

  public void setLon(String lon) {
    this.lon = lon;
  }

  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public String getScrapDate() {
    return scrapDate;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
}
