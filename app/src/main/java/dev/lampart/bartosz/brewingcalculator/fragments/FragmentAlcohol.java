package dev.lampart.bartosz.brewingcalculator.fragments;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.calculators.AlcoholCalc;
import dev.lampart.bartosz.brewingcalculator.calculators.ExtractCalc;
import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.global.AppConfiguration;
import dev.lampart.bartosz.brewingcalculator.helpers.NumberFormatter;
import dev.lampart.bartosz.brewingcalculator.helpers.Tuple;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAlcohol extends Fragment {

    private boolean editedByProgram = false;

    public FragmentAlcohol() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_alcohol, container, false);

        getActivity().setTitle(getResources().getString(R.string.title_alcohol_calculator));

        final Spinner spBefore = (Spinner)rootView.findViewById(R.id.sp_extract_before);
        final Spinner spAfter = (Spinner)rootView.findViewById(R.id.sp_extract_after);

        final EditText txtExtBefore = (EditText)rootView.findViewById(R.id.txt_extract_before);
        final EditText txtExtAfter = (EditText)rootView.findViewById(R.id.txt_extract_after);

        final TextView lblAlco = (TextView)rootView.findViewById(R.id.txt_calc_alc);
        final TextView lblAtt = (TextView)rootView.findViewById(R.id.txt_calc_att);

        final CheckBox chbUseRefractometer = (CheckBox)rootView.findViewById(R.id.chb_use_refractometer);
        chbUseRefractometer.setChecked(AppConfiguration.getInstance().defaultSettings.isDefUseRefractometer());

        final EditText txtWortCorrectionFactor = (EditText)rootView.findViewById(R.id.txt_calc_wort_correction_factor);
        txtWortCorrectionFactor.setText(Double.toString(AppConfiguration.getInstance().defaultSettings.getDefWortCorrectionFactor()));

        final LinearLayout layWortCorrectionFactor = (LinearLayout)rootView.findViewById(R.id.layout_wort_correction_factor);
        layWortCorrectionFactor.setVisibility(AppConfiguration.getInstance().defaultSettings.isDefUseRefractometer() ? View.VISIBLE : View.GONE);

        spAfter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("SPINNER", "change value");
                if (NumberFormatter.isNumeric(txtExtAfter.getText().toString()) &&
                        NumberFormatter.isNumeric(txtExtBefore.getText().toString()) &&
                        NumberFormatter.isNumeric(txtWortCorrectionFactor.getText().toString())) {

                    Log.d("SPINNER", "isnumeric true");
                    double dBefore = Double.parseDouble(txtExtBefore.getText().toString());
                    double dAfter = Double.parseDouble(txtExtAfter.getText().toString());
                    double dWortFactor = Double.parseDouble(txtWortCorrectionFactor.getText().toString());

                    Tuple<Double, Double> alcatt = AlcoholCalc.CalculateAlcohol(dBefore, dAfter, ExtractUnit.valueOf(spBefore.getSelectedItem().toString()),
                            ExtractUnit.valueOf(spAfter.getSelectedItem().toString()), chbUseRefractometer.isChecked(), dWortFactor);

                    setValues(alcatt.x, alcatt.y, lblAlco, lblAtt);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spBefore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (NumberFormatter.isNumeric(txtExtAfter.getText().toString()) &&
                        NumberFormatter.isNumeric(txtExtBefore.getText().toString()) &&
                        NumberFormatter.isNumeric(txtWortCorrectionFactor.getText().toString())) {

                    double dBefore = Double.parseDouble(txtExtBefore.getText().toString());
                    double dAfter = Double.parseDouble(txtExtAfter.getText().toString());
                    double dWortFactor = Double.parseDouble(txtWortCorrectionFactor.getText().toString());

                    Tuple<Double, Double> alcatt = AlcoholCalc.CalculateAlcohol(dBefore, dAfter, ExtractUnit.valueOf(spBefore.getSelectedItem().toString()),
                            ExtractUnit.valueOf(spAfter.getSelectedItem().toString()), chbUseRefractometer.isChecked(), dWortFactor);

                    setValues(alcatt.x, alcatt.y, lblAlco, lblAtt);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        txtExtBefore.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!editedByProgram && NumberFormatter.isNumeric(s.toString())
                        && NumberFormatter.isNumeric(txtExtAfter.getText().toString()) &&
                        NumberFormatter.isNumeric(txtWortCorrectionFactor.getText().toString())) {
                    editedByProgram = true;

                    double dBefore = Double.parseDouble(s.toString());
                    double dAfter = Double.parseDouble(txtExtAfter.getText().toString());
                    double dWortFactor = Double.parseDouble(txtWortCorrectionFactor.getText().toString());

                    Tuple<Double, Double> alcatt = AlcoholCalc.CalculateAlcohol(dBefore, dAfter, ExtractUnit.valueOf(spBefore.getSelectedItem().toString()),
                            ExtractUnit.valueOf(spAfter.getSelectedItem().toString()), chbUseRefractometer.isChecked(), dWortFactor);

                    setValues(alcatt.x, alcatt.y, lblAlco, lblAtt);

                    editedByProgram = false;
                }
            }
        });

        txtWortCorrectionFactor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!editedByProgram && NumberFormatter.isNumeric(s.toString())
                        && NumberFormatter.isNumeric(txtExtAfter.getText().toString())
                        && NumberFormatter.isNumeric(txtExtBefore.getText().toString())) {
                    editedByProgram = true;

                    double dBefore = Double.parseDouble(txtExtBefore.getText().toString());
                    double dAfter = Double.parseDouble(txtExtAfter.getText().toString());
                    double dWortFactor = Double.parseDouble(s.toString());

                    Tuple<Double, Double> alcatt = AlcoholCalc.CalculateAlcohol(dBefore, dAfter, ExtractUnit.valueOf(spBefore.getSelectedItem().toString()),
                            ExtractUnit.valueOf(spAfter.getSelectedItem().toString()), chbUseRefractometer.isChecked(), dWortFactor);

                    setValues(alcatt.x, alcatt.y, lblAlco, lblAtt);

                    editedByProgram = false;
                }
            }
        });

        txtExtAfter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!editedByProgram && NumberFormatter.isNumeric(s.toString())
                        && NumberFormatter.isNumeric(txtExtBefore.getText().toString()) &&
                        NumberFormatter.isNumeric(txtWortCorrectionFactor.getText().toString())) {
                    editedByProgram = true;

                    double dAfter = Double.parseDouble(s.toString());
                    double dBefore = Double.parseDouble(txtExtBefore.getText().toString());
                    double dWortFactor = Double.parseDouble(txtWortCorrectionFactor.getText().toString());

                    Tuple<Double, Double> alcatt = AlcoholCalc.CalculateAlcohol(dBefore, dAfter, ExtractUnit.valueOf(spBefore.getSelectedItem().toString()),
                            ExtractUnit.valueOf(spAfter.getSelectedItem().toString()), chbUseRefractometer.isChecked(), dWortFactor);

                    setValues(alcatt.x, alcatt.y, lblAlco, lblAtt);

                    editedByProgram = false;
                }
            }
        });

        chbUseRefractometer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                layWortCorrectionFactor.setVisibility(b ? View.VISIBLE : View.GONE);

                if (NumberFormatter.isNumeric(txtExtAfter.getText().toString()) &&
                        NumberFormatter.isNumeric(txtExtBefore.getText().toString()) &&
                        NumberFormatter.isNumeric(txtWortCorrectionFactor.getText().toString())) {

                    double dBefore = Double.parseDouble(txtExtBefore.getText().toString());
                    double dAfter = Double.parseDouble(txtExtAfter.getText().toString());
                    double dWortFactor = Double.parseDouble(txtWortCorrectionFactor.getText().toString());

                    Tuple<Double, Double> alcatt = AlcoholCalc.CalculateAlcohol(dBefore, dAfter, ExtractUnit.valueOf(spBefore.getSelectedItem().toString()),
                            ExtractUnit.valueOf(spAfter.getSelectedItem().toString()), chbUseRefractometer.isChecked(), dWortFactor);

                    setValues(alcatt.x, alcatt.y, lblAlco, lblAtt);
                }
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.extract_units,
                android.R.layout.simple_spinner_item);

        int spinnerPosition = adapter.getPosition(AppConfiguration.getInstance().defaultSettings.getDefExtractUnit().toString());

        spAfter.setSelection(spinnerPosition);
        spBefore.setSelection(spinnerPosition);

        return rootView;
    }



    private void setValues(double alco, double att, TextView txtAlco, TextView txtAtt) {
        if (alco <0 || alco > 100) {
            txtAlco.setTextColor(getResources().getColor(R.color.colorError));
            txtAlco.setText(getResources().getText(R.string.incorrect_value));
        }
        else {
            txtAlco.setTextColor(getResources().getColor(R.color.colorAccent));
            txtAlco.setText(String.format(Locale.US, "%.2f %%", alco));
        }
        if (att < 0 || att > 100) {
            txtAtt.setTextColor(getResources().getColor(R.color.colorError));
            txtAtt.setText(getResources().getText(R.string.incorrect_value));
        }
        else {
            txtAtt.setTextColor(getResources().getColor(R.color.colorAccent));
            txtAtt.setText(String.format(Locale.US, "%.2f %%", att));
        }
    }

}
