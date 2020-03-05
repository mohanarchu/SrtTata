package cbots.b_to_c.Clients;




import cbots.b_to_c.details.UpdatePojo;
import cbots.b_to_c.home.DataPojo;
import cbots.b_to_c.login.LoginPojo;
import com.google.gson.JsonObject;

import org.json.JSONObject;

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

    String TEAMLEADER = "507";
    String FINANCE = "508";
    String MASTER = "504";

    @POST("mobile/exeLogin")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Observable<LoginPojo> getLogin(@Body JsonObject statePost);
    @POST("mobile/masterData/{Name}/{Role}")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Observable<DataPojo> getDatas(@Header("access-token") String header, @Path("Name") String name, @Path("Role") String role , @Body JsonObject jsonObject);
    @PATCH("socket/masterData/{OrNo}")
    @Headers({ "Content-Type: application/json;charset=UTF-8"} )
    Observable<UpdatePojo> updateData(@Header("access-token") String header, @Path("OrNo") String id, @Body JsonObject jsonObject);
    @PATCH("mobile/updatePassword")
    @Headers({ "Content-Type: application/json;charset=UTF-8"} )
    Observable<UpdatePojo> updatePassword(@Header("access-token") String header, @Body JsonObject jsonObject);
    @PATCH("socket/alarmSet/{OrNo}")
    @Headers({ "Content-Type: application/json;charset=UTF-8"} )
    Observable<UpdatePojo> setAlarm(@Header("access-token") String header, @Path("OrNo") String id,@Body JsonObject jsonObject);
    @POST("socket/alarmDelete")
    @Headers({ "Content-Type: application/json;charset=UTF-8"} )
    Observable<UpdatePojo> cancelAlarm(@Header("access-token") String header, @Body JsonObject jsonObject);

}
