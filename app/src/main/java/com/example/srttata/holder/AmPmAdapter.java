package com.example.srttata.holder;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.srttata.R;
import com.example.srttata.decorations.InputFilterMinMax;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.WINDOW_SERVICE;

public class AmPmAdapter extends RecyclerView.Adapter<AmPmAdapter.timeHolder> {

    ArrayList<String> arrayList;
    boolean values,twoDigits;
    Context context;
    public AmPmAdapter(ArrayList<String> arrayList, boolean values ,boolean towDigits, Context context) {
        this.arrayList = arrayList;
        this.values =values;
        this.context =context;
        this.twoDigits = towDigits;
    }


    @NonNull
    @Override
    public AmPmAdapter.timeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (!values){
            return new timeHolder(LayoutInflater.from(context).inflate(R.layout.text_view, parent, false));
        } else
            return new timeHolder(LayoutInflater.from(context).inflate(R.layout.am_pm, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull timeHolder holder, int position) {
        NumberFormat f = new DecimalFormat("00");
        holder.itemView.getLayoutParams().height = (int) (getScreenWidth() / 10);
        int positionInList = position;
        if (!values){

        } else {
            holder.vocab.setText( arrayList.get(position));
            holder.vocab.setKeyListener(null);
        }
        //  DisplayMetrics metrics = new DisplayMetrics();

    }

    @Override
    public int getItemCount() {
        if (!values){
            return  Integer.MAX_VALUE  ;
        }else {
            return  arrayList.size();
        }
    }

    public int getScreenWidth() {
        WindowManager wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        return height;
    }
    class timeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.vocab)
        TextView vocab;

        public timeHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
