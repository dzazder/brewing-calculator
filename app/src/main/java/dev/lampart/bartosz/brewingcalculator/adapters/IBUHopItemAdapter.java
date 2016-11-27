package dev.lampart.bartosz.brewingcalculator.adapters;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.dicts.HopType;
import dev.lampart.bartosz.brewingcalculator.dicts.WeightUnit;
import dev.lampart.bartosz.brewingcalculator.entities.IBUData;
import dev.lampart.bartosz.brewingcalculator.helpers.NumberFormatter;
import dev.lampart.bartosz.brewingcalculator.helpers.spinner.SpinnerWeightUnitHelper;
import dev.lampart.bartosz.brewingcalculator.listeners.IEditTextTextChangedListener;
import dev.lampart.bartosz.brewingcalculator.listeners.IOnItemSelectedListener;

/**
 * Created by bartek on 22.10.2016.
 */
public class IBUHopItemAdapter extends ArrayAdapter<IBUData> {
    private Activity context;
    private ArrayList<IBUData> ibuData;
    private TextView txtAlpha;
    private TextView txtWeight;
    private TextView txtTime;
    private TextView spWeightUnit;
    private TextView spHopType;


    private IEditTextTextChangedListener mTextChangedListener = null;
    private IOnItemSelectedListener mOnItemSelectedListener = null;

    public IBUHopItemAdapter(Activity context, ArrayList<IBUData> resource,
                             IEditTextTextChangedListener iEditTextTextChangedListener,
                             IOnItemSelectedListener iOnItemSelectedListener) {
        super(context, R.layout.ibu_hop_item, resource);
        this.context = context;
        this.ibuData = resource;
        mTextChangedListener = iEditTextTextChangedListener;
        mOnItemSelectedListener = iOnItemSelectedListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            rowView =  layoutInflater.inflate(R.layout.ibu_hop_item, null, true);
        }

        txtAlpha = (TextView)rowView.findViewById(R.id.txt_ibu_alpha);
        txtAlpha.setTag(position);
        txtAlpha.setText(String.valueOf(ibuData.get(position).getAlpha()));

        txtWeight = (TextView)rowView.findViewById(R.id.txt_ibu_weight);
        txtWeight.setTag(position);
        txtWeight.setText(String.valueOf(ibuData.get(position).getWeight()));

        txtTime = (TextView)rowView.findViewById(R.id.txt_ibu_time);
        txtTime.setTag(position);
        txtTime.setText(String.valueOf(ibuData.get(position).getTime()));

        spWeightUnit = (TextView) rowView.findViewById(R.id.sp_hop_weight_unit);
        spWeightUnit.setTag(position);
        spWeightUnit.setText(ibuData.get(position).getWeightUnit().toString());

        spHopType = (TextView) rowView.findViewById(R.id.sp_hop_type);
        spHopType.setTag(position);
        spHopType.setText(ibuData.get(position).getHopType().toString());

        return rowView;
    }

    public void updateDataSet(IBUData ibu) {
        Log.d("IBU", "update data set");
        this.ibuData.add(ibu);
        notifyDataSetChanged();
    }
}
