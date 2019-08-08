
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeStamp {
    /* DATE와 TIME 정수 */
    private static final String DATE_TIME = "yyyy.MM.dd_HH:mm:ss";
    private static final String DATETIME = "yyyyMMddHHmmss";
    private static final String DATE = "yyyy.MM.dd";
    private static final String TIME = "HH:mm:ss";
  
    static DateFormat DF;
  
    /* GMT DATE TIME STAMP */
    public static String getGMTStamp(String form) {
      return new SimpleDateFormat(form).format(new Date());
    }
  
    public static String getDateTime() {
      return getGMTStamp(DATE_TIME);
    }
  
    public static String getDate() {
      return getGMTStamp(DATE);
    }
  
    public static String getTime() {
      return getGMTStamp(TIME);
    }
  
    public static String getGMTTimeZone() {
      return new SimpleDateFormat().getTimeZone().getID();
    }
  
    /* UTC DATE TIME STAMP */
    public static String getUTCStamp(String form) {
      DF = new SimpleDateFormat(form);
      DF.setTimeZone(TimeZone.getTimeZone("UTC"));
      return DF.format(new Date());
    }
  
    public static String getUTCDateTime() {
      return getUTCStamp(DATE_TIME);
    }
  
    public static String getUTCDate() {
      return getUTCStamp(DATE);
    }
  
    public static String getUTCTime() {
      return getUTCStamp(TIME);
    }
  
    /* Getter */
    public static String getDATETIME_form() {
      return DATETIME;
    }
  
    /**
     * @param target 날짜
     * @return 문자열 날짜의 월이 영문 축약형일 경우 숫자로 반환.
     */
    public static String strMonthToInt(String target) {
      target =
          target.replace("Jan", "01").replace("Feb", "02").replace("Mar", "03").replace("Apr", "04")
              .replace("May", "05").replace("Jun", "06").replace("Jul", "07").replace("Aug", "08")
              .replace("Sep", "09").replace("Oct", "10").replace("Nov", "11").replace("Dec", "12");
      target =
          target.replace("JAN", "01").replace("FEB", "02").replace("MAR", "03").replace("APR", "04")
              .replace("MAY", "05").replace("JUN", "06").replace("JUL", "07").replace("AUG", "08")
              .replace("SEP", "09").replace("OCT", "10").replace("NOV", "11").replace("DEC", "12");
      return target;
  
    }
  
    /**
     * 문자열 형태의 날짜를 원하는 형태의 폼으로 변환해 주는 메소드.
     * 
     * @param target : 문자열 형태의 날짜 원본.
     * @param fromFormatString : 원본 날짜의 포멧.
     * @param toFormatString : 변환하고 싶은 포멧.
     * @return : 원하는 포멧으로 변환된 문자열 형태의 날짜.
     */
    public static String dateFormatConvert(String target, String fromFormatString,
        String toFormatString) {
      SimpleDateFormat fromFormat = new SimpleDateFormat(fromFormatString);
      SimpleDateFormat toFormat = new SimpleDateFormat(toFormatString);
      Date fromDate = null;
      try {
        fromDate = fromFormat.parse(strMonthToInt(target));
      } catch (ParseException e) {
        System.out.println("\n\t!! Date Format Convert ERR !!\n" + e.getMessage());
        System.out.println("target : " + target);
        System.out.println("fromFormatString : " + fromFormatString);
        System.out.println("toFormatString : " + toFormatString);
        System.out.println("\n\n");
        return null;
      }
  
      return toFormat.format(fromDate);
    }
  
    /**
     * 문자열 형태의 날짜를 long 형식의 unix time(time millis)으로 변환하는 메소드.
     * 
     * @param target
     * @return
     */
    public static long unixTimeConvert(String target) {
      DF = new SimpleDateFormat(DATETIME);
      long unixTime = 0;
  
      DF.setTimeZone(TimeZone.getTimeZone("UTC"));
      try {
        unixTime = DF.parse(target).getTime();
      } catch (ParseException e) {
        System.out.println("\n\t!! Unix Time Convert ERR !!\n" + e.getMessage());
        System.out.printf("target : %s\n\n", target);
      }
      return unixTime;
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
  
  
    public static void main(String[] args) {
      System.out.println(getUTCDateTime());
      System.out.println(dateFormatConvert("281532 Jul 19", "ddhhmm MM yy", DATETIME));
      System.out.println(unixTimeConvert(dateFormatConvert(getDateTime(), DATE_TIME, DATETIME)));
    }
  }