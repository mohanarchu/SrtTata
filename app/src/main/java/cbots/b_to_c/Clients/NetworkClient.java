package cbots.b_to_c.Clients;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {

    public static Retrofit retrofit;

    public void NetworkClient(){

    }

    public static Retrofit getRetrofit(){
        Gson gson = new Gson();
        gson = new GsonBuilder().setLenient().create();
        if(retrofit==null){
            HttpLoggingInterceptor.Level logLevel = HttpLoggingInterceptor.Level.BODY;
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(logLevel);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            OkHttpClient okHttpClient = builder.addInterceptor(interceptor).build();
            retrofit = new Retrofit.Builder()
                        .baseUrl(ApiUrl.L_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(okHttpClient)
                        .build();
        }

        return retrofit;
    }

}
