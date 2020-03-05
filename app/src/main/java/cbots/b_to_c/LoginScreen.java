package cbots.b_to_c;

import android.content.Intent;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import cbots.b_to_c.appointmant.alerm.Alerm;
import cbots.b_to_c.config.Checkers;
import cbots.b_to_c.login.LoginModel;
import cbots.b_to_c.login.LoginPojo;
import cbots.b_to_c.login.LoginPresenter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import java.util.Arrays;

import butterknife.BindView;

public class LoginScreen extends BaseActivity implements LoginModel {

    LoginPresenter loginPresenter;
    @BindView(R.id.userName)
    TextInputEditText userName;
    @BindView(R.id.password)
    TextInputEditText password;
    @BindView(R.id.logIn)
    CardView logIn;

    @Override
    protected void onViewBound() {

        loginPresenter = new LoginPresenter(getApplicationContext(), this);
        logIn.setOnClickListener(view ->  doLogin() );
        password.setImeOptions(EditorInfo.IME_ACTION_DONE);
        password.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_DONE){
                Alerm.hideKeyboardFrom(getApplicationContext(),password);
                doLogin();
                return true;
            }
            return false;
        });
    }

    @Override
    protected int layoutRes() {
        return R.layout.activity_login_screen;
    }

    void doLogin(){
        if (Checkers.isNetworkConnectionAvailable(getApplicationContext())){
            if (Checkers.isEmtpy(userName) || Checkers.isEmtpy(password)) {
                Toast.makeText(getApplicationContext(), "Fill all details", Toast.LENGTH_SHORT).show();
            } else {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("userName", Checkers.getText(userName));
                jsonObject.addProperty("password", Checkers.getText(password));
                loginPresenter.Login(jsonObject);
            }
        } else {
            showMessage("Check your internet connection");
        }
    }

    @Override
    public void showProgress() {
        showDialogue();
    }

    @Override
    public void hideProgress() {
        dismissDialogue();
    }
    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showStatus(LoginPojo.Results loginPojo) {
        Checkers.setUserToken(getApplicationContext(), loginPojo.getToken());
        Checkers.setName(getApplicationContext(),loginPojo.getName());
        Checkers.setMobile(getApplicationContext(),loginPojo.getMobile());
        Checkers.setRoleId(getApplicationContext(),Integer.valueOf(loginPojo.getUserRoleId()));
        if (loginPojo.getTeams() != null) {
            StringBuilder nameBuilder = new StringBuilder();
            for (int i=0;i<loginPojo.getTeams().length;i++){
              nameBuilder.append(loginPojo.getTeams()[i]).append("//");
            }
            Checkers.setTeams(getApplicationContext(), nameBuilder.toString());
        }


    }
    @Override
    public void success() {
         startActivity(new Intent(LoginScreen.this, MainActivity.class));
         finish();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

    }

}

