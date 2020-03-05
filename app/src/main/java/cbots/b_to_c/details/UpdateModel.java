package cbots.b_to_c.details;

import cbots.b_to_c.base.ViewModel;

public interface UpdateModel extends ViewModel {

    @Override
    void showProgress();

    @Override
    void hideProgress();

    @Override
    void showMessage(String message);

    void success(int pos);

}
