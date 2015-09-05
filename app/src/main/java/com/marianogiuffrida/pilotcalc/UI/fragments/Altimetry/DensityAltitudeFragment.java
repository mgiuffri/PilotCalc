package com.marianogiuffrida.pilotcalc.UI.fragments.Altimetry;

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

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

public class DensityAltitudeFragment extends StatefulFragment {
    public static final int ID = 1;
    private static final String TEMPERATURE = "temperature";
    private static final String TEMPERATURE_UNIT = "temp_unit";
    private static final String PRESSURE_ALTITUDE = "pressAlt";
    private static final String PRESSURE_ALTITUDE_UNIT = "pressAlt_unit";
    private static final String DENSITY_ALTITUDE_UNIT = "altitude_unit";

    private View rootView;
    private UnitConversionRepository unitConversionsRepository;
    private StandardAtmosphere calculator;

    private BigDecimal inputPressureAltitude;
    private String selectedPressureAltitudeUnit;
    private EditText pressureAltitude;
    private Spinner pressureAltitudeSpinner;

    private BigDecimal inputTemperature;
    private String selectedTemperatureUnit;
    private EditText temperatureText;
    private Spinner temperatureSpinner;

    private String selectedDensityAltitudeUnit;
    private TextView densityAltitudeText;
    private Spinner densityAltitudeSpinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_altimetry_density_altitude, container, false);

        SqlLiteDataStore dataStore = new SqlLiteDataStore(getActivity().getApplicationContext());
        unitConversionsRepository = new UnitConversionRepository(dataStore);
        calculator = new StandardAtmosphere(unitConversionsRepository);

        pressureAltitude = (EditText) rootView.findViewById(R.id.altitude);
        pressureAltitudeSpinner = (Spinner) rootView.findViewById(R.id.altitudeSpinner);

        temperatureText = (EditText) rootView.findViewById(R.id.temperature);
        temperatureSpinner = (Spinner) rootView.findViewById(R.id.temperatureUnit);

        densityAltitudeText = (TextView) rootView.findViewById(R.id.density_altitude);
        densityAltitudeSpinner = (Spinner) rootView.findViewById(R.id.density_altitudeSpinner);

        fillSpinner(pressureAltitudeSpinner, Units.Pressure.Name);
        fillSpinner(temperatureSpinner, Units.Length.Name);
        fillSpinner(densityAltitudeSpinner, Units.Length.Name);

        pressureAltitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && StringUtils.isNumeric(s)) {
                    inputPressureAltitude = new BigDecimal(s.toString());
                    calculate();
                } else {
                    inputPressureAltitude = null;
                    densityAltitudeText.setText(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pressureAltitudeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPressureAltitudeUnit = ((Unit) parent.getItemAtPosition(position)).Name;
                calculate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        temperatureText.addTextChangedListener(new TextWatcher() {
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
                    densityAltitudeText.setText(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
        densityAltitudeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDensityAltitudeUnit = ((Unit) parent.getItemAtPosition(position)).Name;
                calculate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    private void calculate() {
        if (inputPressureAltitude != null
                && selectedPressureAltitudeUnit != null
                && inputTemperature != null
                && selectedTemperatureUnit != null
                && selectedDensityAltitudeUnit != null) {

            Measurement temperature = calculator.calculateDensityAltitude(
                    new Measurement(inputPressureAltitude, selectedPressureAltitudeUnit),
                    new Measurement(inputTemperature, selectedTemperatureUnit),
                    selectedTemperatureUnit);

            NumberFormat format = NumberFormat.getInstance();
            format.setMaximumFractionDigits(0);
            densityAltitudeText.setText(format.format(temperature.getMagnitude()));
        }
    }

    private String fillSpinner(Spinner spinner, String unit) {
        List<Unit> units = unitConversionsRepository.getSupportedUnitsByConversionType(unit);
        UnitAdapter adapter = new UnitAdapter(getActivity(), R.layout.spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return units.get(0).Name;
    }

    @Override
    protected void onSaveState(Bundle outState) {
        outState.putString(PRESSURE_ALTITUDE, pressureAltitude.getText().toString());
        outState.putString(PRESSURE_ALTITUDE_UNIT, selectedPressureAltitudeUnit);
        outState.putString(TEMPERATURE, temperatureText.getText().toString());
        outState.putString(TEMPERATURE_UNIT, selectedTemperatureUnit);
        outState.putString(DENSITY_ALTITUDE_UNIT, selectedDensityAltitudeUnit);
    }

    @Override
    protected void onRestoreState(Bundle inState) {
        if (inState != null) {
            pressureAltitude.setText(inState.getCharSequence(PRESSURE_ALTITUDE));
            selectedPressureAltitudeUnit = inState.getString(PRESSURE_ALTITUDE_UNIT);
            pressureAltitudeSpinner.setSelection(((UnitAdapter) pressureAltitudeSpinner.getAdapter())
                    .getPositionByName(selectedPressureAltitudeUnit));

            temperatureText.setText(inState.getCharSequence(TEMPERATURE));
            selectedTemperatureUnit = inState.getString(TEMPERATURE_UNIT);
            temperatureSpinner.setSelection(((UnitAdapter) temperatureSpinner.getAdapter())
                    .getPositionByName(selectedTemperatureUnit));

            selectedDensityAltitudeUnit = inState.getString(DENSITY_ALTITUDE_UNIT);
            densityAltitudeSpinner.setSelection(((UnitAdapter) densityAltitudeSpinner.getAdapter())
                    .getPositionByName(selectedDensityAltitudeUnit));
        }
    }
}
