package dev.lampart.bartosz.brewingcalculator.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import dev.lampart.bartosz.brewingcalculator.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentIBU extends Fragment {


    public FragmentIBU() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ibu, container, false);

        final LinearLayout mainLayout = (LinearLayout) view.findViewById(R.id.layout_ibu_main);
        final LinearLayout noHopLayout = (LinearLayout)view.findViewById(R.id.layout_noHopLabel);

        Button btnAddHop = (Button)view.findViewById(R.id.btn_add_hop);
        btnAddHop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainLayout.addView(createHopLine());
                if (noHopLayout.getVisibility() == View.VISIBLE) {
                    noHopLayout.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

    private LinearLayout createHopLine() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams lp_button = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);

        LinearLayout lay = new LinearLayout(getContext());
        lay.setLayoutParams(lp);
        lay.setOrientation(LinearLayout.HORIZONTAL);
        lay.setDividerDrawable(getResources().getDrawable(R.drawable.empty_divider_vertical));
        lay.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        lay.setPadding((int)getResources().getDimension(R.dimen.padding_small),
                (int)getResources().getDimension(R.dimen.padding_small),
                (int)getResources().getDimension(R.dimen.padding_small),
                (int)getResources().getDimension(R.dimen.padding_small));

        EditText txtAlfa = new EditText(new ContextThemeWrapper(getContext(), R.style.StyleEditText_CalcFieldSmall));
        txtAlfa.setBackgroundResource(R.drawable.calc_field);
        txtAlfa.setLayoutParams(lp_button);
        txtAlfa.setHint(getString(R.string.lbl_alpha_acids));

        EditText txtAmount = new EditText(new ContextThemeWrapper(getContext(), R.style.StyleEditText_CalcFieldSmall));
        txtAmount.setBackgroundResource(R.drawable.calc_field);
        txtAmount.setLayoutParams(lp_button);
        txtAmount.setHint(getString(R.string.lbl_hop_weight));

        EditText txtMinutes = new EditText(new ContextThemeWrapper(getContext(), R.style.StyleEditText_CalcFieldSmall));
        txtMinutes.setBackgroundResource(R.drawable.calc_field);
        txtMinutes.setLayoutParams(lp_button);
        txtMinutes.setHint(getString(R.string.lbl_boiling_time));

        lay.addView(txtAlfa);
        lay.addView(txtAmount);
        lay.addView(txtMinutes);

        return lay;
    }

}
