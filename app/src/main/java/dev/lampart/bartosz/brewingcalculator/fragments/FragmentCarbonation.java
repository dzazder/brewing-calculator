package dev.lampart.bartosz.brewingcalculator.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.calculators.AlcoholCalc;
import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.helpers.NumberFormatter;
import dev.lampart.bartosz.brewingcalculator.helpers.Tuple;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCarbonation extends Fragment {


    public FragmentCarbonation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_carbonation, container, false);
        getActivity().setTitle(getResources().getString(R.string.title_carbonation_calculator));

        final EditText txtPrimingSize = (EditText)rootView.findViewById(R.id.txt_priming_size);
        final Spinner spPrimingSize = (Spinner)rootView.findViewById(R.id.sp_priming_size);

        spPrimingSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (NumberFormatter.isNumeric(txtPrimingSize.getText().toString())) {
                    double dBefore = Double.parseDouble(txtPrimingSize.getText().toString());

                    String selectedItemString = spPrimingSize.getSelectedItem().toString();

                    if (selectedItemString.equals(getContext().getString(R.string.volume_unit_gallons))) {

                    }
                    long a = spPrimingSize.getSelectedItemId();
                    Log.d("Carbonation", "selected item id: " + a);
                    Log.d("Carbonation", "selected item string: " + selectedItemString);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return rootView;
    }

}
