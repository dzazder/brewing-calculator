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
        BrewingCalculatorApplication.setContext(this);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            switchFragment(DictFragment.FRAGMENT_HOME);
        }

        fragmentManager = getSupportFragmentManager();

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Log.d("MainActivity", "StackBackEntryCount: " + fragmentManager.getBackStackEntryCount());
                if (fragmentManager.findFragmentByTag("FRAGMENT_DISPLAYED") instanceof FragmentHome) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        final CheckBox chbUseRefractometer = (CheckBox) dialogview.findViewById(R.id.chb_always_use_refractometer);
        final EditText txtWortCorrectFactor = (EditText) dialogview.findViewById(R.id.txt_default_wort_correction_factor);

        switch (item.getItemId()) {
            case R.id.menu_item_settings:

                //if (alertDialog == null || !alertDialog.isShowing()) {
                alertDialog = new AlertDialog.Builder(this).create(); //Read Update
                alertDialog.setView(dialogview);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.extract_units,
                        android.R.layout.simple_spinner_item);
                //adapter.setDropDownViewResource(R.layout.bcalc_spinner_dropdown_item);
                int spinnerUnitPosition = adapter.getPosition(AppConfiguration.getInstance().defaultSettings.getDefExtractUnit().toString());
                spDefaultExtractUnit.setSelection(spinnerUnitPosition);

                chbUseRefractometer.setChecked(AppConfiguration.getInstance().defaultSettings.isDefUseRefractometer());
                txtWortCorrectFactor.setText(Double.toString(AppConfiguration.getInstance().defaultSettings.getDefWortCorrectionFactor()));

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
                        boolean useRefractometer = chbUseRefractometer.isChecked();
                        double wortCorrectFactor = Double.parseDouble(txtWortCorrectFactor.getText().toString());

                        //FileDB.saveDefaultUnit(selectedUnit, getApplicationContext());
                        AppConfiguration.getInstance().defaultSettings.setDefExtractUnit(selectedUnit);
                        AppConfiguration.getInstance().defaultSettings.setDefUseRefractometer(useRefractometer);
                        AppConfiguration.getInstance().defaultSettings.setDefWortCorrectionFactor(wortCorrectFactor);

                        BCalcConf conf = new BCalcConf();
                        conf.setDefExtractUnit(selectedUnit);
                        conf.setDefUseRefractometer(useRefractometer);
                        conf.setDefWortCorrectionFactor(wortCorrectFactor);
                        FileDB.saveConfiguration(conf, getApplicationContext());

                        alertDialog.hide();
                        alertDialog.dismiss();
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
