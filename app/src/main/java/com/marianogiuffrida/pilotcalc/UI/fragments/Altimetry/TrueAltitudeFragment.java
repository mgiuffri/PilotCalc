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

public class TrueAltitudeFragment extends StatefulFragment {
    public static final int ID = 2;
    private static final String TEMPERATURE = "temperature";
    private static final String TEMPERATURE_UNIT = "temp_unit";
    private static final String PRESSURE_ALTITUDE = "pressAlt";
    private static final String PRESSURE_ALTITUDE_UNIT = "pressAlt_unit";
    private static final String CALIBRATED_ALTITUDE_UNIT = "cal_altitude_unit";
    private static final String CALIBRATED_ALTITUDE = "cal_altitude_unit";
    private static final String ELEVATION = "elevation";
    private static final String ELEVATION_UNIT = "elevation_unit";
    private static final String TRUE_ALTITUDE_UNIT = "true_altitude_unit";

    private View rootView;
    private UnitConversionRepository unitConversionsRepository;
    private StandardAtmosphere calculator;

    private BigDecimal inputPressureAltitude;
    private String selectedPressureAltitudeUnit;
    private EditText pressureAltitude;
    private Spinner pressureAltitudeSpinner;

    private BigDecimal inputCalibratedAltitude;
    private String selectedCalibratedAltitudeUnit;
    private EditText calibratedAltitude;
    private Spinner calibratedAltitudeSpinner;

    private BigDecimal inputElevation;
    private String selectedElevationUnit;
    private EditText elevationText;
    private Spinner elevationSpinner;

    private BigDecimal inputTemperature;
    private String selectedTemperatureUnit;
    private EditText temperatureText;
    private Spinner temperatureSpinner;

    private String selectedTrueAltitudeUnit;
    private TextView trueAltitudeText;
    private Spinner trueAltitudeSpinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_altimetry_true_altitude, container, false);

        SqlLiteDataStore dataStore = new SqlLiteDataStore(getActivity().getApplicationContext());
        unitConversionsRepository = new UnitConversionRepository(dataStore);
        calculator = new StandardAtmosphere(unitConversionsRepository);

        pressureAltitude = (EditText) rootView.findViewById(R.id.pressureAltitude);
        pressureAltitudeSpinner = (Spinner) rootView.findViewById(R.id.pressureAltitudeSpinner);

        calibratedAltitude = (EditText) rootView.findViewById(R.id.calibratedAltitude);
        calibratedAltitudeSpinner = (Spinner) rootView.findViewById(R.id.calibratedAltitudeSpinner);

        elevationText = (EditText) rootView.findViewById(R.id.elevation);
        elevationSpinner = (Spinner) rootView.findViewById(R.id.elevationSpinner);

        temperatureText = (EditText) rootView.findViewById(R.id.temperature);
        temperatureSpinner = (Spinner) rootView.findViewById(R.id.temperatureSpinner);

        trueAltitudeText = (TextView) rootView.findViewById(R.id.trueAltitude);
        trueAltitudeSpinner = (Spinner) rootView.findViewById(R.id.trueAltitudeSpinner);

        fillSpinner(pressureAltitudeSpinner, Units.Length.Name);
        fillSpinner(calibratedAltitudeSpinner, Units.Length.Name);
        fillSpinner(temperatureSpinner, Units.Temperature.Name);
        fillSpinner(elevationSpinner, Units.Length.Name);
        fillSpinner(trueAltitudeSpinner, Units.Length.Name);

        calibratedAltitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && StringUtils.isNumeric(s)) {
                    inputCalibratedAltitude = new BigDecimal(s.toString());
                    calculate();
                } else {
                    inputCalibratedAltitude = null;
                    trueAltitudeText.setText(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        calibratedAltitudeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCalibratedAltitudeUnit = ((Unit) parent.getItemAtPosition(position)).Name;
                calculate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                    trueAltitudeText.setText(null);
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
                    trueAltitudeText.setText(null);
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
        trueAltitudeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTrueAltitudeUnit = ((Unit) parent.getItemAtPosition(position)).Name;
                calculate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        elevationText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && StringUtils.isNumeric(s)) {
                    inputElevation = new BigDecimal(s.toString());
                    calculate();
                } else {
                    inputElevation = null;
                    trueAltitudeText.setText(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        elevationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedElevationUnit = ((Unit) parent.getItemAtPosition(position)).Name;
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
                && inputCalibratedAltitude != null
                && selectedCalibratedAltitudeUnit != null
                && inputTemperature != null
                && selectedTemperatureUnit != null
                && inputElevation != null
                && selectedElevationUnit != null
                && selectedTrueAltitudeUnit != null) {

            Measurement temperature = calculator.calculateTrueAltitude(
                    new Measurement(inputPressureAltitude, selectedPressureAltitudeUnit),
                    new Measurement(inputCalibratedAltitude, selectedCalibratedAltitudeUnit),
                    new Measurement(inputTemperature, selectedTemperatureUnit),
                    new Measurement(inputElevation, selectedElevationUnit),
                    selectedTrueAltitudeUnit);

            NumberFormat format = NumberFormat.getInstance();
            format.setMaximumFractionDigits(2);
            trueAltitudeText.setText(format.format(temperature.getMagnitude()));
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
        outState.putString(CALIBRATED_ALTITUDE, calibratedAltitude.getText().toString());
        outState.putString(CALIBRATED_ALTITUDE_UNIT, selectedCalibratedAltitudeUnit);
        outState.putString(TEMPERATURE, temperatureText.getText().toString());
        outState.putString(TEMPERATURE_UNIT, selectedTemperatureUnit);
        outState.putString(ELEVATION, elevationText.getText().toString());
        outState.putString(ELEVATION_UNIT, selectedElevationUnit);
        outState.putString(TRUE_ALTITUDE_UNIT, selectedTrueAltitudeUnit);
    }

    @Override
    protected void onRestoreState(Bundle inState) {
        if (inState != null) {
            pressureAltitude.setText(inState.getCharSequence(PRESSURE_ALTITUDE));
            selectedPressureAltitudeUnit = inState.getString(PRESSURE_ALTITUDE_UNIT);
            pressureAltitudeSpinner.setSelection(((UnitAdapter) pressureAltitudeSpinner.getAdapter())
                    .getPositionByName(selectedPressureAltitudeUnit));

            calibratedAltitude.setText(inState.getCharSequence(CALIBRATED_ALTITUDE));
            selectedCalibratedAltitudeUnit = inState.getString(CALIBRATED_ALTITUDE_UNIT);
            calibratedAltitudeSpinner.setSelection(((UnitAdapter) calibratedAltitudeSpinner.getAdapter())
                    .getPositionByName(selectedCalibratedAltitudeUnit));

            temperatureText.setText(inState.getCharSequence(TEMPERATURE));
            selectedTemperatureUnit = inState.getString(TEMPERATURE_UNIT);
            temperatureSpinner.setSelection(((UnitAdapter) temperatureSpinner.getAdapter())
                    .getPositionByName(selectedTemperatureUnit));

            elevationText.setText(inState.getCharSequence(ELEVATION));
            selectedElevationUnit = inState.getString(ELEVATION_UNIT);
            elevationSpinner.setSelection(((UnitAdapter) elevationSpinner.getAdapter())
                    .getPositionByName(selectedElevationUnit));

            selectedTrueAltitudeUnit = inState.getString(TRUE_ALTITUDE_UNIT);
            trueAltitudeSpinner.setSelection(((UnitAdapter) trueAltitudeSpinner.getAdapter())
                    .getPositionByName(selectedTrueAltitudeUnit));
        }
    }
}
