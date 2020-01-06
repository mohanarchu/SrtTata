package com.example.srttata.holder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public  class DocumentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        enum VIEWTYPE  {   NORMAl, HEADER, TEXT    }



        DataPojo.Results.Docs[] addDocus;
        boolean filter;
        Context context;
        DataPojo.Results.Docs[] docs;
        public DocumentAdapter(DataPojo.Results.Docs[] docs, DataPojo.Results.Docs[] addDocus, Context context,boolean filter) {
            this.addDocus = addDocus;
            this.docs = docs;
            this.filter = filter;
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
                    viewHolder.headerText.setText(addDocus.length != 0 ?  "Additional Documents": "Additional Documents : N/A"  );
                }
            } else if (holder.getItemViewType() == VIEWTYPE.NORMAl.ordinal()) {

                DataPojo.Results.Docs  docs1 = docs[position - 1];

                DocHolder viewHolder = (DocHolder) holder;
                viewHolder.documentType.setText(docs1.getProof());
                if (Boolean.valueOf(docs1.getChecked()) || Boolean.valueOf(docs1.getCollected()))
                viewHolder.docCheck.setChecked(true);
                else
                viewHolder.docCheck.setChecked(false);
                viewHolder.collectedTime.setText(docs1.getCheckedDate());
                if (Boolean.valueOf(docs1.getChecked()))
                viewHolder.docCheck.setEnabled(false);
                viewHolder.docCheck.setOnCheckedChangeListener((compoundButton, b) -> {
                    if (b){
                        viewHolder.docCheck.setChecked(true);
                        DetailsView.booleans[position - 1] = true;
                        setCurrentDate(viewHolder.collectedTime);
                        DetailsView.dates1[position - 1] = setCurrentDate(viewHolder.collectedTime);
                    }else {
                        DetailsView.booleans[position -1 ] = false;
                        viewHolder.collectedTime.setText("");
//                        if (docs1.getDate() != null){
//                            viewHolder.collectedTime.setText(docs1.getDate());
//                        } else {
//                            viewHolder.collectedTime.setText("");
//                        }
                        viewHolder.docCheck.setChecked(false);
                        DetailsView.dates1[position - 1] = "";
                    }
                });
                if (docs1.getDate() != null){
                    viewHolder.collectedTime.setText(docs1.getDate());
                } else {
                    viewHolder.collectedTime.setText("");
                }
                if (filter)
                {
                    if ( !Boolean.valueOf(docs1.getCollected()))
                        viewHolder.itemView.setVisibility(View.GONE);
                }

            } else {
                DataPojo.Results.Docs  docs1 = addDocus[position - docs.length - 2];
                DocHolder viewHolder = (DocHolder) holder;
                viewHolder.documentType.setText(docs1.getProof());
                viewHolder.docCheck.setChecked(Boolean.valueOf(docs1.getCollected()));
                viewHolder.collectedTime.setText(docs1.getCheckedDate());
                viewHolder.docCheck.setOnCheckedChangeListener((compoundButton, b) -> {
                    if (b){
                        DetailsView.addBooleans[position - docs.length - 2] = true;
                        viewHolder.docCheck.setChecked(true);
                        setCurrentDate(viewHolder.collectedTime);
                        DetailsView.addDates[position - docs.length - 2] = setCurrentDate(viewHolder.collectedTime);
                    } else {
                        DetailsView.addBooleans[position - docs.length - 2] = false;
                        viewHolder.collectedTime.setText("");

//                        if (docs1.getDate() != null) {
//                            viewHolder.collectedTime.setText(docs1.getDate());
//                        } else {
//                            viewHolder.collectedTime.setText("");
//                        }
                        viewHolder.docCheck.setChecked(false);
                        DetailsView.addDates[position - docs.length - 2] ="";

                    }
                });
                if (docs1.getDate() != null){
                    viewHolder.collectedTime.setText(docs1.getDate());
                } else {
                    viewHolder.collectedTime.setText("");
                }
                if (filter)
                {
                    if ( !Boolean.valueOf(docs1.getCollected()))
                        viewHolder.itemView.setVisibility(View.GONE);
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
       String month =   String.format(Locale.US,"%tB",calendar).substring(0,3);
        // String dayLongName = Objects.requireNonNull(calendar.getDisplayName(Calendar.MONTH+1, Calendar.LONG, Locale.getDefault())).substring(0,3);
        Log.i("TAG","Months"+ month);
        @SuppressLint("DefaultLocale") String formats = String.format("%d %s %d, %s %2s" , calendar.get(Calendar.DATE) ,month, calendar.get(Calendar.YEAR) ,
                       getTime(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)+":"+f.format(calendar.get(Calendar.MINUTE)))) ,calendar.get(Calendar.AM_PM) ==
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
    String getTime(String time){
        Date dateObj =  null;
        String formattedDate="";
        try {
            @SuppressLint("SimpleDateFormat") final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat( "hh:mm");
            dateObj = sdf.parse(time);
            formattedDate = outputFormat.format(dateObj);
            System.out.println(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        assert dateObj != null;
        return  formattedDate;
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
