package cbots.b_to_c.team_leader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.angads25.toggle.widget.LabeledSwitch;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cbots.b_to_c.BaseActivity;
import cbots.b_to_c.R;
import cbots.b_to_c.config.DateConversion;
import cbots.b_to_c.details.SharedArray;
import cbots.b_to_c.home.DataPojo;

public class LeaderDocsView extends BaseActivity {


    @BindView(R.id.makeCall)
    LinearLayout makeCall;
    @BindView(R.id.makeSms)
    LinearLayout makeSms;
    @BindView(R.id.makeWhatsapp)
    LinearLayout makeWhatsapp;
    @BindView(R.id.makeMail)
    LinearLayout makeMail;
    @BindView(R.id.Tldatesss)
    TextView Tldates;
    @BindView(R.id.TldayOfweeksss)
    TextView TldayOfweeks;
    @BindView(R.id.tlCusNamess)
    TextView tlCusName;
    @BindView(R.id.tLCaMobiless)
    TextView tLCaMobile;
    @BindView(R.id.tLCaNamess)
    TextView tLCaName;
    @BindView(R.id.tlInhousess)
    TextView tlInhouse;
    @BindView(R.id.tLVechileModelss)
    TextView tLVechileModel;
    @BindView(R.id.tLVechileStatusss)
    TextView tLVechileStatus;
    @BindView(R.id.fullNameTeam)
    TextView fullNameTeam;
    @BindView(R.id.fullAddressTeam)
    TextView fullAddressTeam;
    @BindView(R.id.customerTypeTeam)
    TextView customerTypeTeam;
    @BindView(R.id.financierTeam)
    TextView financierTeam;
    @BindView(R.id.switchAlternateNumberTeam)
    LabeledSwitch switchAlternateNumberTeam;
    @BindView(R.id.alternamtePhoneTeam)
    EditText alternamtePhoneTeam;
    DataPojo.Results results;
    int position;
    boolean type, filter;
    @BindView(R.id.detailCusname)
    TextView detailCusname;
    @BindView(R.id.detailsCusmobileNumbr)
    TextView detailsCusmobileNumbr;
    @BindView(R.id.detailsCustokenNumber)
    TextView detailsCustokenNumber;
    @BindView(R.id.detailsCuspendingDocsCount)
    TextView detailsCuspendingDocsCount;
    @BindView(R.id.detailsCusdisplayAddress)
    TextView detailsCusdisplayAddress;

    boolean fromcaLogin;
    @BindView(R.id.exchanegVechileLayout)
    LinearLayout exchanegVechileLayout;

    @Override
    protected int layoutRes() {
        return R.layout.activity_leader_docs_view;
    }

    @Override
    protected void onViewBound() {
        Intent intent = getIntent();
        if (intent != null) {
            position = intent.getIntExtra("position", 0);
            results = SharedArray.getFilterResult().get(position);
            fromcaLogin = intent.getBooleanExtra("caLogin", false);
            showDetails(results, fromcaLogin);
        }
    }

    @SuppressLint("SetTextI18n")
    private void showDetails(DataPojo.Results result, boolean filter) {
        List<String> elephantList = Arrays.asList(DateConversion.getDay(result.getOrderDate()).split("-"));
        Tldates.setText(elephantList.get(0).trim());
        TldayOfweeks.setText(elephantList.get(1).toUpperCase());
        String upperStringName = result.getContactName().substring(0, 1).toUpperCase() + result.getContactName().substring(1);
        tLCaName.setText(upperStringName);
        tLCaMobile.setText(result.getContactPhones());
        String part = result.getOrderNo().substring(result.getOrderNo().length() - 6);
        detailsCustokenNumber.setText(part);
        detailCusname.setText(result.getContactName());
        fullNameTeam.setText(result.getContactName());
        List<String> dates = Arrays.asList(result.getContactFullAddress().split(","));
        // detailsCusdisplayAddress.setText(dates.get(0) + "," + dates.get(1));
        detailsCusmobileNumbr.setText(result.getContactPhones());
        alternamtePhoneTeam.setText(result.getAlternateMobileNumber() != null && !result.getAlternateMobileNumber().equals("") ?
                result.getAlternateMobileNumber() : "--");
        fullAddressTeam.setText(result.getContactFullAddress());
        tLVechileModel.setText(result.getParentProductLine());
        customerTypeTeam.setText(result.getCustomerType() != null && !result.getCustomerType().equals("") ? result.getCustomerType() : "NOT FOUND");
        switchAlternateNumberTeam.setOn(Boolean.parseBoolean(result.getAlternateMobileToggle()));
        tlCusName.setText(result.getContactName());
        tLCaName.setText(result.getCa());
        tlInhouse.setText(result.getFinanceOption());
        tLVechileStatus.setText(result.getStockStatus());
        tLVechileModel.setText(result.getProductLine());
        tLCaMobile.setText(result.getCaMobile());
        detailsCusdisplayAddress.setText("PAN : " + "DXBPM1816F");
        if (filter) {
            exchanegVechileLayout.setVisibility(View.VISIBLE);
        } else {
            exchanegVechileLayout.setVisibility(View.GONE);
        }
    }

}
