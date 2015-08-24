package com.marianogiuffrida.pilotcalc.UI.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.marianogiuffrida.pilotcalc.R;
import com.marianogiuffrida.pilotcalc.UI.adapters.UnitAdapter;
import com.marianogiuffrida.pilotcalc.data.SqlLiteDataStore;
import com.marianogiuffrida.pilotcalc.data.UnitConversionRepository;
import com.marianogiuffrida.pilotcalc.model.Common.Measurement;
import com.marianogiuffrida.pilotcalc.model.Common.Unit;
import com.marianogiuffrida.pilotcalc.model.Conversions.Units;
import com.marianogiuffrida.pilotcalc.model.WindTriangle.CompassDirection;
import com.marianogiuffrida.pilotcalc.model.WindTriangle.WindTriangleCalculator;
import com.marianogiuffrida.pilotcalc.model.WindTriangle.WindTriangleVector;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelChangedListener;
import antistatic.spinnerwheel.adapters.NumericWheelAdapter;

public class GroundVectorFragment extends Fragment {
    public static final int ID = 1;

    private View rootView;
    private Spinner trueAirspeedSpinner;
    private Spinner groundSpeedSpinner;
    private Spinner windSpeedSpinner;
    private UnitConversionRepository unitConversionsRepository;

    private String selectedGroundSpeedUnit;
    private String selectedTrueAirspeedUnit;
    private String selectedWindSpeedUnit;
    private WindTriangleCalculator calculator;
    private EditText tasEditText;
    private EditText windSpeedEditText;
    private BigDecimal inputTas;
    private BigDecimal inputWS;
    private TextView trackText;
    private TextView groundSpeedText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_ground_vector, container, false);
        trueAirspeedSpinner = (Spinner) rootView.findViewById(R.id.tasSpinner);
        groundSpeedSpinner = (Spinner) rootView.findViewById(R.id.groundSpeedSpinner);
        windSpeedSpinner = (Spinner) rootView.findViewById(R.id.windSpeedSpinner);

        tasEditText = (EditText) rootView.findViewById(R.id.tas);
        windSpeedEditText = (EditText) rootView.findViewById(R.id.windSpeed);
        trackText = (TextView) rootView.findViewById(R.id.track);
        groundSpeedText = (TextView) rootView.findViewById(R.id.groundSpeed);

        SqlLiteDataStore dataStore = new SqlLiteDataStore(getActivity().getApplicationContext());
        unitConversionsRepository = new UnitConversionRepository(dataStore);
        calculator = new WindTriangleCalculator(unitConversionsRepository);

        initWheel(R.id.heading0, 3);
        getWheel(R.id.heading0).addChangingListener(limitHeadingTrigger);
        initWheel(R.id.heading1, 9);
        initWheel(R.id.heading2, 9);

        initWheel(R.id.wind0, 3);
        getWheel(R.id.wind0).addChangingListener(limitWindTrigger);
        initWheel(R.id.wind1, 9);
        initWheel(R.id.wind2, 9);

        selectedGroundSpeedUnit = fillSpinner(groundSpeedSpinner);
        selectedWindSpeedUnit = fillSpinner(windSpeedSpinner);
        selectedTrueAirspeedUnit = fillSpinner(trueAirspeedSpinner);

        tasEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    inputTas = new BigDecimal(s.toString());
                    calculate();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        windSpeedEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    inputWS = new BigDecimal(s.toString());
                    calculate();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        groundSpeedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGroundSpeedUnit = ((Unit) parent.getItemAtPosition(position)).Name;
                calculate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        windSpeedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedWindSpeedUnit = ((Unit) parent.getItemAtPosition(position)).Name;
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

    private String fillSpinner(Spinner spinner) {
        List<Unit> units = unitConversionsRepository.getSupportedUnitsByConversionType(Units.Speed.Name);
        UnitAdapter adapter = new UnitAdapter(getActivity(), R.layout.spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return units.get(0).Name;
    }

    private void initWheel(int id, int maxValue) {
        AbstractWheel wheel = getWheel(id);
        wheel.setViewAdapter(new NumericWheelAdapter(this.getActivity().getApplicationContext(), 0, maxValue));
        wheel.setCurrentItem(0);
        wheel.addChangingListener(calculationTrigger);
        wheel.setVisibleItems(3);
        wheel.setCyclic(true);
        wheel.setInterpolator(new AnticipateOvershootInterpolator());
    }

    private OnWheelChangedListener calculationTrigger = new OnWheelChangedListener() {
        public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
            calculate();
        }
    };

    private OnWheelChangedListener limitHeadingTrigger = new OnWheelChangedListener() {
        public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
            AbstractWheel heading1 = getWheel(R.id.heading1);
            if (wheel.getCurrentItem() == 3)
                heading1.setViewAdapter(new NumericWheelAdapter(getActivity().getApplicationContext(), 0, 5));
            else{
                heading1.setViewAdapter(new NumericWheelAdapter(getActivity().getApplicationContext(), 0, 9));
            }
        }
    };

    private OnWheelChangedListener limitWindTrigger = new OnWheelChangedListener() {
        public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
            AbstractWheel wind1 = getWheel(R.id.wind1);
            if (wheel.getCurrentItem() == 3)
                wind1.setViewAdapter(new NumericWheelAdapter(getActivity().getApplicationContext(), 0, 5));
            else{
                wind1.setViewAdapter(new NumericWheelAdapter(getActivity().getApplicationContext(), 0, 9));
            }
        }
    };


    private AbstractWheel getWheel(int id) {
        return (AbstractWheel) rootView.findViewById(id);
    }

    private int getCurrentItem(int id) {
        return getWheel(id).getCurrentItem();
    }

    private CompassDirection getWind() {
        return new CompassDirection(getCurrentItem(R.id.wind0) * 100
                + getCurrentItem(R.id.wind1) * 10 + getCurrentItem(R.id.wind2), 0, 0);
    }

    private CompassDirection getHeading() {
        return new CompassDirection(getCurrentItem(R.id.heading0) * 100
                + getCurrentItem(R.id.heading1) * 10 + getCurrentItem(R.id.heading2), 0, 0);
    }

    private void calculate() {
        CompassDirection wind = getWind();
        CompassDirection heading = getHeading();
        if (inputWS != null && inputTas != null) {
            WindTriangleVector groundVector = calculator.calculateGroundVector(
                    new WindTriangleVector(wind, new Measurement(inputWS, selectedWindSpeedUnit)),
                    new WindTriangleVector(heading, new Measurement(inputTas, selectedTrueAirspeedUnit)),
                    selectedGroundSpeedUnit);

            NumberFormat format = NumberFormat.getInstance();
            format.setMaximumFractionDigits(0);
            trackText.setText(format.format(groundVector.getDirection().getDegrees()));
            groundSpeedText.setText(format.format(groundVector.getSpeed().getMagnitude()));
        }
    }
}
