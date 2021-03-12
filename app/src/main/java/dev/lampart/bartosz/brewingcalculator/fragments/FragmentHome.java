package dev.lampart.bartosz.brewingcalculator.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import dev.lampart.bartosz.brewingcalculator.BuildConfig;
import dev.lampart.bartosz.brewingcalculator.MainActivity;
import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.adapters.HomeMenuItemAdapter;
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

        ListView menuList = view.findViewById(R.id.view_home_menu);

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

        Button btnReportIssue = view.findViewById(R.id.btn_report_issue);
        btnReportIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });

        setVersionLabel(view);

        return view;
    }

    public void sendMail() {
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

    private void setVersionLabel(View view) {
        TextView versionName = view.findViewById(R.id.lbl_version);
        versionName.setText("v." + BuildConfig.VERSION_NAME);
    }
}
