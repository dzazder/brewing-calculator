package dev.lampart.bartosz.brewingcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import dev.lampart.bartosz.brewingcalculator.fragments.FragmentHome;

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
}
