package com.example.srttata.login;

import com.example.srttata.base.ViewModel;

public interface LoginModel extends ViewModel {
    @Override
    void showProgress();
    @Override
    void hideProgress();
    @Override
    void showMessage(String message);
    void showStatus(LoginPojo.Results loginPojo);
    void success();
    void showError(String message);
}
