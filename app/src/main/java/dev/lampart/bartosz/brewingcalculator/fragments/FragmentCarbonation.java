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
public class FragmentCarbonation extends Fragment {


    public FragmentCarbonation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_carbonation, container, false);

        getActivity().setTitle(getResources().getString(R.string.title_carbonation_calculator));

        return rootView;
    }

}
