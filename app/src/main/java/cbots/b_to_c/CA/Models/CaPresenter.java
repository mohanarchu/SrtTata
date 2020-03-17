package cbots.b_to_c.CA.Models;

import android.content.Context;

import com.google.gson.JsonObject;

import cbots.b_to_c.Clients.NetworkingUtils;
import cbots.b_to_c.base.ViewModel;
import cbots.b_to_c.config.Checkers;
import cbots.b_to_c.details.UpdatePojo;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CaPresenter {
    Context context;
    CaView caView;
    public CaPresenter(Context context, CaView caView){
        this.caView = caView;
        this.context = context;
    }
    public void getModels(JsonObject jsonObject){
        caView.showProgress();
        NetworkingUtils.getUserApiInstance().getModels (Checkers.getUserToken(context),jsonObject).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<CarModels>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(CarModels carModels) {

                if (carModels.getStatus().equals("200")) {
                    caView.showCarModels(carModels.getValue());
                } else {
                    caView.showMessage("Try again");
                }
                caView.hideProgress();
            }

            @Override
            public void onError(Throwable e) {
                caView.hideProgress();
            }

            @Override
            public void onComplete() {
                caView.hideProgress();
            }
        });
    }
    public void createCustomer(JsonObject jsonObject){
        caView.showProgress();
        NetworkingUtils.getUserApiInstance().createCustomer (Checkers.getUserToken(context),jsonObject).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<UpdatePojo>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(UpdatePojo carModels) {

                if (carModels.getStatus().equals("200")) {
                   caView.showCreates();
                } else {
                    caView.showMessage("Try again");
                }
                caView.hideProgress();
            }

            @Override
            public void onError(Throwable e) {
                caView.hideProgress();
            }

            @Override
            public void onComplete() {
                caView.hideProgress();
            }
        });
    }



    public interface CaView extends ViewModel {
        @Override
        void showProgress();
        @Override
        void hideProgress();
        @Override
        void showMessage(String message);
        void showCarModels(CarModels.Value[] varients);
        void showCreates();
    }
}
