package org.nmnw.apac.request;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {
  public static final String SELF_URL = "http://192.168.0.79:8080";
  public static final String TO_URL = "http://211.43.202.190:3000";

  private static final HttpLoggingInterceptor interceptor =
      new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

  private static final OkHttpClient client = new OkHttpClient.Builder()
      .connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS)
      .writeTimeout(30, TimeUnit.SECONDS).addInterceptor(interceptor).build();

  private static final Retrofit retrofit = new Retrofit.Builder().baseUrl(TO_URL).client(client)
      .addConverterFactory(GsonConverterFactory.create()).build();

  public static Retrofit getClient() {
    return retrofit;
  }
}
