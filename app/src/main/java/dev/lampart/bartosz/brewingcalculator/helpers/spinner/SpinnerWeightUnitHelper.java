package dev.lampart.bartosz.brewingcalculator.helpers.spinner;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.adapters.IBUHopItemAdapter;
import dev.lampart.bartosz.brewingcalculator.dicts.WeightUnit;

/**
 * Created by bartek on 29.10.2016.
 */
public class SpinnerWeightUnitHelper {

    public static int getPosition(WeightUnit value, Context context) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.weight_light_unit, android.R.layout.simple_spinner_item);

        String selectedVal = "";
        switch (value) {
            case G: selectedVal =  context.getResources().getString(R.string.weight_unit_g); break;
            case OZ: selectedVal = context.getResources().getString(R.string.weight_unit_oz); break;
        }
        int spinnerPosition = adapter.getPosition(selectedVal);

        return spinnerPosition;
    }
}
