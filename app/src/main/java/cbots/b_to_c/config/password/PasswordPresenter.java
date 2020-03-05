package cbots.b_to_c.config.password;

import android.content.Context;
import android.util.Log;

import cbots.b_to_c.Clients.NetworkingUtils;
import cbots.b_to_c.config.Checkers;
import cbots.b_to_c.details.UpdatePojo;
import com.google.gson.JsonObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PasswordPresenter  {
    private Context context;
    private PasswordView passwordView;
    public PasswordPresenter(Context context,PasswordView passwordView) {
        this.context  = context;
        this.passwordView = passwordView;
    }

    public void updatePassword(JsonObject jsonObject){
        passwordView.showProgress();
        NetworkingUtils.getUserApiInstance().updatePassword (Checkers.getUserToken(context),jsonObject).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<UpdatePojo>() {
            @Override
            public void onSubscribe(Disposable d) {

            }
            @Override
            public void onNext(UpdatePojo updatePojo) {
                passwordView.hideProgress();
                if ( updatePojo.getStatus().equals("200"))
                    passwordView.success();
                else
                    passwordView.showMessage("Update failed");
            }
            @Override
            public void onError(Throwable e) {
                Log.i("TAG","Update failed"+e.toString());
                passwordView.hideProgress();
            }
            @Override
            public void onComplete() {
                passwordView.hideProgress();
            }
        }) ;
    }
}
