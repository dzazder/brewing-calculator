package dev.lampart.bartosz.brewingcalculator;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import org.xml.sax.DTDHandler;

import dev.lampart.bartosz.brewingcalculator.dbfile.FileDB;
import dev.lampart.bartosz.brewingcalculator.dicts.DictFragment;
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

        AppConfiguration.getInstance().defaultExtractUnit = FileDB.getDefaultExtractUnit(getApplicationContext());

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

        switch(item.getItemId()) {
            case R.id.menu_item_settings:

                if (alertDialog == null || !alertDialog.isShowing()) {
                    alertDialog = new AlertDialog.Builder(this).create(); //Read Update
                    alertDialog.setView(dialogview);

                    Button btnSettingsCancel = (Button)dialogview.findViewById(R.id.btn_settings_cancel);
                    btnSettingsCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.hide();
                        }
                    });

                    Button btnSettingsSave = (Button)dialogview.findViewById(R.id.btn_settings_save);
                    btnSettingsSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.hide();
                        }
                    });

                    alertDialog.show();
                }
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
            case DictFragment.FRAGMENT_HOME: newFragment = new FragmentHome(); break;
            case DictFragment.FRAGMENT_OG_PLATO: newFragment = new FragmentSgPlato(); break;
            case DictFragment.FRAGMENT_ALCOHOL: newFragment = new FragmentAlcohol(); break;
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
