package com.example.srttata.Clients;




import com.example.srttata.details.UpdatePojo;
import com.example.srttata.home.DataPojo;
import com.example.srttata.login.LoginPojo;
import com.google.gson.JsonObject;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MainInterface {
    @POST("mobile/exeLogin")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Observable<LoginPojo> getLogin(@Body JsonObject statePost);
    @GET("mobile/masterData/{Name}")
    @Headers({ "Content-Type: application/json;charset=UTF-8"} )
    Observable<DataPojo> getDatas(@Header("access-token") String header, @Path("Name") String name);
    @PATCH("mobile/masterData/{OrNo}")
    @Headers({ "Content-Type: application/json;charset=UTF-8"} )
    Observable<UpdatePojo> updateData(@Header("access-token") String header, @Path("OrNo") String id,@Body JsonObject jsonObject);

}
