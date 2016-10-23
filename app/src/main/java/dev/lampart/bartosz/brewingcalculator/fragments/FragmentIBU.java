package dev.lampart.bartosz.brewingcalculator.fragments;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.adapters.IBUHopItemAdapter;
import dev.lampart.bartosz.brewingcalculator.calculators.IBUCalc;
import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.HopType;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.WeightUnit;
import dev.lampart.bartosz.brewingcalculator.entities.IBUData;
import dev.lampart.bartosz.brewingcalculator.global.AppConfiguration;
import dev.lampart.bartosz.brewingcalculator.helpers.NumberFormatter;
import dev.lampart.bartosz.brewingcalculator.listeners.IEditTextTextChangedListener;
import dev.lampart.bartosz.brewingcalculator.listeners.IOnItemSelectedListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentIBU extends Fragment {

    LinearLayout mainLayout;
    LinearLayout noHopLayout;
    TextView txtEstimatedIBURager;
    TextView txtEstimatedIBUTinseth;
    EditText txtPrimingSize;
    EditText txtGravity;
    Spinner spSizeUnit;
    Spinner spGravityUnit;
    ListView lvHops;

    ArrayList<IBUData> items = new ArrayList<IBUData>();
    IBUHopItemAdapter hopItemAdapter;

    public FragmentIBU() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ibu, container, false);

        mainLayout = (LinearLayout) view.findViewById(R.id.layout_ibu_main);
        noHopLayout = (LinearLayout)view.findViewById(R.id.layout_noHopLabel);
        txtEstimatedIBURager = (TextView)view.findViewById(R.id.txt_estimated_ibu_rager);
        txtEstimatedIBUTinseth = (TextView)view.findViewById(R.id.txt_estimated_ibu_tinseth);
        txtPrimingSize = (EditText)view.findViewById(R.id.txt_ibu_priming_size);
        txtGravity = (EditText)view.findViewById(R.id.txt_ibu_extract_after);
        spSizeUnit = (Spinner) view.findViewById(R.id.sp_ibu_priming_size);
        spGravityUnit = (Spinner)view.findViewById(R.id.sp_ibu_extract_after);
        lvHops = (ListView)view.findViewById(R.id.lv_hops);

        hopItemAdapter = new IBUHopItemAdapter(getActivity(), items, new IEditTextTextChangedListener() {
            @Override
            public void afterTextChanged(int position) {
                calculateIBU(txtPrimingSize, txtGravity, spSizeUnit, spGravityUnit, txtEstimatedIBURager, txtEstimatedIBUTinseth);
            }
        }, new IOnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                calculateIBU(txtPrimingSize, txtGravity, spSizeUnit, spGravityUnit, txtEstimatedIBURager, txtEstimatedIBUTinseth);
            }
        });
        lvHops.setAdapter(hopItemAdapter);

        txtPrimingSize.setText(Double.toString(AppConfiguration.getInstance().defaultSettings.getDefPrimingSize()));

        ArrayAdapter<CharSequence> primingSizeAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.volume_units, android.R.layout.simple_spinner_item);
        VolumeUnit selDefVolUnit = VolumeUnit.valueOf(AppConfiguration.getInstance().defaultSettings.getDefVolumeUnit().toString());
        String valSelectedInSpinnerVol = "";
        switch (selDefVolUnit) {
            case Liter: valSelectedInSpinnerVol = getActivity().getResources().getString(R.string.volume_unit_liters); break;
            case Gallon: valSelectedInSpinnerVol = getActivity().getResources().getString(R.string.volume_unit_gallons); break;
        }
        int spinnerPrimingUnigPosition = primingSizeAdapter.getPosition(valSelectedInSpinnerVol);
        spSizeUnit.setSelection(spinnerPrimingUnigPosition);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.extract_units,
                android.R.layout.simple_spinner_item);
        int spinnerPosition = adapter.getPosition(AppConfiguration.getInstance().defaultSettings.getDefExtractUnit().toString());
        spGravityUnit.setSelection(spinnerPosition);

        txtGravity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                calculateIBU(txtPrimingSize, txtGravity, spSizeUnit, spGravityUnit,
                        txtEstimatedIBURager, txtEstimatedIBUTinseth);
            }
        });

        txtPrimingSize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                calculateIBU(txtPrimingSize, txtGravity, spSizeUnit, spGravityUnit,
                        txtEstimatedIBURager, txtEstimatedIBUTinseth);
            }
        });

        spGravityUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                calculateIBU(txtPrimingSize, txtGravity, spSizeUnit, spGravityUnit,
                        txtEstimatedIBURager, txtEstimatedIBUTinseth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spSizeUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                calculateIBU(txtPrimingSize, txtGravity, spSizeUnit, spGravityUnit,
                        txtEstimatedIBURager, txtEstimatedIBUTinseth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button btnAddHop = (Button)view.findViewById(R.id.btn_add_hop);
        btnAddHop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hopItemAdapter.updateDataSet(new IBUData(0, 0,
                        AppConfiguration.getInstance().defaultSettings.getDefWeightUnit(), 0,
                        HopType.PELLETS));

                if (noHopLayout.getVisibility() == View.VISIBLE) {
                    noHopLayout.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

    protected void calculateIBU(EditText txtPrimingSize, EditText txtGravity,
                                Spinner spSizeUnit, Spinner spGravityUnit,
                                TextView txtEstimatedIBURager, TextView txtEstimatedIBUTinseth) {

        if (NumberFormatter.isNumeric(txtPrimingSize.getText().toString()) &&
            NumberFormatter.isNumeric(txtGravity.getText().toString())) {

            double primingSize = Double.parseDouble(txtPrimingSize.getText().toString());
            double gravity = Double.parseDouble(txtGravity.getText().toString());
            String selectedVolUnit = spSizeUnit.getSelectedItem().toString();

            ExtractUnit gravityUnit = ExtractUnit.valueOf(spGravityUnit.getSelectedItem().toString());
            VolumeUnit volUnit = VolumeUnit.Gallon;
            if (selectedVolUnit == getContext().getString(R.string.volume_unit_liters)) {
                volUnit = VolumeUnit.Liter;
            }

            ArrayList<IBUData> ibuDatas = new ArrayList<>();

            for (int i = 0; i < hopItemAdapter.getCount(); i++) {
                IBUData ibuData = hopItemAdapter.getItem(i);
                ibuDatas.add(ibuData);
            }

            double ibuRager = IBUCalc.calcIBU(ibuDatas, gravity, primingSize, gravityUnit, volUnit,
                    IBUCalc.FormulaTypeIBU.RAGER);

            double ibuTinseth = IBUCalc.calcIBU(ibuDatas, gravity, primingSize, gravityUnit, volUnit,
                    IBUCalc.FormulaTypeIBU.TINSETH);

            setEstimatedIBUValue(txtEstimatedIBURager, ibuRager);
            setEstimatedIBUValue(txtEstimatedIBUTinseth, ibuTinseth);

        }
    }

    private void setEstimatedIBUValue(TextView txtToSet, double valToSet) {
        if (valToSet < 0) {
            txtToSet.setTextColor(getResources().getColor(R.color.colorError));
            txtToSet.setText(getResources().getText(R.string.incorrect_value));
        }
        else {
            txtToSet.setTextColor(getResources().getColor(R.color.colorAccent));
            txtToSet.setText(String.format(Locale.US, "%.1f IBU", valToSet));
        }
    }
}
