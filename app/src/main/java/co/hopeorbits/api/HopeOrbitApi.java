package co.hopeorbits.api;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Tabish Hussain on 7/10/2017.
 */

public interface HopeOrbitApi {

    //    String BASE_URL = "http://anushadatta.com/shrimaa/";
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

    @POST("verification")
    Call<JsonObject> verifyUSer(@Body HashMap<String, Object> map);

    @POST("user/login")
    Call<JsonObject> loginUser(@Body HashMap<String, Object> map);

    @POST("user/forgotPassword")
    Call<JsonObject> forgotpass(@Body HashMap<String, Object> map);
    @POST("user/findUserByNumber")
    Call<JsonObject> findUserByNumber(@Body HashMap<String, Object> map);

    @POST("creditManagement/showCredit")
    Call<JsonObject> showCredit(@Body HashMap<String, Object> map);

    @POST("list")
    Call<JsonObject> getList();
   /* @GET("user/populatePage")
    Call<JsonObject> getStores(@Query("id") String tags);*/

    @GET("getAllPages")
    Call<JsonObject> getAllPages();

   /* @POST("manageOrder")
    Call<JsonObject> orderPlace(@Body HashMap<String, Object> map);*/

    @POST("manageOrder/getOrdersByUser")
    Call<JsonObject> getOrdersByUserId(@Body HashMap<String, Object> map);


    @POST("manageOrder/getOrdersByPageid")
    Call<JsonObject> getOrdersByPageId(@Body HashMap<String, Object> map);

// ==================dipen code
//    http://13.58.110.101:8080/hoprepositoryweb/creditManagement/transferCredit

    @POST("creditManagement/transferCredit")
    Call<JsonObject> credittransfer(@Body HashMap<String, Object> map);

    @POST("user/saveAddress")
    Call<JSONObject> saveaddress(@Body HashMap<String, Object> map);

    @GET("user/populatePage")
    Call<JsonObject> getMyThing(@Query("id") String param1);

    @FormUrlEncoded
    @POST("user/checkPageAvailability")
    Call<ResponseBody> checkavil(@Field("pageName") String username);

    @POST("user/createPage")
    Call<JsonObject> createpage(@Body HashMap<String, Object> map);

    @POST("user/createPageCategories")
    Call<JsonObject> addpagecat(@Body HashMap<String, Object> map);

    @POST("user/createPageChildCategories")
    Call<JsonObject> createPageChildCategories(@Body HashMap<String, Object> map);

    @POST("user/createPageCategoriesItems")
    Call<JsonObject> createcategoryitemset(@Body HashMap<String, Object> map);
    @POST("manageOrder/editOrder")
    Call<JsonObject> removeOrder(@Body HashMap<String, Object> map);
}
