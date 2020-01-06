package com.example.srttata.holder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.srttata.MainActivity;
import com.example.srttata.R;
import com.example.srttata.config.Checkers;
import com.example.srttata.config.DateConversion;

import com.example.srttata.config.SendSMs;
import com.example.srttata.config.mail.GMailSender;
import com.example.srttata.config.mail.SendMail;
import com.example.srttata.expand.VisibleData;
import com.example.srttata.home.DataPojo;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class A extends RecyclerView.Adapter<A.VH> implements Filterable {


    private int previous = -1;
    private boolean values, searchValue, type, filter;
    private Context context;
    List<DataPojo.Results> results;
    List<DataPojo.Results> filterResults;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    StringBuilder stringBuilder = new StringBuilder();
    HolderClicked holderClicked;
    private Fragment fragment;
    CancelClicked cancelClicked;

    public A(Context context, boolean values, List<DataPojo.Results> results, boolean searchValue,
             boolean type, HolderClicked holderClicked, CancelClicked cancelClicked) {
        this.context = context;
        this.searchValue = searchValue;
        this.values = values;
        this.results = results;
        this.holderClicked = holderClicked;
        this.cancelClicked = cancelClicked;
        this.type = type;
        this.filterResults = results;
        viewBinderHelper.setOpenOnlyOne(true);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(context).inflate(R.layout.activity_screen, parent, false));
    }

    @SuppressLint({"SetTextI18n", "NewApi"})
    @Override
    public void onBindViewHolder(VH holder, int position) {

        setDatas(filterResults, position, holder, type);

        if (values) {
            holder.visibleAlarm.setVisibility(View.GONE);
        } else {
            holder.visibleAlarm.setVisibility(View.VISIBLE);
            holder.alarmTime.setText(filterResults.get(position).getAlarmDate() != null ?
                    Arrays.asList(filterResults.get(position).getAlarmDate().split(",")).get(1) : "");
            holder.removeAlarm.setOnClickListener(view -> cancelClicked.cancel(position, filterResults));
        }
        holder.notiChoose.setOnClickListener(view -> holderClicked.clicked(position, type, filterResults, true));
        holder.sharedLayout.setOnClickListener(view -> {
            holderClicked.clicked(position, type, filterResults, false);
        });


        String sms = "Dear " + filterResults.get(position).getContactName() + "\n\n" + "Welcome to SRT TATA\n" +
                "Pls provide below documents to process your vechile loan:\n\n" + stringBuilder.toString() + "\n" + "To :" + Checkers.getName(context) + "\n" + "Phone:" + Checkers.getMobile(context);
        holder.makeSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).getReadSMSPermission(new MainActivity.RequestPermissionAction() {
                    @Override
                    public void permissionDenied() {

                    }

                    @Override
                    public void permissionGranted() {
                        String[] number = {filterResults.get(position).getContactPhones()};
                        List<String> strings = new ArrayList<String>();
                        int index = 0;
                        while (index < sms.length()) {
                            SendSMs.MultipleSMS(number, filterResults.get(position).getContactPhones(), sms.substring(index, Math.min(index + 160, sms.length())), context);
                            // SendSMs. MultipleSMS(number, number[0], sms.substring(index, Math.min(index + 160, sms.length()))));
                            index += 160;
                        }

                        Toast.makeText(context, SendSMs.MultipleSMS(number, filterResults.get(position).getContactPhones(), sms, context), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(context,"",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        holder.makeCall.setOnClickListener(view -> {

            ((MainActivity) context).getCallPermission(new MainActivity.RequestPermissionAction() {
                @Override
                public void permissionDenied() {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    callIntent.setData(Uri.parse("tel:"+filterResults.get(position).getContactPhones()));//change the number
                    context. startActivity(callIntent);
                }
                @SuppressLint("MissingPermission")
                @Override
                public void permissionGranted() {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + filterResults.get(position).getContactPhones()));
                    context.startActivity(callIntent);
                }
            });
        });

        holder.makeWhatsapp.setOnClickListener(view -> {
            PackageManager packageManager = context.getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String phone = "+91" + filterResults.get(position).getContactPhones();
            String message ="Hello";
            try {
                String url = "https://api.whatsapp.com/send?phone=" + phone + "&text=" + URLEncoder.encode(sms, "UTF-8");
                i.setPackage("com.whatsapp");
                i.setData(Uri.parse(url));
                if (i.resolveActivity(packageManager) != null) {
                   context.startActivity(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        holder.makeMail.setOnClickListener(view -> {



            Log.i("TAG","Emails"+filterResults.get(position).getCustomerEmail());
            Intent i = new Intent(Intent.ACTION_SENDTO);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{filterResults.get(position).getCustomerEmail()});
            i.putExtra(Intent.EXTRA_SUBJECT, "LOAN PROCESS DOCUMENTS");
            i.putExtra(Intent.EXTRA_TEXT, sms);
            try {
                context.startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(context, "this is sample mail.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void sendEmail(Context context) {
        //Getting content for email
        String email = "mohanraj969@gmail.com";
        String subject = "Hi";
        String message = "Hello";

        //Creating SendMail object
        SendMail sm = new SendMail(context, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }

    @Override
    public int getItemCount() {
        return filterResults.size();
    }

    private void updateVisibilitys(int position) {


    }

    private void updateVisibility(int position) {

    }


    @SuppressLint("SetTextI18n")
    private void setDatas(List<DataPojo.Results> results, int posi, VH holdder, boolean type) {
        DataPojo.Results result = results.get(posi);
        List<String> elephantList = Arrays.asList(DateConversion.getDay(result.getOrderDate()).split("-"));
        holdder.date.setText(elephantList.get(0).trim());
        holdder.dayofweek.setText(elephantList.get(1).toUpperCase());
        String upperStringName = result.getContactName().substring(0, 1).toUpperCase() + result.getContactName().substring(1);
        holdder.name.setText(upperStringName);
        holdder.mobileNumbr.setText(result.getContactPhones());
        String part = result.getOrderNo().substring(result.getOrderNo().length() - 6);
        holdder.tokenNumber.setText(part);
        List<String> dates = Arrays.asList(result.getContactFullAddress().split(","));
        if (!type) {
            holdder.displayAddress.setText(dates.get(0) + "," + dates.get(1));
            holdder.pendingCount.setText(result.getPendingDocsCount());
            if (result.getDocs() != null) {
                for (int j = 0; j < result.getDocs().length; j++) {
                    if (!Boolean.valueOf(result.getDocs()[j].getChecked()) &&
                            Boolean.valueOf(result.getDocs()[j].getCollected())) {
                    }else {
                        stringBuilder.append(result.getDocs()[j].getProof()).append("\n");
                    }
                }
            }
            if (result.getAddDocs() != null) {
                for (int j = 0; j < result.getAddDocs().length; j++) {
                    if (!Boolean.valueOf(result.getAddDocs()[j].getChecked()) &&
                            Boolean.valueOf(result.getAddDocs()[j].getCollected())) {
                    }else {
                        Log.i("TAG","No collected");
                        stringBuilder.append(result.getAddDocs()[j].getProof()).append("\n");
                    }
                }
            }
        } else {
            int counts = 0;
            StringBuilder values = new StringBuilder();
            if (result.getDocs() != null) {
                for (int j = 0; j < result.getDocs().length; j++) {
                    if (!Boolean.valueOf(result.getDocs()[j].getChecked()) &&
                            Boolean.valueOf(result.getDocs()[j].getCollected())) {
                        values.append(result.getDocs()[j].getProof()).append(",");

                        counts++;
                    }else {
                        stringBuilder.append(result.getDocs()[j].getProof()).append("\n");
                    }
                }
            }
            if (result.getAddDocs() != null) {
                for (int j = 0; j < result.getAddDocs().length; j++) {
                    if (!Boolean.valueOf(result.getAddDocs()[j].getChecked()) &&
                            Boolean.valueOf(result.getAddDocs()[j].getCollected())) {
                        values.append(result.getAddDocs()[j].getProof());

                        counts++;
                    }else {
                        Log.i("TAG","No collected");
                        stringBuilder.append(result.getAddDocs()[j].getProof()).append("\n");

                    }
                }
            }
            holdder.displayAddress.setText(values.toString().substring(0, values.toString().length() - 1));
            if (!values.toString().isEmpty()) {

                holdder.pendingCount.setText(counts + "");
            } else {
                holdder.pendingCount.setText(result.getPendingDocsCount());
//                holdder.displayAddress.setText("No documents collected yet");
            }
        }
        if (result.getAlarm() == null || !Boolean.valueOf(result.getAlarm()))
            holdder.fixedAppoinments.setVisibility(View.GONE);
        else
            holdder.fixedAppoinments.setVisibility(View.VISIBLE);
        if (result.getAlarmDate() != null) {
            //   holdder.alarmTime.setText(result.getAlarmDate().split(",")[1]);
        }
        viewBinderHelper.bind(holdder.swipeRevealLayout, result.get_id());
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filterResults = results;
                } else {
                    List<DataPojo.Results> filteredList = new ArrayList<>();
                    for (DataPojo.Results row : results) {
                        if (searchValue) {
                            if (row.getContactName().toLowerCase().contains(charString.toLowerCase()) || row.getContactPhones().contains(charSequence)
                                    || row.getOrderNo().contains(charSequence) || row.getContactFullAddress().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        } else {
                            if (row.getAgeing().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }
                    }
                    filterResults = filteredList;
                }
                FilterResults filterResults1 = new FilterResults();
                filterResults1.values = filterResults;
                return filterResults1;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResult) {
                filterResults = (List<DataPojo.Results>) filterResult.values;
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };


    }


    class VH extends RecyclerView.ViewHolder {
        @BindView(R.id.dates)
        TextView date;
        @BindView(R.id.names)
        TextView name;
        @BindView(R.id.mobileNumbrs)
        TextView mobileNumbr;
        @BindView(R.id.tokenNumbers)
        TextView tokenNumber;
        @BindView(R.id.sharedLayouts)
        LinearLayout sharedLayout;
        @BindView(R.id.swipe_layouts)
        SwipeRevealLayout swipeRevealLayout;
        @BindView(R.id.displayAddresss)
        TextView displayAddress;
        @BindView(R.id.visibleAlarm)
        LinearLayout visibleAlarm;
        @BindView(R.id.removeAlarm)
        ImageView removeAlarm;
        @BindView(R.id.alermTime)
        TextView alarmTime;
        @BindView(R.id.notoficationChooosers)
        ConstraintLayout notiChoose;
        @BindView(R.id.mainLayout)
        LinearLayout mainLayout;
        @BindView(R.id.pendingCOunt)
        TextView pendingCount;
        @BindView(R.id.dayOfweeks)
        TextView dayofweek;
        @BindView(R.id.fixedAppointment)
        ImageView fixedAppoinments;
        @BindView(R.id.makeCall)
        LinearLayout makeCall;
        @BindView(R.id.makeSms)
        LinearLayout makeSms;
        @BindView(R.id.makeWhatsapp)
        LinearLayout makeWhatsapp;
        @BindView(R.id.makeMail)
        LinearLayout makeMail;
        VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
