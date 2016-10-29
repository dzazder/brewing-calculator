package dev.lampart.bartosz.brewingcalculator.adapters;

import android.app.Activity;
import android.content.Context;
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
import dev.lampart.bartosz.brewingcalculator.listeners.IEditTextTextChangedListener;
import dev.lampart.bartosz.brewingcalculator.listeners.IOnItemSelectedListener;

/**
 * Created by bartek on 22.10.2016.
 */
public class IBUHopItemAdapter extends ArrayAdapter<IBUData> {
    private Activity context;
    private ArrayList<IBUData> ibuData;

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
        Log.d("IBU", "get View");
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            rowView =  layoutInflater.inflate(R.layout.ibu_hop_item, null, true);
        }

        final EditText txtAlpha = (EditText)rowView.findViewById(R.id.txt_ibu_alpha);
        txtAlpha.setTag(position);
        txtAlpha.setText(String.valueOf(ibuData.get(position).getAlpha()));
        txtAlpha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (NumberFormatter.isNumeric(editable.toString())) {
                    int position = (Integer) txtAlpha.getTag();
                    ibuData.get(position).setAlpha(Double.parseDouble(editable.toString()));
                    if (mTextChangedListener != null) {
                        mTextChangedListener.afterTextChanged(position);
                    }
                }
            }
        });

        final EditText txtWeight = (EditText)rowView.findViewById(R.id.txt_ibu_weight);
        txtWeight.setTag(position);
        txtWeight.setText(String.valueOf(ibuData.get(position).getWeight()));
        txtWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (NumberFormatter.isNumeric(editable.toString())) {
                    int position = (Integer) txtWeight.getTag();
                    ibuData.get(position).setWeight(Double.parseDouble(editable.toString()));
                    if (mTextChangedListener != null) {
                        mTextChangedListener.afterTextChanged(position);
                    }
                }
            }
        });

        final EditText txtTime = (EditText)rowView.findViewById(R.id.txt_ibu_time);
        txtTime.setTag(position);
        txtTime.setText(String.valueOf(ibuData.get(position).getTime()));
        txtTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (NumberFormatter.isNumeric(editable.toString())) {
                    int position = (Integer) txtTime.getTag();
                    ibuData.get(position).setTime(Double.parseDouble(editable.toString()));
                    if (mTextChangedListener != null) {
                        mTextChangedListener.afterTextChanged(position);
                    }
                }
            }
        });

        final Spinner spWeightUnit = (Spinner)rowView.findViewById(R.id.sp_hop_weight_unit);
        spWeightUnit.setTag(position);
       
        spWeightUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int position = (Integer) spWeightUnit.getTag();
                String val = spWeightUnit.getSelectedItem().toString();
                if (val == context.getResources().getString(R.string.weight_unit_g)) {
                    ibuData.get(position).setWeightUnit(WeightUnit.G);
                }
                if (val == context.getResources().getString(R.string.weight_unit_oz)) {
                    ibuData.get(position).setWeightUnit(WeightUnit.OZ);
                }
                if (mOnItemSelectedListener != null) {
                    mOnItemSelectedListener.onItemSelected(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final Spinner spHopType = (Spinner)rowView.findViewById(R.id.sp_hop_type);
        spHopType.setTag(position);
        spHopType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int position = (Integer) spHopType.getTag();
                String val = spHopType.getSelectedItem().toString();
                if (val == context.getResources().getString(R.string.hop_type_whole_hops)) {
                    ibuData.get(position).setHopType(HopType.WHOLE_HOPS);
                }
                if (val == context.getResources().getString(R.string.hop_type_pellet)) {
                    ibuData.get(position).setHopType(HopType.PELLETS);
                }
                if (mOnItemSelectedListener != null) {
                    mOnItemSelectedListener.onItemSelected(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return rowView;
    }

    public void updateDataSet(IBUData ibu) {
        Log.d("IBU", "update data set");
        this.ibuData.add(ibu);
        notifyDataSetChanged();
    }
}
