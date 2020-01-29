package com.example.srttata.config.password;

import com.example.srttata.base.ViewModel;

public interface PasswordView extends ViewModel {

    @Override
    void showProgress();

    @Override
    void hideProgress();

    @Override
    void showMessage(String message);

    void  success();

}
