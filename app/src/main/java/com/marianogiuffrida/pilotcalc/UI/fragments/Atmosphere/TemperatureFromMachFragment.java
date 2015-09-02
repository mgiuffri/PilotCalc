package com.marianogiuffrida.pilotcalc.UI.fragments.Atmosphere;

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
import com.marianogiuffrida.pilotcalc.model.Altimetry.StandardAtmosphere;
import com.marianogiuffrida.pilotcalc.model.Common.Measurement;
import com.marianogiuffrida.pilotcalc.model.Common.Unit;
import com.marianogiuffrida.pilotcalc.model.Conversions.Units;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

public class TemperatureFromMachFragment extends StatefulFragment {
    public static final int ID = 1;
    private static final String INDICATED_TEMPERATURE = "indicated_Temperature";
    private static final String INDICATED_TEMPERATURE_UNIT = "indicated_Temperature_unit";
    private static final String OUTSIDE_TEMPERATURE_UNIT = "outside_Temperature_unit";
    private static final String MACH = "mach";
    private static final String COEFFICIENT = "coefficient";

    private View rootView;
    private UnitConversionRepository unitConversionsRepository;
    private StandardAtmosphere calculator;

    private Spinner outsideTemperatureSpinner;
    private Spinner indicatedTemperatureSpinner;
    private BigDecimal inputTemperature;
    private String selectedIndicatedTemperatureUnit;
    private String selectedOutsideTemperatureUnit;
    private TextView outsideTemperatureText;
    private EditText indicatedTemperatureText;
    private EditText machText;
    private EditText coefficientText;
    private BigDecimal inputMach;
    private BigDecimal inputCoeff;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_atmosphere_temperture_from_mach, container, false);

        SqlLiteDataStore dataStore = new SqlLiteDataStore(getActivity().getApplicationContext());
        unitConversionsRepository = new UnitConversionRepository(dataStore);
        calculator = new StandardAtmosphere(unitConversionsRepository);

        indicatedTemperatureText = (EditText) rootView.findViewById(R.id.indicatedTemperature);
        outsideTemperatureSpinner = (Spinner) rootView.findViewById(R.id.indicatedTemperatureSpinner);
        outsideTemperatureText = (TextView) rootView.findViewById(R.id.outsideTemperature);
        indicatedTemperatureSpinner = (Spinner) rootView.findViewById(R.id.outsideTemperatureUnit);

        machText = (EditText) rootView.findViewById(R.id.mach);
        coefficientText = (EditText) rootView.findViewById(R.id.coeff);
        coefficientText.setFilters(new InputFilter[]{ new RangedInputFilter(0.0, 1.0)});

        fillSpinner(indicatedTemperatureSpinner, Units.Temperature.Name);
        fillSpinner(outsideTemperatureSpinner, Units.Temperature.Name);

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
                    outsideTemperatureText.setText(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        machText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    inputMach = new BigDecimal(s.toString());
                    calculate();
                } else {
                    inputMach = null;
                    outsideTemperatureText.setText(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        coefficientText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    inputCoeff = new BigDecimal(s.toString());
                    calculate();
                } else {
                    inputMach = null;
                    outsideTemperatureText.setText(null);
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

        outsideTemperatureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOutsideTemperatureUnit = ((Unit) parent.getItemAtPosition(position)).Name;
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
                && inputMach != null
                && inputCoeff != null
                && selectedOutsideTemperatureUnit != null) {

            Measurement temperature = calculator.calculateOutsideAirTemperature(
                    new Measurement(inputTemperature, selectedIndicatedTemperatureUnit),
                    inputMach.doubleValue(), inputCoeff.doubleValue(), selectedOutsideTemperatureUnit);

            NumberFormat format = NumberFormat.getInstance();
            format.setMaximumFractionDigits(0);
            outsideTemperatureText.setText(format.format(temperature.getMagnitude()));
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
        outState.putString(OUTSIDE_TEMPERATURE_UNIT, selectedOutsideTemperatureUnit);
        outState.putString(MACH, machText.getText().toString());
        outState.putString(COEFFICIENT, coefficientText.getText().toString());
    }

    @Override
    protected void onRestoreState(Bundle inState) {
        if (inState != null) {
            indicatedTemperatureText.setText(inState.getCharSequence(INDICATED_TEMPERATURE));
            selectedIndicatedTemperatureUnit = inState.getString(INDICATED_TEMPERATURE_UNIT);
            outsideTemperatureSpinner.setSelection(((UnitAdapter) outsideTemperatureSpinner.getAdapter())
                    .getPositionByName(selectedOutsideTemperatureUnit));

            machText.setText(inState.getCharSequence(MACH));
            coefficientText.setText(inState.getCharSequence(COEFFICIENT));

            selectedOutsideTemperatureUnit = inState.getString(OUTSIDE_TEMPERATURE_UNIT);
            indicatedTemperatureSpinner.setSelection(((UnitAdapter) indicatedTemperatureSpinner.getAdapter())
                    .getPositionByName(selectedIndicatedTemperatureUnit));
        }
    }
}
