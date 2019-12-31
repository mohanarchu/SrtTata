package com.example.srttata.holder;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.srttata.MainActivity;
import com.example.srttata.R;
import com.example.srttata.appointmant.alerm.Alerm;
import com.example.srttata.appointmant.alerm.Notification2;
import com.example.srttata.config.DateConversion;
import com.example.srttata.details.DetailsView;
import com.example.srttata.details.SharedArray;
import com.example.srttata.expand.VisibleData;
import com.example.srttata.home.DataPojo;
import com.github.angads25.toggle.widget.LabeledSwitch;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.ALARM_SERVICE;

public class A extends RecyclerView.Adapter<A.VH> implements Filterable {
    private int previous = -1;
    private boolean values,searchValue,type;
    private Context context;
    List<DataPojo.Results>  results;
    List<DataPojo.Results>  filterResults;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    private  Fragment fragment;
    public A(Context context,  boolean values,  List<DataPojo.Results>  results,boolean searchValue,boolean type) {
        this.context = context;
        this.searchValue = searchValue;
        this.values = values;
        this.results = results;

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

        setDatas(filterResults, position, holder,type);

        if (values){
            holder.visibleAlarm.setVisibility(View.GONE);
        }else {
            holder.visibleAlarm.setVisibility(View.VISIBLE);
            holder.removeAlarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, Notification2.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
                    AlarmManager alarmManager = (AlarmManager)context. getSystemService(ALARM_SERVICE);
                    alarmManager.cancel(pendingIntent);
                }
            });
        }
        holder.notiChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context.startActivity(new Intent(context, Alerm.class));
            }
        });


        holder.sharedLayout.setOnClickListener(view -> {
            if (context instanceof MainActivity) {
                DetailsView detailsView = new DetailsView();

                Intent intent = new Intent(context,DetailsView.class);
                intent.putExtra("transitionName","");
                intent.putExtra("position",position);
                intent.putExtra("type",type);
                SharedArray.setArray( filterResults);
                context.startActivity(intent);

            }
        });



    }
    @Override
    public int getItemCount() {
        return filterResults.size();
    }

    private void updateVisibilitys(int position ) {


    }
    private void updateVisibility(int position) {

    }


    private void setDatas(  List<DataPojo.Results> results, int posi, VH holdder,boolean type) {
        DataPojo.Results result = results.get(posi);
        List<String> elephantList = Arrays.asList(DateConversion.getDay(result.getOrderDate()).split("-"));
        holdder.date.setText(elephantList.get(0).trim());
        holdder.dayofweek.setText(elephantList.get(1).toUpperCase());
        String upperStringName = result.getContactName().substring(0, 1).toUpperCase() + result.getContactName().substring(1);
        holdder.name.setText(upperStringName);
        holdder.mobileNumbr.setText(result.getContactPhones());
        String part = result.getOrderNo().substring(result.getOrderNo().length() - 6);
        holdder.tokenNumber.setText(part);
        holdder.pendingCount.setText(result.getPendingDocsCount());
        List<String> dates = Arrays.asList(result.getContactFullAddress().split(","));
        if (!type){
            holdder. displayAddress.setText(dates.get(0)+","+dates.get(1));
        }else {
            StringBuilder values= new StringBuilder();
            if (result.getDocs() != null){
                for (int j=0;j<result.getDocs().length;j++){
                    if (!Boolean.valueOf(result.getDocs()[j].getChecked()) &&
                            Boolean.valueOf(result.getDocs()[j].getCollected()) ){
                        values.append(result.getDocs()[j].getProof()).append(",");
                    }
                }
            }
            if (result.getAddDocs() != null){
                for (int j=0;j<result.getAddDocs().length;j++){
                    if (!Boolean.valueOf(result.getAddDocs()[j].getChecked()) &&
                            Boolean.valueOf(result.getAddDocs()[j].getCollected()) ){
                        values.append(result.getAddDocs()[j].getProof());
                    }
                }
            }
            if (!values.toString().isEmpty()){
                holdder.displayAddress.setText(values.toString() .substring(0,values.toString() .length() - 1));
            }else {
                holdder.displayAddress.setText("No documents collected yet");
            }
        }
        if (result.getAlarm() == null || !Boolean.valueOf(result.getAlarm()))
            holdder.fixedAppoinments.setVisibility(View.GONE);
        else
            holdder.fixedAppoinments.setVisibility(View.VISIBLE);
        if (result.getAlarmDate() != null){
      //      holdder.alarmTime.setText(result.getAlarmDate().split(",")[1]);
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
                        if (searchValue){
                            if (row .getContactName().toLowerCase().contains(charString.toLowerCase()) || row.getContactPhones().contains(charSequence)  ) {
                                filteredList.add(row);
                            }
                        } else {
                            if (row.getAgeing().toLowerCase().contains(charString.toLowerCase())  ) {
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

        @BindView(R.id.pendingCOunt)
        TextView pendingCount;
        @BindView(R.id.dayOfweeks)
        TextView dayofweek;
        @BindView(R.id.fixedAppointment)
                ImageView fixedAppoinments;


        VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }


    }






}
