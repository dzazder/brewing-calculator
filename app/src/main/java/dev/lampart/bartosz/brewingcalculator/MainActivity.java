package dev.lampart.bartosz.brewingcalculator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.xml.sax.DTDHandler;

import dev.lampart.bartosz.brewingcalculator.dicts.DictFragment;
import dev.lampart.bartosz.brewingcalculator.fragments.FragmentHome;
import dev.lampart.bartosz.brewingcalculator.fragments.FragmentSgPlato;

public class MainActivity extends AppCompatActivity {

    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            switchFragment(DictFragment.FRAGMENT_HOME);
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
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("FRAGMENT_DISPLAYED");

        if (currentFragment != null) {
            if (currentFragment instanceof FragmentHome) {
                finish();
            }
            if (currentFragment instanceof FragmentSgPlato) {
                switchFragment(DictFragment.FRAGMENT_HOME);
            }
        }
        else {
            super.onBackPressed();
        }
    }

}
