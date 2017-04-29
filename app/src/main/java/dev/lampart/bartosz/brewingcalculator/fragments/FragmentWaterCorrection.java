package dev.lampart.bartosz.brewingcalculator.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import dev.lampart.bartosz.brewingcalculator.R;

/**
 * Created by bartek on 29.04.2017.
 */
public class FragmentWaterCorrection extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_water_correction, container, false);
        getActivity().setTitle(getResources().getString(R.string.title_water_correction));
        initControls(view);

        return view;
    }

    private void initControls(View view) {

    }
}
