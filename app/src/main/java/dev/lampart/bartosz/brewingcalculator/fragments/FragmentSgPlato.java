package dev.lampart.bartosz.brewingcalculator.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import dev.lampart.bartosz.brewingcalculator.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSgPlato extends Fragment {


    public FragmentSgPlato() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sg_plato, container, false);

        final EditText txtBrix = (EditText)view.findViewById(R.id.txt_calc_brix);
        final EditText txtSG = (EditText)view.findViewById(R.id.txt_calc_og);
        final EditText txtPlato = (EditText)view.findViewById(R.id.txt_calc_plato);

        txtBrix.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                Log.d("TEXT_CHANGE", "TXT BRIX CHANGED");
                String brixText = s.toString();
                double brixNum = Double.parseDouble(brixText);
                double platoNum = 1;
                double sgNum = 1;

                setValues(platoNum, sgNum, txtPlato, txtSG);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        txtSG.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                Log.d("TEXT_CHANGE", "TXT OG CHANGED");
                String sgText = s.toString();
                double sgNum = Double.parseDouble(sgText);
                double platoNum = 1;
                double brixNum = 1;

                setValues(brixNum, platoNum, txtBrix, txtPlato);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        txtPlato.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                Log.d("TEXT_CHANGE", "TXT PLATO CHANGED");

                String platoTxt = s.toString();
                double platoNum = Double.parseDouble(platoTxt);
                double sgNum = 1;
                double brixNum = 1;

                setValues(brixNum, sgNum, txtBrix, txtSG);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        return view;
    }

    private void setValues(double v1, double v2, EditText t1, EditText t2) {
        t1.setText(Double.toString(v1));
        t2.setText(Double.toString(v2));
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
