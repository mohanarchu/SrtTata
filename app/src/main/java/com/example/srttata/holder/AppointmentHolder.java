package com.example.srttata.holder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.srttata.MainActivity;
import com.example.srttata.R;
import com.example.srttata.details.DetailsView;
import com.example.srttata.expand.VisibleData;

import java.util.ArrayList;

public class AppointmentHolder extends   RecyclerView.Adapter<AppointHol>{


    int previous = -1;

    boolean values;
    Context context;
    ArrayList<VisibleData> visibleData;


    public AppointmentHolder(Context context, ArrayList<VisibleData> visibleData){
        this.context =context;

        this.visibleData =visibleData;

    }

    @NonNull
    @Override
    public AppointHol onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new  AppointHol(LayoutInflater.from(context).inflate(R.layout.appointment_view, parent, false));
    }

    @SuppressLint({"SetTextI18n", "NewApi"})
    @Override
    public void onBindViewHolder( AppointHol holder, int position) {
        final VisibleData stationData  = visibleData.get(position);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

//            if (previous != -1){
//
//                if (position == previous){
//                    holder.visible.setVisibility(View.VISIBLE);
//                }else {
//                    holder.visible.setVisibility(View.GONE);
//                }
//            }


    }
    @Override
    public int getItemCount() {
        return visibleData.size();
    }
    void updateVisibilitys(int position, A.VH hol) {
//        hol.visible.setVisibility(View.GONE);
//        notifyItemChanged(position);
    }
    void updateVisibility(int position) {
        if (visibleData.get(position).getVisibility() == View.VISIBLE)
            visibleData.get(position).setVisibility(View.GONE);
        else
            visibleData.get(position).setVisibility(View.VISIBLE);
        notifyItemChanged(position);
    }


}
