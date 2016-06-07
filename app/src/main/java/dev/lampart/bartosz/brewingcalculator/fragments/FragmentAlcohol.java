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

        final Spinner spBefore = (Spinner)rootView.findViewById(R.id.sp_extract_before);
        final Spinner spAfter = (Spinner)rootView.findViewById(R.id.sp_extract_after);

        final EditText txtExtBefore = (EditText)rootView.findViewById(R.id.txt_extract_before);
        final EditText txtExtAfter = (EditText)rootView.findViewById(R.id.txt_extract_after);

        final TextView lblAlco = (TextView)rootView.findViewById(R.id.txt_calc_alc);
        final TextView lblAtt = (TextView)rootView.findViewById(R.id.txt_calc_att);

        final CheckBox chbUseRefractometer = (CheckBox)rootView.findViewById(R.id.chb_use_refractometer);

        final LinearLayout layWortCorrectionFactor = (LinearLayout)rootView.findViewById(R.id.layout_wort_correction_factor);

        spAfter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("SPINNER", "change value");
                if (NumberFormatter.isNumeric(txtExtAfter.getText().toString()) &&
                        NumberFormatter.isNumeric(txtExtBefore.getText().toString())) {

                    Log.d("SPINNER", "isnumeric true");
                    double dBefore = Double.parseDouble(txtExtBefore.getText().toString());
                    double dAfter = Double.parseDouble(txtExtAfter.getText().toString());

                    Tuple<Double, Double> alcatt = calculateAlcohol(dBefore, dAfter, ExtractUnit.valueOf(spBefore.getSelectedItem().toString()),
                            ExtractUnit.valueOf(spAfter.getSelectedItem().toString()), chbUseRefractometer.isChecked());

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
                        NumberFormatter.isNumeric(txtExtBefore.getText().toString())) {

                    double dBefore = Double.parseDouble(txtExtBefore.getText().toString());
                    double dAfter = Double.parseDouble(txtExtAfter.getText().toString());

                    Tuple<Double, Double> alcatt = calculateAlcohol(dBefore, dAfter, ExtractUnit.valueOf(spBefore.getSelectedItem().toString()),
                            ExtractUnit.valueOf(spAfter.getSelectedItem().toString()), chbUseRefractometer.isChecked());

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
                        && NumberFormatter.isNumeric(txtExtAfter.getText().toString())) {
                    editedByProgram = true;

                    double dBefore = Double.parseDouble(s.toString());
                    double dAfter = Double.parseDouble(txtExtAfter.getText().toString());

                    Tuple<Double, Double> alcatt = calculateAlcohol(dBefore, dAfter, ExtractUnit.valueOf(spBefore.getSelectedItem().toString()),
                            ExtractUnit.valueOf(spAfter.getSelectedItem().toString()), chbUseRefractometer.isChecked());

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
                        && NumberFormatter.isNumeric(txtExtBefore.getText().toString())) {
                    editedByProgram = true;

                    double dAfter = Double.parseDouble(s.toString());
                    double dBefore = Double.parseDouble(txtExtBefore.getText().toString());

                    Tuple<Double, Double> alcatt = calculateAlcohol(dBefore, dAfter, ExtractUnit.valueOf(spBefore.getSelectedItem().toString()),
                            ExtractUnit.valueOf(spAfter.getSelectedItem().toString()), chbUseRefractometer.isChecked());

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
                        NumberFormatter.isNumeric(txtExtBefore.getText().toString())) {

                    double dBefore = Double.parseDouble(txtExtBefore.getText().toString());
                    double dAfter = Double.parseDouble(txtExtAfter.getText().toString());

                    Tuple<Double, Double> alcatt = calculateAlcohol(dBefore, dAfter, ExtractUnit.valueOf(spBefore.getSelectedItem().toString()),
                            ExtractUnit.valueOf(spAfter.getSelectedItem().toString()), chbUseRefractometer.isChecked());

                    setValues(alcatt.x, alcatt.y, lblAlco, lblAtt);
                }
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.extract_units,
                android.R.layout.simple_spinner_item);

        int spinnerPosition = adapter.getPosition(AppConfiguration.getInstance().defaultExtractUnit.toString());

        spAfter.setSelection(spinnerPosition);
        spBefore.setSelection(spinnerPosition);

        return rootView;
    }

    public Tuple<Double, Double> calculateAlcohol(double extBefore, double extAfter,
                                                  ExtractUnit extBeforeUnit, ExtractUnit extAfterUnit,
                                                  boolean useRefractometer) {
        double alc = 0;
        double att = 0;

        if (useRefractometer) {
            switch (extBeforeUnit) {
                case Brix:
                    extBefore = ExtractCalc.calcBrixToSG(extBefore);
                    break;
                case Plato:
                    extBefore = ExtractCalc.calcPlatoToSG(extBefore);
                    break;
            }
            switch (extAfterUnit) {
                case Brix:
                    extAfter = ExtractCalc.calcBrixToSG(extAfter);
                    break;
                case Plato:
                    extAfter = ExtractCalc.calcPlatoToSG(extAfter);
                    break;
            }

            alc = 100 * ((extBefore - extAfter) / 0.75);
            att = ((extBefore - 1) - (extAfter - 1)) / (extBefore - 1) * 100;
        }
        else {
            switch (extBeforeUnit) {
                case Brix:
                    extBefore = ExtractCalc.calcBrixToPlato(extBefore);
                    break;
                case SG:
                    extBefore = ExtractCalc.calcSGToPlato(extBefore);
                    break;
            }

            switch (extAfterUnit) {
                case Brix:
                    extAfter = ExtractCalc.calcBrixToPlato(extAfter);
                    break;
                case SG:
                    extAfter = ExtractCalc.calcSGToPlato(extAfter);
                    break;
            }

            alc = (extBefore - extAfter) / 1.938;
            att = (extBefore - extAfter) / (extBefore) * 100;
        }

        return new Tuple<>(alc, att);
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
