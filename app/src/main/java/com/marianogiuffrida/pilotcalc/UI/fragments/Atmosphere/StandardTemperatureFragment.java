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
import java.text.NumberFormat;
import java.util.List;

public class StandardTemperatureFragment extends StatefulFragment {
    public static final int ID = 0;
    private static final String ALTITUDE = "altitude";
    private static final String ALTITUDE_UNIT = "altitude_UNIT";
    private static final String TEMPERATURE_UNIT = "temp_unit";

    private View rootView;
    private UnitConversionRepository unitConversionsRepository;
    private StandardAtmosphere calculator;
    private Spinner altitudeSpinner;
    private Spinner temperatureSpinner;
    private BigDecimal inputAltitude;
    private String selectedAltitudeUnit;
    private String selectedTemperatureUnit;
    private TextView temperatureText;
    private EditText altitudeEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_atmosphere_standard_temperature, container, false);

        altitudeEditText = (EditText) rootView.findViewById(R.id.altitude);
        altitudeSpinner = (Spinner) rootView.findViewById(R.id.altitudeSpinner);

        temperatureText = (TextView) rootView.findViewById(R.id.temperature);
        temperatureSpinner = (Spinner) rootView.findViewById(R.id.temperatureUnit);

        SqlLiteDataStore dataStore = new SqlLiteDataStore(getActivity().getApplicationContext());
        unitConversionsRepository = new UnitConversionRepository(dataStore);
        calculator = new StandardAtmosphere(unitConversionsRepository);

        fillSpinner(altitudeSpinner, Units.Length.Name);
        fillSpinner(temperatureSpinner, Units.Temperature.Name);

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
                    temperatureText.setText(null);
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
            if (inputAltitude != null && selectedAltitudeUnit != null) {
                Measurement temperature = calculator.calculateStandardTemperature(
                        new Measurement(inputAltitude, selectedAltitudeUnit),
                        selectedTemperatureUnit);

                NumberFormat format = NumberFormat.getInstance();
                format.setMaximumFractionDigits(0);
                temperatureText.setText(format.format(temperature.getMagnitude()));
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
        outState.putString(TEMPERATURE_UNIT, selectedTemperatureUnit);
    }

    @Override
    protected void onRestoreState(Bundle inState) {
        if (inState != null) {
            altitudeEditText.setText(inState.getCharSequence(ALTITUDE));
            selectedAltitudeUnit = inState.getString(ALTITUDE_UNIT);
            altitudeSpinner.setSelection(((UnitAdapter) altitudeSpinner.getAdapter()).getPositionByName(selectedAltitudeUnit));

            selectedTemperatureUnit = inState.getString(TEMPERATURE_UNIT);
            temperatureSpinner.setSelection(((UnitAdapter) temperatureSpinner.getAdapter()).getPositionByName(selectedTemperatureUnit));
        }
    }
}
