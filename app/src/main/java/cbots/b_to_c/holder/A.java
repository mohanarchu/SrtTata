package cbots.b_to_c.holder;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import cbots.b_to_c.MainActivity;

import cbots.b_to_c.config.Checkers;
import cbots.b_to_c.config.DateConversion;

import cbots.b_to_c.home.DataPojo;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cbots.b_to_c.R;
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
        return new VH(LayoutInflater.from(context).inflate(R.layout.activity_screen,
                parent, false));
    }

    @SuppressLint({"SetTextI18n", "NewApi"})
    @Override
    public void onBindViewHolder(VH holder, int position) {

        setDatas(filterResults, position, holder, type);

        if (values) {
            holder.visibleAlarm.setVisibility(View.GONE);
        } else {
            holder.visibleAlarm.setVisibility(View.VISIBLE);
            holder.alarmTime.setText(filterResults.get(position).getAlarms() != null ?
                    Arrays.asList(filterResults.get(position).getAlarms()[position].getAlarmDate().split(",")).get(1) : "");
            holder.removeAlarm.setOnClickListener(view ->
                    cancelClicked.cancel(position, filterResults , Arrays.asList(filterResults.get(position).getAlarms()[position].getAlarmDate().split(",")).get(1) ));
        }
        holder.notiChoose.setOnClickListener(view -> holderClicked.clicked(position, type, filterResults, true));
        holder.sharedLayout.setOnClickListener(view -> {
            holderClicked.clicked(position, type, filterResults, false);
        });


        holder.makeSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("smsto:" + filterResults.get(position).getContactPhones())); // This ensures only SMS apps respond
                intent.putExtra("sms_body", sendSms(filterResults,position,false));
                context. startActivity(intent);

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
                String url = "https://api.whatsapp.com/send?phone=" + phone + "&text=" +
                        URLEncoder.encode( sendSms(filterResults,position,true), "UTF-8");
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

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{filterResults.get(position).getCustomerEmail()});
            i.putExtra(Intent.EXTRA_SUBJECT, "LOAN PROCESS DOCUMENTS");
            i.putExtra(Intent.EXTRA_TEXT, sendSms(results,position,false));
            try {
                context.startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(context, "this is sample mail.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return filterResults.size();
    }


    private String sendSms(List<DataPojo.Results> results,int posi,boolean type){
        stringBuilder.setLength(0);
        DataPojo.Results result = results.get(posi);
        String sms ="";
            if (result.getDocs() != null) {
                for (int j = 0; j < result.getDocs().length; j++) {
                    if (!Boolean.valueOf(result.getDocs()[j].getChecked()) &&
                            !Boolean.valueOf(result.getDocs()[j].getCollected())) {
                        stringBuilder.append(result.getDocs()[j].getProof()).append("\n");
                    }
                }
            }
            if (result.getAddDocs() != null) {
                for (int j = 0; j < result.getAddDocs().length; j++) {
                    if (!Boolean.valueOf(result.getAddDocs()[j].getChecked()) &&
                            !Boolean.valueOf(result.getAddDocs()[j].getCollected())) {
                        stringBuilder.append(result.getAddDocs()[j].getProof()).append("\n");

                    }
                }
            }
        SpannableStringBuilder str = new
                SpannableStringBuilder("SRT TATA");
            str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    0, str.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            String srt = "";
            if (type){
                srt = " *SRT TATA* ";
            }else {
                srt = "SRT TATA";
            }

        sms = "Dear " + filterResults.get(posi).getContactName() +
                     "\n\n" + "Welcome to"+ srt +"\n" +
                    "Pls provide below documents to process your vechile loan:\n\n" +
                    stringBuilder.toString() + "\n" + "To :" + Checkers.getName(context) + "\n" + "Phone : " +
                    Checkers.getMobile(context);

        return sms;
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
        } else {
            int counts = 0;
            StringBuilder values = new StringBuilder();
            if (result.getDocs() != null) {
                for (int j = 0; j < result.getDocs().length; j++) {
                    if (!Boolean.valueOf(result.getDocs()[j].getChecked()) &&
                            Boolean.valueOf(result.getDocs()[j].getCollected())) {
                        values.append(result.getDocs()[j].getProof()).append(",");
                        counts++;
                    }
                }
            }
            if (result.getAddDocs() != null) {
                for (int j = 0; j < result.getAddDocs().length; j++) {
                    if (!Boolean.valueOf(result.getAddDocs()[j].getChecked()) &&
                            Boolean.valueOf(result.getAddDocs()[j].getCollected())) {
                        values.append(result.getAddDocs()[j].getProof());
                        counts++;
                    }
                }
            }
            holdder.displayAddress.setText
                    (values.toString().substring(0, values.toString().length() - 1));
            if (!values.toString().isEmpty()) {
                holdder.pendingCount.setText(counts + "");
            } else {
                holdder.pendingCount.setText(result.getPendingDocsCount());
//                holdder.displayAddress.setText("No documents collected yet");
            }
        }
        if (result.getAlarms() == null || result.getAlarms().length == 0)
            holdder.fixedAppoinments.setVisibility(View.GONE);
        else
            holdder.fixedAppoinments.setVisibility(View.VISIBLE);

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
