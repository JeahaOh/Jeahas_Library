package org.nmnw.apac.util;

public class Const {
  public static final String REG_EXP_POINT_COORDINATE =
      "((\\d{1,3})( |.|-)(\\d{1,3}).(\\d{1,2}))(\\W{0,2})(!?S|!?N)(.)((\\d{1,3})( |.|-)(\\d{1,3}).(\\d{1,2}))(\\W{0,2})(!?E|!?W)";
  public static final String REG_EXP_LINE_WITHOUT_DEGREE_COORDINATE =
      "(((\\d{1,3})( |.|-)(\\d{1,3}))(\\W{0,2})(!?S|!?N))(.)((\\d{1,3})( |.|-)(\\d{1,3}))(\\W{0,2})(!?E|!?W)(.)((TO)(.)((\\d{1,3})( |.|-)(\\d{1,3}))(\\W{0,2})(!?S|!?N))(.)((\\d{1,3})( |.|-)(\\d{1,3}))(\\W{0,2})(!?E|!?W)";
  public static final String REG_EXP_LINE_WITH_DEGREE_COORDINATE =
      "(((\\d{1,3})( |.|-)(\\d{1,3})( |.|-)(\\d{1,3}))(\\W{0,2})(!?S|!?N))(.)(((\\d{1,3})( |.|-)(\\d{1,3})( |.|-)(\\d{1,3}))(\\W{0,2})(!?E|!?W))(.)(TO)(.)(((\\d{1,3})( |.|-)(\\d{1,3})( |.|-)(\\d{1,3}))(\\W{0,2})(!?S|!?N))(.)(((\\d{1,3})( |.|-)(\\d{1,3})( |.|-)(\\d{1,3}))(\\W{0,2})(!?E|!?W))";
  public static final String REG_EXP_LAT =
      "((\\d{1,3})( |.|-)(\\d{1,3}).(\\d{1,2}))(\\W{0,2})(!?S|!?N)";
  public static final String REG_EXP_LON =
      "((\\d{1,3})( |.|-)(\\d{1,3}).(\\d{1,2}))(\\W{0,2})(!?E|!?W)";

}
