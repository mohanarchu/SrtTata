package cbots.b_to_c.home;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.ViewBinderHelper;
import cbots.b_to_c.appointmant.alerm.Alerm;
import cbots.b_to_c.base.FragmentBase;
import cbots.b_to_c.config.Checkers;
import cbots.b_to_c.details.DetailsView;
import cbots.b_to_c.details.FragmentInterface;
import cbots.b_to_c.details.Message;
import cbots.b_to_c.details.SharedArray;
import cbots.b_to_c.expand.VisibleData;
import cbots.b_to_c.holder.A;
import cbots.b_to_c.holder.HolderClicked;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import cbots.b_to_c.R;
import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home_Frag extends FragmentBase implements DataModel, OnChartValueSelectedListener, HolderClicked {

    private FragmentInterface fragmentInterfaceListener;
    private DataPresenter dataPresenter;
    @BindView(R.id.activity_recycler)
    RecyclerView activityRecycler;
    @BindView(R.id.barChart)
    BarChart mChart;
    @BindView(R.id.searchActivities)
    ImageView referesh;
    boolean[] visibled;
    private A adapter;
    private List<DataPojo.Results> list;
    ArrayList<VisibleData> visibleData;
    private ChartScreen chartScreen;
    private ArrayList<BarEntry> valueSet1 = new ArrayList<>();
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    boolean value = true,value1 = false;
    String receivedSocket;
    @Override
    protected void onViewBound(View view) {
        mSocket.connect();
        setListening();
        LinearLayoutManager centerZoomLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        activityRecycler.setLayoutManager(centerZoomLayoutManager);
        chartScreen   = new ChartScreen(getActivity());
        mChart.setOnChartValueSelectedListener(this);
        referesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Checkers.isNetworkConnectionAvailable(getActivity()))
                    getResult();
                else
                    showMessage("Check your internet connection");
            }
        });

        if (Checkers.isNetworkConnectionAvailable(getActivity()))
            getResult();
        else
            showMessage("Check your internet connection");
    }

    public static Socket mSocket;
    static {
        try {
            mSocket = IO.socket("https://test.srt-tata.in");
        } catch (URISyntaxException e) {
            Log.i("TAG","URl error"+e.toString());
        }
    }
    private void setListening() {
         mSocket.on("mobileReload", args -> {

            receivedSocket   = Arrays.toString(args).replace("[","").replace("]","").trim();

             dataPresenter.getDetails(Checkers.getUserToken(Objects.requireNonNull(getActivity())) , Checkers.getName(getActivity()),
                     String.valueOf(Checkers.getRoleId(getActivity())), new JsonObject());
        });
    }
    @SuppressLint("NewApi")
    private void showResult(){
        adapter   = new A(getActivity(), true, getList(),false,false,this,null);
        activityRecycler.setAdapter(adapter);
        EventBus.getDefault().post(new Message(receivedSocket));
    }
    @SuppressLint("NewApi")
    List<DataPojo.Results> getList(){
        List<DataPojo.Results> list = SharedArray.getResult();
        list = list.stream().filter(pulse ->  pulse.getPendingDocsCount() == null  || !pulse.getPendingDocsCount().equals("0")).collect(Collectors.toList());
        return list;
    }
    protected int layoutRes() {
        return R.layout.fragment_home_;
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!isHidden()){
            showResult();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            dataPresenter.getDetails(Checkers.getUserToken(Objects.requireNonNull(getActivity())) ,
                    Checkers.getName(getActivity()),String.valueOf(Checkers.getRoleId(getActivity())), new JsonObject());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Handler handler = new Handler();
        Runnable timedTask = new Runnable(){
            @Override
            public void run() {
                getResult();
            }};
        handler.postDelayed(timedTask,1800000000);
    }


    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    private void getResult(){
        dataPresenter = new DataPresenter(getActivity(),this);
        dataPresenter.getDetails(Checkers.getUserToken(Objects.requireNonNull(getActivity())) ,
                Checkers.getName(getActivity()),String.valueOf(Checkers.getRoleId(getActivity())), new JsonObject());
    }

    @SuppressLint("NewApi")
    @Override
    public void showDatas(DataPojo.Results[] results,DataPojo.Count[] counts,int total,int alarmCount) {
        valueSet1.clear();
        list = new ArrayList<DataPojo.Results>(Arrays.asList(results));
        List<DataPojo.Count>  list1 = new ArrayList<>(Arrays.asList(counts));
        valueSet1.add(new BarEntry(0,0));
        valueSet1.add(new BarEntry(1,0));
        valueSet1.add(new BarEntry(2,0));
        valueSet1.add(new BarEntry(3,0));
        for (DataPojo.Count lists : list1) {
           if (lists.get_id().getAgeing().equals(">1week") && lists.get_id().getAgeing() != null )
               valueSet1.set(2, new BarEntry(2,Integer.valueOf(lists.getCount())));
               // valueSet1.add(2, new BarEntry(2,Integer.valueOf(lists.getCount())));
           else if (lists.get_id().getAgeing().equals("new") && lists.get_id().getAgeing() != null  )
               valueSet1.set(0, new BarEntry(0,Integer.valueOf(lists.getCount())));
                //  valueSet1.add(0, new BarEntry(0,Integer.valueOf(lists.getCount())));
           else if ( lists.get_id().getAgeing().equals(">3days") &&  lists.get_id().getAgeing() != null )
               valueSet1.set(1, new BarEntry(1,Integer.valueOf(lists.getCount())));
               //   valueSet1.add(new BarEntry(1,Integer.valueOf(lists.getCount())));
           else if (lists.get_id().getAgeing().equals(">2weeks") &&  lists.get_id().getAgeing() != null )
               valueSet1.set(3, new BarEntry(3,Integer.valueOf(lists.getCount())));
              // valueSet1.add(new BarEntry(3,Integer.valueOf(lists.getCount())));
        }
        chartScreen.setCharts(mChart,valueSet1,false,null);
        mChart.invalidate();
        showResult();
    }

    @Override
    public void success() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMessage(String message) {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if ( e.getX() == 0 && e.getY() != 0 ){
            adapter.getFilter().filter("new");
        } else if ( e.getX() == 1 && e.getY() != 0 ){
            adapter.getFilter().filter(">3days");
        }else if (e.getX() == 2 && e.getY() != 0 ){
            adapter.getFilter().filter(">1week");
        }else if (e.getX() == 3 && e.getY() != 0 ){
            adapter.getFilter().filter(">2weeks");
        }
    }

    @Override
    public void onNothingSelected() {
        adapter.getFilter().filter("");
    }

    @Override
    public void clicked(int position, boolean type, List<DataPojo.Results> list,boolean alarm) {


        if (alarm){
            Intent intent = new Intent(getActivity(), Alerm.class);
            intent.putExtra("position",position);
            SharedArray.setFilterResult(list);
            startActivityForResult(intent,100);
        } else {
            Intent intent = new Intent(getActivity(), DetailsView.class);
            intent.putExtra("transitionName", "");
            intent.putExtra("position", position);
            intent.putExtra("type", type);
            intent.putExtra("filter", false);
            SharedArray.setFilterResult(list);
            startActivityForResult(intent, 100);
        }
    }
}
