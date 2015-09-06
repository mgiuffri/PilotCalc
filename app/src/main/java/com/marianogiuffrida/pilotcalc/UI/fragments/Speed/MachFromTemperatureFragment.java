package com.marianogiuffrida.pilotcalc.UI.fragments.Speed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.marianogiuffrida.helpers.StringUtils;
import com.marianogiuffrida.pilotcalc.R;
import com.marianogiuffrida.pilotcalc.UI.adapters.UnitAdapter;
import com.marianogiuffrida.pilotcalc.UI.fragments.StatefulFragment;
import com.marianogiuffrida.pilotcalc.data.SqlLiteDataStore;
import com.marianogiuffrida.pilotcalc.data.UnitConversionRepository;
import com.marianogiuffrida.pilotcalc.model.Common.Measurement;
import com.marianogiuffrida.pilotcalc.model.Common.Unit;
import com.marianogiuffrida.pilotcalc.model.Conversions.Units;
import com.marianogiuffrida.pilotcalc.model.Speed.SpeedCalculator;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

public class MachFromTemperatureFragment extends StatefulFragment{
    public static final int ID = 2;
    private static final String TEMPERATURE = "Temperature";
    private static final String TEMPERATURE_UNIT = "Temperature_unit";
    private static final String TRUE_AIRSPEED = "true_airspeed";
    private static final String TRUE_AIRSPEED_UNIT = "true_airspeed_unit";

    private View rootView;
    private UnitConversionRepository unitConversionsRepository;
    private SpeedCalculator calculator;

    private BigDecimal inputTemperature;
    private String selectedIndicatedTemperatureUnit;
    private EditText outsideTemperatureText;
    private Spinner outsideTemperatureSpinner;

    private BigDecimal inputTrueAirspeed;
    private String selectedTrueAirspeedUnit;
    private EditText trueAirspeedText;
    private Spinner trueAirspeedSpinner;

    private TextView machText;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_speed_mach_from_temperature, container, false);

        SqlLiteDataStore dataStore = new SqlLiteDataStore(getActivity().getApplicationContext());
        unitConversionsRepository = new UnitConversionRepository(dataStore);
        calculator = new SpeedCalculator(unitConversionsRepository);

        outsideTemperatureText = (EditText) rootView.findViewById(R.id.temperature);
        outsideTemperatureSpinner = (Spinner) rootView.findViewById(R.id.temperatureSpinner);

        trueAirspeedText = (EditText) rootView.findViewById(R.id.airspeed);
        trueAirspeedSpinner = (Spinner) rootView.findViewById(R.id.airspeedSpinner);

        machText = (TextView) rootView.findViewById(R.id.mach);

        fillSpinner(outsideTemperatureSpinner, Units.Temperature.Name);
        fillSpinner(trueAirspeedSpinner, Units.Speed.Name);

        outsideTemperatureText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && StringUtils.isNumeric(s)) {
                    inputTemperature = new BigDecimal(s.toString());
                    calculate();
                } else {
                    inputTemperature = null;
                    machText.setText(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        trueAirspeedText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    inputTrueAirspeed = new BigDecimal(s.toString());
                    calculate();
                } else {
                    inputTrueAirspeed = null;
                    machText.setText(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        outsideTemperatureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedIndicatedTemperatureUnit = ((Unit) parent.getItemAtPosition(position)).Name;
                calculate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        trueAirspeedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTrueAirspeedUnit = ((Unit) parent.getItemAtPosition(position)).Name;
                calculate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    private void calculate() {
        if (inputTemperature != null
                && selectedIndicatedTemperatureUnit != null
                && inputTrueAirspeed != null
                && selectedTrueAirspeedUnit != null) {

            double mach = calculator.calculateMachNumber(
                    new Measurement(inputTrueAirspeed, selectedTrueAirspeedUnit),
                    new Measurement(inputTemperature, selectedIndicatedTemperatureUnit));

            NumberFormat format = NumberFormat.getInstance();
            format.setMaximumFractionDigits(2);
            machText.setText(format.format(mach));
        }
    }

    private String fillSpinner(Spinner spinner, String unit ) {
        List<Unit> units = unitConversionsRepository.getSupportedUnitsByConversionType(unit);
        UnitAdapter adapter = new UnitAdapter(getActivity(), R.layout.spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return units.get(0).Name;
    }

    @Override
    protected void onSaveState(Bundle outState) {
        outState.putString(TEMPERATURE, outsideTemperatureText.getText().toString());
        outState.putString(TEMPERATURE_UNIT, selectedIndicatedTemperatureUnit);
        outState.putString(TRUE_AIRSPEED, trueAirspeedText.getText().toString());
        outState.putString(TRUE_AIRSPEED_UNIT, selectedTrueAirspeedUnit);
    }

    @Override
    protected void onRestoreState(Bundle inState) {
        if (inState != null) {
            outsideTemperatureText.setText(inState.getCharSequence(TEMPERATURE));
            selectedIndicatedTemperatureUnit = inState.getString(TEMPERATURE_UNIT);
            outsideTemperatureSpinner.setSelection(((UnitAdapter) outsideTemperatureSpinner.getAdapter())
                    .getPositionByName(selectedIndicatedTemperatureUnit));

            trueAirspeedText.setText(inState.getCharSequence(TRUE_AIRSPEED));
            selectedTrueAirspeedUnit = inState.getString(TRUE_AIRSPEED_UNIT);
            trueAirspeedSpinner.setSelection(((UnitAdapter) trueAirspeedSpinner.getAdapter())
                    .getPositionByName(selectedTrueAirspeedUnit));

            selectedTrueAirspeedUnit = inState.getString(TRUE_AIRSPEED_UNIT);
            trueAirspeedSpinner.setSelection(((UnitAdapter) trueAirspeedSpinner.getAdapter())
                    .getPositionByName(selectedTrueAirspeedUnit));

        }
    }
}
