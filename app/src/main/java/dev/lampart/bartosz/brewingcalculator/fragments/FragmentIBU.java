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
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.calculators.IBUCalc;
import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.HopType;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.WeightUnit;
import dev.lampart.bartosz.brewingcalculator.entities.IBUData;
import dev.lampart.bartosz.brewingcalculator.global.AppConfiguration;
import dev.lampart.bartosz.brewingcalculator.helpers.NumberFormatter;

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

    private static final String LAY_TYPE_HOP = "layout_hop";
    private static final String TXT_TYPE_ALPHA = "txt_alpha";
    private static final String TXT_TYPE_WEIGHT = "txt_weight";
    private static final String TXT_TYPE_TIME = "txt_time";
    private static final String SP_WEIGHT_UNIT = "sp_weight_unit";
    private static final String SP_HOP_TYPE = "sp_hop_type";

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
                ArrayList<LinearLayout> layouts = createHopLine();
                for (LinearLayout lay: layouts) {
                    mainLayout.addView(lay);
                }
                if (noHopLayout.getVisibility() == View.VISIBLE) {
                    noHopLayout.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

    private ArrayList<LinearLayout> createHopLine() {
        ArrayList<LinearLayout> result = new ArrayList<>();

        //full line BLACK
        LinearLayout layFullLine = createLinearLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.VERTICAL);

        // left half BROWN
        LinearLayout layLeftHalf = createLinearLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.HORIZONTAL);

        // alfa & weight GREEN
        LinearLayout layHopInfo = createLinearLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.VERTICAL);

        // alfa BLUE: label alpha, textbox alpha
        LinearLayout layHopAlpha = createLinearLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.HORIZONTAL);

        // weight BLUE: label weight, textbox weight
        LinearLayout layHopWeight = createLinearLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.HORIZONTAL);

        // hop type GREEN: label type, spinner type
        LinearLayout layHopType = createLinearLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.HORIZONTAL);

        // right half BROWN: lay green, remove button
        LinearLayout layRightHalf = createLinearLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.VERTICAL);

        // time GREEN: label time, textbox time
        LinearLayout layHopTime = createLinearLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.HORIZONTAL);

        //-------
        LinearLayout.LayoutParams lp_button = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);

        TextView lblAlpha = new TextView(getContext());
        lblAlpha.setText(getResources().getString(R.string.lbl_alpha_acids));

        TextView lblWeight = new TextView(getContext());
        lblAlpha.setText(getResources().getString(R.string.lbl_alpha_acids));

        TextView lblType = new TextView(getContext());
        lblAlpha.setText(getResources().getString(R.string.lbl_alpha_acids));

        TextView lblTime = new TextView(getContext());
        lblAlpha.setText(getResources().getString(R.string.lbl_alpha_acids));

        EditText txtAlfa = new EditText(new ContextThemeWrapper(getContext(), R.style.StyleEditText_CalcFieldSmall));
        txtAlfa.setBackgroundResource(R.drawable.calc_field);
        txtAlfa.setLayoutParams(lp_button);
        txtAlfa.setHint(getString(R.string.lbl_alpha_acids));
        txtAlfa.setTag(TXT_TYPE_ALPHA);
        txtAlfa.addTextChangedListener(new TextWatcher() {
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

        EditText txtWeight = new EditText(new ContextThemeWrapper(getContext(), R.style.StyleEditText_CalcFieldSmall));
        txtWeight.setBackgroundResource(R.drawable.calc_field);
        txtWeight.setLayoutParams(lp_button);
        txtWeight.setHint(getString(R.string.lbl_hop_weight));
        txtWeight.setTag(TXT_TYPE_WEIGHT);
        txtWeight.addTextChangedListener(new TextWatcher() {
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

        Spinner spWeightUnit = new Spinner(getContext());
        ArrayAdapter<CharSequence> spinnerArrayAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.weight_light_unit, android.R.layout.simple_spinner_dropdown_item);
        spWeightUnit.setAdapter(spinnerArrayAdapter);
        spWeightUnit.setTag(SP_WEIGHT_UNIT);
        WeightUnit defWeightUnit = WeightUnit.valueOf(AppConfiguration.getInstance().defaultSettings.getDefWeightUnit().toString());
        String valSelectedWeightUnit = "";
        switch (defWeightUnit) {
            case OZ: valSelectedWeightUnit = getActivity().getResources().getString(R.string.weight_unit_oz); break;
            case G: valSelectedWeightUnit = getActivity().getResources().getString(R.string.weight_unit_g); break;
        }
        int spinnerTempPosition = spinnerArrayAdapter.getPosition(valSelectedWeightUnit);
        spWeightUnit.setSelection(spinnerTempPosition);
        spWeightUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                calculateIBU(txtPrimingSize, txtGravity, spSizeUnit, spGravityUnit,
                        txtEstimatedIBURager, txtEstimatedIBUTinseth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Spinner spHopTypes = new Spinner(getContext());
        ArrayAdapter<CharSequence> spHopTypesArrayAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.hop_types, android.R.layout.simple_spinner_dropdown_item);
        spHopTypes.setAdapter(spHopTypesArrayAdapter);
        spHopTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                calculateIBU(txtPrimingSize, txtGravity, spSizeUnit, spGravityUnit,
                        txtEstimatedIBURager, txtEstimatedIBUTinseth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        EditText txtMinutes = new EditText(new ContextThemeWrapper(getContext(), R.style.StyleEditText_CalcFieldSmall));
        txtMinutes.setBackgroundResource(R.drawable.calc_field);
        txtMinutes.setLayoutParams(lp_button);
        txtMinutes.setHint(getString(R.string.lbl_boiling_time));
        txtMinutes.setTag( TXT_TYPE_TIME);
        txtMinutes.addTextChangedListener(new TextWatcher() {
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

        // -----
        layHopAlpha.addView(lblAlpha);
        layHopAlpha.addView(txtAlfa);

        layHopWeight.addView(lblWeight);
        layHopWeight.addView(txtWeight);
        layHopWeight.addView(spWeightUnit);

        layHopType.addView(lblType);
        layHopType.addView(spHopTypes);

        layHopTime.addView(lblTime);
        layHopTime.addView(txtMinutes);

        // -----
        layRightHalf.addView(layHopTime);
        layHopInfo.addView(layHopAlpha);
        layHopInfo.addView(layHopWeight);
        layLeftHalf.addView(layHopInfo);
        layLeftHalf.addView(layHopType);
        layFullLine.addView(layLeftHalf);
        layFullLine.addView(layRightHalf);

        result.add(layFullLine);

        return result;
    }


    private LinearLayout createLinearLayout(int width, int height, int orientation) {
        return createLinearLayout(width, height, orientation, getResources().getDrawable(R.drawable.empty_divider_vertical_small),
                LinearLayout.SHOW_DIVIDER_MIDDLE, (int)getResources().getDimension(R.dimen.padding_small),
                (int)getResources().getDimension(R.dimen.padding_small),
                (int)getResources().getDimension(R.dimen.padding_small),
                (int)getResources().getDimension(R.dimen.padding_small));
    }

    private LinearLayout createLinearLayout(int width, int height, int orientation, Drawable divider,
                                            int dividerShowType, int paddingLeft, int paddingTop,
                                            int paddingRight, int paddingBottom) {

        LinearLayout ll = new LinearLayout(getContext());
        ll.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        ll.setOrientation(orientation);
        ll.setDividerDrawable(divider);
        ll.setShowDividers(dividerShowType);
        ll.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

        return ll;
    }

    private ArrayList<LinearLayout> createHopLin() {
        ArrayList<LinearLayout> result = new ArrayList<>();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams lp_button = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);

        LinearLayout layLabels = new LinearLayout(getContext());
        layLabels.setLayoutParams(lp);
        layLabels.setOrientation(LinearLayout.HORIZONTAL);
        layLabels.setDividerDrawable(getResources().getDrawable(R.drawable.empty_divider_vertical_small));
        layLabels.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        layLabels.setPadding((int)getResources().getDimension(R.dimen.padding_small),
                (int)getResources().getDimension(R.dimen.padding_small),
                (int)getResources().getDimension(R.dimen.padding_small),
                (int)getResources().getDimension(R.dimen.padding_small));

        TextView lblAlpha = new TextView(getContext());
        lblAlpha.setText(getResources().getString(R.string.lbl_alpha_acids));
        layLabels.addView(lblAlpha);



        LinearLayout lay = new LinearLayout(getContext());
        lay.setLayoutParams(lp);
        lay.setTag(LAY_TYPE_HOP);
        lay.setOrientation(LinearLayout.HORIZONTAL);
        lay.setDividerDrawable(getResources().getDrawable(R.drawable.empty_divider_vertical_small));
        lay.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        lay.setPadding((int)getResources().getDimension(R.dimen.padding_small),
                (int)getResources().getDimension(R.dimen.padding_small),
                (int)getResources().getDimension(R.dimen.padding_small),
                (int)getResources().getDimension(R.dimen.padding_small));

        EditText txtAlfa = new EditText(new ContextThemeWrapper(getContext(), R.style.StyleEditText_CalcFieldSmall));
        txtAlfa.setBackgroundResource(R.drawable.calc_field);
        txtAlfa.setLayoutParams(lp_button);
        txtAlfa.setHint(getString(R.string.lbl_alpha_acids));

        txtAlfa.setTag(TXT_TYPE_ALPHA);
        txtAlfa.addTextChangedListener(new TextWatcher() {
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

        EditText txtAmount = new EditText(new ContextThemeWrapper(getContext(), R.style.StyleEditText_CalcFieldSmall));
        txtAmount.setBackgroundResource(R.drawable.calc_field);
        txtAmount.setLayoutParams(lp_button);
        txtAmount.setHint(getString(R.string.lbl_hop_weight));
        txtAmount.setTag(TXT_TYPE_WEIGHT);
        txtAmount.addTextChangedListener(new TextWatcher() {
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

        Spinner spWeightUnit = new Spinner(getContext());
        ArrayAdapter<CharSequence> spinnerArrayAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.weight_light_unit, android.R.layout.simple_spinner_dropdown_item);
        spWeightUnit.setAdapter(spinnerArrayAdapter);
        spWeightUnit.setTag(SP_WEIGHT_UNIT);
        WeightUnit defWeightUnit = WeightUnit.valueOf(AppConfiguration.getInstance().defaultSettings.getDefWeightUnit().toString());
        String valSelectedWeightUnit = "";
        switch (defWeightUnit) {
            case OZ: valSelectedWeightUnit = getActivity().getResources().getString(R.string.weight_unit_oz); break;
            case G: valSelectedWeightUnit = getActivity().getResources().getString(R.string.weight_unit_g); break;
        }
        int spinnerTempPosition = spinnerArrayAdapter.getPosition(valSelectedWeightUnit);
        spWeightUnit.setSelection(spinnerTempPosition);
        spWeightUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                calculateIBU(txtPrimingSize, txtGravity, spSizeUnit, spGravityUnit,
                        txtEstimatedIBURager, txtEstimatedIBUTinseth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        Spinner spHopTypes = new Spinner(getContext());
        ArrayAdapter<CharSequence> spHopTypesArrayAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.hop_types, android.R.layout.simple_spinner_dropdown_item);
        spHopTypes.setAdapter(spHopTypesArrayAdapter);
        spHopTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                calculateIBU(txtPrimingSize, txtGravity, spSizeUnit, spGravityUnit,
                        txtEstimatedIBURager, txtEstimatedIBUTinseth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        EditText txtMinutes = new EditText(new ContextThemeWrapper(getContext(), R.style.StyleEditText_CalcFieldSmall));
        txtMinutes.setBackgroundResource(R.drawable.calc_field);
        txtMinutes.setLayoutParams(lp_button);
        txtMinutes.setHint(getString(R.string.lbl_boiling_time));
        txtMinutes.setTag( TXT_TYPE_TIME);
        txtMinutes.addTextChangedListener(new TextWatcher() {
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

        lay.addView(txtAlfa);
        lay.addView(txtAmount);
        lay.addView(spWeightUnit);
        lay.addView(spHopTypes);
        lay.addView(txtMinutes);

        result.add(layLabels);
        result.add(lay);

        return result;
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

            for (int i = 0; i < mainLayout.getChildCount(); i++) {
                View nextChild = mainLayout.getChildAt(i);
                if (nextChild instanceof LinearLayout && nextChild.getTag() != null
                        && nextChild.getTag().toString() == LAY_TYPE_HOP) {

                    IBUData ibuData = new IBUData();

                    for (int j = 0; j <= ((LinearLayout) nextChild).getChildCount(); j++) {
                        View innerChild = ((LinearLayout) nextChild).getChildAt(j);
                        if (innerChild instanceof Spinner) {
                            Object tagValue = innerChild.getTag();
                            String val = ((Spinner)innerChild).getSelectedItem().toString();
                            if (tagValue != null) {
                                switch (tagValue.toString()) {
                                    case SP_WEIGHT_UNIT:
                                        if (val == getActivity().getResources().getString(R.string.weight_unit_g)) {
                                            ibuData.setWeightUnit(WeightUnit.G);
                                        }
                                        if (val == getActivity().getResources().getString(R.string.weight_unit_oz)) {
                                            ibuData.setWeightUnit(WeightUnit.OZ);
                                        }
                                        break;
                                    case SP_HOP_TYPE:
                                        if (val == getActivity().getResources().getString(R.string.hop_type_whole_hops)) {
                                            ibuData.setHopType(HopType.WHOLE_HOPS);
                                        }
                                        if (val == getActivity().getResources().getString(R.string.hop_type_pellet)) {
                                            ibuData.setHopType(HopType.PELLETS);
                                        }
                                        break;
                                }
                            }
                        }
                        if (innerChild instanceof EditText) {
                            if (NumberFormatter.isNumeric(((EditText) innerChild).getText().toString())) {
                                double txtValue = Double.parseDouble(((EditText) innerChild).getText().toString());

                                Object tagValue = innerChild.getTag();
                                if (tagValue != null) {
                                    switch (tagValue.toString()) {
                                        case TXT_TYPE_ALPHA:
                                            ibuData.setAlpha(txtValue);
                                            break;
                                        case TXT_TYPE_WEIGHT:
                                            ibuData.setWeight(txtValue);
                                            break;
                                        case TXT_TYPE_TIME:
                                            ibuData.setTime(txtValue);
                                            break;
                                    }
                                }
                            }
                        }
                    }

                    ibuDatas.add(ibuData);
                }
            }

            Log.d("IBU", "Ibus data length: " + ibuDatas.size());
            for (IBUData ibu : ibuDatas) {
                Log.d("IBU", ibu.getAlpha() + ", " + ibu.getWeight() + ", " + ibu.getTime());
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
