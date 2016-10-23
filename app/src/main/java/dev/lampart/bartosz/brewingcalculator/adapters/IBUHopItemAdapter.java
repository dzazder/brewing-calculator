package dev.lampart.bartosz.brewingcalculator.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.entities.IBUData;
import dev.lampart.bartosz.brewingcalculator.helpers.NumberFormatter;
import dev.lampart.bartosz.brewingcalculator.listeners.IEditTextTextChangedListener;

/**
 * Created by bartek on 22.10.2016.
 */
public class IBUHopItemAdapter extends ArrayAdapter<IBUData> {
    private Activity context;
    private ArrayList<IBUData> ibuData;

    private IEditTextTextChangedListener mTextChangedListener = null;

    public IBUHopItemAdapter(Activity context, ArrayList<IBUData> resource,
                             IEditTextTextChangedListener listener) {
        super(context, R.layout.ibu_hop_item, resource);
        this.context = context;
        this.ibuData = resource;
        mTextChangedListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            rowView =  layoutInflater.inflate(R.layout.ibu_hop_item, null, true);
        }

        final EditText txtAlpha = (EditText)rowView.findViewById(R.id.txt_ibu_alpha);
        txtAlpha.setTag(position);
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
        txtWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mTextChangedListener != null) {
                    mTextChangedListener.afterTextChanged((Integer)txtWeight.getTag());
                }
            }
        });

        final EditText txtTime = (EditText)rowView.findViewById(R.id.txt_ibu_time);
        txtTime.setTag(position);
        txtTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mTextChangedListener != null) {
                    mTextChangedListener.afterTextChanged((Integer)txtTime.getTag());
                }
            }
        });





//        TextView tvLanguage = (TextView) rowView.findViewById(R.id.tvLanguage);
//        TextView tvItemNumber = (TextView) rowView.findViewById(R.id.tvItemNumber);

//        tvItemNumber.setText(Integer.toString(position));
//        tvLanguage.setText(languages[position]);

        return rowView;
    }

    public void updateDataSet(IBUData ibu) {
        this.ibuData.add(ibu);
        notifyDataSetChanged();
    }
}
