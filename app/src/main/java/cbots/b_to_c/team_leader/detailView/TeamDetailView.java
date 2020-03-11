package cbots.b_to_c.team_leader.detailView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
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
import java.util.Iterator;
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

public class TeamDetailView extends BaseActivity  {

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
    int type = 1;
    @Override
    protected void onViewBound() {
        chartScreen = new ChartScreen(getApplicationContext());
        ArrayAdapter<String> aa = new ArrayAdapter<>(
                TeamDetailView.this,android.R.layout.simple_spinner_item,new String[]{"All","MTD","CFD"});
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
                        Map<String, Long> caArray =
                                getPreviousMonth(getCurrentDate()).stream().collect(
                                        Collectors.groupingBy(DataPojo.Results::getAgeing,Collectors.counting()));
                        setCharts(caArray,false);
                        datasNotify(getPreviousMonth(getCurrentDate()));
                    } else if (item.equals("MTD")) {
                        Map<String, Long> caArray = getMonthResult(getCurrentDate()).
                                stream().collect(Collectors.groupingBy(DataPojo.Results::
                                getAgeing,Collectors.counting()));
                        setCharts(caArray,false);
                        datasNotify(getMonthResult(getCurrentDate()));
                    } else {
                        Map<String, Long> caArray = getCaResult(name).stream().collect(
                                Collectors.groupingBy(
                                        DataPojo.Results::getAgeing,Collectors.counting()));
                        setCharts(caArray,true);
                        datasNotify(getCaResult(name));
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
                    Map<String, Long> caArray = getCaResult(name).stream().
                            collect(Collectors.groupingBy(DataPojo.Results::getParentProductLine,
                                    Collectors.counting()));
                    setStackChart(caArray);

                    mtdCfd.setSelection(0);
                    mtdCfd.setVisibility(View.GONE);
                } else if (item.equals("Ageing")){
                    Map<String, Long> caArray = getCaResult(name).stream().
                            collect(Collectors.groupingBy(DataPojo.Results::getAgeing,Collectors.counting()));
                    setCharts(caArray,true);

                    mtdCfd.setSelection(0);
                    mtdCfd.setVisibility(View.VISIBLE);
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
        LinearLayoutManager centerZoomLayoutManager = new
                LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        teamCaRecycler.setLayoutManager(centerZoomLayoutManager);
        teamCaRecycler.setAdapter(teamLeaderAdapter );
        setDatas(name);
    }
    private void datasNotify(List<DataPojo.Results> datas){
        teamLeaderAdapter.setDatas(datas);
        teamLeaderAdapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setDatas(String name){
        teamLeaderAdapter.setDatas(getCaResult(name));
        teamLeaderAdapter.notifyDataSetChanged();
        Map<String, Long> caArray = getCaResult(name).stream().
                collect(Collectors.groupingBy(DataPojo.Results::getAgeing,Collectors.counting()));
        setCharts(caArray,true);
    }

    @SuppressLint("NewApi")
    void setStackChart(Map<String, Long> caArray) {
        valueSet1.clear();

        Iterator<Map.Entry<String, Long>> iterator = caArray.entrySet().iterator();
        int i = 0;
        String[] caNames = new String[caArray.size()];
        while (iterator.hasNext()) {
            valueSet1.add(new BarEntry(i,new float[]{0,0,0}));
          Map.Entry<String, Long> entry = iterator.next();
           // Log.i("TAG","Group entry"+ entry);
            Map<String, Long> modelArray =
                    getModel(entry.getKey()).stream().collect(Collectors.groupingBy
                            (DataPojo.Results::getStockStatus,Collectors.counting()));
            caNames[i] = entry.getKey();

            float[] floats = new float[3];
            for (Map.Entry<String, Long> entrys : modelArray.entrySet()) {
                if (entrys.getKey().equals("G-stock") && entrys.getKey()  != null ) {
                    floats[1] = entrys.getValue();
                } else if (Objects.requireNonNull(entrys.getKey()).equals("VNA") && entrys.getKey() != null) {
                    floats[0] = entrys.getValue();
                }  else if ( Objects.requireNonNull(entrys.getKey()).equals("Stock") && entrys.getKey() != null ) {
                  floats[2] = entrys.getValue();
                }
            }
            valueSet1.set(i,new BarEntry(i,floats));
            i++;
        }
        chartScreen.setCharts(teamCaBarchart,valueSet1,true,caNames);
        teamCaBarchart.invalidate();
    }

    @SuppressLint("NewApi")
    void setCharts(Map<String, Long> caArray,boolean stack){

        valueSet1.clear();
        if (!stack) {
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
                chartScreen.setCharts(teamCaBarchart,valueSet1,false,null);
            }
        } else {
            valueSet1.add(new BarEntry(0,new float[]{0,0,0}));
            valueSet1.add(new BarEntry(1,new float[]{0,0,0}));
            valueSet1.add(new BarEntry(2,new float[]{0,0,0}));
            valueSet1.add(new BarEntry(3,new float[]{0,0,0}));
            float[] floats = new float[3];
            float[] floats1 = new float[3];
            float[] floats2 = new float[3];
            float[] floats3 = new float[3];
            for (Map.Entry<String, Long> entry : caArray.entrySet()) {

                if (entry.getKey().equals(">1week") && entry.getKey()  != null ) {
                    Map<String, Long> modelArray =
                            getAgeing(">1week").stream().collect(Collectors.groupingBy
                                    (DataPojo.Results::getStockStatus,Collectors.counting()));
                    for (Map.Entry<String, Long> vechileEntry : modelArray.entrySet()) {
                        if (vechileEntry.getKey().equals("G-stock") && vechileEntry.getKey()  != null ) {
                            floats[1] = vechileEntry.getValue();
                        } else if (Objects.requireNonNull(vechileEntry.getKey()).equals("VNA") && vechileEntry.getKey() != null) {
                            floats[0] = vechileEntry.getValue();
                        }  else if ( Objects.requireNonNull(vechileEntry.getKey()).equals("Stock") && vechileEntry.getKey() != null ) {
                            floats[2] = vechileEntry.getValue();
                        }
                    }
                    valueSet1.set(2, new BarEntry(2,floats));
                } else if (Objects.requireNonNull(entry.getKey()).equals("new") && entry.getKey() != null  ) {

                    Map<String, Long> modelArray =
                            getAgeing("new").stream().collect(Collectors.groupingBy
                                    (DataPojo.Results::getStockStatus,Collectors.counting()));
                    for (Map.Entry<String, Long> vechileEntry : modelArray.entrySet()) {
                        if (vechileEntry.getKey().equals("G-stock") && vechileEntry.getKey()  != null ) {
                            floats1[1] = vechileEntry.getValue();
                        } else if (Objects.requireNonNull(vechileEntry.getKey()).equals("VNA") && vechileEntry.getKey() != null) {
                            floats1[0] = vechileEntry.getValue();
                        }  else if ( Objects.requireNonNull(vechileEntry.getKey()).equals("Stock") && vechileEntry.getKey() != null ) {
                            floats1[2] = vechileEntry.getValue();
                        }
                    }
                    valueSet1.set(0, new BarEntry(0,floats1));
                }  else if ( Objects.requireNonNull(entry.getKey()).equals(">3days") && entry.getKey() != null ) {

                    Map<String, Long> modelArray =
                            getAgeing(">3days").stream().collect(Collectors.groupingBy
                                    (DataPojo.Results::getStockStatus,Collectors.counting()));
                    for (Map.Entry<String, Long> vechileEntry : modelArray.entrySet()) {
                        if (vechileEntry.getKey().equals("G-stock") && vechileEntry.getKey()  != null ) {
                            floats2[1] = vechileEntry.getValue();
                        } else if (Objects.requireNonNull(vechileEntry.getKey()).equals("VNA") && vechileEntry.getKey() != null) {
                            floats2[0] = vechileEntry.getValue();
                        }  else if ( Objects.requireNonNull(vechileEntry.getKey()).equals("Stock") && vechileEntry.getKey() != null ) {
                            floats2[2] = vechileEntry.getValue();
                        }
                    }
                    valueSet1.set(1, new BarEntry(1,floats2));
                }  else if (Objects.requireNonNull(entry.getKey()).equals(">2weeks") &&  entry.getKey() != null ) {
                    Map<String, Long> modelArray =
                            getAgeing(">2weeks").stream().collect(Collectors.groupingBy
                                    (DataPojo.Results::getStockStatus,Collectors.counting()));
                    for (Map.Entry<String, Long> vechileEntry : modelArray.entrySet()) {
                        if (vechileEntry.getKey().equals("G-stock") && vechileEntry.getKey()  != null ) {
                            floats3[1] = vechileEntry.getValue();
                        } else if (Objects.requireNonNull(vechileEntry.getKey()).equals("VNA") && vechileEntry.getKey() != null) {
                            floats3[0] = vechileEntry.getValue();
                        }  else if ( Objects.requireNonNull(vechileEntry.getKey()).equals("Stock") && vechileEntry.getKey() != null ) {
                            floats3[2] = vechileEntry.getValue();
                        }
                    }
                    valueSet1.set(3, new BarEntry(3,floats3));
                }

                chartScreen.setCharts(teamCaBarchart,valueSet1,false,null);
            }
        }
        teamCaBarchart.invalidate();
    }

    @SuppressLint("NewApi")
    private List<DataPojo.Results> getModel(String nam){
        List<DataPojo.Results> list =  getCaResult(name);
        list = list.stream().filter(pulse -> pulse.getParentProductLine().equals(nam)).collect(Collectors.toList());
        return list;
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
    @SuppressLint("NewApi")
    private List<DataPojo.Results> getAgeing(String ageing){
        List<DataPojo.Results> list = getCaResult(name);
        list = list.stream().filter(pulse ->  pulse.getAgeing().equals(ageing)).collect(Collectors.toList());
        return list;
    }
}
