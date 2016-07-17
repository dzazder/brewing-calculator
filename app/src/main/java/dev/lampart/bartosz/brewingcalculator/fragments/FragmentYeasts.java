package dev.lampart.bartosz.brewingcalculator.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.calculators.ExtractCalc;
import dev.lampart.bartosz.brewingcalculator.calculators.YeastCalc;
import dev.lampart.bartosz.brewingcalculator.dicts.BeerStyle;
import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;
import dev.lampart.bartosz.brewingcalculator.helpers.NumberFormatter;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentYeasts extends Fragment {

    private TabHost mTabHost;
    private EditText txtGravity;
    private EditText txtBeerAmount;
    private TextView txtYeastNeeded;
    private Spinner spGravityUnit;
    private Spinner spVolumeUnit;
    private boolean editedByProgram = false;

    public FragmentYeasts() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_main);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_yeasts, container, false);

        getActivity().setTitle(getResources().getString(R.string.title_yeasts_amount_calculator));

        mTabHost = (TabHost)view.findViewById(R.id.tabhost_yeasts);
        mTabHost.setup();
        //Tab 1
        TabHost.TabSpec spec = mTabHost.newTabSpec(getResources().getString(R.string.title_yeasts_dry));
        spec.setContent(R.id.tab_yeasts_dry);
        spec.setIndicator(getResources().getString(R.string.title_yeasts_dry));
        mTabHost.addTab(spec);

        //Tab 2
        spec = mTabHost.newTabSpec(getResources().getString(R.string.title_yeasts_liquid));
        spec.setContent(R.id.tab_yeasts_liquid);
        spec.setIndicator(getResources().getString(R.string.title_yeasts_liquid));
        mTabHost.addTab(spec);

        //Tab 3
        spec = mTabHost.newTabSpec(getResources().getString(R.string.title_yeasts_slurry));
        spec.setContent(R.id.tab_yeasts_slurry);
        spec.setIndicator(getResources().getString(R.string.title_yeasts_slurry));
        mTabHost.addTab(spec);

        txtBeerAmount = (EditText)view.findViewById(R.id.txt_yeast_priming_size);
        txtGravity = (EditText)view.findViewById(R.id.txt_yeast_extract);
        txtYeastNeeded = (TextView)view.findViewById(R.id.txt_yeast_needed);
        spGravityUnit = (Spinner)view.findViewById(R.id.sp_yeast_extract_unit);
        spVolumeUnit = (Spinner)view.findViewById(R.id.sp_yeast_priming_size);

        txtBeerAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateYeastCells(txtBeerAmount, txtGravity, spVolumeUnit, spGravityUnit, txtYeastNeeded);
            }
        });


        return view;
    }

    private void calculateYeastCells(EditText txtBeerAmount, EditText txtGravity, Spinner spVolumeUnit,
                                     Spinner spGravityUnit, TextView txtYeastNeeded) {
        if (NumberFormatter.isNumeric(txtBeerAmount.getText().toString()) &&
                NumberFormatter.isNumeric(txtGravity.getText().toString())) {

            double beerAmount = Double.parseDouble(txtBeerAmount.getText().toString());
            double gravity = Double.parseDouble(txtGravity.getText().toString());
            String selectedVolUnit = spVolumeUnit.getSelectedItem().toString();
            String selectedGrUnit = spGravityUnit.getSelectedItem().toString();

            VolumeUnit volUnit = VolumeUnit.Liter;
            if (selectedVolUnit == getContext().getString(R.string.volume_unit_gallons)) {
                volUnit = VolumeUnit.Gallon;
            }

            ExtractUnit gravityUnit = ExtractUnit.valueOf(selectedGrUnit);

            long yeastNeeded = YeastCalc.calcYeastCells(beerAmount, gravity, volUnit, gravityUnit, BeerStyle.Ale);

            setYeastNeededValue(yeastNeeded, txtYeastNeeded);
        }
    }

    private void setYeastNeededValue(long yeast, TextView txtToSet) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);

        txtToSet.setText(formatter.format(yeast));
    }

}
