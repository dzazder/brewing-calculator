package dev.lampart.bartosz.brewingcalculator.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
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
import dev.lampart.bartosz.brewingcalculator.dicts.HopType;
import dev.lampart.bartosz.brewingcalculator.dicts.WeightUnit;
import dev.lampart.bartosz.brewingcalculator.entities.IBUData;

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

        //ListView listHops = (ListView)getDialog().findViewById(R.id.lv_hops);
        //IBUHopItemAdapter hopsAdapter = (IBUHopItemAdapter) listHops.getAdapter();
        //hopsAdapter.updateDataSet(new IBUData(12, 5, WeightUnit.G, 60, HopType.WHOLE_HOPS));

        hideDialog();

        Toast.makeText(getActivity(), "Hop added", Toast.LENGTH_SHORT);
    }

    private void hideDialog() {
        getDialog().hide();
    }
}
