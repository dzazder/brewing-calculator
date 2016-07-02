package dev.lampart.bartosz.brewingcalculator.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import dev.lampart.bartosz.brewingcalculator.MainActivity;
import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.adapters.HomeMenuItemAdapter;
import dev.lampart.bartosz.brewingcalculator.dicts.DictFragment;

public class FragmentHome extends Fragment implements View.OnClickListener {

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle(R.string.app_name);

        ListView menuList = (ListView)view.findViewById(R.id.view_home_menu);

        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile" };

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final HomeMenuItemAdapter adapter = new HomeMenuItemAdapter(getActivity(),  list);
        menuList.setAdapter(adapter);

        Log.d("Home", "Menu list count: " + menuList.getCount());
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                Toast t = Toast.makeText(getActivity(), item, Toast.LENGTH_LONG);
                t.show();
            }

        });

        Button btn_fragment_ogplato = (Button)view.findViewById(R.id.btn_fragment_og_plato);
        btn_fragment_ogplato.setOnClickListener(this);

        Button btn_fragment_alcohol = (Button)view.findViewById(R.id.btn_fragment_alcohol);
        btn_fragment_alcohol.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        Log.d("CLICK", "onClickMethod");
        switch (view.getId()) {
            case R.id.btn_fragment_og_plato:
                Log.d("CLICK", "switch fragment to ogplato CLICK");
                ((MainActivity)getActivity()).switchFragment(DictFragment.FRAGMENT_OG_PLATO);
                break;
            case R.id.btn_fragment_alcohol:
                Log.d("CLICK", "switch fragment to alcohol CLICK");
                ((MainActivity)getActivity()).switchFragment(DictFragment.FRAGMENT_ALCOHOL);
                break;
        }
    }
}
