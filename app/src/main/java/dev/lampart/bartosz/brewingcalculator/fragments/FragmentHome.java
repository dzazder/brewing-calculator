package dev.lampart.bartosz.brewingcalculator.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import dev.lampart.bartosz.brewingcalculator.MainActivity;
import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.dicts.DictFragment;

public class FragmentHome extends Fragment implements View.OnClickListener {

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button btn_fragment_ogplato = (Button)view.findViewById(R.id.btn_fragment_og_plato);
        btn_fragment_ogplato.setOnClickListener(this);

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
        }
    }
}
