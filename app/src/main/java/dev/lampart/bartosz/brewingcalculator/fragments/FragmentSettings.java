package dev.lampart.bartosz.brewingcalculator.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.dbfile.FileDB;
import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.TemperatureUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.WeightUnit;
import dev.lampart.bartosz.brewingcalculator.entities.BCalcConf;
import dev.lampart.bartosz.brewingcalculator.global.AppConfiguration;

/**
 * Created by bartek on 14.07.2016.
 */
public class FragmentSettings extends DialogFragment {

    Spinner spDefaultExtractUnit;
    CheckBox chbUseRefractometer;
    EditText txtWortCorrectFactor;
    EditText txtPrimingSize;
    Spinner spDefaultPrimingVol;
    EditText txtBeerTemp;
    Spinner spDefaultTempScale;
    Spinner spDefaultWeightUnit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View dialogview = inflater.inflate(R.layout.dialog_settings, container, false);

        //getDialog().setTitle(getActivity().getResources().getString(R.string.settings));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        spDefaultExtractUnit = (Spinner) dialogview.findViewById(R.id.spinner_choose_default_extract_unit);
        chbUseRefractometer = (CheckBox) dialogview.findViewById(R.id.chb_always_use_refractometer);
        txtWortCorrectFactor = (EditText) dialogview.findViewById(R.id.txt_default_wort_correction_factor);
        txtPrimingSize = (EditText) dialogview.findViewById(R.id.txt_default_priming_size);
        spDefaultPrimingVol = (Spinner) dialogview.findViewById(R.id.sp_default_priming_size);
        txtBeerTemp = (EditText) dialogview.findViewById(R.id.txt_default_beer_temp);
        spDefaultTempScale = (Spinner) dialogview.findViewById(R.id.sp_default_temp_scale);
        spDefaultWeightUnit = (Spinner) dialogview.findViewById(R.id.sp_default_weight_unit);

        //alertDialog = new AlertDialog.Builder(getActivity()).create(); //Read Update
        //alertDialog.setView(dialogview);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.extract_units,
                android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(R.layout.bcalc_spinner_dropdown_item);
        int spinnerUnitPosition = adapter.getPosition(AppConfiguration.getInstance().defaultSettings.getDefExtractUnit().toString());
        spDefaultExtractUnit.setSelection(spinnerUnitPosition);

        chbUseRefractometer.setChecked(AppConfiguration.getInstance().defaultSettings.isDefUseRefractometer());
        txtWortCorrectFactor.setText(Double.toString(AppConfiguration.getInstance().defaultSettings.getDefWortCorrectionFactor()));

        txtPrimingSize.setText(Double.toString(AppConfiguration.getInstance().defaultSettings.getDefPrimingSize()));

        ArrayAdapter<CharSequence> primingSizeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.volume_units,
                android.R.layout.simple_spinner_item);
        VolumeUnit selDefVolUnit = VolumeUnit.valueOf(AppConfiguration.getInstance().defaultSettings.getDefVolumeUnit().toString());
        String valSelectedInSpinnerVol = "";
        switch (selDefVolUnit) {
            case Liter: valSelectedInSpinnerVol = getActivity().getResources().getString(R.string.volume_unit_liters); break;
            case Gallon: valSelectedInSpinnerVol = getActivity().getResources().getString(R.string.volume_unit_gallons); break;
        }
        int spinnerPrimingUnigPosition = primingSizeAdapter.getPosition(valSelectedInSpinnerVol);
        spDefaultPrimingVol.setSelection(spinnerPrimingUnigPosition);

        txtBeerTemp.setText(Double.toString(AppConfiguration.getInstance().defaultSettings.getDefTemperature()));

        ArrayAdapter<CharSequence> tempScale = ArrayAdapter.createFromResource(getActivity(), R.array.temp_scale,
                android.R.layout.simple_spinner_item);
        TemperatureUnit tempDefUnit = TemperatureUnit.valueOf(AppConfiguration.getInstance().defaultSettings.getDefTempUnit().toString());
        String valSelectedInTempUnit = "";
        switch (tempDefUnit) {
            case C: valSelectedInTempUnit = getActivity().getResources().getString(R.string.temp_unit_C); break;
            case F: valSelectedInTempUnit = getActivity().getResources().getString(R.string.temp_unit_F); break;
        }
        int spinnerTempPosition = tempScale.getPosition(valSelectedInTempUnit);
        spDefaultTempScale.setSelection(spinnerTempPosition);

        ArrayAdapter<CharSequence> weightUnit = ArrayAdapter.createFromResource(getActivity(),
                R.array.weight_light_unit, android.R.layout.simple_spinner_item);
        final WeightUnit weightDefUnit = WeightUnit.valueOf(AppConfiguration.getInstance().defaultSettings.getDefWeightUnit().toString());
        String valSelectedInWeightUnit = "";
        switch (weightDefUnit) {
            case OZ: valSelectedInWeightUnit = getActivity().getResources().getString(R.string.weight_unit_oz); break;
            case G: valSelectedInWeightUnit = getActivity().getResources().getString(R.string.weight_unit_g); break;
        }
        int spinnerWeightPosition = weightUnit.getPosition(valSelectedInWeightUnit);
        spDefaultWeightUnit.setSelection(spinnerWeightPosition);

        Button btnSettingsCancel = (Button) dialogview.findViewById(R.id.btn_settings_cancel);
        btnSettingsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().hide();
            }
        });

        Button btnSettingsSave = (Button) dialogview.findViewById(R.id.btn_settings_save);
        btnSettingsSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExtractUnit selectedUnit = ExtractUnit.valueOf(spDefaultExtractUnit.getSelectedItem().toString());
                boolean useRefractometer = chbUseRefractometer.isChecked();
                double wortCorrectFactor = Double.parseDouble(txtWortCorrectFactor.getText().toString());
                double primingSize = Double.parseDouble(txtPrimingSize.getText().toString());
                VolumeUnit defVolUnit = VolumeUnit.Liter;
                String volUnit = spDefaultPrimingVol.getSelectedItem().toString();
                if (volUnit.equals(getActivity().getResources().getString(R.string.volume_unit_gallons))) {
                    defVolUnit = VolumeUnit.Gallon;
                }
                double beerTemp = Double.parseDouble(txtBeerTemp.getText().toString());
                TemperatureUnit defTempUnit = TemperatureUnit.C;
                String tempUnit = spDefaultTempScale.getSelectedItem().toString();

                if (tempUnit.equals(getActivity().getResources().getString(R.string.temp_unit_F))) {
                    defTempUnit = TemperatureUnit.F;
                }
                Log.d("settings", "tempUnit: " + tempUnit);
                Log.d("settings", "def tempUnit: " + defTempUnit.toString());

                WeightUnit defWeightUnit = WeightUnit.G;
                String weightUnit = spDefaultWeightUnit.getSelectedItem().toString();
                if (weightUnit.equals(getActivity().getResources().getString(R.string.weight_unit_oz))) {
                    defWeightUnit = WeightUnit.OZ;
                }

                //FileDB.saveDefaultUnit(selectedUnit, getApplicationContext());
                AppConfiguration.getInstance().defaultSettings.setDefExtractUnit(selectedUnit);
                AppConfiguration.getInstance().defaultSettings.setDefUseRefractometer(useRefractometer);
                AppConfiguration.getInstance().defaultSettings.setDefWortCorrectionFactor(wortCorrectFactor);
                AppConfiguration.getInstance().defaultSettings.setDefPrimingSize(primingSize);
                AppConfiguration.getInstance().defaultSettings.setDefVolumeUnit(defVolUnit);
                AppConfiguration.getInstance().defaultSettings.setDefTemperature(beerTemp);
                AppConfiguration.getInstance().defaultSettings.setDefTempUnit(defTempUnit);
                AppConfiguration.getInstance().defaultSettings.setDefWeightUnit(defWeightUnit);

                BCalcConf conf = new BCalcConf();
                conf.setDefExtractUnit(selectedUnit);
                conf.setDefUseRefractometer(useRefractometer);
                conf.setDefWortCorrectionFactor(wortCorrectFactor);
                conf.setDefTempUnit(defTempUnit);
                conf.setDefWeightUnit(defWeightUnit);
                FileDB.saveConfiguration(conf, getActivity().getApplicationContext());

                getDialog().hide();
                getDialog().dismiss();
            }
        });

        return dialogview;
    }

}
