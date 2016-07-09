package dev.lampart.bartosz.brewingcalculator.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.Locale;

import dev.lampart.bartosz.brewingcalculator.MainActivity;
import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.calculators.ExtractCalc;
import dev.lampart.bartosz.brewingcalculator.helpers.InputFilterMinMax;
import dev.lampart.bartosz.brewingcalculator.helpers.NumberFormatter;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSgPlato extends Fragment {

    private boolean editedByProgram = false;

    public FragmentSgPlato() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sg_plato, container, false);

        getActivity().setTitle(getResources().getString(R.string.title_extract_calculator));


        final EditText txtBrix = (EditText)view.findViewById(R.id.txt_calc_brix);
        final EditText txtSG = (EditText)view.findViewById(R.id.txt_calc_og);
        final EditText txtPlato = (EditText)view.findViewById(R.id.txt_calc_plato);


        txtBrix.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (!editedByProgram && NumberFormatter.isNumeric(s.toString())) {
                    editedByProgram = true;
                    Log.d("TEXT_CHANGE", "TXT BRIX CHANGED");

                    String brixText = s.toString().replace(',', '.');
                    double brixNum = brixText.length() > 0 ? Double.parseDouble(brixText) : 0;
                    double platoNum = ExtractCalc.calcBrixToPlato(brixNum);
                    double sgNum = ExtractCalc.calcBrixToSG(brixNum);

                    setValues(platoNum, sgNum, txtPlato, txtSG);

                    editedByProgram = false;
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        txtSG.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (!editedByProgram && NumberFormatter.isNumeric(s.toString())) {
                    editedByProgram = true;
                    Log.d("TEXT_CHANGE", "TXT OG CHANGED");

                    String sgText = s.toString().replace(',', '.');
                    double sgNum = sgText.length() > 0 ? Double.parseDouble(sgText) : 0;
                    double platoNum = ExtractCalc.calcSGToPlato(sgNum);
                    double brixNum = ExtractCalc.calcSGToBrix(sgNum);

                    setValues(brixNum, platoNum, txtBrix, txtPlato);
                    editedByProgram = false;
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        txtPlato.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (!editedByProgram && NumberFormatter.isNumeric(s.toString())) {
                    editedByProgram = true;
                    Log.d("TEXT_CHANGE", "TXT PLATO CHANGED");

                    String platoTxt = s.toString().replace(',', '.');
                    double platoNum = platoTxt.length() > 0 ? Double.parseDouble(platoTxt) : 0;
                    double sgNum = ExtractCalc.calcPlatoToSG(platoNum);
                    double brixNum = ExtractCalc.calcPlatoToBrix(platoNum);

                    setValues(brixNum, sgNum, txtBrix, txtSG);
                    editedByProgram = false;
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        return view;
    }

    private void setValues(double v1, double v2, EditText t1, EditText t2) {
        v1 = v1 < 0 ? 0 : v1;
        v2 = v2 < 0 ? 0 : v2;
        t1.setText(String.format(Locale.US, t1.getId() == R.id.txt_calc_og ? "%.3f" : "%.2f", v1));
        t2.setText(String.format(Locale.US, t2.getId() == R.id.txt_calc_og ? "%.3f" : "%.2f", v2));
    }

    /*
    private void setValues(double brix, double plato, double og,
                           EditText txtBrix, EditText txtPlato, EditText txtOg) {
        txtBrix.setText(Double.toString(brix));
        txtPlato.setText(Double.toString(plato));
        txtOg.setText(Double.toString(og));
    }
    */

}
