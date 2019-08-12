package org.nmnw.apac.util;

public class MsgParser {
  private static String[] vals;

  public static void main(String[] args) {
    System.out.println(PosiConvert("23-26.49S"));
  }

  public static final String PosiConvert(String target) {
    vals = target.replace("\\W", "").trim().split("\\D");

    if (vals.length != 3) {
      System.out.println("\n\n\t!! Posi Converter get WRONG VALUE !!\n\n");
      return null;
    }
    if (target.contains("S") || target.contains("N")) {
      if (Integer.parseInt((vals[0]).replace("-", "").trim()) > 90) {
        return null;
      }
      return "-" + String.format("%.8f", (Integer.parseInt(vals[0]))
          + (Integer.parseInt(vals[1]) / 60.0) + (Integer.parseInt(vals[2]) / 3600.0));
    } else if (target.contains("E") || target.contains("W")) {
      if (Integer.parseInt((vals[0]).replace("-", "").trim()) > 180) {
        return null;
      }
      return String.format("%.8f", (Integer.parseInt(vals[0])) + (Integer.parseInt(vals[1]) / 60.0)
          + (Integer.parseInt(vals[2]) / 3600.0));
    } else {
      return null;
    }
  }
}

/*
 * SECURITE FM JRCC AUSTRALIA 070549Z AUG 19 NAVAREA X 057/19 SPECIAL PURPOSE VESSEL ILE DE
 * BREHAT/FOUC CONDUCTING CABLE LAYING OPERATIONS FROM 31-30` S 156-40` E TO 34-30` S 153-00` E
 * 2.0NM CLEARANCE REQUESTED. NNNN
 */
