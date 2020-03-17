package cbots.b_to_c.team_leader;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
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
import cbots.b_to_c.Clients.MainInterface;
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
import cbots.b_to_c.team_leader.detailView.CaDetailActivity;
import cbots.b_to_c.team_leader.detailView.TeamDetailView;
import cbots.b_to_c.team_leader.team.TeamArray;
import cbots.b_to_c.team_leader.team.TeamLeaderAdapter;
import cbots.b_to_c.team_leader.team.TeamsAdapter;

public class TeamLeader extends FragmentBase implements OnChartValueSelectedListener, DataModel, TeamLeaderAdapter.ValueClicked {

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
    @BindView(R.id.mtdCfdTeamLeader)
    Spinner mtdSpinner;
    ArrayList<BarEntry> valueSet1 = new ArrayList<>();
    ArrayList<BarEntry> chartValues = new ArrayList<>();

    TeamLeaderAdapter teamLeaderAdapter = new TeamLeaderAdapter(true,false);
    Calendar calendar = Calendar.getInstance();
    ChartScreen chartScreen;
    private String[] caNames,filteredCas,filteredTeams,filteredTeams1;

    float[] chartFloatValues ;
    @Override
    protected int layoutRes() {
        return R.layout.fragment_team_leader;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onViewBound(View view) {
        teamViewText.setText("All");
        teamLbarChart.setOnChartValueSelectedListener(this);
        setRecycler();
        setCharts();
        getDatas();
        setSpinner();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setSpinner(){
        ArrayAdapter<String> aa = new ArrayAdapter<>(
                getActivity(),android.R.layout.simple_spinner_item,new String[]{"All","MTD","CFD"});
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mtdSpinner.setAdapter(aa);
        mtdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getSelectedItem().toString();

                if (SharedArray.getResult() != null) {
                    if (item.equals("CFD")) {
                        Map<String, Long> caArray =
                                getPreviousMonth(getCurrentDate()).stream().collect(
                                        Collectors.groupingBy(DataPojo.Results::getCa,Collectors.counting()));
                        validateCharts(caArray);
                    } else if (item.equals("MTD")) {
                        Map<String, Long> caArray = getMonthResult(getCurrentDate()).
                                stream().collect(Collectors.groupingBy(DataPojo.Results::
                                getCa,Collectors.counting()));
                        validateCharts(caArray);
                    } else {
                        Map<String, Long> caArray = SharedArray.getResult().stream().
                                collect(Collectors.groupingBy(DataPojo.Results::getCa,Collectors.counting()));

                        validateCharts(caArray);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private void setRecycler() {
        LinearLayoutManager centerZoomLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        teamLeadeerRecycler.setLayoutManager(centerZoomLayoutManager);
        teamLeadeerRecycler.setAdapter(teamLeaderAdapter );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setTeamrecycler(RecyclerView teamRecycler) {
        teamsAdapter = new TeamsAdapter();
        LinearLayoutManager centerZoomLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        teamRecycler.setLayoutManager(centerZoomLayoutManager);
        teamRecycler.setAdapter(teamsAdapter);
        List<String> elephantList;
        if (Checkers.getRoleId(Objects.requireNonNull(getActivity())) == Integer.parseInt(MainInterface.MASTER)) {
            Map<String, Long> allTeams = SharedArray.getResult().stream().
                    collect(Collectors.groupingBy(DataPojo.Results::getTeam,Collectors.counting()));
            filteredTeams1 = new String[allTeams.size()];
            Iterator<Map.Entry<String, Long>> iterators = allTeams.entrySet().iterator();
            int i=0;
            while (iterators.hasNext()) {
                Map.Entry<String, Long> entry = iterators.next();
                filteredTeams1[i] = entry.getKey();
                i++;
            }

            String[] lists = new String[filteredTeams1.length+1];
            lists[0] = "All";
            System.arraycopy(filteredTeams1, 0, lists, 1, filteredTeams1.length);
            elephantList =  Arrays.asList(lists);
        } else {
            String[] array = Checkers.getTeamsDetails(Objects.requireNonNull(getActivity())).
                    split("//");
            String[] lists = new String[array.length+1];
            lists[0] = "All";
            System.arraycopy(array, 0, lists, 1, array.length);
            elephantList =  Arrays.asList(lists);
        }
        teamsAdapter.setArray(elephantList);
        teamsAdapter.notifyDataSetChanged();
        teamsAdapter.setListener(item -> {
            if (changeSortPopUp != null && teamLeaderAdapter != null){
                changeSortPopUp.dismiss();
                teamViewText.setText(item);
                if (!item.equals("All"))
                teamLeaderAdapter.getFilter().filter(item);
                else
                teamLeaderAdapter.getFilter().filter("");
            }
        });
    }

    private void setCharts() {
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
        List<String> elephantList = Arrays.asList(
                Checkers.getTeamsDetails(Objects.requireNonNull(getActivity())).split("//"));
        JsonArray jsonElements = new JsonArray();
        for (int i=0;i<elephantList.size();i++) {
                jsonElements.add(elephantList.get(i));
        }
        jsonObject.add("teams",jsonElements);
        dataPresenter.getDetails(Checkers.getUserToken(Objects.requireNonNull(getActivity())) ,
                Checkers.getName(getActivity()), String.valueOf(Checkers.getRoleId(getActivity())),jsonObject);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showAlart() {
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.recycler_pop,
                mainGroup, false);
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

        Intent intent = new Intent(getActivity(), CaDetailActivity.class);
        intent.putExtra("CaName",filteredCas[(int) e.getX()]);
        startActivity(intent);

    }
    @Override
    public void onNothingSelected() {

    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void showDatas(DataPojo.Results[] results, DataPojo.Count[] counts,
                          int total, int alarmCount) {
        teamLeaderAdapter.setDatas(SharedArray.getResult(),this);
        teamLeaderAdapter.notifyDataSetChanged();
        Map<String, Long> caArray = SharedArray.getResult().stream().
                collect(Collectors.groupingBy(DataPojo.Results::getCa,Collectors.counting()));
        validateCharts(caArray);

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void allCaCheck() {
        Map<String, Long> caArray = SharedArray.getResult().stream().
                collect(Collectors.groupingBy(DataPojo.Results::getCa,Collectors.counting()));
        int i = 0;
        Iterator<Map.Entry<String, Long>> iterator = caArray.entrySet().iterator();
        caNames = new String[caArray.size()];
        filteredCas = new String[caArray.size()] ;
        chartFloatValues = new float[caArray.size()];
        Arrays.fill(chartFloatValues,0);
        while (iterator.hasNext()) {
            Map.Entry<String, Long> entry = iterator.next();
            filteredCas[i] = entry.getKey();
            caNames[i] = entry.getKey().subSequence(0, 5) + "...";
            i++;
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.N)
     private   void validateCharts(Map<String, Long> caArray) {
        allCaCheck();
        chartValues.clear();
        for (Map.Entry<String, Long> entry : caArray.entrySet()) {
            int index = Arrays.asList(caNames).indexOf(entry.getKey().subSequence(0, 5) + "...");
            chartFloatValues[index] = entry.getValue();
        }
        for (int j=0;j<filteredCas.length;j++) {
            chartValues.add(new BarEntry(j, chartFloatValues[j]));
        }
        chartScreen.setTeamLeaderChart(teamLbarChart, chartValues, false,caNames);
        teamLbarChart.invalidate();
        teamLbarChart.requestLayout();
    }

    private Date getCurrentDate(){
        NumberFormat f = new DecimalFormat("00");
        Calendar calendar = Calendar.getInstance();
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
        return strDate;
    }
    @SuppressLint("NewApi")
    private List<DataPojo.Results> getCaResult(String name){
        List<DataPojo.Results> list = SharedArray.getResult();
        list = list.stream().filter(pulse -> pulse.getCa().equals(name) ) .collect(Collectors.toList());
        return list;
    }
    @SuppressLint("NewApi")
    private List<DataPojo.Results> getMonthResult(Date currentMonth){
            List<DataPojo.Results> list = SharedArray.getResult();
        list = list.stream().filter(pulse ->
                DateConversion.getDats(pulse.getOrderDate()).after(currentMonth)).collect(Collectors.toList());
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick(R.id.teamViewText)
    public void onViewClicked() {

           showAlart();
    }

    @Override
    public void clicked(int position, List<DataPojo.Results>  results,boolean fromCalOgin) {

        Intent intent = new Intent(getActivity(), LeaderDocsView.class);
        intent.putExtra("position", position);
        intent.putExtra("caLogin",fromCalOgin);
        SharedArray.setFilterResult(results);
        startActivity(intent);
    }
}
