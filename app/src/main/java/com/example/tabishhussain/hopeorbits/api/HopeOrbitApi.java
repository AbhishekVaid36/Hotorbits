package com.example.tabishhussain.hopeorbits.api;

        import com.google.gson.JsonObject;

        import org.json.JSONObject;

        import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Tabish Hussain on 7/10/2017.
 */

public interface HopeOrbitApi {

//    String BASE_URL = "http://13.58.110.101:8080/hoprepositoryweb/";
    String BASE_URL = "http://13.58.110.101:8080/hoprepositoryweb/";
    Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(MyOkHttpClient.getHttpClient());

    HopeOrbitApi retrofit = builder.build().create(HopeOrbitApi.class);

    class MyOkHttpClient {
        static OkHttpClient getHttpClient() {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            clientBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Content-Type", "application/json")
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
            clientBuilder.addInterceptor(loggingInterceptor);
            clientBuilder.connectTimeout(2, TimeUnit.MINUTES);
            clientBuilder.writeTimeout(2, TimeUnit.MINUTES);
            clientBuilder.readTimeout(2, TimeUnit.MINUTES);
            return clientBuilder.build();
        }
    }

    @POST("user")
    Call<JsonObject> registeredUser(@Body HashMap<String, Object> map);
    @GET("verification")
    Call<JsonObject> verifyUSer(@Body HashMap<String, Object> map);
    @POST("user/login")
    Call<JsonObject> loginUser(@Body HashMap<String, Object> map);
    @POST("user/forgotPassword")
    Call<JsonObject> forgotpass(@Body HashMap<String, Object> map);
    @POST("creditManagement/showCredit")
    Call<JsonObject> showCredit(@Body HashMap<String, Object> map);
    @POST("list")
    Call<JsonObject> getList();
    @POST("user/createPage")
    Call<JSONObject> createpage(@Body HashMap<String, Object> map);
}
