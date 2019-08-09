package org.nmnw.apac.request;

import org.nmnw.apac.message.Message;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface Request {
  public static final String SELF_PATH = "/post";
  public static final String TO_PATH = "/api/insertNmnw";


  @POST(TO_PATH)
  Call<Object> postMsg(@Body Message cw);

  @POST("/api/deleteCountryNmnw")
  Call<Object> deleteMsg(@Body Object country);
}
