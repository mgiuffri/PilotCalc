package com.marianogiuffrida.pilotcalc;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.marianogiuffrida.customview.RadioButtonsTable;
import com.marianogiuffrida.pilotcalc.database.UnitConversionDatabase;

/**
 * Created by Mariano on 12/01/2015.
 */
public class ConversionsFragment extends Fragment {

    public static final String ACTIVE_CONVERSION_TYPE = "conversionsType";
    private static final String FROM_UNIT_POSIITION_ID = "fromUnitPosiitionId" ;
    private static final String TO_UNIT_POSIITION_ID = "toUnitPosiitionId" ;

    private Spinner fromUnit;
    private UnitConversionDatabase db;
    private RadioButtonsTable radioConversionsType;
    private final int defaultConversionType = R.id.radio_weight;
    private Spinner toUnit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.conversions_fragment, container, false);
        fromUnit = (Spinner) rootView.findViewById(R.id.conversions_from_spinner);
        toUnit = (Spinner) rootView.findViewById(R.id.conversions_to_spinner);
        db = new UnitConversionDatabase(getActivity().getApplicationContext());

        Fragment calculatorFragment = new CalculatorFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.conversions_child_fragment, calculatorFragment).commit();

        radioConversionsType = (RadioButtonsTable) rootView.findViewById(R.id.radio_conversionType);
        radioConversionsType.setOnCheckedChangeListener(new RadioButtonsTable.OnCheckedChangeListener() {
            @Override
            public void onCheckChanged(View view, int checkedId) {
                resolveConversionType(checkedId);
            }
        });

        if(savedInstanceState!=null){
            int activeConversionType = savedInstanceState.getInt(ACTIVE_CONVERSION_TYPE);
            initializeViewBasedOnConversionType(rootView, activeConversionType);
            fromUnit.setSelection(savedInstanceState.getInt(FROM_UNIT_POSIITION_ID));
            toUnit.setSelection(savedInstanceState.getInt(TO_UNIT_POSIITION_ID));
        }else{
            initializeViewBasedOnConversionType(rootView, defaultConversionType);
        }

        fromUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setToUnitsSpinnerByFromUnits((String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        toUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    private void initializeViewBasedOnConversionType(View rootView, int activeConversionType) {
        radioConversionsType.setActiveRadioButton((RadioButton) rootView.findViewById(activeConversionType));
        resolveConversionType(activeConversionType);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(ACTIVE_CONVERSION_TYPE, radioConversionsType.getCheckedRadioButtonId());
        savedInstanceState.putInt(FROM_UNIT_POSIITION_ID, fromUnit.getSelectedItemPosition());
        savedInstanceState.putInt(TO_UNIT_POSIITION_ID, toUnit.getSelectedItemPosition());
    }

    private void resolveConversionType(int checkedId) {
        switch (checkedId) {
            case R.id.radio_weight:
                setFromUnitsSpinnerByType(UnitConversionDatabase.ConversionTypes.Weight);
                break;
            case R.id.radio_length:
                setFromUnitsSpinnerByType(UnitConversionDatabase.ConversionTypes.Length);
                break;
            case R.id.radio_temp:
                setFromUnitsSpinnerByType(UnitConversionDatabase.ConversionTypes.Temperature);
                break;
            case R.id.radio_pressure:
                setFromUnitsSpinnerByType(UnitConversionDatabase.ConversionTypes.Pressure);
                break;
            case R.id.radio_volume:
                setFromUnitsSpinnerByType(UnitConversionDatabase.ConversionTypes.Volume);
                break;
            case R.id.radio_speed:
                setFromUnitsSpinnerByType(UnitConversionDatabase.ConversionTypes.Speed);
                break;
        }
    }

    private void setFromUnitsSpinnerByType(String conversionType) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, db.getFromUnitsByType(conversionType));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromUnit.setAdapter(adapter);
    }

    private void setToUnitsSpinnerByFromUnits(String fromUnit) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, db.getDestinationUnits(fromUnit));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toUnit.setAdapter(adapter);
    }
}