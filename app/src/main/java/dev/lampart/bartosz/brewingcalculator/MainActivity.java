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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Locale;

import dev.lampart.bartosz.brewingcalculator.dbfile.FileDB;
import dev.lampart.bartosz.brewingcalculator.dicts.DictFragment;
import dev.lampart.bartosz.brewingcalculator.dicts.DictLanguages;
import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.entities.Language;
import dev.lampart.bartosz.brewingcalculator.fragments.FragmentAlcohol;
import dev.lampart.bartosz.brewingcalculator.fragments.FragmentHome;
import dev.lampart.bartosz.brewingcalculator.fragments.FragmentSgPlato;
import dev.lampart.bartosz.brewingcalculator.global.AppConfiguration;

public class MainActivity extends AppCompatActivity {

    public static FragmentManager fragmentManager;
    private AlertDialog alertDialog = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            switchFragment(DictFragment.FRAGMENT_HOME);
        }

        fragmentManager = getSupportFragmentManager();

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (fragmentManager.getBackStackEntryCount() > 1) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }
            }
        });
        final Drawable upArrow = ContextCompat.getDrawable(getApplicationContext(), R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccentDark), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        View dialogview = getLayoutInflater().inflate(R.layout.dialog_settings, null);
        final Spinner spDefaultExtractUnit = (Spinner) dialogview.findViewById(R.id.spinner_choose_default_extract_unit);
        final Spinner spDefaultLanguage = (Spinner) dialogview.findViewById(R.id.spinner_choose_default_language);

        DictLanguages.setSpinner(spDefaultLanguage, this);

        switch (item.getItemId()) {
            case R.id.menu_item_settings:

                //if (alertDialog == null || !alertDialog.isShowing()) {
                alertDialog = new AlertDialog.Builder(this).create(); //Read Update
                alertDialog.setView(dialogview);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.extract_units,
                        android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(R.layout.bcalc_spinner_dropdown_item);
                int spinnerUnitPosition = adapter.getPosition(AppConfiguration.getInstance().defaultExtractUnit.toString());
                spDefaultExtractUnit.setSelection(spinnerUnitPosition);

                ArrayAdapter<String> adapterLang = DictLanguages.getLanguageArrayAdapter(this);
                adapterLang.setDropDownViewResource(R.layout.bcalc_spinner_dropdown_item);
                int spinnetLanguagePosition = adapterLang.getPosition(AppConfiguration.getInstance().defaultLanguage.toString());
                spDefaultLanguage.setSelection(spinnetLanguagePosition);

                Button btnSettingsCancel = (Button) dialogview.findViewById(R.id.btn_settings_cancel);
                btnSettingsCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.hide();
                    }
                });

                Button btnSettingsSave = (Button) dialogview.findViewById(R.id.btn_settings_save);
                btnSettingsSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ExtractUnit selectedUnit = ExtractUnit.valueOf(spDefaultExtractUnit.getSelectedItem().toString());
                        FileDB.saveDefaultUnit(selectedUnit, getApplicationContext());
                        AppConfiguration.getInstance().defaultExtractUnit = selectedUnit;

                        Language selectedLang = DictLanguages.getLanguageByName(spDefaultLanguage.getSelectedItem().toString());
                        FileDB.saveDefaultLanguage(selectedLang, getApplicationContext());
                        AppConfiguration.getInstance().defaultLanguage = selectedLang;
                        alertDialog.hide();
                        alertDialog.dismiss();
                        setLanguage(selectedLang);
                    }
                });

                alertDialog.show();
                //}
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
            default:
                newFragment = new FragmentHome();
                break;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.layout_fragment_area, newFragment, "FRAGMENT_DISPLAYED");
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("FRAGMENT_DISPLAYED");
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
        } else {
            super.onBackPressed();
        }
    }

    public void setLanguage(Language language) {
        Log.d("LANG", "setLanguage in MainActivity");
        Locale locale = new Locale(language.getLocale());

        Log.d("LANG", "NEW: " +  locale.getLanguage());
        Log.d("LANG", "OLD: " + getResources().getConfiguration().locale.getLanguage());

        if (!locale.getLanguage().equals(getResources().getConfiguration().locale.getLanguage())) {
            //BrewingCalculatorApplication app = (BrewingCalculatorApplication) getApplication();
            //app.setLanguage(language);


            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            DisplayMetrics dm = getResources().getDisplayMetrics();
            getResources().updateConfiguration(config,dm);

            recreate();
        }
    }

}
