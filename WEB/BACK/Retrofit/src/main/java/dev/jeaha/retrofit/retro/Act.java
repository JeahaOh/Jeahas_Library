package dev.jeaha.retrofit.retro;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Act {

  private static String responseText = "";
  private static String editText = "Berlin";

  public static void main(String[] args) {
    fetchWeatherDetails();
    System.out.println(responseText);
    System.out.println(editText);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static void fetchWeatherDetails() {
    // Obtain an instance of Retrofit by calling the static method.
    Retrofit retrofit = NetworkClient.getClient();
    /*
     * The main purpose of Retrofit is to create HTTP calls from the Java interface based on the
     * annotation associated with each method. This is achieved by just passing the interface class
     * as parameter to the create method
     */
    WeatherAPIs weatherAPIs = retrofit.create(WeatherAPIs.class);
    /*
     * Invoke the method corresponding to the HTTP request which will return a Call object. This
     * Call object will used to send the actual network request with the specified parameters
     */
    Call<WResponse> call =
        weatherAPIs.getWeatherByCity(editText, "235bef5a99d6bc6193525182c409602c");
    /*
     * This is the line which actually sends a network request. Calling enqueue() executes a call
     * asynchronously. It has two callback listeners which will invoked on the main thread
     */
    call.enqueue(new Callback() {
      @Override
      public void onResponse(Call call, Response response) {
        /*
         * This is the success callback. Though the response type is JSON, with Retrofit we get the
         * response in the form of WResponse POJO class
         */
        if (response.body() != null) {
          WResponse wResponse = (WResponse) response.body();
          responseText = ("Temp: " + wResponse.getMain().getTemp() + "\n " + "Humidity: "
              + wResponse.getMain().getHumidity() + "\n" + "Pressure: "
              + wResponse.getMain().getPressure());
        }
        System.out.println("SUCCESS");
        System.out.println(responseText);
      }

      @Override
      public void onFailure(Call call, Throwable t) {
        System.out.println("FAIL");
        /*
         * Error callback
         */
      }
    });
  }
}
