package dev.lampart.bartosz.brewingcalculator.fragments;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.calculators.AlcoholCalc;
import dev.lampart.bartosz.brewingcalculator.calculators.ExtractCalc;
import dev.lampart.bartosz.brewingcalculator.calculators.UnitCalc;
import dev.lampart.bartosz.brewingcalculator.dicts.AlcFormula;
import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.global.AppConfiguration;
import dev.lampart.bartosz.brewingcalculator.helpers.NumberFormatter;
import dev.lampart.bartosz.brewingcalculator.helpers.Triple;
import dev.lampart.bartosz.brewingcalculator.helpers.Tuple;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAlcohol extends Fragment implements AdapterView.OnItemSelectedListener,
        TextWatcher, CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    private TextView lblAlco;
    private TextView lblAtt;
    private TextView lblFG;
    private Spinner spBefore;
    private Spinner spAfter;
    private EditText txtExtBefore;
    private EditText txtExtAfter;
    private EditText txtWortCorrectionFactor;
    private LinearLayout layWortCorrectionFactor;
    private LinearLayout layFormulaSelector;
    private CheckBox chbUseRefractometer;
    private RadioGroup rgFormula;


    public FragmentAlcohol() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_alcohol, container, false);

        getActivity().setTitle(getResources().getString(R.string.title_alcohol_calculator));

        initControls(rootView);

        return rootView;
    }

    private void initControls(View rootView) {
        getControlsFromView(rootView);
        setControlValues();
        initListeners();
    }

    private void getControlsFromView(View rootView) {
        spBefore = (Spinner)rootView.findViewById(R.id.sp_extract_before);
        spAfter = (Spinner)rootView.findViewById(R.id.sp_extract_after);
        txtExtBefore = (EditText)rootView.findViewById(R.id.txt_extract_before);
        txtExtAfter = (EditText)rootView.findViewById(R.id.txt_extract_after);
        txtWortCorrectionFactor = (EditText)rootView.findViewById(R.id.txt_calc_wort_correction_factor);
        lblAlco = (TextView)rootView.findViewById(R.id.txt_calc_alc);
        lblAtt = (TextView)rootView.findViewById(R.id.txt_calc_att);
        lblFG = (TextView)rootView.findViewById(R.id.lbl_refractometer_final_gravity);
        chbUseRefractometer = (CheckBox)rootView.findViewById(R.id.chb_use_refractometer);
        layWortCorrectionFactor = (LinearLayout)rootView.findViewById(R.id.layout_wort_correction_factor);
        layFormulaSelector = (LinearLayout)rootView.findViewById(R.id.layout_alcohol_formula);
        rgFormula = (RadioGroup)rootView.findViewById(R.id.alc_formula);
    }

    private void setControlValues() {
        chbUseRefractometer.setChecked(AppConfiguration.getInstance().defaultSettings.isDefUseRefractometer());
        txtWortCorrectionFactor.setText(Double.toString(AppConfiguration.getInstance().defaultSettings.getDefWortCorrectionFactor()));
        layWortCorrectionFactor.setVisibility(AppConfiguration.getInstance().defaultSettings.isDefUseRefractometer() ? View.VISIBLE : View.GONE);
        layFormulaSelector.setVisibility(AppConfiguration.getInstance().defaultSettings.isDefUseRefractometer() ? View.GONE : View.VISIBLE);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.extract_units,
                android.R.layout.simple_spinner_item);
        int spinnerPosition = adapter.getPosition(AppConfiguration.getInstance().defaultSettings.getDefExtractUnit().toString());
        spAfter.setSelection(spinnerPosition);
        spBefore.setSelection(spinnerPosition);
    }

    private void initListeners() {
        txtExtAfter.addTextChangedListener(this);
        txtExtBefore.addTextChangedListener(this);
        txtWortCorrectionFactor.addTextChangedListener(this);

        spAfter.setOnItemSelectedListener(this);
        spBefore.setOnItemSelectedListener(this);

        chbUseRefractometer.setOnCheckedChangeListener(this);
        rgFormula.setOnCheckedChangeListener(this);
    }

    private void calculateAlcohol(EditText txtExtBefore, EditText txtExtAfter, 
                                                   Spinner spBefore, Spinner spAfter, 
                                                   CheckBox chbRefractometer, EditText txtWortFactor,
                                                   RadioGroup rgFormula) {
        if (NumberFormatter.isNumeric(txtExtAfter.getText().toString()) &&
                NumberFormatter.isNumeric(txtExtBefore.getText().toString()) &&
                NumberFormatter.isNumeric(txtWortFactor.getText().toString())) {

            double dBefore = Double.parseDouble(txtExtBefore.getText().toString());
            double dAfter = Double.parseDouble(txtExtAfter.getText().toString());
            double dWortFactor = Double.parseDouble(txtWortFactor.getText().toString());
            ExtractUnit extBefore = ExtractUnit.valueOf(spBefore.getSelectedItem().toString());
            ExtractUnit extAfter = ExtractUnit.valueOf(spAfter.getSelectedItem().toString());
            boolean useRefractometer = chbRefractometer.isChecked();
            AlcFormula alcFormula = AlcFormula.Standard;
            if (rgFormula.getCheckedRadioButtonId() == R.id.alc_formula_option_alternative) {
                alcFormula = AlcFormula.Alternative;
            }

            Triple<Double, Double, Double> alcatt =  AlcoholCalc.CalculateAlcohol(dBefore, dAfter, extBefore,
                    extAfter, useRefractometer, dWortFactor, alcFormula);

            double fg = alcatt.z;

            if (extAfter == ExtractUnit.Brix) {
                fg = ExtractCalc.calcSGToBrix(alcatt.z);
            }
            if (extAfter == ExtractUnit.Plato) {
                fg = ExtractCalc.calcSGToPlato(alcatt.z);
            }

            setValues(alcatt.x, alcatt.y, fg);
        }
    }

    private void setValues(double alco, double att, double fg) {
        if (alco <0 || alco > 100) {
            
            lblAlco.setTextColor(getResources().getColor(R.color.colorError));
            lblAlco.setText(getResources().getText(R.string.incorrect_value));
        }
        else {
            lblAlco.setTextColor(getResources().getColor(R.color.colorAccent));
            lblAlco.setText(String.format(Locale.US, "%.2f ", alco));
            lblFG.setText(String.format(Locale.US, "%.2f ", fg));
        }
        if (att < 0 || att > 100) {
            lblAtt.setTextColor(getResources().getColor(R.color.colorError));
            lblAtt.setText(getResources().getText(R.string.incorrect_value));
        }
        else {
            lblAtt.setTextColor(getResources().getColor(R.color.colorAccent));
            lblAtt.setText(String.format(Locale.US, "%.2f ", att));
            lblFG.setText(String.format(Locale.US, "%.2f ", fg));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        calculateAlcohol(txtExtBefore, txtExtAfter, spBefore, spAfter, chbUseRefractometer, txtWortCorrectionFactor, rgFormula);
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
        calculateAlcohol(txtExtBefore, txtExtAfter, spBefore, spAfter, chbUseRefractometer, txtWortCorrectionFactor, rgFormula);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        txtWortCorrectionFactor.setText(Double.toString(AppConfiguration.getInstance().defaultSettings.getDefWortCorrectionFactor()));
        layWortCorrectionFactor.setVisibility(b ? View.VISIBLE : View.GONE);
        layFormulaSelector.setVisibility(b ? View.GONE : View.VISIBLE);

        calculateAlcohol(txtExtBefore, txtExtAfter, spBefore, spAfter, chbUseRefractometer, txtWortCorrectionFactor, rgFormula);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        calculateAlcohol(txtExtBefore, txtExtAfter, spBefore, spAfter, chbUseRefractometer, txtWortCorrectionFactor, rgFormula);
    }
}
