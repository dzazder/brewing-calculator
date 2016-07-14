package dev.lampart.bartosz.brewingcalculator;

import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Locale;

import dev.lampart.bartosz.brewingcalculator.dbfile.FileDB;
import dev.lampart.bartosz.brewingcalculator.dicts.DictFragment;
import dev.lampart.bartosz.brewingcalculator.dicts.DictLanguages;
import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.entities.BCalcConf;
import dev.lampart.bartosz.brewingcalculator.entities.Language;
import dev.lampart.bartosz.brewingcalculator.fragments.FragmentAlcohol;
import dev.lampart.bartosz.brewingcalculator.fragments.FragmentCarbonation;
import dev.lampart.bartosz.brewingcalculator.fragments.FragmentHome;
import dev.lampart.bartosz.brewingcalculator.fragments.FragmentSettings;
import dev.lampart.bartosz.brewingcalculator.fragments.FragmentSgPlato;
import dev.lampart.bartosz.brewingcalculator.global.AppConfiguration;
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


        final Drawable upArrow = ContextCompat.getDrawable(getApplicationContext(), R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccentDark), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(showBackStackButton);

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

                FragmentManager fm = getSupportFragmentManager();
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
     *
     * @param fragment Id of fragment to run, ids are saved in MainActivity fields
     */
    public void switchFragment(int fragment) {
        Fragment newFragment = null;
        switch (fragment) {
            case DictFragment.FRAGMENT_HOME:
                newFragment = new FragmentHome();
                break;
            case DictFragment.FRAGMENT_OG_PLATO:
                newFragment = new FragmentSgPlato();
                break;
            case DictFragment.FRAGMENT_ALCOHOL:
                newFragment = new FragmentAlcohol();
                break;
            case DictFragment.FRAGMENT_CARBONATION:
                newFragment = new FragmentCarbonation();
                break;
            default:
                newFragment = new FragmentHome();
                break;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.layout_fragment_area, newFragment, ConstStrings.CURRENT_FRAGMENT);
        transaction.addToBackStack(null);

        transaction.commit();
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
                Log.d("BACK", "Current fragment is SGPLATO");
                switchFragment(DictFragment.FRAGMENT_HOME);
            }
            if (currentFragment instanceof  FragmentAlcohol) {
                switchFragment(DictFragment.FRAGMENT_HOME);
            }
            if (currentFragment instanceof  FragmentCarbonation) {
                switchFragment(DictFragment.FRAGMENT_HOME);
            }
        } else {
            super.onBackPressed();
        }
    }

    public void setLanguage(Language language) {
        Log.d("LANG", "setLanguage in MainActivity");
        Locale locale = new Locale(language.getLocale());


        String newLang = locale.getLanguage();
        String oldLang = getResources().getConfiguration().locale.getLanguage();

        Log.d("LANG", "NEW: " +  newLang);
        Log.d("LANG", "OLD: " + oldLang);

        if (!newLang.equals(oldLang)) {
            //BrewingCalculatorApplication app = (BrewingCalculatorApplication) getApplication();
            //app.setLanguage(language);
            Log.d("LANG", "I change language");

            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            DisplayMetrics dm = getResources().getDisplayMetrics();
            getResources().updateConfiguration(config,dm);

            recreate();
        }
        else {
            Log.d("LANG", "Languages are identical");
        }
    }

}
