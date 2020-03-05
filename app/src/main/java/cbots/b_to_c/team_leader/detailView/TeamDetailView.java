package cbots.b_to_c.team_leader.detailView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import butterknife.BindView;
import cbots.b_to_c.BaseActivity;
import cbots.b_to_c.R;
import cbots.b_to_c.config.DateConversion;
import cbots.b_to_c.details.SharedArray;
import cbots.b_to_c.home.ChartScreen;
import cbots.b_to_c.home.DataPojo;
import cbots.b_to_c.team_leader.team.TeamLeaderAdapter;

public class TeamDetailView extends BaseActivity {


    @BindView(R.id.mtdCfd)
    Spinner mtdCfd;
    @BindView(R.id.vechileFilter)
    Spinner vechileFilter;
    @BindView(R.id.teamCaBarchart)
    BarChart teamCaBarchart;
    @BindView(R.id.teamCaRecycler)
    RecyclerView teamCaRecycler;
    @BindView(R.id.CaNameDetail)
    TextView caNameDetail;
    TeamLeaderAdapter teamLeaderAdapter = new TeamLeaderAdapter();
    String name = "";
    private ArrayList<BarEntry> valueSet1 = new ArrayList<>();
    ChartScreen chartScreen;
    @Override
    protected void onViewBound() {
        ArrayAdapter<String> aa = new ArrayAdapter<>(TeamDetailView.this,android.R.layout.simple_spinner_item,new String[]{"All","MTD","CFD"});
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mtdCfd.setAdapter(aa);
        ArrayAdapter<String> aa1 = new ArrayAdapter<>(TeamDetailView.this,android.R.layout.simple_spinner_item,new String[]{"Ageing","Vechile"});
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vechileFilter.setAdapter(aa1);
        Intent intent = getIntent();
        if (intent != null){
            name = getIntent().getStringExtra("CaName");
            caNameDetail.setText(name);
        }
        setRecycler();
        mtdCfd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getSelectedItem().toString();
                if (item.equals("CFD")) {
                    Map<String, Long> caArray = getPreviousMonth(getCurrentDate()).stream().collect(Collectors.groupingBy(DataPojo.Results::getAgeing,Collectors.counting()));
                    setCharts(caArray);
                }else if (item.equals("MTD")) {
                    Map<String, Long> caArray = getMonthResult(getCurrentDate()).stream().collect(Collectors.groupingBy(DataPojo.Results::getAgeing,Collectors.counting()));
                    setCharts(caArray);
                }else {
                    Map<String, Long> caArray = getCaResult(name).stream().collect(Collectors.groupingBy(DataPojo.Results::getAgeing,Collectors.counting()));
                    setCharts(caArray);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        vechileFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getSelectedItem().toString();

                if (item.equals("Vechile")) {
                    Map<String, Long> caArray = getCaResult(name).stream().collect(Collectors.groupingBy(DataPojo.Results::getAgeing,Collectors.counting()));
                } else if (item.equals("Ageing")){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    Date getCurrentDate(){
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

    @Override
    protected int layoutRes() {
        return R.layout.activity_team_detail_view;
    }
    @SuppressLint("NewApi")
    private void setRecycler() {
        LinearLayoutManager centerZoomLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        teamCaRecycler.setLayoutManager(centerZoomLayoutManager);
        teamCaRecycler.setAdapter(teamLeaderAdapter );
        setDatas(name);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setDatas(String name){

        teamLeaderAdapter.setDatas(getCaResult(name));
        teamLeaderAdapter.notifyDataSetChanged();
        Map<String, Long> caArray = getCaResult(name).stream().collect(Collectors.groupingBy(DataPojo.Results::getAgeing,Collectors.counting()));
        setCharts(caArray);
    }

    void setStackChart(Map<String, Long> caArray) {

    }

    void setCharts(Map<String, Long> caArray){
        chartScreen = new ChartScreen(getApplicationContext());
        valueSet1.clear();
        valueSet1.add(new BarEntry(0,0));
        valueSet1.add(new BarEntry(1,0));
        valueSet1.add(new BarEntry(2,0));
        valueSet1.add(new BarEntry(3,0));
        for (Map.Entry<String, Long> entry : caArray.entrySet()) {
            if (entry.getKey().equals(">1week") && entry.getKey()  != null ) {
                valueSet1.set(2, new BarEntry(2,Integer.valueOf(String.valueOf(entry.getValue()))));
            } else if (Objects.requireNonNull(entry.getKey()).equals("new") && entry.getKey() != null  ) {
                valueSet1.set(0, new BarEntry(0,Integer.valueOf(String.valueOf(entry.getValue()))));
            }  else if ( Objects.requireNonNull(entry.getKey()).equals(">3days") && entry.getKey() != null ) {
                valueSet1.set(1, new BarEntry(1,Integer.valueOf(String.valueOf(entry.getValue()))));
            }  else if (Objects.requireNonNull(entry.getKey()).equals(">2weeks") &&  entry.getKey() != null ) {
                valueSet1.set(3, new BarEntry(3,Integer.valueOf(String.valueOf(entry.getValue()))));
            }
        }
        chartScreen.setCharts(teamCaBarchart,valueSet1,false,null);
        teamCaBarchart.invalidate();
    }
    @SuppressLint("NewApi")
    private List<DataPojo.Results> getCaResult(String name){
        List<DataPojo.Results> list = SharedArray.getResult();
        list = list.stream().filter(pulse -> pulse.getCa().equals(name)).collect(Collectors.toList());
        return list;
    }
    @SuppressLint("NewApi")
    private List<DataPojo.Results> getMonthResult(Date currentMonth){
        List<DataPojo.Results> list = getCaResult(name);
        list = list.stream().filter(pulse -> DateConversion.getDats(pulse.getOrderDate()).after(currentMonth)).collect(Collectors.toList());
        return list;
    }
    @SuppressLint("NewApi")
    private List<DataPojo.Results> getPreviousMonth(Date month){
        List<DataPojo.Results> list = getCaResult(name);
        list = list.stream().filter(pulse -> DateConversion.getDats(pulse.getOrderDate()).before(month)).collect(Collectors.toList());
        return list;
    }
}
