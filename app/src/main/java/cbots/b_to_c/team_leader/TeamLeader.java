package cbots.b_to_c.team_leader;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.OnClick;
import cbots.b_to_c.R;
import cbots.b_to_c.base.FragmentBase;
import cbots.b_to_c.calendar.Views;
import cbots.b_to_c.config.Checkers;
import cbots.b_to_c.config.DateConversion;
import cbots.b_to_c.details.SharedArray;
import cbots.b_to_c.home.ChartScreen;
import cbots.b_to_c.home.DataModel;
import cbots.b_to_c.home.DataPojo;
import cbots.b_to_c.home.DataPresenter;
import cbots.b_to_c.team_leader.detailView.TeamDetailView;
import cbots.b_to_c.team_leader.team.TeamArray;
import cbots.b_to_c.team_leader.team.TeamLeaderAdapter;
import cbots.b_to_c.team_leader.team.TeamsAdapter;

public class TeamLeader extends FragmentBase implements OnChartValueSelectedListener, DataModel {

    @BindView(R.id.teamLeadeerRecycler)
    RecyclerView teamLeadeerRecycler;
    TeamsAdapter teamsAdapter;
    @BindView(R.id.teamLbarChart)
    BarChart teamLbarChart;
    DataPresenter dataPresenter;
    @BindView(R.id.mainGroup)
    LinearLayout mainGroup;
    PopupWindow changeSortPopUp;
    @BindView(R.id.teamViewText)
    TextView teamViewText;
    ArrayList<BarEntry> valueSet1 = new ArrayList<>();
    ArrayList<BarEntry> chartValues = new ArrayList<>();
    TeamLeaderAdapter teamLeaderAdapter = new TeamLeaderAdapter();
    Calendar calendar = Calendar.getInstance();
    ChartScreen chartScreen;
    private float[] newFloat,thereeDays,oneWeek,twoWeeks = new float[2];
    String[] caNames,filterTeams;
    @Override
    protected int layoutRes() {
        return R.layout.fragment_team_leader;
    }

    @Override
    protected void onViewBound(View view) {
        teamViewText.setText("All");
        teamLbarChart.setOnChartValueSelectedListener(this);
        setRecycler();
        setCharts();
        getDatas();
        teamLbarChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void setRecycler() {
        LinearLayoutManager centerZoomLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        teamLeadeerRecycler.setLayoutManager(centerZoomLayoutManager);
        teamLeadeerRecycler.setAdapter(teamLeaderAdapter );
    }

    private void setTeamrecycler(RecyclerView teamRecycler) {
        teamsAdapter = new TeamsAdapter();
        LinearLayoutManager centerZoomLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        teamRecycler.setLayoutManager(centerZoomLayoutManager);
        teamRecycler.setAdapter(teamsAdapter);
        List<String> elephantList = Arrays.asList(Checkers.getTeamsDetails(Objects.requireNonNull(getActivity())).split("//"));
        teamsAdapter.setArray(elephantList);
        teamsAdapter.notifyDataSetChanged();
        teamsAdapter.setListener(item -> {
            if (changeSortPopUp != null && teamLeaderAdapter != null){
                changeSortPopUp.dismiss();
                teamViewText.setText(item);
                teamLeaderAdapter.getFilter().filter(item);
            }
        });
    }
    private void setCharts() {

        oneWeek = new float[]{0f,0f};
        thereeDays = new float[]{0f,0f};
        newFloat = new float[]{0f,0f};
        twoWeeks = new float[]{0f,0f};
        chartScreen = new ChartScreen(getActivity());
        valueSet1.add(new BarEntry(0, new float[]{2}));
        valueSet1.add(new BarEntry(1,0));
        valueSet1.add(new BarEntry(2,0));
        valueSet1.add(new BarEntry(3,0));
        valueSet1.add(new BarEntry(0, 0));
    }

    private void getDatas() {
        dataPresenter = new DataPresenter(getActivity(), this);
        JsonObject jsonObject = new JsonObject();
        List<String> elephantList = Arrays.asList(Checkers.getTeamsDetails(Objects.requireNonNull(getActivity())).split("//"));
        JsonArray jsonElements = new JsonArray();
        for (int i=0;i<elephantList.size();i++){
                jsonElements.add(elephantList.get(i));
        }
        jsonObject.add("teams",jsonElements);
        dataPresenter.getDetails(Checkers.getUserToken(Objects.requireNonNull(getActivity())) ,"SRTMD","504",jsonObject);


    }

    private void showAlart() {
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.recycler_pop, mainGroup, false);
        changeSortPopUp = new PopupWindow(getActivity());
        changeSortPopUp.setContentView(layout);
        RecyclerView recyclerView = layout.findViewById(R.id.teamRecycler);
        setTeamrecycler(recyclerView);
        changeSortPopUp.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeSortPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeSortPopUp.setFocusable(true);
        changeSortPopUp.showAsDropDown(teamViewText);
        Views.dimBehind(changeSortPopUp);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Intent intent = new Intent(getActivity(),TeamDetailView.class);
        intent.putExtra("CaName",filterTeams[(int) e.getX()]);
        startActivity(intent);

    }

    @Override
    public void onNothingSelected() {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void showDatas(DataPojo.Results[] results, DataPojo.Count[] counts, int total, int alarmCount) {

        teamLeaderAdapter.setDatas(SharedArray.getResult());
        teamLeaderAdapter.notifyDataSetChanged();
        validateCharts();

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
     private   void validateCharts() {
        NumberFormat f = new DecimalFormat("00");
        calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH , 1);
        String  currentMonth = f.format(calendar.get(Calendar.MONTH));
        String currentDate = f.format(calendar.get(Calendar.DATE));
        int currentYear = calendar.get(Calendar.YEAR);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate = null;
        try {
            strDate = sdf.parse(01 + "/" + currentMonth + "/" + currentYear);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Map<String, Long> monthResult = getMonthResult(strDate).stream().collect(Collectors.groupingBy(DataPojo.Results::getAgeing,Collectors.counting()));
        Map<String, Long> couterMap = getPreviousMonth(strDate).stream().collect(Collectors.groupingBy(DataPojo.Results::getAgeing,Collectors.counting()));


        Map<String, Long> caArray = SharedArray.getResult().stream().collect(Collectors.groupingBy(DataPojo.Results::getCa,Collectors.counting()));


      //  Map.Entry<String, Long> entry = monthResult.entrySet();
        Iterator<Map.Entry<String, Long>> iterator = caArray.entrySet().iterator();
        int i = 0;
        filterTeams = new String[caArray.size()] ;
        caNames = new String[caArray.size()];
        while (iterator.hasNext()) {
            Map.Entry<String, Long> entry = iterator.next();
            chartValues.add(new BarEntry(i,entry.getValue()));
            caNames[i] = entry.getKey().subSequence(0, 7) + "...";
            filterTeams[i] =  entry.getKey();
            i++;
        }

      /*  for (Map.Entry<String, Long> entry : monthResult.entrySet()) {
            if (entry.getKey().equals(">1week") && entry.getKey()  != null ) {
                oneWeek[0] = Float.valueOf(String.valueOf(entry.getValue()));
            } else if (Objects.requireNonNull(entry.getKey()).equals("new") && entry.getKey() != null  ) {
                newFloat[0] = Float.valueOf(String.valueOf(entry.getValue()));
            }  else if ( Objects.requireNonNull(entry.getKey()).equals(">3days") && entry.getKey() != null ) {
                thereeDays[0] = Float.valueOf(String.valueOf(entry.getValue()));
                //   valueSet1.add(new BarEntry(1,Integer.valueOf(lists.getCount())));
            }  else if (Objects.requireNonNull(entry.getKey()).equals(">2weeks") &&  entry.getKey() != null ) {
                twoWeeks[0] = Float.valueOf(String.valueOf(entry.getValue()));
            }
        }
        for (Map.Entry<String, Long> entry : couterMap.entrySet()) {

            if (entry.getKey().equals(">1week") && entry.getKey()  != null ) {
                oneWeek[1] = Float.valueOf(String.valueOf(entry.getValue()));
            } else if (Objects.requireNonNull(entry.getKey()).equals("new") && entry.getKey() != null  ) {
                newFloat[1] = Float.valueOf(String.valueOf(entry.getValue()));
            }  else if ( Objects.requireNonNull(entry.getKey()).equals(">3days") && entry.getKey() != null ) {
                thereeDays[1] = Float.valueOf(String.valueOf(entry.getValue()));
            }  else if (Objects.requireNonNull(entry.getKey()).equals(">2weeks") &&  entry.getKey() != null ) {
                twoWeeks[1] = Float.valueOf(String.valueOf(entry.getValue()));
            }
        }*/
//        valueSet1.set(0, new BarEntry(0, newFloat));
//        valueSet1.set(2, new BarEntry(2,  oneWeek));
//        valueSet1.set(1, new BarEntry(1,thereeDays));
//        valueSet1.set(3, new BarEntry(3, twoWeeks));
        chartScreen.setCharts(teamLbarChart, chartValues, true,caNames);
        teamLbarChart.invalidate();

    }

    @SuppressLint("NewApi")
    private List<DataPojo.Results> getMonthResult(Date currentMonth){
        List<DataPojo.Results> list = SharedArray.getResult();
        list = list.stream().filter(pulse -> DateConversion.getDats(pulse.getOrderDate()).after(currentMonth)).collect(Collectors.toList());
        return list;
    }
    @SuppressLint("NewApi")
    private List<DataPojo.Results> getPreviousMonth(Date month){
        List<DataPojo.Results> list = SharedArray.getResult();
        list = list.stream().filter(pulse -> DateConversion.getDats(pulse.getOrderDate()).before(month)).collect(Collectors.toList());
        return list;
    }
    @Override
    public void success() {
        dismissDialogue();
    }

    @Override
    public void showProgress() {
        showDialogue();
    }

    @Override
    public void hideProgress() {
          dismissDialogue();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.teamViewText)
    public void onViewClicked() {
        showAlart();
    }
}
