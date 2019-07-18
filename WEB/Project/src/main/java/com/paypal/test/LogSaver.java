package com.paypal.test;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogSaver {
  StringBuilder log = new StringBuilder();
  
  LogSaver() {
    this.log.append(":::: logSaver is called ::::\n");
  }
  
  void log(String cont) {
    System.out.println(cont);
    log.append(cont);
  }
  
  void save(String master_no){
    log.append(":::: " + master_no + "log END ::::\n");
    //  로그 파일을 저장하기 위해 사용하는 변수
    String log_time = new SimpleDateFormat ( "yyyy_MM_dd", Locale.KOREA ).format( new Date() );
    String file_name = "C:\\Users\\GMTC_JH\\" + "TEST\\" + log_time + ".log";
    
    File file = new File(file_name);
    FileWriter writer = null;
    
    // -> 파일 저장
    try {
      writer = new FileWriter(file, true);
      writer.write(log.toString());
      writer.flush();
      
      System.out.println(":: " + master_no + " customer paypal log save::\n");
    } catch (Exception e) {
      System.out.println("!!! paypal log saving fail at " + log_time +" !!!");
      System.out.println("\tCAUSE -> " + e.getCause());
      System.out.println("\tMESSAGE -> " + e.getMessage());
      e.printStackTrace();
    } finally {
      try{ if(writer != null) { writer.close(); } } catch(Exception e){ System.out.println("\t BufferdOutputStream Close Error MESSAGE -> " + e.getMessage()); }
    }
    //  <- 파일 저장
  }
  
//public static void main(String[] args) {
//LS ls = new LS();
//ls.log("loggerTEST\n");
//ls.save("test");
//}
}
