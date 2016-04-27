package dev.lampart.bartosz.brewingcalculator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import dev.lampart.bartosz.brewingcalculator.dicts.DictFragment;
import dev.lampart.bartosz.brewingcalculator.fragments.FragmentHome;
import dev.lampart.bartosz.brewingcalculator.fragments.FragmentSgPlato;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.layout_fragment_area, new FragmentHome(), "fragmentHome")
                    .commit();
        }
    }

    /**
     * It runs selected fragment
     * @param fragment Id of fragment to run, ids are saved in MainActivity fields
     */
    public void switchFragment(int fragment) {
        Fragment newFragment = null;
        switch (fragment) {
            case DictFragment.FRAGMENT_HOME: newFragment = new FragmentHome(); break;
            case DictFragment.FRAGMENT_OG_PLATO: newFragment = new FragmentSgPlato(); break;
            default: newFragment = new FragmentHome(); break;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.layout_fragment_area, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
            return;
        }

        super.onBackPressed();
    }
}
