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
public class FragmentOgPlato extends Fragment {


    public FragmentOgPlato() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_og_plato, container, false);
    }

}
