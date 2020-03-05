package cbots.b_to_c.config.password;

import cbots.b_to_c.base.ViewModel;

public interface PasswordView extends ViewModel {

    @Override
    void showProgress();

    @Override
    void hideProgress();

    @Override
    void showMessage(String message);

    void  success();

}
