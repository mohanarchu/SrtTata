package com.example.srttata;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity extends AppCompatActivity {
    private Unbinder unbinder;
    AlertDialog dialog;
    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutRes());
        unbinder = ButterKnife.bind(this);
        onViewBound();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
        }
    }


    protected void showDialogue(){
        AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.alery_dialogue_layout);
        dialog  = builder.create();
        dialog.show();
    }

    protected void dismissDialogue(){
        if (dialog != null){
            dialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    protected void onViewBound() {


    }


    protected Disposable[] subscriptions() {
        return new Disposable[0];
    }

    @LayoutRes
    protected abstract int layoutRes();

}
