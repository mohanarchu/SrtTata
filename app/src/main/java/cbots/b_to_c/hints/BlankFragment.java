package cbots.b_to_c.hints;


import android.annotation.SuppressLint;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.fragment.app.Fragment;

import cbots.b_to_c.base.FragmentBase;

import cbots.b_to_c.R;
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
