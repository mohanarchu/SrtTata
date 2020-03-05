package cbots.b_to_c.login;

import cbots.b_to_c.base.ViewModel;

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
