package com.marianogiuffrida.pilotcalc.UI.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.marianogiuffrida.helpers.StringUtils;
import com.marianogiuffrida.pilotcalc.R;
import com.marianogiuffrida.pilotcalc.UI.notifications.IProvideResult;
import com.marianogiuffrida.pilotcalc.UI.adapters.UnitAdapter;
import com.marianogiuffrida.pilotcalc.model.UnitConversions;
import com.marianogiuffrida.pilotcalc.model.ConversionTypes;
import com.marianogiuffrida.pilotcalc.model.Unit;
import com.marianogiuffrida.pilotcalc.model.UnitConversionDescriptor;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mariano on 12/01/2015.
 */
public class ConversionsFragment extends Fragment implements IProvideResult {

    private static final String ACTIVE_CONVERSION_TYPE = "conversionsType";
    private static final String SELECTED_SOURCE_UNIT = "selectedSourceUnit";
    private static final String SELECTED_DESTINATION_UNIT = "selectedDestinationUnit";
    private static final HashMap<Integer, String> typeMap;
    static{
        typeMap = new HashMap();
        typeMap.put(R.id.radio_weight, ConversionTypes.Weight);
        typeMap.put(R.id.radio_length, ConversionTypes.Length);
        typeMap.put(R.id.radio_temp, ConversionTypes.Temperature);
        typeMap.put(R.id.radio_pressure, ConversionTypes.Pressure);
        typeMap.put(R.id.radio_speed, ConversionTypes.Speed);
        typeMap.put(R.id.radio_volume, ConversionTypes.Volume);
    }

    private Spinner sourceUnitSpinner, destinationUnitSpinner;
    private TextView inputTextView, outputTextView;
    private UnitConversions unitConversionsRepository;
//    private RadioButtonsTable radioConversionsType;
    private RadioGroup radioConvType;
    private final int defaultConversionType = R.id.radio_length;
    private String selectedSourceUnit, selectedDestinationUnit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.conversions_fragment, container, false);
        destinationUnitSpinner = (Spinner) rootView.findViewById(R.id.conversions_to_spinner);
        sourceUnitSpinner = (Spinner) rootView.findViewById(R.id.conversions_from_spinner);
        inputTextView = (TextView) rootView.findViewById(R.id.conversion_Input);
        outputTextView = (TextView) rootView.findViewById(R.id.conversion_output);
        unitConversionsRepository = new UnitConversions(getActivity().getApplicationContext());

        Fragment calculatorFragment = new CalculatorFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.conversions_child_fragment, calculatorFragment).commit();

//        radioConversionsType = (RadioButtonsTable) rootView.findViewById(R.id.radio_conversionType);
//        radioConversionsType.setOnCheckedChangeListener(new RadioButtonsTable.OnCheckedChangeListener() {
//            @Override
//            public void onCheckChanged(View view, int checkedId) {
//                onSelectedConversionType(checkedId);
//            }
//        });
        radioConvType = (RadioGroup) rootView.findViewById(R.id.radio_conversionType);
        radioConvType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onSelectedConversionType(checkedId);
            }
        });

        if (savedInstanceState != null) {
            int activeConversionType = savedInstanceState.getInt(ACTIVE_CONVERSION_TYPE);
            initializeViewBasedOnConversionType(rootView, activeConversionType);
            selectedSourceUnit = savedInstanceState.getString(SELECTED_SOURCE_UNIT);
            selectedDestinationUnit = savedInstanceState.getString(SELECTED_DESTINATION_UNIT);
            fillDestinationUnitSpinner(selectedSourceUnit);
            destinationUnitSpinner.setSelection(((UnitAdapter) destinationUnitSpinner.getAdapter()).getPositionByName(selectedDestinationUnit));
            sourceUnitSpinner.setSelection(((UnitAdapter) sourceUnitSpinner.getAdapter()).getPositionByName(selectedSourceUnit));
        } else {
            initializeViewBasedOnConversionType(rootView, defaultConversionType);
        }

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

        return rootView;
    }

    private void initializeViewBasedOnConversionType(View rootView, int activeConversionType) {
//        radioConversionsType.setActiveRadioButton((RadioButton) rootView.findViewById(activeConversionType));
        radioConvType.check(activeConversionType);
        onSelectedConversionType(activeConversionType);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
//        savedInstanceState.putInt(ACTIVE_CONVERSION_TYPE, radioConversionsType.getCheckedRadioButtonId());
        savedInstanceState.putInt(ACTIVE_CONVERSION_TYPE, radioConvType.getCheckedRadioButtonId());
        savedInstanceState.putString(SELECTED_SOURCE_UNIT, selectedSourceUnit);
        savedInstanceState.putString(SELECTED_DESTINATION_UNIT, selectedDestinationUnit);
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
        }else{
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
        DecimalFormat defaultFormat = new DecimalFormat( "0.######" );
        String convertedValue = defaultFormat.format(unitConversionsRepository.getConverter().convertValue(Double.parseDouble(inputValue), d));
        outputTextView.setText(convertedValue);
    }

    private void switchUnits(){
        destinationUnitSpinner.setSelection(((UnitAdapter) destinationUnitSpinner.getAdapter()).getPositionByName(selectedSourceUnit));
        sourceUnitSpinner.setSelection(((UnitAdapter) sourceUnitSpinner.getAdapter()).getPositionByName(selectedDestinationUnit));
    }

}