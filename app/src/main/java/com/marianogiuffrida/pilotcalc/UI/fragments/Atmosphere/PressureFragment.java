package com.marianogiuffrida.pilotcalc.UI.fragments.Atmosphere;

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
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;

public class PressureFragment extends StatefulFragment {
    public static final int ID = 3;
    private static final String ALTITUDE = "altitude";
    private static final String ALTITUDE_UNIT = "altitude_UNIT";
    private static final String PRESSURE_UNIT = "pressure_unit";

    private View rootView;
    private UnitConversionRepository unitConversionsRepository;
    private StandardAtmosphere calculator;
    private Spinner altitudeSpinner;
    private Spinner pressureSpinner;
    private BigDecimal inputAltitude;
    private String selectedAltitudeUnit;
    private String selectedPressureUnit;
    private TextView pressureText;
    private EditText altitudeEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_atmosphere_pressure, container, false);

        altitudeEditText = (EditText) rootView.findViewById(R.id.altitude);
        altitudeSpinner = (Spinner) rootView.findViewById(R.id.altitudeSpinner);

        pressureText = (TextView) rootView.findViewById(R.id.pressure);
        pressureSpinner = (Spinner) rootView.findViewById(R.id.pressureUnit);

        SqlLiteDataStore dataStore = new SqlLiteDataStore(getActivity().getApplicationContext());
        unitConversionsRepository = new UnitConversionRepository(dataStore);
        calculator = new StandardAtmosphere(unitConversionsRepository);

        fillSpinner(altitudeSpinner, Units.Length.Name);
        fillSpinner(pressureSpinner, Units.Pressure.Name);

        altitudeEditText.addTextChangedListener(new TextWatcher() {
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
                    pressureText.setText(null);
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
        if (inputAltitude != null && selectedAltitudeUnit != null && selectedPressureUnit != null) {
            Measurement pressure = calculator.calculatePressure(
                    new Measurement(inputAltitude, selectedAltitudeUnit),
                    selectedPressureUnit);

            NumberFormat format = NumberFormat.getInstance();
            format.setMaximumFractionDigits(2);
            pressureText.setText(format.format(pressure.getMagnitude()));
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
        outState.putString(ALTITUDE, altitudeEditText.getText().toString());
        outState.putString(ALTITUDE_UNIT, selectedAltitudeUnit);
        outState.putString(PRESSURE_UNIT, selectedPressureUnit);
    }

    @Override
    protected void onRestoreState(Bundle inState) {
        if (inState != null) {
            altitudeEditText.setText(inState.getCharSequence(ALTITUDE));
            selectedAltitudeUnit = inState.getString(ALTITUDE_UNIT);
            altitudeSpinner.setSelection(((UnitAdapter) altitudeSpinner.getAdapter()).getPositionByName(selectedAltitudeUnit));

            selectedPressureUnit = inState.getString(PRESSURE_UNIT);
            pressureSpinner.setSelection(((UnitAdapter) pressureSpinner.getAdapter()).getPositionByName(selectedPressureUnit));
        }
    }
}
