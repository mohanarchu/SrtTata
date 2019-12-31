package com.example.srttata;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.srttata.appointmant.alerm.Alerm;
import com.example.srttata.config.Checkers;
import com.example.srttata.login.LoginModel;
import com.example.srttata.login.LoginPojo;
import com.example.srttata.login.LoginPresenter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginScreen extends AppCompatActivity implements LoginModel {

    LoginPresenter loginPresenter;
    @BindView(R.id.userName)
    TextInputEditText userName;
     @BindView(R.id.password)
    TextInputEditText password;
      @BindView(R.id.logIn)
    CardView logIn;
    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle  savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        ButterKnife.bind(this);
        if (Checkers.getUserLoggedInStatus(getApplicationContext())) {
            startActivity(new Intent(LoginScreen.this, MainActivity.class));
        }
        loginPresenter = new LoginPresenter(getApplicationContext(), this);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               doLogin();
            }
        });
        password.setImeOptions(EditorInfo.IME_ACTION_DONE);
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    Alerm.hideKeyboardFrom(getApplicationContext(),password);
                    doLogin();
                    return true;
                }
                return false;
            }
        });
    }
    void doLogin(){
        if (Checkers.isEmtpy(userName) || Checkers.isEmtpy(password)) {
            Toast.makeText(getApplicationContext(), "Fill all details", Toast.LENGTH_SHORT).show();
        } else {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("userName", Checkers.getText(userName));
            jsonObject.addProperty("password", Checkers.getText(password));
            loginPresenter.Login(jsonObject);
        }
    }


    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showStatus(LoginPojo.Results loginPojo) {
        Checkers.setUserToken(getApplicationContext(), loginPojo.getToken());
        Checkers.setName(getApplicationContext(),loginPojo.getName());
        Log.i("TAG","Exe Name"+loginPojo.getName());

    }

    @Override
    public void success() {
        startActivity(new Intent(LoginScreen.this, MainActivity.class));
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

    }

}

