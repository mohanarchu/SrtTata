package cbots.b_to_c.CA;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cbots.b_to_c.BaseActivity;
import cbots.b_to_c.CA.Models.CaPresenter;
import cbots.b_to_c.CA.Models.CarModels;
import cbots.b_to_c.R;

public class CreateCustomerActivity extends BaseActivity implements CaPresenter.CaView {


    @BindView(R.id.mrOrMs)
    Spinner mrOrMs;
    @BindView(R.id.cusNameInput)
    TextInputEditText cusNameInput;
    @BindView(R.id.cusPanInput)
    TextInputEditText cusPanInput;
    @BindView(R.id.cusvechileCategory)
    TextView cusvechileCategory;
    @BindView(R.id.cusVechleModel)
    TextView cusVechleModel;
    @BindView(R.id.cusVechileColor)
    TextInputEditText cusVechileColor;
    @BindView(R.id.createCus)
    CardView createCus;
    @BindView(R.id.vechileYear)
    Spinner vehicleYear;
    CaPresenter caPresenter;
    JsonObject jsonObject;
    int selected = 0;
    @BindView(R.id.yes)
    RadioButton yes;
    @BindView(R.id.no)
    RadioButton no;
    @BindView(R.id.radioGroupVehicle)
    RadioGroup radioGroupVehicle;
    @BindView(R.id.cusMobNumberInput)
    TextInputEditText cusMobNumberInput;
    @BindView(R.id.finSpinner)
    AppCompatSpinner finSpinner;
    private RadioButton radioButton;
    String radioButtonvalue = "No";

    @Override
    protected int layoutRes() {
        return R.layout.activity_create_customer;
    }

    @Override
    protected void onViewBound() {
        no.setChecked(true);
        caPresenter = new CaPresenter(getApplicationContext(), this);
        setSpinner();
        radioGroupVehicle.setOnCheckedChangeListener((radioGroup, i) -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            radioButton = findViewById(selectedId);
            radioButtonvalue = radioButton.getText().toString();
        });
    }

    void setSpinner() {
        ArrayAdapter<String> aa = new ArrayAdapter<>(
                getApplicationContext(), android.R.layout.simple_spinner_item, new String[]{"Mr", "Mrs" ,"Ms"});
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mrOrMs.setAdapter(aa);
        ArrayList<String> years = new ArrayList<>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= thisYear - 4; i--) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> year = new ArrayAdapter<>(
                getApplicationContext(), android.R.layout.simple_spinner_item, years);
        year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleYear.setAdapter(year);
        ArrayAdapter<String> finAdapter = new ArrayAdapter<>(
                getApplicationContext(), android.R.layout.simple_spinner_item, new String[]{"Cash", "Inhouse", "Outhouse"});
        finAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        finSpinner.setAdapter(finAdapter);
    }

    boolean panNumberCheck(String panNumber) {
        Pattern pattern = Pattern.compile(getString(R.string.pan_number_pattern));
        Matcher matcher = pattern.matcher(panNumber);
        return matcher.matches();
    }


    @OnClick({R.id.cusvechileCategory, R.id.cusVechleModel, R.id.createCus})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cusvechileCategory:
                selected = 0;
                jsonObject = new JsonObject();
                jsonObject.addProperty("model", "");
                caPresenter.getModels(jsonObject);
                break;
            case R.id.cusVechleModel:
                if (cusvechileCategory.getText().toString().isEmpty()) {
                    showMessage("Kindly choose vechile model");
                } else {
                    selected = 1;
                    jsonObject = new JsonObject();
                    jsonObject.addProperty("model", cusvechileCategory.getText().toString());
                    caPresenter.getModels(jsonObject);
                }
                break;
            case R.id.createCus:
                if (cusNameInput.getText().toString().isEmpty() || cusPanInput.getText().toString().isEmpty() ||
                        cusvechileCategory.getText().toString().isEmpty() && cusVechleModel.getText().toString().isEmpty() &&
                                cusVechileColor.getText().toString().isEmpty()) {
                    showMessage("Fill all basic details");
                } else {
                    if (!panNumberCheck(cusPanInput.getText().toString().trim())) {
                        showMessage("Enter valid PAN number");
                        return;
                    }
                    jsonObject = new JsonObject();
                    jsonObject.addProperty("cusName", mrOrMs.getSelectedItem().toString() + "." + cusNameInput.getText().toString());
                    jsonObject.addProperty("vehicleModel", cusvechileCategory.getText().toString());
                    jsonObject.addProperty("cusPan", cusPanInput.getText().toString());
                    jsonObject.addProperty("vehicleVariant", cusVechleModel.getText().toString());
                    jsonObject.addProperty("exchangeVehicle", radioButtonvalue);
                    jsonObject.addProperty("vehicleColour", Objects.requireNonNull(cusVechileColor.getText()).toString());
                    jsonObject.addProperty("vehicleYear", Objects.requireNonNull(vehicleYear.getSelectedItem()).toString());
                    jsonObject.addProperty("cusMobile", Objects.requireNonNull(cusMobNumberInput.getText()).toString());
                    jsonObject.addProperty("financeOption", Objects.requireNonNull(finSpinner.getSelectedItem()).toString());
                    caPresenter.createCustomer(jsonObject);
                }
                break;
        }
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
        showToastMessage(message);
    }

    @Override
    public void showCarModels(CarModels.Value[] varients) {
        ChooseFragment chooseFragment = new ChooseFragment();
        ArrayList<String> arrayList = new ArrayList<>();
        if (selected == 0) {
            for (CarModels.Value varient : varients) {
                arrayList.add(varient.getModel());
            }
        } else if (selected == 1) {
            for (int i = 0; i < varients[0].getVarients().length; i++) {
                arrayList.add(varients[0].getVarients()[i].getName());
            }
        }
        chooseFragment.setItems(arrayList);
        chooseFragment.setInterface(model -> {
            if (selected == 0) {
                cusvechileCategory.setText(model);
                cusVechleModel.setText("");
            } else if (selected == 1) {
                cusVechleModel.setText(model);
            }

        });
        chooseFragment.show(getSupportFragmentManager(), "Chooser Fragment");
    }

    @Override
    public void showCreates() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
