package org.nmnw.apac.request;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {
  // public static final String TO_URL = "http://192.168.0.79:8080";
  public static final String TO_URL = "http://211.43.202.190:3000";
  // public static final String TO_URL = "http://118.220.143.158:3000/";
  // public static final String TO_URL = "http://54.180.100.232:80/";

  // 로그를 위한 Interceptor
  private static final HttpLoggingInterceptor interceptor =
      new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
  // 로그 클라이언트
  private static final OkHttpClient loggingClient = new OkHttpClient.Builder()
      .connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS)
      .writeTimeout(30, TimeUnit.SECONDS).addInterceptor(interceptor).build();
  // 로그 retrofit
  private static final Retrofit loggingRetrofit = new Retrofit.Builder().baseUrl(TO_URL)
      .client(loggingClient).addConverterFactory(GsonConverterFactory.create()).build();

  // 로그 없는 client
  private static final OkHttpClient client =
      new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
          .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).build();

  // 로그 없는 retrofit
  private static final Retrofit retrofit = new Retrofit.Builder().baseUrl(TO_URL).client(client)
      .addConverterFactory(GsonConverterFactory.create()).build();



  public static Retrofit getClient(Boolean log) {
    if (log) {
      return loggingRetrofit;
    } else {
      return retrofit;
    }
  }

  public static Retrofit getClient() {
    return retrofit;
  }
}
