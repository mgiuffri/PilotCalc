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

public class PressureAltitudeFragment extends StatefulFragment {
    public static final int ID = 0;
    private static final String ELEVATION = "elevation";
    private static final String ELEVATION_UNIT = "elevation_unit";
    private static final String PRESSURE = "pressure";
    private static final String PRESSURE_UNIT = "pressure_unit";
    private static final String ALTITUDE_UNIT = "altitude_unit";

    private View rootView;
    private UnitConversionRepository unitConversionsRepository;
    private StandardAtmosphere calculator;

    private BigDecimal inputPressure;
    private String selectedPressureUnit;
    private EditText pressureText;
    private Spinner pressureSpinner;

    private BigDecimal inputElevation;
    private String selectedElevationUnit;
    private EditText elevationText;
    private Spinner elevationSpinner;

    private String selectedAltitudeUnit;
    private TextView altitudeText;
    private Spinner altitudeSpinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_altimetry_pressure_altitude, container, false);

        SqlLiteDataStore dataStore = new SqlLiteDataStore(getActivity().getApplicationContext());
        unitConversionsRepository = new UnitConversionRepository(dataStore);
        calculator = new StandardAtmosphere(unitConversionsRepository);

        pressureText = (EditText) rootView.findViewById(R.id.pressure);
        pressureSpinner = (Spinner) rootView.findViewById(R.id.pressureSpinner);

        elevationText = (EditText) rootView.findViewById(R.id.elevation);
        elevationSpinner = (Spinner) rootView.findViewById(R.id.elevationSpinner);

        altitudeText = (TextView) rootView.findViewById(R.id.altitude);
        altitudeSpinner = (Spinner) rootView.findViewById(R.id.altitudeSpinner);

        fillSpinner(pressureSpinner, Units.Pressure.Name);
        fillSpinner(elevationSpinner, Units.Length.Name);
        fillSpinner(altitudeSpinner, Units.Length.Name);

        pressureText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && StringUtils.isNumeric(s)) {
                    inputPressure = new BigDecimal(s.toString());
                    calculate();
                } else {
                    inputPressure = null;
                    altitudeText.setText(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pressureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPressureUnit = ((Unit) parent.getItemAtPosition(position)).Name;
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
                    altitudeText.setText(null);
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
        altitudeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAltitudeUnit = ((Unit) parent.getItemAtPosition(position)).Name;
                calculate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    private void calculate() {
        if (inputPressure != null
                && selectedPressureUnit != null
                && inputElevation != null
                && selectedElevationUnit != null
                && selectedAltitudeUnit != null) {

            Measurement pressureAltitude = calculator.calculatePressureAltitude(
                    new Measurement(inputPressure, selectedPressureUnit),
                    new Measurement(inputElevation, selectedElevationUnit),
                    selectedAltitudeUnit);

            NumberFormat format = NumberFormat.getInstance();
            format.setMaximumFractionDigits(2);
            altitudeText.setText(format.format(pressureAltitude.getMagnitude()));
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
        outState.putString(PRESSURE, pressureText.getText().toString());
        outState.putString(PRESSURE_UNIT, selectedPressureUnit);
        outState.putString(ELEVATION, elevationText.getText().toString());
        outState.putString(ELEVATION_UNIT, selectedElevationUnit);
        outState.putString(ALTITUDE_UNIT, selectedAltitudeUnit);
    }

    @Override
    protected void onRestoreState(Bundle inState) {
        if (inState != null) {
            pressureText.setText(inState.getCharSequence(PRESSURE));
            selectedPressureUnit = inState.getString(PRESSURE_UNIT);
            pressureSpinner.setSelection(((UnitAdapter) pressureSpinner.getAdapter())
                    .getPositionByName(selectedPressureUnit));

            elevationText.setText(inState.getCharSequence(ELEVATION));
            selectedElevationUnit = inState.getString(ELEVATION_UNIT);
            elevationSpinner.setSelection(((UnitAdapter) elevationSpinner.getAdapter())
                    .getPositionByName(selectedElevationUnit));

            selectedAltitudeUnit = inState.getString(ALTITUDE_UNIT);
            altitudeSpinner.setSelection(((UnitAdapter) altitudeSpinner.getAdapter())
                    .getPositionByName(selectedAltitudeUnit));

        }
    }
}
