import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeZoneTest {
  public static void main(String[] args) {
    Date localTime = new Date();
    DateFormat convert = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    
    convert.setTimeZone(TimeZone.getTimeZone("GMT+9"));
    System.out.println("local time : " + localTime);
    System.out.println("time in GMT : " + convert.format(localTime) + " (GMT+9)");
  }
}