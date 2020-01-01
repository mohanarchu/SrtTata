package com.example.srttata.holder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.srttata.R;
import com.example.srttata.details.Booleans;
import com.example.srttata.details.DetailsView;
import com.example.srttata.home.DataPojo;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public  class DocumentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        enum VIEWTYPE  {   NORMAl, HEADER, TEXT    }



        DataPojo.Results.Docs[] addDocus;
        Context context;
        DataPojo.Results.Docs[] docs;
        public DocumentAdapter(DataPojo.Results.Docs[] docs, DataPojo.Results.Docs[] addDocus, Context context) {
            this.addDocus = addDocus;
            this.docs = docs;
            this.context = context;
        }



        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            if (viewType == VIEWTYPE.HEADER.ordinal()) {
                view = LayoutInflater.from(context).inflate(R.layout.header_text, parent, false);
                return new  HeaderHolder(view);
            } else if (viewType == VIEWTYPE.NORMAl.ordinal()) {
                view = LayoutInflater.from(context).inflate(R.layout.checkbox_design, parent, false);
                return new DocHolder(view);
            } else {
                view = LayoutInflater.from(context).inflate(R.layout.checkbox_design, parent, false);
                return new DocHolder(view);
            }

        }
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder.getItemViewType() == VIEWTYPE.HEADER.ordinal()) {
                HeaderHolder viewHolder = (HeaderHolder) holder;
                if (position == 0) {
                    viewHolder.headerText.setText("Standard Documents");
                } else {
                    viewHolder.headerText.setText("Additional Documents");
                }
            } else if (holder.getItemViewType() == VIEWTYPE.NORMAl.ordinal()) {
                DataPojo.Results.Docs  docs1 = docs[position - 1];
                DocHolder viewHolder = (DocHolder) holder;
                viewHolder.documentType.setText(docs1.getProof());
                viewHolder.docCheck.setChecked(Boolean.valueOf(docs1.getCollected()));
                if (Boolean.valueOf(docs1.getCollected()) && Boolean.valueOf(docs1.getChecked()))
                viewHolder.docCheck.setEnabled(false);
                viewHolder.docCheck.setOnCheckedChangeListener((compoundButton, b) -> {
                    if (b){
                        viewHolder.docCheck.setChecked(true);
                        DetailsView.booleans[position - 1] = true;
                        DetailsView.dates1[position - 1] = setCurrentDate(viewHolder.collectedTime);
                        setCurrentDate(viewHolder.collectedTime);

                    }else {
                        DetailsView.booleans[position -1 ] = false;
                        if (docs1.getDate() != null){
                            viewHolder.collectedTime.setText(docs1.getDate());
                        } else {
                            viewHolder.collectedTime.setText("");
                        }
                        viewHolder.docCheck.setChecked(false);
                    }
                });
                if (docs1.getDate() != null){
                    viewHolder.collectedTime.setText(docs1.getDate());
                } else {
                    viewHolder.collectedTime.setText("");
                }
            } else {
                DataPojo.Results.Docs  docs1 = addDocus[position - docs.length - 2];
                DocHolder viewHolder = (DocHolder) holder;
                viewHolder.documentType.setText(docs1.getProof());
                viewHolder.docCheck.setChecked(Boolean.valueOf(docs1.getCollected()));
                viewHolder.docCheck.setOnCheckedChangeListener((compoundButton, b) -> {
                    if (b){
                        if (Boolean.valueOf(docs1.getCollected()) && !Boolean.valueOf(docs1.getChecked())){
                            viewHolder.docCheck.setChecked(true);
                        } else if (!Boolean.valueOf(docs1.getCollected()) && !Boolean.valueOf(docs1.getChecked())){
                            viewHolder.docCheck.setChecked(true);
                        }
                    } else {
                        if (Boolean.valueOf(docs1.getCollected()) && !Boolean.valueOf(docs1.getChecked())){
                            viewHolder.docCheck.setChecked(false);
                        } else if (!Boolean.valueOf(docs1.getCollected()) && !Boolean.valueOf(docs1.getChecked())){
                            viewHolder.docCheck.setChecked(false);
                        }
                    }
                });
                viewHolder.docCheck.setOnCheckedChangeListener((compoundButton, b) -> {
                    if (b){
                        DetailsView.addBooleans[position - docs.length - 2] = true;
                        viewHolder.docCheck.setChecked(true);
                        DetailsView.addDates[position - docs.length - 2] = setCurrentDate(viewHolder.collectedTime);
                        setCurrentDate(viewHolder.collectedTime);
                    }else {
                        DetailsView.addBooleans[position - docs.length - 2] = false;
                        if (docs1.getDate() != null) {
                            viewHolder.collectedTime.setText(docs1.getDate());
                        } else {
                            viewHolder.collectedTime.setText("");
                        }
                        viewHolder.docCheck.setChecked(false);
                    }
                });
                if (docs1.getDate() != null){
                    viewHolder.collectedTime.setText(docs1.getDate());
                } else {
                    viewHolder.collectedTime.setText("");
                }
            }
        }
        @Override
        public int getItemCount() {
            return docs.length+addDocus.length+2;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return VIEWTYPE.HEADER.ordinal();
            } else if (position <= docs.length) {
                return VIEWTYPE.NORMAl.ordinal();
            } else if (position == docs.length + 1) {
                return VIEWTYPE.HEADER.ordinal();
            } else {
                return VIEWTYPE.TEXT.ordinal();
            }
        }

    private String setCurrentDate(TextView textView){
        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.YEAR);
        NumberFormat f = new DecimalFormat("00");

         String dayLongName = Objects.requireNonNull(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())).substring(0,3);
        @SuppressLint("DefaultLocale") String formats = String.format("%d %s %d, %d:%s %2s" , calendar.get(Calendar.DATE) ,
                dayLongName, calendar.get(Calendar.YEAR) ,
                         calendar.get(Calendar.HOUR), f.format(calendar.get(Calendar.MINUTE)) ,calendar.get(Calendar.AM_PM) ==
                        Calendar.PM ? "PM" : "AM"  );
        textView.setText(formats);;
        return formats;
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.headerText)
        TextView headerText;


        public HeaderHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    class DocHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.collectedTime)
        TextView collectedTime;
        @BindView(R.id.documentType)
        TextView documentType;
        @BindView(R.id.docCheck)
        CheckBox docCheck;


        public DocHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    }
