package cbots.b_to_c.CA;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cbots.b_to_c.BaseActivity;
import cbots.b_to_c.R;

public class CreateCustomerActivity extends BaseActivity {


    @BindView(R.id.mrOrMs)
    Spinner mrOrMs;
    @BindView(R.id.cusNameInput)
    TextInputEditText cusNameInput;
    @BindView(R.id.cusPanInput)
    TextInputEditText cusPanInput;
    @BindView(R.id.cusvechileCategory)
    TextInputEditText cusvechileCategory;
    @BindView(R.id.cusVechleModel)
    TextInputEditText cusVechleModel;
    @BindView(R.id.cusExchangeVechile)
    TextInputEditText cusExchangeVechile;
    @BindView(R.id.cusVechileColor)
    TextInputEditText cusVechileColor;
    @BindView(R.id.createCus)
    CardView createCus;

    @Override
    protected int layoutRes() {
        return R.layout.activity_create_customer;
    }

    @Override
    protected void onViewBound() {
        setSpinner();
    }

    void setSpinner() {
        ArrayAdapter<String> aa = new ArrayAdapter<>(
                getApplicationContext(), android.R.layout.simple_spinner_item, new String[]{"Mr", "Ms", "Others"});
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mrOrMs.setAdapter(aa);

    }

    boolean panNumberCheck(String panNumber) {
        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
        Matcher matcher = pattern.matcher(panNumber);
        return matcher.matches();

    }


    @OnClick(R.id.createCus)
    public void onViewClicked() {
        if (panNumberCheck(Objects.requireNonNull(cusPanInput.getText()).toString())) {
            showToastMessage("Is matching");
        }else {
            showToastMessage("Not matching");
        }
    }
}
