package dev.lampart.bartosz.brewingcalculator.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import dev.lampart.bartosz.brewingcalculator.BrewingCalculatorApplication;
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

    public void sendMail(View v) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"dzazder+brewingcalculator@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "[Brewing Calculator] Report issue");
        i.putExtra(Intent.EXTRA_TEXT, "");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this.getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
