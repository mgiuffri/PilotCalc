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
import com.marianogiuffrida.pilotcalc.model.Altimetry.StandardAtmosphere;
import com.marianogiuffrida.pilotcalc.model.Common.Measurement;
import com.marianogiuffrida.pilotcalc.model.Common.Unit;
import com.marianogiuffrida.pilotcalc.model.Conversions.Units;
import com.marianogiuffrida.pilotcalc.model.Speed.SpeedCalculator;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

public class SoundSpeedFragment extends StatefulFragment{
    public static final int ID = 4;
    private static final String TEMPERATURE = "temperature";
    private static final String TEMPERATURE_UNIT = "temp_unit";
    private static final String SPEED_UNIT = "speed_unit";

    private View rootView;
    private UnitConversionRepository unitConversionsRepository;
    private SpeedCalculator calculator;

    private Spinner temperatureSpinner;
    private Spinner speedSpinner;

    private BigDecimal inputTemperature;
    private String selectedTemperatureUnit;
    private String selectedSpeedUnit;
    private TextView speedText;
    private EditText temperatureEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_speed_sound_speed, container, false);

        temperatureEditText = (EditText) rootView.findViewById(R.id.temperature);
        temperatureSpinner = (Spinner) rootView.findViewById(R.id.temperatureSpinner);

        speedText = (TextView) rootView.findViewById(R.id.speed);
        speedSpinner = (Spinner) rootView.findViewById(R.id.speedSpinner);

        SqlLiteDataStore dataStore = new SqlLiteDataStore(getActivity().getApplicationContext());
        unitConversionsRepository = new UnitConversionRepository(dataStore);
        calculator = new SpeedCalculator(unitConversionsRepository);

        fillSpinner(temperatureSpinner, Units.Temperature.Name);
        fillSpinner(speedSpinner, Units.Speed.Name);

        temperatureEditText.addTextChangedListener(new TextWatcher() {
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
                    speedText.setText(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        speedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSpeedUnit = ((Unit) parent.getItemAtPosition(position)).Name;
                calculate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        temperatureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTemperatureUnit = ((Unit) parent.getItemAtPosition(position)).Name;
                calculate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    private void calculate() {
        if (inputTemperature != null && selectedTemperatureUnit != null && selectedSpeedUnit != null) {
            Measurement pressure = calculator.calculateSpeedSound(
                    new Measurement(inputTemperature, selectedTemperatureUnit),
                    selectedSpeedUnit);

            NumberFormat format = NumberFormat.getInstance();
            format.setMaximumFractionDigits(2);
            speedText.setText(format.format(pressure.getMagnitude()));
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
        outState.putString(TEMPERATURE, temperatureEditText.getText().toString());
        outState.putString(TEMPERATURE_UNIT, selectedTemperatureUnit);
        outState.putString(SPEED_UNIT, selectedSpeedUnit);
    }

    @Override
    protected void onRestoreState(Bundle inState) {
        if (inState != null) {
            temperatureEditText.setText(inState.getCharSequence(TEMPERATURE));
            selectedTemperatureUnit = inState.getString(TEMPERATURE_UNIT);
            temperatureSpinner.setSelection(((UnitAdapter) temperatureSpinner.getAdapter()).getPositionByName(selectedTemperatureUnit));

            selectedSpeedUnit = inState.getString(SPEED_UNIT);
            speedSpinner.setSelection(((UnitAdapter) speedSpinner.getAdapter()).getPositionByName(selectedSpeedUnit));
        }
    }
}