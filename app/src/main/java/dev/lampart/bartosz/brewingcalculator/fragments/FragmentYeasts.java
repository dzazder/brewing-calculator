package dev.lampart.bartosz.brewingcalculator.fragments;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import dev.lampart.bartosz.brewingcalculator.R;
import dev.lampart.bartosz.brewingcalculator.calculators.ExtractCalc;
import dev.lampart.bartosz.brewingcalculator.calculators.YeastCalc;
import dev.lampart.bartosz.brewingcalculator.dicts.BeerStyle;
import dev.lampart.bartosz.brewingcalculator.dicts.ExtractUnit;
import dev.lampart.bartosz.brewingcalculator.dicts.VolumeUnit;
import dev.lampart.bartosz.brewingcalculator.global.AppConfiguration;
import dev.lampart.bartosz.brewingcalculator.helpers.NumberFormatter;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentYeasts extends Fragment {

    private TabHost mTabHost;
    private EditText txtGravity;
    private EditText txtBeerAmount;
    private TextView txtYeastNeeded;
    private Spinner spGravityUnit;
    private Spinner spVolumeUnit;
    private RadioGroup rgBeerStyle;
    private EditText txtHarvestDate;
    private EditText txtDryProdDate;
    private EditText txtLiquidProdDate;

    // dry
    private TextView txtDryYeastNeeded;

    // liquid
    private TextView txtLiquidPacksNeeded;
    private TextView txtStarterSizeNeeded;

    // slurry
    private TextView txtSlurryYeastNeeded;

    private DatePickerDialog dryDatePickerDialog;
    private DatePickerDialog liquidDatePickerDialog;
    private DatePickerDialog harvestDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    public FragmentYeasts() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_main);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_yeasts, container, false);

        getActivity().setTitle(getResources().getString(R.string.title_yeasts_amount_calculator));

        mTabHost = (TabHost)view.findViewById(R.id.tabhost_yeasts);
        mTabHost.setup();
        //Tab 1
        /*TabHost.TabSpec spec = mTabHost.newTabSpec(getResources().getString(R.string.title_yeasts_dry));
        spec.setContent(R.id.tab_yeasts_dry);
        spec.setIndicator(getResources().getString(R.string.title_yeasts_dry));
        mTabHost.addTab(spec);

        //Tab 2
        spec = mTabHost.newTabSpec(getResources().getString(R.string.title_yeasts_liquid));
        spec.setContent(R.id.tab_yeasts_liquid);
        spec.setIndicator(getResources().getString(R.string.title_yeasts_liquid));
        mTabHost.addTab(spec);
*/
        //Tab 3
        TabHost.TabSpec spec = mTabHost.newTabSpec(getResources().getString(R.string.title_yeasts_slurry));
        spec.setContent(R.id.tab_yeasts_slurry);
        spec.setIndicator(getResources().getString(R.string.title_yeasts_slurry));
        mTabHost.addTab(spec);

        txtBeerAmount = (EditText)view.findViewById(R.id.txt_yeast_priming_size);
        txtGravity = (EditText)view.findViewById(R.id.txt_yeast_extract);
        txtYeastNeeded = (TextView)view.findViewById(R.id.txt_yeast_needed);
        spGravityUnit = (Spinner)view.findViewById(R.id.sp_yeast_extract_unit);
        spVolumeUnit = (Spinner)view.findViewById(R.id.sp_yeast_priming_size);
        rgBeerStyle = (RadioGroup)view.findViewById(R.id.toggle_yeast_beer_style);
        txtHarvestDate = (EditText)view.findViewById(R.id.txt_harvest_date);
        //txtDryProdDate = (EditText)view.findViewById(R.id.txt_dry_production_date);
        //txtLiquidProdDate = (EditText)view.findViewById(R.id.txt_liquid_production_date);
        txtSlurryYeastNeeded = (TextView) view.findViewById(R.id.txt_yeast_slurry_needed);
        //txtLiquidPacksNeeded = (TextView) view.findViewById(R.id.txt_yeast_liquid_needed);
        //txtStarterSizeNeeded = (TextView) view.findViewById(R.id.txt_starter_size);

        txtBeerAmount.setText(Double.toString(AppConfiguration.getInstance().defaultSettings.getDefPrimingSize()));
        ArrayAdapter<CharSequence> primingSizeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.volume_units,
                android.R.layout.simple_spinner_item);
        VolumeUnit selDefVolUnit = VolumeUnit.valueOf(AppConfiguration.getInstance().defaultSettings.getDefVolumeUnit().toString());
        String valSelectedInSpinnerVol = "";
        switch (selDefVolUnit) {
            case Liter: valSelectedInSpinnerVol = getActivity().getResources().getString(R.string.volume_unit_liters); break;
            case Gallon: valSelectedInSpinnerVol = getActivity().getResources().getString(R.string.volume_unit_gallons); break;
        }
        int spinnerPrimingUnigPosition = primingSizeAdapter.getPosition(valSelectedInSpinnerVol);
        spVolumeUnit.setSelection(spinnerPrimingUnigPosition);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.extract_units,
                android.R.layout.simple_spinner_item);
        int spinnerPosition = adapter.getPosition(AppConfiguration.getInstance().defaultSettings.getDefExtractUnit().toString());
        spGravityUnit.setSelection(spinnerPosition);

        // dry
        //txtDryYeastNeeded = (TextView)view.findViewById(R.id.txt_yeast_dry_needed);

        txtBeerAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateYeastCells(txtBeerAmount, txtGravity, spVolumeUnit, spGravityUnit, rgBeerStyle, txtYeastNeeded);
            }
        });

        txtGravity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateYeastCells(txtBeerAmount, txtGravity, spVolumeUnit, spGravityUnit, rgBeerStyle, txtYeastNeeded);
            }
        });

        spVolumeUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                calculateYeastCells(txtBeerAmount, txtGravity, spVolumeUnit, spGravityUnit, rgBeerStyle, txtYeastNeeded);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spGravityUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                calculateYeastCells(txtBeerAmount, txtGravity, spVolumeUnit, spGravityUnit, rgBeerStyle, txtYeastNeeded);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rgBeerStyle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                calculateYeastCells(txtBeerAmount, txtGravity, spVolumeUnit, spGravityUnit, rgBeerStyle, txtYeastNeeded);
            }
        });

        txtHarvestDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                calculateYeastCells(txtBeerAmount, txtGravity, spVolumeUnit, spGravityUnit, rgBeerStyle, txtYeastNeeded);
            }
        });
/*
        txtDryProdDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                calculateYeastCells(txtBeerAmount, txtGravity, spVolumeUnit, spGravityUnit, rgBeerStyle, txtYeastNeeded);
            }
        });

        txtLiquidProdDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                calculateYeastCells(txtBeerAmount, txtGravity, spVolumeUnit, spGravityUnit, rgBeerStyle, txtYeastNeeded);
            }
        });
*/
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        txtHarvestDate.setInputType(InputType.TYPE_NULL);
        if (txtHarvestDate.getText().toString().length() == 0) {
            txtHarvestDate.setText(dateFormatter.format(new Date()));
        }
        Calendar calendar = Calendar.getInstance();
        harvestDatePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                txtHarvestDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        txtHarvestDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                harvestDatePickerDialog.show();
            }
        });
/*
        txtDryProdDate.setInputType(InputType.TYPE_NULL);
        if (txtDryProdDate.getText().toString().length() == 0) {
            txtDryProdDate.setText(dateFormatter.format(new Date()));
        }
        calendar = Calendar.getInstance();
        dryDatePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                txtDryProdDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        txtDryProdDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dryDatePickerDialog.show();
            }
        });

        txtLiquidProdDate.setInputType(InputType.TYPE_NULL);
        if (txtLiquidProdDate.getText().toString().length() == 0) {
            txtLiquidProdDate.setText(dateFormatter.format(new Date()));
        }
        calendar = Calendar.getInstance();
        liquidDatePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                txtLiquidProdDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        txtLiquidProdDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                liquidDatePickerDialog.show();
            }
        });
*/
        return view;
    }

    private void calculateYeastCells(EditText txtBeerAmount, EditText txtGravity, Spinner spVolumeUnit,
                                     Spinner spGravityUnit, RadioGroup rgBeerStyle, TextView txtYeastNeeded) {
        if (NumberFormatter.isNumeric(txtBeerAmount.getText().toString()) &&
                NumberFormatter.isNumeric(txtGravity.getText().toString())) {

            double beerAmount = Double.parseDouble(txtBeerAmount.getText().toString());
            double gravity = Double.parseDouble(txtGravity.getText().toString());
            String selectedVolUnit = spVolumeUnit.getSelectedItem().toString();
            String selectedGrUnit = spGravityUnit.getSelectedItem().toString();
            Date harvestDate = new Date();
            try {
                harvestDate = dateFormatter.parse(txtHarvestDate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            /*
            Date dryProdDate = new Date();
            try {
                dryProdDate = dateFormatter.parse(txtDryProdDate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date liquidProdDate = new Date();
            try {
                liquidProdDate = dateFormatter.parse(txtLiquidProdDate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
*/
            VolumeUnit volUnit = VolumeUnit.Liter;
            if (selectedVolUnit == getContext().getString(R.string.volume_unit_gallons)) {
                volUnit = VolumeUnit.Gallon;
            }

            ExtractUnit gravityUnit = ExtractUnit.valueOf(selectedGrUnit);
            BeerStyle beerStyle = BeerStyle.Ale;

            switch (rgBeerStyle.getCheckedRadioButtonId()) {
                case R.id.toggle_option_yeast_ale: beerStyle = BeerStyle.Ale; break;
                case R.id.toggle_option_yeast_lager: beerStyle = BeerStyle.Lager; break;
            }

            long yeastNeeded = YeastCalc.calcYeastCells(beerAmount, gravity, volUnit, gravityUnit,beerStyle);
            setYeastNeededValue(yeastNeeded, txtYeastNeeded);

            //setDryYeastNeeded(yeastNeeded, dryProdDate);
            //setLiquidPacksNeeded(yeastNeeded, liquidProdDate);
            //setStarterSize(yeastNeeded, liquidProdDate);
            setSlurryNeeded(yeastNeeded, harvestDate);
        }
    }

    private void setLiquidPacksNeeded(long yeastNeeded, Date prodDate) {
        if (yeastNeeded < 0) {
            txtLiquidPacksNeeded.setTextColor(getResources().getColor(R.color.colorError));
            txtLiquidPacksNeeded.setText(getResources().getText(R.string.incorrect_value));
        }
        else {
            txtLiquidPacksNeeded.setTextColor(getResources().getColor(R.color.colorAccent));
            double liquidYeastPacksNeeded = YeastCalc.calcLiquidPackwWithoutStarter(yeastNeeded, prodDate);
            txtLiquidPacksNeeded.setText(String.format(Locale.US, "%.2f", liquidYeastPacksNeeded));
        }
    }

    private void setStarterSize(long yeastNeeded, Date prodDate) {
        if (yeastNeeded < 0) {
            txtStarterSizeNeeded.setTextColor(getResources().getColor(R.color.colorError));
            txtStarterSizeNeeded.setText(getResources().getText(R.string.incorrect_value));
        }
        else {
            txtStarterSizeNeeded.setTextColor(getResources().getColor(R.color.colorAccent));
            double starterSizeNeeded = YeastCalc.calcMililitersOfStarter(yeastNeeded, prodDate);
            txtStarterSizeNeeded.setText(String.format(Locale.US, "%.2f ml", starterSizeNeeded));
        }
    }

    private void setSlurryNeeded(long yeastNeeded, Date harvestDate) {
        if (yeastNeeded < 0) {
            txtSlurryYeastNeeded.setTextColor(getResources().getColor(R.color.colorError));
            txtSlurryYeastNeeded.setText(getResources().getText(R.string.incorrect_value));
        }
        else {
            txtSlurryYeastNeeded.setTextColor(getResources().getColor(R.color.colorAccent));
            double slurryYeastNeeded = YeastCalc.calcMililitersOfSlurry(yeastNeeded, harvestDate);
            txtSlurryYeastNeeded.setText(String.format(Locale.US, "%.2f ml", slurryYeastNeeded));
        }
    }

    private void setDryYeastNeeded(long yeastNeeded, Date productionDate) {
        if (yeastNeeded < 0) {
            txtDryYeastNeeded.setTextColor(getResources().getColor(R.color.colorError));
            txtDryYeastNeeded.setText(getResources().getText(R.string.incorrect_value));
        }
        else {
            txtDryYeastNeeded.setTextColor(getResources().getColor(R.color.colorAccent));
            double dryYeastNeeded = YeastCalc.calcGramsOfDryYeast(yeastNeeded, productionDate);
            txtDryYeastNeeded.setText(String.format(Locale.US, "%.2f g", dryYeastNeeded));
        }
    }

    private void setYeastNeededValue(long yeast, TextView txtToSet) {
        if (yeast < 0) {
            txtToSet.setTextColor(getResources().getColor(R.color.colorError));
            txtToSet.setText(getResources().getText(R.string.incorrect_value));
        }
        else {
            txtToSet.setTextColor(getResources().getColor(R.color.colorAccent));

            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

            symbols.setGroupingSeparator(' ');
            formatter.setDecimalFormatSymbols(symbols);

            txtToSet.setText(formatter.format(yeast));
        }
    }

}
