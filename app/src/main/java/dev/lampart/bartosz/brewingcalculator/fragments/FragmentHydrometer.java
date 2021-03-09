package dev.lampart.bartosz.brewingcalculator.fragments;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Locale;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.calculators.HydrometerCalc;
import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.TemperatureUnit;
import dev.lampart.bartosz.brewingcalculator.helpers.NumberFormatter;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHydrometer extends Fragment implements TextWatcher,
        AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {

    private EditText txtGravity;
    private EditText txtTemperature;
    private Spinner spGravity;
    private Spinner spRealGravity;
    private RadioGroup rgTemperature;
    private TextView lblRealGravity;

    public FragmentHydrometer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View rootView = inflater.inflate(R.layout.fragment_hydrometer, container, false);
        getActivity().setTitle(getResources().getString(R.string.title_hydrometer_adjustment));

        initControls(rootView);

        return rootView;
    }


    private void initControls(View rootView) {
        getControlsFromView(rootView);
        initListeners();
    }

    private void initListeners() {
        txtGravity.addTextChangedListener(this);
        txtTemperature.addTextChangedListener(this);
        spGravity.setOnItemSelectedListener(this);
        spRealGravity.setOnItemSelectedListener(this);
        rgTemperature.setOnCheckedChangeListener(this);
    }

    private void getControlsFromView(View rootView) {
        txtGravity = rootView.findViewById(R.id.txt_hydrometer_measured_gravity);
        txtTemperature = rootView.findViewById(R.id.txt_adjustment_temperature);
        spGravity = rootView.findViewById(R.id.sp_hydrometer_gravity_unit);
        spRealGravity = rootView.findViewById(R.id.sp_hydrometer_real_gravity_unit);
        rgTemperature = rootView.findViewById(R.id.temp_unit_selector);
        lblRealGravity = rootView.findViewById(R.id.lbl_hydrometer_real_gravity);
    }

    private void calculateGravity(EditText txtGravity, EditText txtTemperature, Spinner spGravity,
                                  RadioGroup rgTemperature, TextView lblRealGravity, Spinner spRealGravity) {
        if (NumberFormatter.isNumeric(txtGravity.getText().toString()) &&
                NumberFormatter.isNumeric(txtTemperature.getText().toString())) {
            double gravity = Double.parseDouble(txtGravity.getText().toString());
            double temperature = Double.parseDouble(txtTemperature.getText().toString());

            ExtractUnit grUnit = ExtractUnit.valueOf(spGravity.getSelectedItem().toString());

            TemperatureUnit tempUnit = TemperatureUnit.C;
            if (rgTemperature.getCheckedRadioButtonId() == R.id.temp_selector_f) {
                tempUnit = TemperatureUnit.F;
            }

            ExtractUnit resultUnit = ExtractUnit.valueOf(spRealGravity.getSelectedItem().toString());

            double realTemp = HydrometerCalc.calcAdjustmentGravity(gravity, temperature, grUnit,
                    tempUnit, resultUnit);

            setResultValue(realTemp);
        }
    }

    private void setResultValue(double realGravity) {
        if (realGravity < 0) {
            lblRealGravity.setTextColor(ContextCompat.getColor(getContext(), R.color.colorError));
            lblRealGravity.setText(getResources().getText(R.string.incorrect_value));
        }
        else {
            lblRealGravity.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            lblRealGravity.setText(String.format(Locale.US,
                    ExtractUnit.valueOf(spRealGravity.getSelectedItem().toString()) == ExtractUnit.SG ?
                            "%.3f" : "%.2f", realGravity));
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        calculateGravity(txtGravity, txtTemperature, spGravity, rgTemperature, lblRealGravity, spRealGravity);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        calculateGravity(txtGravity, txtTemperature, spGravity, rgTemperature, lblRealGravity, spRealGravity);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        calculateGravity(txtGravity, txtTemperature, spGravity, rgTemperature, lblRealGravity, spRealGravity);
    }
}
