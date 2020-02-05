package com.example.srttata.config;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.srttata.BaseActivity;
import com.example.srttata.R;
import com.example.srttata.config.password.PasswordPresenter;
import com.example.srttata.config.password.PasswordView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePassword extends BaseActivity implements PasswordView {


    @BindView(R.id.changePasswordTool)
    Toolbar changePasswordTool;
    @BindView(R.id.oldPaswsword)
    TextInputEditText oldPaswsword;
    @BindView(R.id.newPassword)
    TextInputEditText newPassword;
    @BindView(R.id.reEnterPassword)
    TextInputEditText reEnterPassword;
    @BindView(R.id.updatePass)
    CardView updatePass;
    PasswordPresenter passwordPresenter;

    @Override
    protected int layoutRes() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void onViewBound() {
        passwordPresenter = new PasswordPresenter(getApplicationContext(),this);
        setSupportActionBar(changePasswordTool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @OnClick(R.id.updatePass)
    public void onViewClicked() {
        if (Checkers.isEmtpy(oldPaswsword) && Checkers.isEmtpy(newPassword) && Checkers.isEmtpy(reEnterPassword)) {
            showMessage("Fill all details");
        } else if (Checkers.getText(newPassword).equals(Checkers.getText(reEnterPassword))){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("userName",Checkers.getName(getApplicationContext()));
            jsonObject.addProperty("oldPassword",Checkers.getText(oldPaswsword));
            jsonObject.addProperty("newPassword",Checkers.getText(newPassword));
            passwordPresenter.updatePassword(jsonObject);
        }else {
            showMessage("The password doesn't match");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void success() {
        showMessage("Password updated");
        finish();

    }
}
