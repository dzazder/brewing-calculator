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
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import javax.inject.Inject;

import androidx.fragment.app.Fragment;
import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.calculators.GravityCorrectionCalc;
import dev.lampart.bartosz.brewingcalculator.calculators.UnitCalc;
import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;
import dev.lampart.bartosz.brewingcalculator.entities.ExtractRaws;
import dev.lampart.bartosz.brewingcalculator.global.AppConfiguration;
import dev.lampart.bartosz.brewingcalculator.helpers.NumberFormatter;

/**
 * Created by bartek on 29.04.2017.
 */
public class FragmentGravityCorrection extends Fragment implements TextWatcher, AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {

    private EditText txtCurrentSize;
    private Spinner spCurrentSizeUnit;
    private EditText txtCurrentGravity;
    private Spinner spCurrentGravityUnit;
    private EditText txtExpGravity;
    private Spinner spExpGravityUnit;
    private EditText txtExpSize;
    private Spinner spExpSizeUnit;
    private TextView txtGravityChangesResult;
    private RadioGroup rgCorrection;
    private LinearLayout viewCorrectionGravity;
    private LinearLayout viewCorrectionSize;
    private TextView lblGravityChanges;

    private AdView mAdView;

    private final GravityCorrectionCalc gravityCorrectionCalcService;
    private final UnitCalc unitCalcService;

    @Inject
    public FragmentGravityCorrection(GravityCorrectionCalc gravityCorrectionCalcService, UnitCalc unitCalcService) {
        this.gravityCorrectionCalcService = gravityCorrectionCalcService;
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
        initSizeSpinner();
        initGravityUnitSpinner();
        initListeners();
        setControlsView();
    }

    private void initListeners() {
        txtCurrentGravity.addTextChangedListener(this);
        txtCurrentSize.addTextChangedListener(this);
        txtExpGravity.addTextChangedListener(this);
        txtExpSize.addTextChangedListener(this);
        spCurrentGravityUnit.setOnItemSelectedListener(this);
        spCurrentSizeUnit.setOnItemSelectedListener(this);
        spExpGravityUnit.setOnItemSelectedListener(this);
        spExpSizeUnit.setOnItemSelectedListener(this);

        rgCorrection.setOnCheckedChangeListener(this);
    }

    private void initSizeSpinner() {
        ArrayAdapter<CharSequence> primingSizeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.volume_units,
                android.R.layout.simple_spinner_item);
        VolumeUnit selDefVolUnit = VolumeUnit.valueOf(AppConfiguration.getInstance().defaultSettings.getDefVolumeUnit().toString());
        String valSelectedInSpinnerVol = "";
        switch (selDefVolUnit) {
            case Liter:
                valSelectedInSpinnerVol = getActivity().getResources().getString(R.string.volume_unit_liters);
                break;
            case Gallon:
                valSelectedInSpinnerVol = getActivity().getResources().getString(R.string.volume_unit_gallons);
                break;
        }
        int spinnerUnitPosition = primingSizeAdapter.getPosition(valSelectedInSpinnerVol);
        spCurrentSizeUnit.setSelection(spinnerUnitPosition);
        spExpSizeUnit.setSelection(spinnerUnitPosition);
    }

    private void initGravityUnitSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.extract_units,
                android.R.layout.simple_spinner_item);
        int spinnerPosition = adapter.getPosition(AppConfiguration.getInstance().defaultSettings.getDefExtractUnit().toString());
        spCurrentGravityUnit.setSelection(spinnerPosition);
        spExpGravityUnit.setSelection(spinnerPosition);
    }

    private void getControlsFromView(View rootView) {
        txtCurrentSize = rootView.findViewById(R.id.txt_gc_current_size);
        spCurrentSizeUnit = rootView.findViewById(R.id.sp_gc_current_size_units);
        txtCurrentGravity = rootView.findViewById(R.id.txt_gc_current_gravity);
        spCurrentGravityUnit = rootView.findViewById(R.id.sp_gc_current_gravity_units);
        txtExpGravity = rootView.findViewById(R.id.txt_gc_expected_gravity);
        spExpGravityUnit = rootView.findViewById(R.id.sp_gc_expected_gravity_units);
        txtExpSize = rootView.findViewById(R.id.txt_gc_expected_size);
        spExpSizeUnit = rootView.findViewById(R.id.sp_gc_expected_size_units);
        txtGravityChangesResult = rootView.findViewById(R.id.txt_gravity_changes);
        rgCorrection = rootView.findViewById(R.id.correction);
        viewCorrectionGravity = rootView.findViewById(R.id.view_correction_gravity);
        viewCorrectionSize = rootView.findViewById(R.id.view_correction_size);
        lblGravityChanges = rootView.findViewById(R.id.lbl_gravity_changes);
    }

    private void calculate() {

        resetLabels();
        double currentSize = NumberFormatter.getDoubleValue(txtCurrentSize);
        double expSize = NumberFormatter.getDoubleValue(txtExpSize);
        double gravity = NumberFormatter.getDoubleValue(txtCurrentGravity);
        double expGravity = NumberFormatter.getDoubleValue(txtExpGravity);
        String selectedVolUnit = spCurrentSizeUnit.getSelectedItem().toString();
        String expVolUnit = spExpSizeUnit.getSelectedItem().toString();
        ExtractUnit gravityUnit = ExtractUnit.valueOf(spCurrentGravityUnit.getSelectedItem().toString());
        ExtractUnit expGravityUnit = ExtractUnit.valueOf(spExpGravityUnit.getSelectedItem().toString());

        switch (rgCorrection.getCheckedRadioButtonId()) {
            case R.id.correction_gravity:
                calculateValuesForGravity(currentSize, selectedVolUnit, gravity, gravityUnit, expGravity, expGravityUnit);
                break;
            case R.id.correction_size:
                calculateValueForSize(currentSize, selectedVolUnit, gravity, gravityUnit, expSize, expVolUnit);
                break;
        }
    }

    private void calculateValueForSize(double currentSize, String selectedVolUnit, double gravity,
                                       ExtractUnit gravityUnit, double expSize, String expVolUnit) {
        if (currentSize > 0 && gravity > 0 && expSize > 0) {
            if (currentSize > expSize) {
                lblGravityChanges.setText(getText(R.string.lbl_after_evaporation));
            } else if (currentSize < expSize) {
                lblGravityChanges.setText(getText(R.string.lbl_after_adding_water));
            }

            VolumeUnit volUnit = VolumeUnit.Gallon;
            if (selectedVolUnit == getContext().getString(R.string.volume_unit_liters)) {
                volUnit = VolumeUnit.Liter;
            }

            VolumeUnit volExpUnit = VolumeUnit.Gallon;
            if (expVolUnit == getContext().getString(R.string.volume_unit_liters)) {
                volExpUnit = VolumeUnit.Liter;
            }

            double result = gravityCorrectionCalcService.calcExtractAfterEvaporation(currentSize, volUnit, expSize, volExpUnit, gravity, gravityUnit);

            String gravityUnitLabel = "";
            switch (gravityUnit) {
                case SG:
                    gravityUnitLabel = "SG";
                    break;
                case Plato:
                    gravityUnitLabel = "Plato";
                    break;
                case Brix:
                    gravityUnitLabel = "Brix";
                    break;
            }

            txtGravityChangesResult.setText(String.format("%.2f %s", result, gravityUnitLabel));
        }
    }

    private void calculateValuesForGravity(double currentSize, String selectedVolUnit, double gravity,
                                           ExtractUnit gravityUnit, double expGravity, ExtractUnit expGravityUnit) {

        if (expGravity > 0 && currentSize > 0 && gravity > 0) {
            lblGravityChanges.setText(getText(R.string.lbl_have_to_add));

            VolumeUnit volUnit = VolumeUnit.Gallon;
            if (selectedVolUnit == getContext().getString(R.string.volume_unit_liters)) {
                volUnit = VolumeUnit.Liter;
            }

            if (expGravity < gravity) {
                double result = gravityCorrectionCalcService.calcAdditionalWater(currentSize, volUnit,
                        gravity, gravityUnit, expGravity, expGravityUnit);

                double gallResult = unitCalcService.calcLitresToGallons(result);

                txtGravityChangesResult.setText(String.format("%.2f %s %s \n %.2f %s %s",
                        result,
                        getResources().getText(R.string.volume_unit_liters),
                        getResources().getText(R.string.lbl_of_water),
                        gallResult,
                        getResources().getText(R.string.volume_unit_gallons),
                        getResources().getText(R.string.lbl_of_water)));
            } else {
                ExtractRaws result = gravityCorrectionCalcService.calculateExtract(currentSize, volUnit, gravity, gravityUnit, expGravity, expGravityUnit);

                txtGravityChangesResult.setText(String.format("%.2f lbs %s %.2f kg %s \n %.2f lbs %s %.2f kg %s \n %.2f lbs %s %.2f kg %s \n %.2f lbs %s %.2f kg %s",
                        result.getLiquidMaltExtract(), getResources().getText(R.string.lbl_or), unitCalcService.calcPoundsToKilograms(result.getLiquidMaltExtract()), getResources().getText(R.string.liquid_extract),
                        result.getDryMaltExtract(), getResources().getText(R.string.lbl_or), unitCalcService.calcPoundsToKilograms(result.getDryMaltExtract()), getResources().getText(R.string.dry_extract),
                        result.getCornSugar(), getResources().getText(R.string.lbl_or), unitCalcService.calcPoundsToKilograms(result.getCornSugar()), getResources().getText(R.string.corn_sugar),
                        result.getTableSugar(), getResources().getText(R.string.lbl_or), unitCalcService.calcPoundsToKilograms(result.getTableSugar()), getResources().getText(R.string.table_sugar)));
            }
        }
    }

    private void setControlsView() {
        switch (rgCorrection.getCheckedRadioButtonId()) {
            case R.id.correction_gravity:
                viewCorrectionSize.setVisibility(View.GONE);
                viewCorrectionGravity.setVisibility(View.VISIBLE);
                break;
            case R.id.correction_size:
                viewCorrectionSize.setVisibility(View.VISIBLE);
                viewCorrectionGravity.setVisibility(View.GONE);
                break;
        }
    }

    private void resetLabels() {
        txtGravityChangesResult.setText("");
        lblGravityChanges.setText("");
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

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        setControlsView();
        calculate();
    }
}
