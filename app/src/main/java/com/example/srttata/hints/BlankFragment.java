package com.example.srttata.hints;


import android.annotation.SuppressLint;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.fragment.app.Fragment;

import com.example.srttata.R;
import com.example.srttata.base.FragmentBase;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends FragmentBase {


    @BindView(R.id.webView)
    WebView webView;

    @Override
    protected int layoutRes() {
        return R.layout.fragment_blank;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onViewBound(View view) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("http://aashitechno.in/spin/terms-conditions.html");

        String data = "<html><body><h1>Hello, Javatpoint!</h1></body></html>";

    }
}
