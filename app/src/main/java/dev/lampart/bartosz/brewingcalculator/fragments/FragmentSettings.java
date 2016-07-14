package dev.lampart.bartosz.brewingcalculator.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.dbfile.FileDB;
import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.entities.BCalcConf;
import dev.lampart.bartosz.brewingcalculator.global.AppConfiguration;

/**
 * Created by bartek on 14.07.2016.
 */
public class FragmentSettings extends DialogFragment {

    Spinner spDefaultExtractUnit;
    CheckBox chbUseRefractometer;
    EditText txtWortCorrectFactor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View dialogview = inflater.inflate(R.layout.dialog_settings, container);

        spDefaultExtractUnit = (Spinner) dialogview.findViewById(R.id.spinner_choose_default_extract_unit);
        chbUseRefractometer = (CheckBox) dialogview.findViewById(R.id.chb_always_use_refractometer);
        txtWortCorrectFactor = (EditText) dialogview.findViewById(R.id.txt_default_wort_correction_factor);

        //alertDialog = new AlertDialog.Builder(getActivity()).create(); //Read Update
        //alertDialog.setView(dialogview);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.extract_units,
                android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(R.layout.bcalc_spinner_dropdown_item);
        int spinnerUnitPosition = adapter.getPosition(AppConfiguration.getInstance().defaultSettings.getDefExtractUnit().toString());
        spDefaultExtractUnit.setSelection(spinnerUnitPosition);

        chbUseRefractometer.setChecked(AppConfiguration.getInstance().defaultSettings.isDefUseRefractometer());
        txtWortCorrectFactor.setText(Double.toString(AppConfiguration.getInstance().defaultSettings.getDefWortCorrectionFactor()));

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

                //FileDB.saveDefaultUnit(selectedUnit, getApplicationContext());
                AppConfiguration.getInstance().defaultSettings.setDefExtractUnit(selectedUnit);
                AppConfiguration.getInstance().defaultSettings.setDefUseRefractometer(useRefractometer);
                AppConfiguration.getInstance().defaultSettings.setDefWortCorrectionFactor(wortCorrectFactor);

                BCalcConf conf = new BCalcConf();
                conf.setDefExtractUnit(selectedUnit);
                conf.setDefUseRefractometer(useRefractometer);
                conf.setDefWortCorrectionFactor(wortCorrectFactor);
                FileDB.saveConfiguration(conf, getActivity().getApplicationContext());

                getDialog().hide();
                getDialog().dismiss();
            }
        });

        return dialogview;
    }

}
