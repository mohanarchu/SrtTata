package cbots.b_to_c.login;

import android.content.Context;
import android.util.Log;

import cbots.b_to_c.Clients.NetworkingUtils;

import cbots.b_to_c.config.Checkers;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cbots.b_to_c.R;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class LoginPresenter {

    Context context;
    LoginModel loginModel;
    public LoginPresenter(Context context,LoginModel loginModel) {
        this.context = context;
        this.loginModel = loginModel;
    }
    public void Login(JsonObject jsonObject){
        loginModel.showProgress();
        NetworkingUtils.getUserApiInstance().getLogin (jsonObject).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<LoginPojo>() {
            @Override
            public void onSubscribe(Disposable d) {

            }
            @Override
            public void onNext(LoginPojo comPojo) {
                loginModel.hideProgress();
                Log.i("TAG","Results "+ comPojo.getMessage());
                if (comPojo.getStatus().equals("200")) {
                    Checkers.setUserLoggedInStatus(context,true);
                    loginModel.showMessage(comPojo.getMessage());
                    loginModel.showStatus(comPojo.getResults());
                    loginModel.success();
                } else {
                    loginModel.showError(comPojo.getMessage());
                }
            }
            @Override
            public void onError(Throwable e) {

                loginModel.hideProgress();
                HttpException error = (HttpException)e;
                try {
                    String errorBody = error.response().errorBody().string();
                    JSONObject jsonObject = new JSONObject(errorBody);
                    loginModel.showError(jsonObject.getString(context.getString(R.string.message)));

                } catch (IOException | JSONException e1) {
                    e1.printStackTrace();
                }

            }

            @Override
            public void onComplete() {
                loginModel.hideProgress();
            }
        });
    }
}
