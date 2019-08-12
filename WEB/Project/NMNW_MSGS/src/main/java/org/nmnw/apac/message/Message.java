package org.nmnw.apac.message;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
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


  /* Constructor */
  public Message() {}

  /* getter & setter */
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

  public String getScrapDate() {
    return scrapDate;
  }

  public void setScrapDate(String scrapDate) {
    this.scrapDate = scrapDate;
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


  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
}
