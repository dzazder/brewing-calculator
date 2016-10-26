package dev.lampart.bartosz.brewingcalculator.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.calculators.CarbonationCalc;
import dev.lampart.bartosz.brewingcalculator.dicts.SugarType;
import dev.lampart.bartosz.brewingcalculator.dicts.TemperatureUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;
import dev.lampart.bartosz.brewingcalculator.global.AppConfiguration;
import dev.lampart.bartosz.brewingcalculator.helpers.NumberFormatter;
import dev.lampart.bartosz.brewingcalculator.helpers.Tuple;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCarbonation extends Fragment implements TextWatcher, AdapterView.OnItemSelectedListener {

    private EditText txtPrimingSize;
    private EditText txtCO2;
    private EditText txtBeerTemp;

    private Spinner spPrimingSize;
    private Spinner spBeerTemp;

    private TextView txtAmountTableSugar;
    private TextView txtAmountCornSugar;
    private TextView txtAmountDME;

    public FragmentCarbonation() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_carbonation, container, false);
        getActivity().setTitle(getResources().getString(R.string.title_carbonation_calculator));

        initControls(rootView);

        return rootView;
    }

    private void initControls(View rootView) {
        getControlsFromView(rootView);
        setControlValues();
        initListeners();
    }

    private void initListeners() {
        txtPrimingSize.addTextChangedListener(this);
        txtCO2.addTextChangedListener(this);
        txtBeerTemp.addTextChangedListener(this);

        spPrimingSize.setOnItemSelectedListener(this);
        spBeerTemp.setOnItemSelectedListener(this);
    }

    private void setControlValues() {
        txtPrimingSize.setText(Double.toString(AppConfiguration.getInstance().defaultSettings.getDefPrimingSize()));
        txtBeerTemp.setText(Double.toString(AppConfiguration.getInstance().defaultSettings.getDefTemperature()));

        initPrimingSizeSpinner();
        initBeerTemperatureSpinner();
    }

    private void initBeerTemperatureSpinner() {
        ArrayAdapter<CharSequence> tempScale = ArrayAdapter.createFromResource(getActivity(), R.array.temp_scale,
                android.R.layout.simple_spinner_item);
        TemperatureUnit tempDefUnit = TemperatureUnit.valueOf(AppConfiguration.getInstance().defaultSettings.getDefTempUnit().toString());
        String valSelectedInTempUnit = "";
        switch (tempDefUnit) {
            case C: valSelectedInTempUnit = getActivity().getResources().getString(R.string.temp_unit_C); break;
            case F: valSelectedInTempUnit = getActivity().getResources().getString(R.string.temp_unit_F); break;
        }
        int spinnerTempPosition = tempScale.getPosition(valSelectedInTempUnit);
        spBeerTemp.setSelection(spinnerTempPosition);
    }

    private void initPrimingSizeSpinner() {
        ArrayAdapter<CharSequence> primingSizeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.volume_units,
                android.R.layout.simple_spinner_item);
        VolumeUnit selDefVolUnit = VolumeUnit.valueOf(AppConfiguration.getInstance().defaultSettings.getDefVolumeUnit().toString());
        String valSelectedInSpinnerVol = "";
        switch (selDefVolUnit) {
            case Liter: valSelectedInSpinnerVol = getActivity().getResources().getString(R.string.volume_unit_liters); break;
            case Gallon: valSelectedInSpinnerVol = getActivity().getResources().getString(R.string.volume_unit_gallons); break;
        }
        int spinnerPrimingUnigPosition = primingSizeAdapter.getPosition(valSelectedInSpinnerVol);
        spPrimingSize.setSelection(spinnerPrimingUnigPosition);
    }


    private void getControlsFromView(View rootView) {
        txtPrimingSize = (EditText)rootView.findViewById(R.id.txt_priming_size);
        txtCO2 = (EditText)rootView.findViewById(R.id.txt_co2_volumes);
        txtBeerTemp = (EditText)rootView.findViewById(R.id.txt_beer_temp);

        spPrimingSize = (Spinner)rootView.findViewById(R.id.sp_priming_size);
        spBeerTemp = (Spinner)rootView.findViewById(R.id.sp_temp_scale);

        txtAmountTableSugar = (TextView)rootView.findViewById(R.id.txt_priming_sugar_amount_value_table_sugar);
        txtAmountCornSugar = (TextView)rootView.findViewById(R.id.txt_priming_sugar_amount_value_corn_sugar);
        txtAmountDME = (TextView)rootView.findViewById(R.id.txt_priming_sugar_amount_value_dme);
    }

    private void calculateCarbonation(EditText txtPrimingSize, EditText txtCO2, EditText txtBeerTemp,
                                      Spinner spPrimingSize, Spinner spBeerTemp, TextView txtAmountTableSugar,
                                      TextView txtAmountCornSugar, TextView txtAmountDME) {
        if (NumberFormatter.isNumeric(txtPrimingSize.getText().toString()) &&
                NumberFormatter.isNumeric(txtCO2.getText().toString()) &&
                NumberFormatter.isNumeric(txtBeerTemp.getText().toString())) {

            double dPrimingSize = Double.parseDouble(txtPrimingSize.getText().toString());
            double dCO2 = Double.parseDouble(txtCO2.getText().toString());
            double dBeerTemp = Double.parseDouble(txtBeerTemp.getText().toString());
            String selectedVolUnit = spPrimingSize.getSelectedItem().toString();
            String selectedTempUnit = spBeerTemp.getSelectedItem().toString();

            TemperatureUnit tempUnit = TemperatureUnit.F;
            if (selectedTempUnit == getContext().getString(R.string.temp_unit_C)) {
                tempUnit = TemperatureUnit.C;
            }

            VolumeUnit volUnit = VolumeUnit.Gallon;
            if (selectedVolUnit == getContext().getString(R.string.volume_unit_liters)) {
                volUnit = VolumeUnit.Liter;
            }

            ArrayList<Tuple<SugarType, Double>> sugarAmount =
                    CarbonationCalc.calcSugarAmount(dPrimingSize, dCO2, dBeerTemp, volUnit, tempUnit);

            for (Tuple<SugarType, Double> el: sugarAmount) {
                switch (el.x) {
                    case TableSugar: setSugarAmountValue(txtAmountTableSugar, el.y); break;
                    case CornSugar: setSugarAmountValue(txtAmountCornSugar, el.y); break;
                    case DME: setSugarAmountValue(txtAmountDME, el.y); break;
                }
            }
        }
    }

    private void setSugarAmountValue(TextView txtToSet, double valToSet) {
        if (valToSet < 0) {
            txtToSet.setTextColor(getResources().getColor(R.color.colorError));
            txtToSet.setText(getResources().getText(R.string.incorrect_value));
        }
        else {
            txtToSet.setTextColor(getResources().getColor(R.color.colorAccent));
            txtToSet.setText(String.format(Locale.US, "%.2f g", valToSet));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        calculateCarbonation(txtPrimingSize, txtCO2, txtBeerTemp, spPrimingSize, spBeerTemp,
                txtAmountTableSugar, txtAmountCornSugar, txtAmountDME);
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
        calculateCarbonation(txtPrimingSize, txtCO2, txtBeerTemp, spPrimingSize, spBeerTemp,
                txtAmountTableSugar, txtAmountCornSugar, txtAmountDME);
    }
}
