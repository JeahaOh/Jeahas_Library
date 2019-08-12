package org.nmnw.apac.request;

import java.util.HashMap;
import java.util.List;
import org.nmnw.apac.amsa.Crawler;
import org.nmnw.apac.message.Message;
import org.nmnw.apac.util.TimeHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Communicater {

  private static final Logger logger = LoggerFactory.getLogger(Communicater.class);
  public static int SEND_CNT = 0;

  public void sendListAsObject(List<Message> list) {
    if (!Crawler.PROGRESS && list.size() < 0)
      return;
    for (Message msg : list) {
      sender(msg);
      TimeHandle.interceptor(1000);
    }
    TimeHandle.interceptor(5000);
    logger.info(":::: MSG SEND END {}/{} ::::", SEND_CNT, list.size());
    SEND_CNT = 0;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private void sender(Message cw) {
    logger.info("\t:: Send Message ::\n{}\n", cw.toString());
    // logger.info("\t:: Send Message ::");

    Retrofit retrofit = RetrofitClient.getClient(false);
    Request request = retrofit.create(Request.class);
    Call<?> call = request.postMsg(cw);

    call.enqueue(new Callback() {
      @Override
      public void onResponse(Call call, Response response) {
        logger.info("CONNECTION SUCCESS ->");
        logger.info("STATUS COUNT    : {}", SEND_CNT);
        logger.info("RESPONSE        : {}", response);
        logger.info("RESPONSE CODE   : {}", response.code());
        logger.info("RESPONSE MSG    : {}", response.message());
        logger.info("RESPONSE HEADER : [\n" + response.headers() + "] <-\n\n");
        if (response.code() == 200 && !(response.body() + "").contains("ERROR"))
          SEND_CNT++;
      }

      @Override
      public void onFailure(Call call, Throwable t) {
        logger.info("Connection FAIL");
        logger.info("FAIL CAUSE : {}", t.getMessage());
      }
    });
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public void deleteRequest() {
    logger.info("\t:: Send Delete Request ::");
    HashMap<String, String> country = new HashMap<String, String>();
    country.put("country", "AUS");

    Retrofit retrofit = RetrofitClient.getClient(true);
    Request request = retrofit.create(Request.class);

    Call<?> call = request.deleteMsg(country);
    call.enqueue(new Callback() {
      @Override
      public void onResponse(Call call, Response response) {
        logger.info("CONNECTION SUCCESS ->");
        logger.info("RESPONSE        : {}", response);
        logger.info("RESPONSE CODE   : {}", response.code());
        logger.info("RESPONSE MSG    : {}", response.message());
        logger.info("RESPONSE BODY   : {}", response.body());
        logger.info("RESPONSE HEADER : [\n" + response.headers() + "] <-\n\n");
        if (response.code() != 200 && !(response.body() + "").contains("ERROR"))
          logger.error("\t!! DELETE REQUEST IS ON ERROR !!");
      }

      @Override
      public void onFailure(Call call, Throwable t) {
        logger.info("Connection FAIL");
        logger.info("FAIL CAUSE : {}", t.getMessage());
      }
    });
  }
}
