package dev.lampart.bartosz.brewingcalculator.fragments;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Locale;

import androidx.fragment.app.Fragment;
import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.calculators.ExtractCalc;
import dev.lampart.bartosz.brewingcalculator.helpers.NumberFormatter;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSgPlato extends Fragment {

    private boolean editedByProgram = false;

    private EditText txtBrix;
    private EditText txtSG;
    private EditText txtPlato;

    private AdView mAdView;

    private final ExtractCalc extractCalcService;

    public FragmentSgPlato(ExtractCalc extractCalcService) {
        this.extractCalcService = extractCalcService;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sg_plato, container, false);

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        getActivity().setTitle(getResources().getString(R.string.title_extract_calculator));

        initControls(view);

        return view;
    }

    private void initControls(View view) {
        txtBrix = view.findViewById(R.id.txt_calc_brix);
        txtSG = view.findViewById(R.id.txt_calc_og);
        txtPlato = view.findViewById(R.id.txt_calc_plato);

        initBrixTextChangedListener();
        initSGTextChangedListener();
        initPlatoTextChangedListener();
    }

    private void initPlatoTextChangedListener() {
        txtPlato.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (!editedByProgram && NumberFormatter.isNumeric(s.toString())) {
                    editedByProgram = true;
                    Log.d("TEXT_CHANGE", "TXT PLATO CHANGED");

                    String platoTxt = s.toString().replace(',', '.');
                    double platoNum = platoTxt.length() > 0 ? Double.parseDouble(platoTxt) : 0;
                    double sgNum = extractCalcService.calcPlatoToSG(platoNum);
                    double brixNum = extractCalcService.calcPlatoToBrix(platoNum);

                    setValues(brixNum, sgNum, txtBrix, txtSG);
                    editedByProgram = false;
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    private void initSGTextChangedListener() {
        txtSG.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (!editedByProgram && NumberFormatter.isNumeric(s.toString())) {
                    editedByProgram = true;
                    Log.d("TEXT_CHANGE", "TXT OG CHANGED");

                    String sgText = s.toString().replace(',', '.');
                    double sgNum = sgText.length() > 0 ? Double.parseDouble(sgText) : 0;
                    double platoNum = extractCalcService.calcSGToPlato(sgNum);
                    double brixNum = extractCalcService.calcSGToBrix(sgNum);

                    setValues(brixNum, platoNum, txtBrix, txtPlato);
                    editedByProgram = false;
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    private void initBrixTextChangedListener() {
        txtBrix.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (!editedByProgram && NumberFormatter.isNumeric(s.toString())) {
                    editedByProgram = true;
                    Log.d("TEXT_CHANGE", "TXT BRIX CHANGED");

                    String brixText = s.toString().replace(',', '.');
                    double brixNum = brixText.length() > 0 ? Double.parseDouble(brixText) : 0;
                    double platoNum = extractCalcService.calcBrixToPlato(brixNum);
                    double sgNum = extractCalcService.calcBrixToSG(brixNum);

                    setValues(platoNum, sgNum, txtPlato, txtSG);

                    editedByProgram = false;
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }



    private void setValues(double v1, double v2, EditText t1, EditText t2) {
        v1 = v1 < 0 ? 0 : v1;
        v2 = v2 < 0 ? 0 : v2;
        t1.setText(String.format(Locale.US, t1.getId() == R.id.txt_calc_og ? "%.3f" : "%.2f", v1));
        t2.setText(String.format(Locale.US, t2.getId() == R.id.txt_calc_og ? "%.3f" : "%.2f", v2));
    }

}
