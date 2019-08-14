package org.nmnw.apac.util;

public class Const {
  private static final String REG_EXP_POINT_COORDINATE =
      "((\\d{1,3})( |.|-)(\\d{1,3}).(\\d{1,2}))(\\W{0,2})(!?S|!?N)(.)((\\d{1,3})( |.|-)(\\d{1,3}).(\\d{1,2}))(\\W{0,2})(!?E|!?W)";
  private static final String REG_EXP_LINE_WITHOUT_DEGREE_COORDINATE =
      "(((\\d{1,3})( |.|-)(\\d{1,3}))(\\W{0,2})(!?S|!?N))(.)((\\d{1,3})( |.|-)(\\d{1,3}))(\\W{0,2})(!?E|!?W)(.)((TO)(.)((\\d{1,3})( |.|-)(\\d{1,3}))(\\W{0,2})(!?S|!?N))(.)((\\d{1,3})( |.|-)(\\d{1,3}))(\\W{0,2})(!?E|!?W)";
  private static final String REG_EXP_LINE_WITH_DEGREE_COORDINATE =
      "(((\\d{1,3})( |.|-)(\\d{1,3})( |.|-)(\\d{1,3}))(\\W{0,2})(!?S|!?N))(.)(((\\d{1,3})( |.|-)(\\d{1,3})( |.|-)(\\d{1,3}))(\\W{0,2})(!?E|!?W))(.)(TO)(.)(((\\d{1,3})( |.|-)(\\d{1,3})( |.|-)(\\d{1,3}))(\\W{0,2})(!?S|!?N))(.)(((\\d{1,3})( |.|-)(\\d{1,3})( |.|-)(\\d{1,3}))(\\W{0,2})(!?E|!?W))";
  private static final String REG_EXP_LAT =
      "((\\d{1,3})( |.|-)(\\d{1,3}).(\\d{1,2}))(\\W{0,2})(!?S|!?N)";
  private static final String REG_EXP_LON =
      "((\\d{1,3})( |.|-)(\\d{1,3}).(\\d{1,2}))(\\W{0,2})(!?E|!?W)";


  public static String getRegExpPointCoordinate() {
    return REG_EXP_POINT_COORDINATE;
  }
  public static String getRegExpLineWithoutDegreeCoordinate() {
    return REG_EXP_LINE_WITHOUT_DEGREE_COORDINATE;
  }
  public static String getRegExpLineWithDegreeCoordinate() {
    return REG_EXP_LINE_WITH_DEGREE_COORDINATE;
  }
  public static String getRegExpLat() {
    return REG_EXP_LAT;
  }
  public static String getRegExpLon() {
    return REG_EXP_LON;
  }

}
