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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.dicts.HopType;
import dev.lampart.bartosz.brewingcalculator.dicts.RequestCodes;
import dev.lampart.bartosz.brewingcalculator.dicts.WeightUnit;
import dev.lampart.bartosz.brewingcalculator.entities.IBUData;
import dev.lampart.bartosz.brewingcalculator.fragments.FragmentIBU;
import dev.lampart.bartosz.brewingcalculator.helpers.NumberFormatter;
import dev.lampart.bartosz.brewingcalculator.helpers.spinner.SpinnerWeightUnitHelper;
import dev.lampart.bartosz.brewingcalculator.listeners.IEditTextTextChangedListener;
import dev.lampart.bartosz.brewingcalculator.listeners.IOnItemSelectedListener;

/**
 * Created by bartek on 22.10.2016.
 */
public class IBUHopItemAdapter extends ArrayAdapter<IBUData> implements View.OnClickListener {
    private Activity context;
    private ArrayList<IBUData> ibuData;
    private TextView txtAlpha;
    private TextView txtWeight;
    private TextView txtTime;
    private TextView spWeightUnit;
    private TextView spHopType;
    private ImageButton btnEditHop;
    private ImageButton btnRemoveHop;
    private FragmentIBU fragmentIBU;

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

        btnEditHop = (ImageButton) rowView.findViewById(R.id.ibtn_ibu_edit);
        btnEditHop.setTag(position);
        btnEditHop.setOnClickListener(this);

        btnRemoveHop = (ImageButton) rowView.findViewById(R.id.ibtn_ibu_remove);
        btnRemoveHop.setTag(position);
        btnRemoveHop.setOnClickListener(this);

        return rowView;
    }

    public void updateDataSet(IBUData ibu) {
        Log.d("IBU", "update data set");
        this.ibuData.add(ibu);
        notifyDataSetChanged();
    }

    public ArrayList<IBUData> getValues() {
        return ibuData;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_ibu_remove:
                ImageButton btnRemoveHop = (ImageButton)view;
                int indexToRemove = (int)btnRemoveHop.getTag();
                if (this.ibuData.size() > indexToRemove) {
                    this.ibuData.remove(indexToRemove);
                    notifyDataSetChanged();
                    fragmentIBU.updateIBUControls();
                }
                break;
            case R.id.ibtn_ibu_edit:
                ImageButton btnEditHop = (ImageButton)view;
                int indexToEdit = (int)btnEditHop.getTag();
                if (this.ibuData.size() > indexToEdit) {
                    getFragmentIBU().showAddHopDialog(RequestCodes.EDIT_HOP_REQUEST_CODE, ibuData.get(indexToEdit));
                }
                break;
        }
    }

    public void setFragmentIBU(FragmentIBU fragmentIBU) {
        this.fragmentIBU = fragmentIBU;
    }

    public FragmentIBU getFragmentIBU() {
        return fragmentIBU;
    }
}