import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class getSimpleDate {
  public static void main(String[] args){
    String time = new SimpleDateFormat( "yyy.MM.dd_HH:mm:ss" ).format( new Date() );
    System.out.println(time);
  }
}

/**
 * JAVA에서 Date Time String 다루는 방법도 익혀야 할것 같다
 */