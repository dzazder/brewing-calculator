package dev.lampart.bartosz.brewingcalculator;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import dev.lampart.bartosz.brewingcalculator.dicts.DictFragment;
import dev.lampart.bartosz.brewingcalculator.fragments.FragmentAlcohol;
import dev.lampart.bartosz.brewingcalculator.fragments.FragmentCarbonation;
import dev.lampart.bartosz.brewingcalculator.fragments.FragmentHome;
import dev.lampart.bartosz.brewingcalculator.fragments.FragmentHydrometer;
import dev.lampart.bartosz.brewingcalculator.fragments.FragmentIBU;
import dev.lampart.bartosz.brewingcalculator.fragments.FragmentSettings;
import dev.lampart.bartosz.brewingcalculator.fragments.FragmentSgPlato;
import dev.lampart.bartosz.brewingcalculator.fragments.FragmentExtractCorrection;
import dev.lampart.bartosz.brewingcalculator.fragments.FragmentYeasts;
import dev.lampart.bartosz.brewingcalculator.global.ConstStrings;

public class MainActivity extends AppCompatActivity {

    public static FragmentManager fragmentManager;
    private boolean showBackStackButton;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BrewingCalculatorApplication.setContext(this);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            switchFragment(DictFragment.FRAGMENT_HOME);
        }
        else {
            showBackStackButton = savedInstanceState.getBoolean(ConstStrings.BACK_BUTTON_VISIBILITY);
        }

        fragmentManager = getSupportFragmentManager();
        addOnBackStackChangedListenerToFragmentManager();

        final Drawable upArrow = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_back);
        upArrow.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccentDark), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(showBackStackButton);

        //MobileAds.initialize(this, "ca-app-pub-4293040940774831~5599977907");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(ConstStrings.BACK_BUTTON_VISIBILITY, showBackStackButton);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.menu_item_settings:

                //FragmentManager fm = getSupportFragmentManager();
                FragmentSettings fs = new FragmentSettings();
                fs.show(getFragmentManager(), "fragment_settings");
                break;

            default:
                onBackPressed();
                break;
        }
        return true;
    }

    /**
     * It runs selected fragment
     * @param fragment Id of fragment to run, ids are saved in MainActivity fields
     */
    public void switchFragment(int fragment) {
        Fragment newFragment = null;
        switch (fragment) {
            case DictFragment.FRAGMENT_OG_PLATO:
                newFragment = new FragmentSgPlato();
                break;
            case DictFragment.FRAGMENT_ALCOHOL:
                newFragment = new FragmentAlcohol();
                break;
            case DictFragment.FRAGMENT_CARBONATION:
                newFragment = new FragmentCarbonation();
                break;
            case DictFragment.FRAGMENT_YEASTS:
                newFragment = new FragmentYeasts();
                break;
            case DictFragment.FRAGMENT_HYDROMETER:
                newFragment = new FragmentHydrometer();
                break;
            case DictFragment.FRAGMENT_IBU:
                newFragment = new FragmentIBU();
                break;
            case DictFragment.FRAGMENT_EXTRACT_CORRECTION:
                newFragment = new FragmentExtractCorrection();
                break;
            default:
                newFragment = new FragmentHome();
                break;
        }
        if (!isFinishing()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.layout_fragment_area, newFragment, ConstStrings.CURRENT_FRAGMENT);
            transaction.addToBackStack(null);

            transaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(ConstStrings.CURRENT_FRAGMENT);
        Log.d("BACK", "Back pressed");
        if (currentFragment != null) {
            if (currentFragment instanceof FragmentHome) {
                Log.d("BACK", "Finish");
                finish();
                System.exit(0);
            }
            if (currentFragment instanceof FragmentSgPlato) {
                switchFragment(DictFragment.FRAGMENT_HOME);
            }
            if (currentFragment instanceof  FragmentAlcohol) {
                switchFragment(DictFragment.FRAGMENT_HOME);
            }
            if (currentFragment instanceof  FragmentCarbonation) {
                switchFragment(DictFragment.FRAGMENT_HOME);
            }
            if (currentFragment instanceof  FragmentYeasts) {
                switchFragment(DictFragment.FRAGMENT_HOME);
            }
            if (currentFragment instanceof FragmentHydrometer) {
                switchFragment(DictFragment.FRAGMENT_HOME);
            }
            if (currentFragment instanceof FragmentIBU) {
                switchFragment(DictFragment.FRAGMENT_HOME);
            }
            if (currentFragment instanceof FragmentExtractCorrection) {
                switchFragment(DictFragment.FRAGMENT_HOME);
            }
        } else {
            super.onBackPressed();
        }
    }


    private void addOnBackStackChangedListenerToFragmentManager() {
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {

            @Override
            public void onBackStackChanged() {
                if (fragmentManager.findFragmentByTag(ConstStrings.CURRENT_FRAGMENT) instanceof FragmentHome) {
                    showBackStackButton = false;
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                } else {
                    showBackStackButton = true;
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
            }
        });
    }


}
