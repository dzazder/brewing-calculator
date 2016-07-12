package dev.lampart.bartosz.brewingcalculator.fragments;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.calculators.AlcoholCalc;
import dev.lampart.bartosz.brewingcalculator.calculators.CarbonationCalculator;
import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.SugarType;
import dev.lampart.bartosz.brewingcalculator.dicts.TemperatureUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;
import dev.lampart.bartosz.brewingcalculator.helpers.NumberFormatter;
import dev.lampart.bartosz.brewingcalculator.helpers.Tuple;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCarbonation extends Fragment {


    public FragmentCarbonation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_carbonation, container, false);
        getActivity().setTitle(getResources().getString(R.string.title_carbonation_calculator));

        final EditText txtPrimingSize = (EditText)rootView.findViewById(R.id.txt_priming_size);
        final EditText txtCO2 = (EditText)rootView.findViewById(R.id.txt_co2_volumes);
        final EditText txtBeerTemp = (EditText)rootView.findViewById(R.id.txt_beer_temp);

        final Spinner spPrimingSize = (Spinner)rootView.findViewById(R.id.sp_priming_size);
        final Spinner spBeerTemp = (Spinner)rootView.findViewById(R.id.sp_temp_scale);

        final TextView txtAmountTableSugar = (TextView)rootView.findViewById(R.id.txt_priming_sugar_amount_value_table_sugar);
        final TextView txtAmountCornSugar = (TextView)rootView.findViewById(R.id.txt_priming_sugar_amount_value_corn_sugar);
        final TextView txtAmountDME = (TextView)rootView.findViewById(R.id.txt_priming_sugar_amount_value_dme);

        txtPrimingSize.addTextChangedListener(new TextWatcher() {
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
        });

        txtCO2.addTextChangedListener(new TextWatcher() {
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
        });

        txtBeerTemp.addTextChangedListener(new TextWatcher() {
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
        });

        spPrimingSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                calculateCarbonation(txtPrimingSize, txtCO2, txtBeerTemp, spPrimingSize, spBeerTemp,
                        txtAmountTableSugar, txtAmountCornSugar, txtAmountDME);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spBeerTemp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                calculateCarbonation(txtPrimingSize, txtCO2, txtBeerTemp, spPrimingSize, spBeerTemp,
                        txtAmountTableSugar, txtAmountCornSugar, txtAmountDME);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return rootView;
    }

    private void calculateCarbonation(EditText txtPrimingSize, EditText txtCO2, EditText txtBeerTemp, Spinner spPrimingSize, Spinner spBeerTemp, TextView txtAmountTableSugar, TextView txtAmountCornSugar, TextView txtAmountDME) {
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
                    CarbonationCalculator.calcSugarAmount(dPrimingSize, dCO2, dBeerTemp, volUnit, tempUnit);

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

}
