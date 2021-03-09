package dev.lampart.bartosz.brewingcalculator.fragments;


import android.os.Bundle;
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

import javax.inject.Inject;

import androidx.fragment.app.Fragment;
import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.calculators.GravityCorrectionCalc;
import dev.lampart.bartosz.brewingcalculator.calculators.UnitCalc;
import dev.lampart.bartosz.brewingcalculator.calculators.WaterCorrectionCalc;
import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;
import dev.lampart.bartosz.brewingcalculator.entities.ExtractRaws;
import dev.lampart.bartosz.brewingcalculator.global.AppConfiguration;
import dev.lampart.bartosz.brewingcalculator.helpers.NumberFormatter;

/**
 * Created by bartek on 29.04.2017.
 */
public class FragmentGravityCorrection extends Fragment implements TextWatcher, AdapterView.OnItemSelectedListener{

    private EditText txtPrimingSize;
    private Spinner spPrimingSize;
    private EditText txtGravity;
    private Spinner spGravityUnit;
    private EditText txtExpGravity;
    private Spinner spExpGravityUnit;
    private TextView txtToAdd;

    private AdView mAdView;

    private final GravityCorrectionCalc gravityCorrectionCalcService;
    private final WaterCorrectionCalc waterCorrectionCalcService;
    private final UnitCalc unitCalcService;

    @Inject
    public FragmentGravityCorrection(GravityCorrectionCalc gravityCorrectionCalcService, WaterCorrectionCalc waterCorrectionCalcService, UnitCalc unitCalcService) {
        this.gravityCorrectionCalcService = gravityCorrectionCalcService;
        this.waterCorrectionCalcService = waterCorrectionCalcService;
        this.unitCalcService = unitCalcService;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gravity_correction, container, false);

        mAdView = view.findViewById(R.id.adViewFragmentWaterCorrection);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        getActivity().setTitle(getResources().getString(R.string.title_gravity_correction));
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
        txtPrimingSize = rootView.findViewById(R.id.txt_wc_priming_size);
        spPrimingSize = rootView.findViewById(R.id.sp_wc_priming_size);
        txtGravity = rootView.findViewById(R.id.txt_wc_extract_after);
        spGravityUnit = rootView.findViewById(R.id.sp_wc_extract_after);
        txtExpGravity = rootView.findViewById(R.id.txt_wc_expected_gravity);
        spExpGravityUnit = rootView.findViewById(R.id.sp_wc_expected_gravity_unit);
        txtToAdd = rootView.findViewById(R.id.txt_additional_water);
    }

    private void calculate() {
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

            if (expGravity < gravity) {
                double result = waterCorrectionCalcService.calcAdditionalWater(batchSize, volUnit,
                        gravity, gravityUnit, expGravity, expGravityUnit);

                double gallResult = unitCalcService.calcLitresToGallons(result);

                txtToAdd.setText(String.format("%.2f %s %s \n %.2f %s %s",
                        result,
                        getResources().getText(R.string.volume_unit_liters),
                        getResources().getText(R.string.lbl_of_water),
                        gallResult,
                        getResources().getText(R.string.volume_unit_gallons),
                        getResources().getText(R.string.lbl_of_water)));
            }
            else {
                ExtractRaws result = gravityCorrectionCalcService.calculateExtract(batchSize, volUnit, gravity, gravityUnit, expGravity, expGravityUnit);

                txtToAdd.setText(String.format("%.2f lbs %s %.2f kg %s \n %.2f lbs %s %.2f kg %s \n %.2f lbs %s %.2f kg %s",
                        result.getLiquidMaltExtract(), getResources().getText(R.string.lbl_or), unitCalcService.calcPoundsToKilograms(result.getLiquidMaltExtract()), getResources().getText(R.string.liquid_extract),
                        result.getDryMaltExtract(), getResources().getText(R.string.lbl_or), unitCalcService.calcPoundsToKilograms(result.getDryMaltExtract()), getResources().getText(R.string.dry_extract),
                        result.getCornSugar(), getResources().getText(R.string.lbl_or), unitCalcService.calcPoundsToKilograms(result.getCornSugar()), getResources().getText(R.string.corn_sugar)));
            }
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        calculate();
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
        calculate();
    }
}
