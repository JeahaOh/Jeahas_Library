
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeStamp {
  
  /* DATE와 TIME 정수 */
  private static final String DATE_TIME = "yyyy.MM.dd_HH:mm:ss";
  private static final String DATE = "yyyy.MM.dd";
  private static final String TIME = "HH:mm:ss";
  
  static DateFormat DF;
  
  /* GMT DATE TIME STAMP */
  public static String getGMTStamp(String form) {
    return new SimpleDateFormat( form ).format( new Date() );
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
    return DF.format( new Date() );
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
  
  
  public static void main(String[] args){
    System.out.println(getDateTime());
  }
}