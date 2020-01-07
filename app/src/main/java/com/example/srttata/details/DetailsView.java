package com.example.srttata.details;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.srttata.BaseActivity;
import com.example.srttata.MainActivity;
import com.example.srttata.R;
import com.example.srttata.config.Checkers;
import com.example.srttata.config.DateConversion;
import com.example.srttata.config.SendSMs;
import com.example.srttata.holder.DocumentAdapter;
import com.example.srttata.home.DataPojo;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsView extends BaseActivity implements UpdateModel {

    @BindView(R.id.transtionLayout)
    LinearLayout transtionLayout;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.dayOfweek)
    TextView dayOfweek;

    @BindView(R.id.relative)
    RelativeLayout relative;
    @BindView(R.id.notoficationChoooser)
    ConstraintLayout notoficationChoooser;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.mobileNumbr)
    TextView mobileNumbr;
    @BindView(R.id.tokenNumber)
    TextView tokenNumber;
    @BindView(R.id.addressReccyler)
    RecyclerView addressReccyler;
    @BindView(R.id.sharedLayout)
    LinearLayout sharedLayout;
    @BindView(R.id.swipe_layout)
    SwipeRevealLayout swipeLayout;
    @BindView(R.id.fullAddress)
    TextView fullAddress;
    @BindView(R.id.customerType)
    TextView customerType;
    @BindView(R.id.alternamtePhone)
    EditText alternamtePhone;
    @BindView(R.id.switchAlternateNumber)
    LabeledSwitch switchAlternateNumber;
    @BindView(R.id.documentRecycler)
    RecyclerView documentRecycler;

    @BindView(R.id.clickLayout)
    RelativeLayout clickLayout;
    @BindView(R.id.closeEdit)
    LinearLayout closeEdit;
    @BindView(R.id.saveEdit)
    LinearLayout saveEdit;
    @BindView(R.id.dullLayout)
    CardView dullLayout;
    @BindView(R.id.remarksExe)
    EditText remarksEze;
    @BindView(R.id.displayAddress)
    TextView displayAddress;
    @BindView(R.id.linear)
    LinearLayout remarksBox;
    @BindView(R.id.fixedAppointments)
    ImageView fixedAppoinments;
    @BindView(R.id.financier)
    TextView financier;
    @BindView(R.id.pendingDocsCount)
    TextView pendingCount;
    @BindView(R.id.fullName)
    TextView fullName;
    boolean alternareState;
    UpdatePresenter updatePresenter;
    public static boolean[] booleans, addBooleans;
    public static String[] dates1, addDates;
    List<UploadDatas> uploadData, addData;
    DataPojo.Results results;
    @BindView(R.id.makeCall)
    LinearLayout makeCall;
    @BindView(R.id.makeSms)
    LinearLayout makeSms;
    @BindView(R.id.makeWhatsapp)
    LinearLayout makeWhatsapp;
    @BindView(R.id.makeMail)
    LinearLayout makeMail;
    StringBuilder stringBuilder = new StringBuilder();
    private final static int REQUEST_READ_SMS_PERMISSION = 3004;
    private final static int REQUEST_CALL_PERMISSION = 3003;
    public final static String READ_SMS_PERMISSION_NOT_GRANTED = "Please allow " + "SRT" + " to access your SMS from setting";
    MainActivity.RequestPermissionAction onPermissionCallBack;

    @SuppressLint("NewApi")
    @Override
    protected void onViewBound() {


        uploadData = new ArrayList<>();
        addData = new ArrayList<>();
        updatePresenter = new UpdatePresenter(getApplicationContext(), this);
        Intent intent = getIntent();
        if (intent != null) {
            String transitionName = intent.getStringExtra("transitionName");
            int position = intent.getIntExtra("position", 0);
            boolean type = intent.getBooleanExtra("type", false);
            boolean filter = intent.getBooleanExtra("filter", false);
            if (type) {
                remarksBox.setVisibility(View.GONE);
            }
            results = SharedArray.getFilterResult().get(position);
            showDetails(results, filter);

            Log.i("TAG", "Filters" + filter);
        }

        new Handler().postDelayed(() -> dullLayout.setVisibility(View.VISIBLE), 600);

        closeEdit.setOnClickListener(view -> finish());

        // switchAlternateNumber.setOnToggledListener((toggleableView, isOn) -> alternareState = isOn);
        String sms   = "Dear " + results.getContactName() +"\n\n"+"Welcome to SRT TATA\n"+
                "Pls provide below documents to process your vechile loan:\n\n"+stringBuilder.toString()  +"\n"+ "To :"+
                Checkers.getName(getApplicationContext())+"\n"+  "Phone:"+ Checkers.getMobile(getApplicationContext());
        makeSms.setOnClickListener(view -> {

            Intent intent1 = new Intent(Intent.ACTION_SENDTO);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.setData(Uri.parse("smsto:" + results.getContactPhones())); // This ensures only SMS apps respond
            intent1.putExtra("sms_body", sms);
             startActivity(intent1);

//               getReadSMSPermission(new MainActivity.RequestPermissionAction() {
//                    @Override
//                    public void permissionDenied() {
//

//                    }
//                    @Override
//                    public void permissionGranted() {
//                        String[] number = {results.getContactPhones()};
//                        List<String> strings = new ArrayList<String>();
//                        int index = 0;

//                        while (index < sms.length()) {
//                            SendSMs.MultipleSMS(number,results.getContactPhones(),sms.substring(index, Math.min(index + 160, sms.length())),getApplicationContext());
//                            // SendSMs. MultipleSMS(number, number[0], sms.substring(index, Math.min(index + 160, sms.length()))));
//                            index += 160;
//                        }
//
//                       showMessage("Sms sent");
//                        //Toast.makeText(context,"",Toast.LENGTH_SHORT).show();
//                    }
//                });
        });

       makeCall.setOnClickListener(view -> {
           getCallPermission(new MainActivity.RequestPermissionAction() {
               @Override
               public void permissionDenied() {
                   Intent callIntent = new Intent(Intent.ACTION_DIAL);
                   callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   callIntent.setData(Uri.parse("tel:"+results.getContactPhones()));//change the number
                   startActivity(callIntent);
               }

               @SuppressLint("MissingPermission")
               @Override
               public void permissionGranted() {
                   Intent callIntent = new Intent(Intent.ACTION_CALL);
                   callIntent.setData(Uri.parse("tel:" + results.getContactPhones()));
                  startActivity(callIntent);
               }
           });
        });
      makeWhatsapp.setOnClickListener(view -> {
            PackageManager packageManager = getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String phone = "+91" + results.getContactPhones();
            String message ="Hello";
            try {
                String url = "https://api.whatsapp.com/send?phone=" + phone + "&text=" + URLEncoder.encode(sms, "UTF-8");
                i.setPackage("com.whatsapp");
                i.setData(Uri.parse(url));
                if (i.resolveActivity(packageManager) != null) {
                   startActivity(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        makeMail.setOnClickListener(view -> {

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{results.getCustomerEmail()});
            i.putExtra(Intent.EXTRA_SUBJECT, "LOAN PROCESS DOCUMENTS");
            i.putExtra(Intent.EXTRA_TEXT, sms);
            try {
             startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(getApplicationContext(), "this is sample mail.", Toast.LENGTH_SHORT).show();
            }
        });

        saveEdit.setOnClickListener(view -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("alternateMobileNumber", Checkers.getText(alternamtePhone));
            jsonObject.addProperty("alternateMobileToggle", alternareState);
            int pendingCOunt = 0;
            JsonArray jsonElements = new JsonArray();
            for (int i = 0; i < uploadData.size(); i++) {
                JsonObject jsons = new JsonObject();
                jsons.addProperty("collectedDate", dates1[i]);
                jsons.addProperty("proof", uploadData.get(i).getProof());
                jsons.addProperty("checked", uploadData.get(i).getChecked());
                jsons.addProperty("collected", booleans[i]);
                jsons.addProperty("checkedDate", uploadData.get(i).getChekedDate());
                jsonElements.add(jsons);
                if (!booleans[i])
                    pendingCOunt++;
            }
            JsonArray addDocsElements = new JsonArray();
            for (int i = 0; i < addData.size(); i++) {
                JsonObject jsons = new JsonObject();
                jsons.addProperty("collectedDate", addDates[i]);
                jsons.addProperty("proof", addData.get(i).getProof());
                jsons.addProperty("checked", addData.get(i).getChecked());
                jsons.addProperty("collected", addBooleans[i]);
                jsons.addProperty("checkedDate", addData.get(i).getChekedDate());
                addDocsElements.add(jsons);
                if (!addBooleans[i])
                    pendingCOunt++;
            }
            jsonObject.addProperty("pendingDocsCount", pendingCOunt);
            jsonObject.addProperty("exeRemarks", remarksEze.getText().toString());
            jsonObject.add("docs", jsonElements);
            jsonObject.add("addDocs", addDocsElements);
            if (results != null) {
                updatePresenter.update(results.getOrderNo(), jsonObject, 0);
            }
            // Log.d("TAG","documents"+jsonObject);
        });
    }

    private void showDetails(DataPojo.Results result, boolean filter) {
        List<String> elephantList = Arrays.asList(DateConversion.getDay(result.getOrderDate()).split("-"));
        date.setText(elephantList.get(0).trim());
        dayOfweek.setText(elephantList.get(1).toUpperCase());
        String upperStringName = result.getContactName().substring(0, 1).toUpperCase() + result.getContactName().substring(1);
        name.setText(upperStringName);
        mobileNumbr.setText(result.getContactPhones());
        String part = result.getOrderNo().substring(result.getOrderNo().length() - 6);
        tokenNumber.setText(part);
        alternamtePhone.setText(result.getAlternateMobileNumber() == null ?
                "" : result.getAlternateMobileNumber());
        fullAddress.setText(result.getContactFullAddress());
        LinearLayoutManager centerZoomLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        documentRecycler.setLayoutManager(centerZoomLayoutManager);
        documentRecycler.setAdapter(new DocumentAdapter(result.getDocs(), result.getAddDocs(), getApplicationContext(), filter));
        switchAlternateNumber.setOn(Boolean.valueOf(result.getAlternateMobileToggle()));
        pendingCount.setText(result.getPendingDocsCount());
        //  switchAlternateNumber.setOn(true);
        remarksEze.setText(result.getExeRemarks());
        List<String> dates = Arrays.asList(result.getContactFullAddress().split(","));

        alternareState = Boolean.valueOf(result.getAlternateMobileToggle());
        booleans = new boolean[result.getDocs().length];
        addBooleans = new boolean[result.getAddDocs().length];
        dates1 = new String[result.getDocs().length];
        addDates = new String[result.getAddDocs().length];
        fullName.setText(upperStringName);
        financier.setText(result.getFinancier() != null ? result.getFinancier() : "---");

        if (!filter) {
            displayAddress.setText(dates.get(0) + "," + dates.get(1));
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
            if (!values.toString().isEmpty()) {
                displayAddress.setText(values.toString().substring(0, values.toString().length() - 1));
                pendingCount.setText(counts + "");
            } else {
                pendingCount.setText(result.getPendingDocsCount());
//                holdder.displayAddress.setText("No documents collected yet");
            }
        }
        if (result.getDocs() != null) {
            for (int i = 0; i < result.getDocs().length; i++) {
                UploadDatas uploadDatas = new UploadDatas();
                uploadDatas.setChecked(Boolean.valueOf(result.getDocs()[i].getChecked()));
                uploadDatas.setCollected(Boolean.valueOf(result.getDocs()[i].getCollected()));
                uploadDatas.setChekedDate(result.getDocs()[i].getCheckedDate());
                uploadDatas.setDate(result.getDocs()[i].getDate() == null ? "" : result.getDocs()[i].getDate());
                uploadDatas.setProof(result.getDocs()[i].getProof());
                dates1[i] = result.getDocs()[i].getDate() == null ? "" : result.getDocs()[i].getDate();
                booleans[i] = Boolean.valueOf(result.getDocs()[i].getCollected());
                uploadData.add(uploadDatas);
            }
        }
        if (result.getAddDocs() != null) {
            for (int i = 0; i < result.getAddDocs().length; i++) {
                UploadDatas uploadDatas = new UploadDatas();
                uploadDatas.setChecked(Boolean.valueOf(result.getAddDocs()[i].getChecked()));
                uploadDatas.setCollected(Boolean.valueOf(result.getAddDocs()[i].getCollected()));
                uploadDatas.setChekedDate(result.getDocs()[i].getCheckedDate());
                uploadDatas.setDate(result.getAddDocs()[i].getDate() == null ? "" : result.getAddDocs()[i].getDate());
                uploadDatas.setProof(result.getAddDocs()[i].getProof());
                addDates[i] = result.getAddDocs()[i].getDate() == null ? "" : result.getAddDocs()[i].getDate();
                addBooleans[i] = Boolean.valueOf(result.getAddDocs()[i].getCollected());
                addData.add(uploadDatas);
            }
        }
        switchAlternateNumber.setOnToggledListener((toggleableView, isOn) -> {
            if (alternamtePhone.getText().length() >= 10) {
                alternareState = isOn;

            } else {
                toggleableView.setOn(true);
                alternareState = false;

            }
        });
        if (result.getAlarm() != null || Boolean.valueOf(result.getAlarm()))
            fixedAppoinments.setVisibility(View.VISIBLE);
        else
            fixedAppoinments.setVisibility(View.GONE);

    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_details_view;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void success(int pos) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
    private boolean checkCallPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void getCallPermission(MainActivity.RequestPermissionAction onPermissionCallBack) {
        this.onPermissionCallBack = onPermissionCallBack;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkCallPermission()) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
                return;
            }
        }
        if (onPermissionCallBack != null)
            onPermissionCallBack.permissionGranted();
    }

    private boolean checkReadSMSPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void getReadSMSPermission(MainActivity.RequestPermissionAction onPermissionCallBack) {
        this.onPermissionCallBack = onPermissionCallBack;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkReadSMSPermission()) {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, REQUEST_READ_SMS_PERMISSION);
                return;
            }
        }
        if (onPermissionCallBack != null)
            onPermissionCallBack.permissionGranted();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (REQUEST_READ_SMS_PERMISSION == requestCode) {
                // TODO Request Granted for READ_SMS.
                System.out.println("REQUEST_READ_SMS_PERMISSION Permission Granted");
            }else if (REQUEST_CALL_PERMISSION == requestCode){
                System.out.println("REQUEST_READ_SMS_PERMISSION Permission Granted");
            }
            if (onPermissionCallBack != null)
                onPermissionCallBack.permissionGranted();

        } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            if (REQUEST_READ_SMS_PERMISSION == requestCode) {

            }else if (REQUEST_CALL_PERMISSION == requestCode){

            }
            if (onPermissionCallBack != null)
                onPermissionCallBack.permissionDenied();
        }
    }


}
