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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.calculators.UnitCalc;
import dev.lampart.bartosz.brewingcalculator.calculators.WaterCorrectionCalc;
import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;
import dev.lampart.bartosz.brewingcalculator.global.AppConfiguration;
import dev.lampart.bartosz.brewingcalculator.helpers.NumberFormatter;

/**
 * Created by bartek on 29.04.2017.
 */
public class FragmentWaterCorrection extends Fragment implements TextWatcher, AdapterView.OnItemSelectedListener{

    private EditText txtPrimingSize;
    private Spinner spPrimingSize;
    private EditText txtGravity;
    private Spinner spGravityUnit;
    private EditText txtExpGravity;
    private Spinner spExpGravityUnit;
    private TextView txtAdditionalWater;

    private AdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_water_correction, container, false);

        mAdView = (AdView) view.findViewById(R.id.adViewFragmentWaterCorrection);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("8488AE5DD406CB17CA7F26FED807020C").build();
        mAdView.loadAd(adRequest);

        getActivity().setTitle(getResources().getString(R.string.title_water_correction));
        initControls(view);

        return view;
    }

    private void initControls(View view) {
        getControlsFromView(view);
        initPrimingSizeSpinner();
        initGravityUnitSpinner();
        initListeners();
    }

    private void initListeners() {
        txtGravity.addTextChangedListener(this);
        txtPrimingSize.addTextChangedListener(this);
        txtExpGravity.addTextChangedListener(this);
        spGravityUnit.setOnItemSelectedListener(this);
        spPrimingSize.setOnItemSelectedListener(this);
        spExpGravityUnit.setOnItemSelectedListener(this);
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

    private void initGravityUnitSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.extract_units,
                android.R.layout.simple_spinner_item);
        int spinnerPosition = adapter.getPosition(AppConfiguration.getInstance().defaultSettings.getDefExtractUnit().toString());
        spGravityUnit.setSelection(spinnerPosition);
        spExpGravityUnit.setSelection(spinnerPosition);
    }

    private void getControlsFromView(View rootView) {
        txtPrimingSize = (EditText)rootView.findViewById(R.id.txt_wc_priming_size);
        spPrimingSize = (Spinner)rootView.findViewById(R.id.sp_wc_priming_size);
        txtGravity = (EditText) rootView.findViewById(R.id.txt_wc_extract_after);
        spGravityUnit = (Spinner) rootView.findViewById(R.id.sp_wc_extract_after);
        txtExpGravity = (EditText) rootView.findViewById(R.id.txt_wc_expected_gravity);
        spExpGravityUnit = (Spinner) rootView.findViewById(R.id.sp_wc_expected_gravity_unit);
        txtAdditionalWater = (TextView) rootView.findViewById(R.id.txt_additional_water);
    }

    private void calculateWater() {
        if (NumberFormatter.isNumeric(txtPrimingSize.getText().toString()) &&
                NumberFormatter.isNumeric(txtGravity.getText().toString()) &&
                NumberFormatter.isNumeric(txtExpGravity.getText().toString())) {

            double batchSize = Double.parseDouble(txtPrimingSize.getText().toString());
            double gravity = Double.parseDouble(txtGravity.getText().toString());
            double expGravity = Double.parseDouble(txtExpGravity.getText().toString());
            String selectedVolUnit = spPrimingSize.getSelectedItem().toString();
            ExtractUnit gravityUnit = ExtractUnit.valueOf(spGravityUnit.getSelectedItem().toString());
            ExtractUnit expGravityUnit = ExtractUnit.valueOf(spExpGravityUnit.getSelectedItem().toString());

            VolumeUnit volUnit = VolumeUnit.Gallon;
            if (selectedVolUnit == getContext().getString(R.string.volume_unit_liters)) {
                volUnit = VolumeUnit.Liter;
            }

            double result = WaterCorrectionCalc.calcAdditionalWater(batchSize, volUnit,
                    gravity, gravityUnit, expGravity, expGravityUnit);

            double gallResult = UnitCalc.calcLitresToGallons(result);

            txtAdditionalWater.setText(String.format("%.2f %s / %.2f %s",
                    result,
                    getResources().getText(R.string.volume_unit_liters),
                    gallResult,
                    getResources().getText(R.string.volume_unit_gallons)));
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        calculateWater();
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
        calculateWater();
    }
}
