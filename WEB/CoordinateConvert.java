public class CoordinateConvert {
    private static String[] vals;
    
    /**
     * 도 분 초 로 표현되는 좌표를, (19-29.93S 또는 116-35.88E)
     * 10진수 도 단위 좌표(진수 좌표)로 변환하는 메소드.
     * 10진수 좌표 = 도 + (분/60) + (초/3600)
     */
    public static void main(String[] args) {
      System.out.println(PosiConvert("23-26.49S"));
    }
  
    public static String PosiConvert(String target) {
      vals = target.replace("\\W", "").trim().split("\\D");
      if (vals.length != 3) {
        System.out.println("\n\n\t!! Posi Converter get WRONG VALUE !!\n\n");
        return null;
      }
      return String.format("%.8f", (Integer.parseInt(vals[0])) + (Integer.parseInt(vals[1]) / 60.0)
          + (Integer.parseInt(vals[2]) / 3600.0));
    }
    /**
     * 이 반대도 나중에 알아보자.
     */
}