package dev.lampart.bartosz.brewingcalculator.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import dev.lampart.bartosz.brewingcalculator.dicts.WeightUnit;
import dev.lampart.bartosz.brewingcalculator.entities.IBUData;
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
        return data;
    }

    private void hideDialog() {
        getDialog().hide();
    }
}
