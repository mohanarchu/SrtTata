package com.example.srttata.base;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.srttata.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

public abstract class FragmentBase extends Fragment {
    private Unbinder unbinder;
    AlertDialog dialog;
    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutRes(), container, false);
        unbinder = ButterKnife.bind(this, view);
        onViewBound(view);
        return view;
    }

    @Override
    public void onDestroyView() {

        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
    public boolean onBackPressed() {
        return false;
    }
    protected void onViewBound(View view) {

    }

    protected void showDialogue(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

    protected void backClicked() {
    }
    protected Disposable[] subscriptions() {
        return new Disposable[0];
    }

    @LayoutRes
    protected abstract int layoutRes();


}

