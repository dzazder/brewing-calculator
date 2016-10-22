package dev.lampart.bartosz.brewingcalculator.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.entities.IBUData;

/**
 * Created by bartek on 22.10.2016.
 */
public class IBUHopItemAdapter extends ArrayAdapter<IBUData> {
    private Activity context;
    private ArrayList<IBUData> ibuData;

    public IBUHopItemAdapter(Activity context, ArrayList<IBUData> resource) {
        super(context, R.layout.ibu_hop_item, resource);
        this.context = context;
        this.ibuData = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            rowView =  layoutInflater.inflate(R.layout.ibu_hop_item, null, true);
        }



//        TextView tvLanguage = (TextView) rowView.findViewById(R.id.tvLanguage);
//        TextView tvItemNumber = (TextView) rowView.findViewById(R.id.tvItemNumber);

//        tvItemNumber.setText(Integer.toString(position));
//        tvLanguage.setText(languages[position]);

        return rowView;
    }
}
