package dev.lampart.bartosz.brewingcalculator.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.adapters.IBUHopItemAdapter;
import dev.lampart.bartosz.brewingcalculator.calculators.IBUCalc;
import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.HopIntentValues;
import dev.lampart.bartosz.brewingcalculator.dicts.RequestCodes;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;
import dev.lampart.bartosz.brewingcalculator.entities.IBUData;
import dev.lampart.bartosz.brewingcalculator.global.AppConfiguration;
import dev.lampart.bartosz.brewingcalculator.global.ConstStrings;
import dev.lampart.bartosz.brewingcalculator.helpers.ArraysHelper;
import dev.lampart.bartosz.brewingcalculator.helpers.NumberFormatter;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentIBU extends Fragment implements TextWatcher, AdapterView.OnItemSelectedListener,
        View.OnClickListener {

    private LinearLayout mainLayout;
    private LinearLayout noHopLayout;
    private TextView txtEstimatedIBURager;
    private TextView txtEstimatedIBUTinseth;
    private EditText txtPrimingSize;
    private EditText txtGravity;
    private Spinner spSizeUnit;
    private Spinner spGravityUnit;
    private ListView lvHops;
    private Button btnAddHop;

    private ArrayList<IBUData> items = new ArrayList<IBUData>();
    private IBUHopItemAdapter hopItemAdapter;

    public FragmentIBU() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ibu, container, false);

        Log.d("IBU", "IBU view created");

        if (savedInstanceState != null) {
            ArrayList<IBUData> savedData = savedInstanceState.getParcelableArrayList(ConstStrings.IBU_DATA);
            if (savedData != null) {
                items = savedData;
            }
        }

        initControls(view);

        return view;
    }

    public void updateIBUControls() {
        calculateIBU(txtPrimingSize, txtGravity, spSizeUnit, spGravityUnit, txtEstimatedIBURager, txtEstimatedIBUTinseth);
    }

    private void initControls(View view) {
        getControlsFromView(view);
        initHopItemsAdapter();
        initControlValues();
        initPrimingSizeSpinner();
        initGravityUnitSpinner();
        initListeners();
    }

    private void initListeners() {
        txtGravity.addTextChangedListener(this);
        txtPrimingSize.addTextChangedListener(this);
        spGravityUnit.setOnItemSelectedListener(this);
        spSizeUnit.setOnItemSelectedListener(this);
        btnAddHop.setOnClickListener(this);
    }

    private void initControlValues() {
        txtPrimingSize.setText(Double.toString(AppConfiguration.getInstance().defaultSettings.getDefPrimingSize()));
    }

    private void initGravityUnitSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.extract_units,
                android.R.layout.simple_spinner_item);
        int spinnerPosition = adapter.getPosition(AppConfiguration.getInstance().defaultSettings.getDefExtractUnit().toString());
        spGravityUnit.setSelection(spinnerPosition);
    }

    private void initPrimingSizeSpinner() {
        ArrayAdapter<CharSequence> primingSizeAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.volume_units, android.R.layout.simple_spinner_item);
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
        int spinnerPrimingUnigPosition = primingSizeAdapter.getPosition(valSelectedInSpinnerVol);
        spSizeUnit.setSelection(spinnerPrimingUnigPosition);
    }

    private void getControlsFromView(View view) {
        mainLayout = view.findViewById(R.id.layout_ibu_main);
        noHopLayout = view.findViewById(R.id.layout_noHopLabel);
        txtEstimatedIBURager = view.findViewById(R.id.txt_estimated_ibu_rager);
        txtEstimatedIBUTinseth = view.findViewById(R.id.txt_estimated_ibu_tinseth);
        txtPrimingSize = view.findViewById(R.id.txt_ibu_priming_size);
        txtGravity = view.findViewById(R.id.txt_ibu_extract_after);
        spSizeUnit = view.findViewById(R.id.sp_ibu_priming_size);
        spGravityUnit = view.findViewById(R.id.sp_ibu_extract_after);
        lvHops = view.findViewById(R.id.lv_hops);
        btnAddHop = view.findViewById(R.id.btn_add_hop);
    }

    private void initHopItemsAdapter() {
        hopItemAdapter = new IBUHopItemAdapter(getActivity(), items);
        hopItemAdapter.setFragmentIBU(this);
        lvHops.setAdapter(hopItemAdapter);
        lvHops.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    public void showAddHopDialog() {
        showAddHopDialog(RequestCodes.ADD_HOP_REQUEST_CODE);
    }

    public void showAddHopDialog(int requestCode) {
        showAddHopDialog(requestCode, -1, null);
    }

    public void showAddHopDialog(int requestCode, int ibuIndex, IBUData ibuData) {
        FragmentAddHop fAddHop = new FragmentAddHop();
        if (ibuIndex >= 0 && ibuData != null) {
            Bundle bundle = new Bundle();
            bundle.putInt(ConstStrings.ID, ibuIndex);
            bundle.putDouble(ConstStrings.WEIGHT, ibuData.getWeight());
            bundle.putDouble(ConstStrings.ALPHA, ibuData.getAlpha());
            bundle.putDouble(ConstStrings.MINUTES, ibuData.getTime());
            bundle.putString(ConstStrings.HOP_TYPE, ibuData.getHopType().toString());
            bundle.putString(ConstStrings.WEIGHT_UNIT, ibuData.getWeightUnit().toString());
            fAddHop.setArguments(bundle);
        }
        fAddHop.setTargetFragment(this, requestCode);
        fAddHop.show(getActivity().getSupportFragmentManager(), "fragment_add_hop");

        //hopItemAdapter.updateDataSet(new IBUData(0, 0,
        //        AppConfiguration.getInstance().defaultSettings.getDefWeightUnit(), 0,
        //        HopType.PELLETS));

        if (noHopLayout.getVisibility() == View.VISIBLE) {
            noHopLayout.setVisibility(View.GONE);
        }
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
            txtToSet.setTextColor(ContextCompat.getColor(getContext(), R.color.colorError));
            txtToSet.setText(getResources().getText(R.string.incorrect_value));
        } else {
            txtToSet.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            txtToSet.setText(String.format(Locale.US, "%.1f IBU", valToSet));
        }
    }

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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        calculateIBU(txtPrimingSize, txtGravity, spSizeUnit, spGravityUnit,
                txtEstimatedIBURager, txtEstimatedIBUTinseth);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_hop:
                showAddHopDialog();
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RequestCodes.RESULT_OK) {
            if (requestCode == RequestCodes.ADD_HOP_REQUEST_CODE || requestCode == RequestCodes.EDIT_HOP_REQUEST_CODE) {
                hopItemAdapter.updateDataSet(data.getIntExtra(HopIntentValues.IBU_INDEX, -1),
                        new IBUData(data.getDoubleExtra(HopIntentValues.ALPHA, 0),
                                data.getDoubleExtra(HopIntentValues.WEIGHT, 0),
                                ArraysHelper.getWeightUnit(data.getStringExtra(HopIntentValues.WEIGHT_UNIT), getActivity()),
                                data.getDoubleExtra(HopIntentValues.BOILING_TIME, 0),
                                ArraysHelper.getHopType(data.getStringExtra(HopIntentValues.HOP_TYPE), getActivity())));

                calculateIBU(txtPrimingSize, txtGravity, spSizeUnit, spGravityUnit, txtEstimatedIBURager, txtEstimatedIBUTinseth);
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }

    }

    public void onSaveInstanceState(Bundle savedState) {
        //super.onSaveInstanceState(savedState);

        if (hopItemAdapter != null) {
            ArrayList<IBUData> values = hopItemAdapter.getValues();
            savedState.putParcelableArrayList(ConstStrings.IBU_DATA, values);
            super.onSaveInstanceState(savedState);
        }
    }
}
