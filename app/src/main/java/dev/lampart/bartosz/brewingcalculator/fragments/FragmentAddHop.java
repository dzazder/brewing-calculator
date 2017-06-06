package dev.lampart.bartosz.brewingcalculator.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.adapters.IBUHopItemAdapter;
import dev.lampart.bartosz.brewingcalculator.dicts.HopIntentValues;
import dev.lampart.bartosz.brewingcalculator.dicts.HopType;
import dev.lampart.bartosz.brewingcalculator.dicts.RequestCodes;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.WeightUnit;
import dev.lampart.bartosz.brewingcalculator.entities.IBUData;
import dev.lampart.bartosz.brewingcalculator.global.AppConfiguration;
import dev.lampart.bartosz.brewingcalculator.global.ConstStrings;
import dev.lampart.bartosz.brewingcalculator.helpers.NumberFormatter;

/**
 * Created by bartek on 25.10.2016.
 */
public class FragmentAddHop extends DialogFragment implements View.OnClickListener {

    View dialogView;
    EditText txtAlpha;
    EditText txtWeight;
    EditText txtMinutes;
    Spinner spWeightUnit;
    Spinner spHopType;

    IBUData ibuData = null;
    int ibuIndex = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dialogView = inflater.inflate(R.layout.dialog_add_hop, container, false);
        //fragmentIBU = inflater.inflate(R.layout.fragment_ibu, container, false);

        txtAlpha = (EditText) dialogView.findViewById(R.id.txt_ibu_alpha);
        txtWeight = (EditText) dialogView.findViewById(R.id.txt_ibu_weight);
        txtMinutes = (EditText) dialogView.findViewById(R.id.txt_ibu_time);
        spWeightUnit = (Spinner) dialogView.findViewById(R.id.sp_hop_weight_unit);
        spHopType = (Spinner) dialogView.findViewById(R.id.sp_hop_type);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        Button btnCancel = (Button)dialogView.findViewById(R.id.btn_add_hop_cancel);
        btnCancel.setOnClickListener(this);

        Button btnSave = (Button)dialogView.findViewById(R.id.btn_add_hop_save);
        btnSave.setOnClickListener(this);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            ibuIndex = bundle.getInt(ConstStrings.ID);
            txtAlpha.setText(String.valueOf(bundle.getDouble(ConstStrings.ALPHA)));
            txtMinutes.setText(String.valueOf(bundle.getDouble(ConstStrings.MINUTES)));
            txtWeight.setText(String.valueOf(bundle.getDouble(ConstStrings.WEIGHT)));

            // set weight unit
            ArrayAdapter<CharSequence> weightUnit = ArrayAdapter.createFromResource(getActivity(),
                    R.array.weight_light_unit, android.R.layout.simple_spinner_item);
            final WeightUnit weightDefUnit = WeightUnit.valueOf(bundle.getString(ConstStrings.WEIGHT_UNIT));
            String valSelectedInWeightUnit = "";
            switch (weightDefUnit) {
                case OZ: valSelectedInWeightUnit = getActivity().getResources().getString(R.string.weight_unit_oz); break;
                case G: valSelectedInWeightUnit = getActivity().getResources().getString(R.string.weight_unit_g); break;
            }
            int spinnerWeightPosition = weightUnit.getPosition(valSelectedInWeightUnit);
            spWeightUnit.setSelection(spinnerWeightPosition);
            // end of set weight unit

            // set hop type
            ArrayAdapter<CharSequence> hopTypeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.hop_types,
                    android.R.layout.simple_spinner_item);
            HopType hopType = HopType.getEnum(bundle.getString(ConstStrings.HOP_TYPE));
            String valSelectedInSpinnerVol = "";
            switch (hopType) {
                case PELLETS: valSelectedInSpinnerVol = getActivity().getResources().getString(R.string.hop_type_pellet); break;
                case WHOLE_HOPS: valSelectedInSpinnerVol = getActivity().getResources().getString(R.string.hop_type_whole_hops); break;
            }
            int spinnerHopTypeIndex = hopTypeAdapter.getPosition(valSelectedInSpinnerVol);
            spHopType.setSelection(spinnerHopTypeIndex);
            // end of set hop type

        }

        return dialogView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_hop_cancel:
                hideDialog();
                break;
            case R.id.btn_add_hop_save:
                saveHop();
                break;
        }
    }

    private void saveHop() {
        hideDialog();

        Intent data = prepareHopIntentData();
        getTargetFragment().onActivityResult(getTargetRequestCode(), RequestCodes.RESULT_OK, data);
    }

    private Intent prepareHopIntentData() {
        Intent data= new Intent();

        if (NumberFormatter.isNumeric(txtAlpha.getText().toString())) {
            data.putExtra(HopIntentValues.ALPHA, Double.parseDouble(txtAlpha.getText().toString()));
        }

        if (NumberFormatter.isNumeric(txtMinutes.getText().toString())) {
            data.putExtra(HopIntentValues.BOILING_TIME, Double.parseDouble(txtMinutes.getText().toString()));
        }

        if (NumberFormatter.isNumeric(txtWeight.getText().toString())) {
            data.putExtra(HopIntentValues.WEIGHT, Double.parseDouble(txtWeight.getText().toString()));
        }

        data.putExtra(HopIntentValues.HOP_TYPE, spHopType.getSelectedItem().toString());
        data.putExtra(HopIntentValues.WEIGHT_UNIT, spWeightUnit.getSelectedItem().toString());
        data.putExtra(HopIntentValues.IBU_INDEX, ibuIndex);

        return data;
    }

    private void hideDialog() {
        onDestroyView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        try {
            Dialog dialog = getDialog();
            // handles https://code.google.com/p/android/issues/detail?id=17423
            if (dialog != null && getRetainInstance()) {
                dialog.setDismissMessage(null);
                dialog.dismiss();
            }

            this.dismiss(); // error
            //super.onDestroyView();
        }
        catch (Exception e) {
            // todo obsluga błędu
        }

    }
}
