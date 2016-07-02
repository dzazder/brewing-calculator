package dev.lampart.bartosz.brewingcalculator.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.lampart.bartosz.brewingcalculator.R;

/**
 * Created by bartek on 02.07.2016.
 */
public class HomeMenuItemAdapter extends BaseAdapter {

    private Context context;
    private List<String> items;

    public HomeMenuItemAdapter(Context context, List<String> objects) {

        this.context = context;
        this.items = objects;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.home_menu_item, null);

            TextView txtSecond = (TextView)convertView.findViewById(R.id.secondLine);
            txtSecond.setText(items.get(position));
        }

        return convertView;
    }

}
