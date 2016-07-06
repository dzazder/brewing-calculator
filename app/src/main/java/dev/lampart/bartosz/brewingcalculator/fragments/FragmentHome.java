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
import dev.lampart.bartosz.brewingcalculator.entities.MainMenuItem;
import dev.lampart.bartosz.brewingcalculator.global.MainMenu;

public class FragmentHome extends Fragment {

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle(R.string.app_name);

        ListView menuList = (ListView)view.findViewById(R.id.view_home_menu);

        final HomeMenuItemAdapter adapter = new HomeMenuItemAdapter(getActivity(), MainMenu.getInstance().items);
        menuList.setAdapter(adapter);

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final MainMenuItem item = (MainMenuItem) parent.getItemAtPosition(position);
                ((MainActivity)getActivity()).switchFragment(item.getFragment());
            }
        });


        return view;
    }
}
