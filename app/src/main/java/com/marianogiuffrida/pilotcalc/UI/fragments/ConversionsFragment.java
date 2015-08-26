package com.marianogiuffrida.pilotcalc.UI.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.marianogiuffrida.helpers.StringUtils;
import com.marianogiuffrida.pilotcalc.R;
import com.marianogiuffrida.pilotcalc.UI.notification.IProvideResult;
import com.marianogiuffrida.pilotcalc.UI.adapters.UnitAdapter;
import com.marianogiuffrida.pilotcalc.data.SqlLiteDataStore;
import com.marianogiuffrida.pilotcalc.data.UnitConversionRepository;
import com.marianogiuffrida.pilotcalc.model.Conversions.ConversionCalculator;
import com.marianogiuffrida.pilotcalc.model.Conversions.Units;
import com.marianogiuffrida.pilotcalc.model.Common.Unit;
import com.marianogiuffrida.pilotcalc.model.Conversions.UnitConversionDescriptor;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mariano on 12/01/2015.
 */
public class ConversionsFragment extends StatedFragment implements IProvideResult {

    private static final String ACTIVE_CONVERSION_TYPE = "conversionsType";
    private static final String SELECTED_SOURCE_UNIT = "selectedSourceUnit";
    private static final String SELECTED_DESTINATION_UNIT = "selectedDestinationUnit";
    private static final HashMap<Integer, String> typeMap;
    private static final String INPUT_NUMBER = "input";
    private static final String OUTPUT_NUMBER = "output";
    private static final String CALCULATOR_TAG = "calculator";

    static {
        typeMap = new HashMap();
        typeMap.put(R.id.radio_weight, Units.Weight.Name);
        typeMap.put(R.id.radio_length, Units.Length.Name);
        typeMap.put(R.id.radio_temp, Units.Temperature.Name);
        typeMap.put(R.id.radio_pressure, Units.Pressure.Name);
        typeMap.put(R.id.radio_speed, Units.Speed.Name);
        typeMap.put(R.id.radio_volume, Units.Volume.Name);
    }

    private Spinner sourceUnitSpinner, destinationUnitSpinner;
    private TextView inputTextView, outputTextView;
    private UnitConversionRepository unitConversionsRepository;
    private ConversionCalculator conversionCalculator;
    private RadioGroup radioConvType;
    private final int defaultConversionType = R.id.radio_length;
    private String selectedSourceUnit, selectedDestinationUnit;
    private ImageButton swapUnitsButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_conversions, container, false);
        destinationUnitSpinner = (Spinner) rootView.findViewById(R.id.conversions_to_spinner);
        sourceUnitSpinner = (Spinner) rootView.findViewById(R.id.conversions_from_spinner);
        inputTextView = (TextView) rootView.findViewById(R.id.conversion_Input);
        outputTextView = (TextView) rootView.findViewById(R.id.conversion_output);
        swapUnitsButton = (ImageButton) rootView.findViewById(R.id.swap_units);
        radioConvType = (RadioGroup) rootView.findViewById(R.id.radio_conversionType);

        SqlLiteDataStore dataStore = new SqlLiteDataStore(getActivity().getApplicationContext());
        unitConversionsRepository = new UnitConversionRepository(dataStore);
        conversionCalculator = new ConversionCalculator(unitConversionsRepository);



        swapUnitsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchUnits();
            }
        });

        destinationUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDestinationUnit = ((Unit) parent.getItemAtPosition(position)).Name;
                convertInputValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sourceUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSourceUnit = ((Unit) parent.getItemAtPosition(position)).Name;
                fillDestinationUnitSpinner(selectedSourceUnit);
                convertInputValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        radioConvType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onSelectedConversionType(checkedId);
            }
        });

        return rootView;
    }

    @Override
    protected void onFirstTimeLaunched() {
        super.onFirstTimeLaunched();
        Fragment calculatorFragment = new CalculatorFragment();
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.conversions_child_fragment, calculatorFragment, CALCULATOR_TAG)
                .commit();
        initializeViewBasedOnConversionType(defaultConversionType);
    }

    @Override
    protected void onRestoreState(Bundle savedInstanceState) {
        int activeConversionType = savedInstanceState.getInt(ACTIVE_CONVERSION_TYPE);
        initializeViewBasedOnConversionType(activeConversionType);
        selectedSourceUnit = savedInstanceState.getString(SELECTED_SOURCE_UNIT);
        selectedDestinationUnit = savedInstanceState.getString(SELECTED_DESTINATION_UNIT);
        fillDestinationUnitSpinner(selectedSourceUnit);
        destinationUnitSpinner.setSelection(((UnitAdapter) destinationUnitSpinner.getAdapter()).getPositionByName(selectedDestinationUnit));
        sourceUnitSpinner.setSelection(((UnitAdapter) sourceUnitSpinner.getAdapter()).getPositionByName(selectedSourceUnit));
        inputTextView.setText(savedInstanceState.getString(INPUT_NUMBER));
        outputTextView.setText(savedInstanceState.getString(OUTPUT_NUMBER));
    }

    private void initializeViewBasedOnConversionType(int activeConversionType) {
        radioConvType.check(activeConversionType);
        onSelectedConversionType(activeConversionType);
    }

    @Override
    public void onSaveState(Bundle outState) {
        outState.putInt(ACTIVE_CONVERSION_TYPE, radioConvType.getCheckedRadioButtonId());
        outState.putString(SELECTED_SOURCE_UNIT, selectedSourceUnit);
        outState.putString(SELECTED_DESTINATION_UNIT, selectedDestinationUnit);
        outState.putString(INPUT_NUMBER, inputTextView.getText().toString());
        outState.putString(OUTPUT_NUMBER, outputTextView.getText().toString());
    }

    private void onSelectedConversionType(int checkedId) {
        selectedSourceUnit = "";
        selectedDestinationUnit = "";
        fillSourceUnitSpinner(typeMap.get(checkedId));
    }

    private void fillSourceUnitSpinner(String conversionType) {
        List<Unit> sourceUnits = unitConversionsRepository.getSupportedUnitsByConversionType(conversionType);
        UnitAdapter adapter = new UnitAdapter(getActivity(), R.layout.spinner_item,
                sourceUnits);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceUnitSpinner.setAdapter(adapter);
        selectedSourceUnit = sourceUnits.get(0).Name;
    }

    private void fillDestinationUnitSpinner(String fromUnit) {
        List<Unit> destinationUnits = unitConversionsRepository.getDestinationUnitsBySourceUnit(fromUnit);
        UnitAdapter adapter = new UnitAdapter(getActivity(), R.layout.spinner_item, destinationUnits);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationUnitSpinner.setAdapter(adapter);
        if (selectedDestinationUnit != null) {
            int index = adapter.getPositionByName(selectedDestinationUnit);
            if (index > 0) destinationUnitSpinner.setSelection(index);
        } else {
            selectedDestinationUnit = destinationUnits.get(0).Name;
        }
    }

    @Override
    public void onNewResult(String result) {
        inputTextView.setText(result);
        convertInputValue();
    }

    private void convertInputValue() {
        String inputValue = inputTextView.getText().toString();
        if (StringUtils.isNullOrEmpty(inputValue) ||
                StringUtils.isNullOrEmpty(selectedSourceUnit) ||
                StringUtils.isNullOrEmpty(selectedDestinationUnit)) return;
        UnitConversionDescriptor d = unitConversionsRepository.getUnitConversionDescriptorBySourceDestination(selectedSourceUnit, selectedDestinationUnit);
        DecimalFormat defaultFormat = new DecimalFormat("0.##");
        String convertedValue = defaultFormat.format(conversionCalculator.convert(Double.parseDouble(inputValue), d));
        outputTextView.setText(convertedValue);
    }

    private void switchUnits() {
        destinationUnitSpinner.setSelection(((UnitAdapter) destinationUnitSpinner.getAdapter()).getPositionByName(selectedSourceUnit));
        sourceUnitSpinner.setSelection(((UnitAdapter) sourceUnitSpinner.getAdapter()).getPositionByName(selectedDestinationUnit));
        inputTextView.setText(outputTextView.getText());
    }
}