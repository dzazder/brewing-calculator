package dev.lampart.bartosz.brewingcalculator.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dev.lampart.bartosz.brewingcalculator.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHydrometer extends Fragment {


    public FragmentHydrometer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View rootView = inflater.inflate(R.layout.fragment_hydrometer, container, false);
        getActivity().setTitle(getResources().getString(R.string.title_hydrometer_adjustment));


        return rootView;
    }

}
