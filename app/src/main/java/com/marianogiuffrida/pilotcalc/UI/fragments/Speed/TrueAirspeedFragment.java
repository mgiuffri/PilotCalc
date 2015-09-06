package com.marianogiuffrida.pilotcalc.UI.fragments.Speed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
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
import com.marianogiuffrida.pilotcalc.UI.RangedInputFilter;
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

public class TrueAirspeedFragment extends StatefulFragment{
    public static final int ID = 0;
    private static final String INDICATED_TEMPERATURE = "indicated_Temperature";
    private static final String INDICATED_TEMPERATURE_UNIT = "indicated_Temperature_unit";
    private static final String CALIB_AIRSPEED = "cal_airspeed";
    private static final String CALIB_AIRSPEED_UNIT = "cal_airspeed_unit";
    private static final String ALTITUDE = "altitude";
    private static final String ALTITUDE_UNIT = "altitude_unit";
    private static final String COEFFICIENT = "coefficient";
    private static final String TRUE_AIRSPEED_UNIT = "true_airspeed_unit";

    private View rootView;
    private UnitConversionRepository unitConversionsRepository;
    private SpeedCalculator calculator;

    private BigDecimal inputTemperature;
    private String selectedIndicatedTemperatureUnit;
    private EditText indicatedTemperatureText;
    private Spinner indicatedTemperatureSpinner;

    private BigDecimal inputAltitude;
    private String selectedAltitudeUnit;
    private EditText inputAltitudeText;
    private Spinner inputAltitudeSpinner;

    private BigDecimal inputCalibratedAirspeed;
    private String selectedCalibratedAirspeedUnit;
    private EditText inputCalibratedAirspeedText;
    private Spinner inputCalibratedAirspeedSpinner;

    private Spinner trueAirspeedSpinner;
    private TextView trueAirspeedText;
    private String selectedTrueAirspeedUnit;

    private BigDecimal inputCoeff;
    private EditText inputCoeffText;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_speed_true_airspeed, container, false);

        SqlLiteDataStore dataStore = new SqlLiteDataStore(getActivity().getApplicationContext());
        unitConversionsRepository = new UnitConversionRepository(dataStore);
        calculator = new SpeedCalculator(unitConversionsRepository);

        indicatedTemperatureText = (EditText) rootView.findViewById(R.id.indicatedTemperature);
        indicatedTemperatureSpinner = (Spinner) rootView.findViewById(R.id.indicatedTemperatureSpinner);

        inputCalibratedAirspeedText = (EditText) rootView.findViewById(R.id.airspeed);
        inputCalibratedAirspeedSpinner = (Spinner) rootView.findViewById(R.id.airspeedSpinner);

        inputAltitudeText = (EditText) rootView.findViewById(R.id.altitude);
        inputAltitudeSpinner = (Spinner) rootView.findViewById(R.id.altitudeSpinner);

        trueAirspeedText = (TextView) rootView.findViewById(R.id.trueAirspeed);
        trueAirspeedSpinner = (Spinner) rootView.findViewById(R.id.trueAirspeedSpinner);

        inputCoeffText = (EditText)rootView.findViewById(R.id.coeff);
        inputCoeffText.setFilters(new InputFilter[]{ new RangedInputFilter(0.0, 1.0)});

        fillSpinner(indicatedTemperatureSpinner, Units.Temperature.Name);
        fillSpinner(inputCalibratedAirspeedSpinner, Units.Speed.Name);
        fillSpinner(inputAltitudeSpinner, Units.Length.Name);
        fillSpinner(trueAirspeedSpinner, Units.Speed.Name);

        indicatedTemperatureText.addTextChangedListener(new TextWatcher() {
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
                    trueAirspeedText.setText(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputCoeffText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    inputCoeff = new BigDecimal(s.toString());
                    calculate();
                } else {
                    inputCoeff = null;
                    trueAirspeedText.setText(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputAltitudeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    inputAltitude = new BigDecimal(s.toString());
                    calculate();
                } else {
                    inputAltitude = null;
                    trueAirspeedText.setText(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputCalibratedAirspeedText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    inputCalibratedAirspeed = new BigDecimal(s.toString());
                    calculate();
                } else {
                    inputCalibratedAirspeed = null;
                    trueAirspeedText.setText(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        indicatedTemperatureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedIndicatedTemperatureUnit = ((Unit) parent.getItemAtPosition(position)).Name;
                calculate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        inputCalibratedAirspeedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCalibratedAirspeedUnit = ((Unit) parent.getItemAtPosition(position)).Name;
                calculate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        inputAltitudeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAltitudeUnit = ((Unit) parent.getItemAtPosition(position)).Name;
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
                && inputCalibratedAirspeed != null
                && selectedCalibratedAirspeedUnit != null
                && inputAltitude!= null
                && selectedAltitudeUnit != null
                && inputCoeff != null
                && selectedTrueAirspeedUnit != null) {

            Measurement temperature = calculator.calculateTrueAirspeed(
                    new Measurement(inputCalibratedAirspeed, selectedCalibratedAirspeedUnit),
                    new Measurement(inputAltitude, selectedAltitudeUnit),
                    new Measurement(inputTemperature, selectedIndicatedTemperatureUnit),
                    inputCoeff.doubleValue(), selectedTrueAirspeedUnit);

            NumberFormat format = NumberFormat.getInstance();
            format.setMaximumFractionDigits(2);
            trueAirspeedText.setText(format.format(temperature.getMagnitude()));
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
        outState.putString(INDICATED_TEMPERATURE, indicatedTemperatureText.getText().toString());
        outState.putString(INDICATED_TEMPERATURE_UNIT, selectedIndicatedTemperatureUnit);
        outState.putString(CALIB_AIRSPEED, inputCalibratedAirspeedText.getText().toString());
        outState.putString(CALIB_AIRSPEED_UNIT, selectedCalibratedAirspeedUnit);
        outState.putString(ALTITUDE, inputAltitudeText.getText().toString());
        outState.putString(ALTITUDE_UNIT, selectedAltitudeUnit);
        outState.putString(TRUE_AIRSPEED_UNIT, selectedTrueAirspeedUnit);
        outState.putString(COEFFICIENT, inputCoeffText.getText().toString());
    }

    @Override
    protected void onRestoreState(Bundle inState) {
        if (inState != null) {
            indicatedTemperatureText.setText(inState.getCharSequence(INDICATED_TEMPERATURE));
            selectedIndicatedTemperatureUnit = inState.getString(INDICATED_TEMPERATURE_UNIT);
            indicatedTemperatureSpinner.setSelection(((UnitAdapter) indicatedTemperatureSpinner.getAdapter())
                    .getPositionByName(selectedIndicatedTemperatureUnit));

            inputCalibratedAirspeedText.setText(inState.getCharSequence(CALIB_AIRSPEED));
            selectedCalibratedAirspeedUnit = inState.getString(CALIB_AIRSPEED_UNIT);
            inputCalibratedAirspeedSpinner.setSelection(((UnitAdapter) inputCalibratedAirspeedSpinner.getAdapter())
                    .getPositionByName(selectedCalibratedAirspeedUnit));

            inputAltitudeText.setText(inState.getCharSequence(ALTITUDE));
            selectedAltitudeUnit = inState.getString(ALTITUDE_UNIT);
            inputAltitudeSpinner.setSelection(((UnitAdapter) inputAltitudeSpinner.getAdapter())
                    .getPositionByName(selectedAltitudeUnit));

            inputCoeffText.setText(inState.getCharSequence(COEFFICIENT));

            selectedTrueAirspeedUnit = inState.getString(TRUE_AIRSPEED_UNIT);
            trueAirspeedSpinner.setSelection(((UnitAdapter) trueAirspeedSpinner.getAdapter())
                    .getPositionByName(selectedTrueAirspeedUnit));

        }
    }

}
