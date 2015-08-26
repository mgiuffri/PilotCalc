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
import com.marianogiuffrida.pilotcalc.model.WindTriangle.WindComponents;
import com.marianogiuffrida.pilotcalc.model.WindTriangle.WindTriangleCalculator;
import com.marianogiuffrida.pilotcalc.model.WindTriangle.WindTriangleVector;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelChangedListener;
import antistatic.spinnerwheel.adapters.NumericWheelAdapter;

public class WindComponentsFragment extends StatedFragment {
    public static final int ID = 3;
    private static final String RWY0 = "rwy0";
    private static final String RWY1 = "rwy1";
    private static final String WIND0 = "wind0";
    private static final String WIND1 = "wind1";
    private static final String WIND2 = "wind2";
    private static final String WIND_SPEED = "windspeed";
    private static final String WIND_UNIT = "wind_unit";

    private View rootView;
    private TextView headwindUnitText;
    private TextView crosswindUnitText;
    private Spinner windSpeedSpinner;
    private UnitConversionRepository unitConversionsRepository;

    private String selectedWindSpeedUnit;
    private WindTriangleCalculator calculator;
    private EditText windSpeedEditText;
    private BigDecimal inputWS;
    private TextView headwindText;
    private TextView crosswindText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_wind_components, container, false);
        headwindUnitText = (TextView) rootView.findViewById(R.id.headwindUnit);
        crosswindUnitText = (TextView) rootView.findViewById(R.id.crosswindUnit);
        windSpeedSpinner = (Spinner) rootView.findViewById(R.id.windSpeedSpinner);

        windSpeedEditText = (EditText) rootView.findViewById(R.id.windSpeed);
        headwindText = (TextView) rootView.findViewById(R.id.headwind);
        crosswindText = (TextView) rootView.findViewById(R.id.crosswind);

        SqlLiteDataStore dataStore = new SqlLiteDataStore(getActivity().getApplicationContext());
        unitConversionsRepository = new UnitConversionRepository(dataStore);
        calculator = new WindTriangleCalculator(unitConversionsRepository);

        initWheel(R.id.rwy0, 3);
        getWheel(R.id.rwy0).addChangingListener(limitRunwayTrigger);
        initWheel(R.id.rwy1, 9);

        initWheel(R.id.wind0, 3);
        getWheel(R.id.wind0).addChangingListener(limitWindTrigger);
        initWheel(R.id.wind1, 9);
        initWheel(R.id.wind2, 9);

        selectedWindSpeedUnit = fillSpinner(windSpeedSpinner);
        windSpeedEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    inputWS = new BigDecimal(s.toString());
                    calculate();
                } else {
                    inputWS = null;
                    crosswindText.setText(null);
                    headwindText.setText(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        windSpeedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Unit unit = ((Unit) parent.getItemAtPosition(position));
                selectedWindSpeedUnit = unit.Name;
                headwindUnitText.setText(unit.Symbol);
                crosswindUnitText.setText(unit.Symbol);
                calculate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return rootView;
    }

    @Override
    public void onSaveState(Bundle outState) {
        outState.putInt(RWY0, getCurrentItem(R.id.rwy0));
        outState.putInt(RWY1, getCurrentItem(R.id.rwy1));
        outState.putInt(WIND0, getCurrentItem(R.id.wind0));
        outState.putInt(WIND1, getCurrentItem(R.id.wind1));
        outState.putInt(WIND2, getCurrentItem(R.id.wind2));
        outState.putString(WIND_SPEED, windSpeedEditText.getText().toString());
        outState.putString(WIND_UNIT, selectedWindSpeedUnit);
    }

    @Override
    protected void onRestoreState(Bundle inState) {
        if (inState != null) {
            getWheel(R.id.rwy0).setCurrentItem(inState.getInt(RWY0));
            getWheel(R.id.rwy1).setCurrentItem(inState.getInt(RWY1));
            getWheel(R.id.wind0).setCurrentItem(inState.getInt(WIND0));
            getWheel(R.id.wind1).setCurrentItem(inState.getInt(WIND1));
            getWheel(R.id.wind2).setCurrentItem(inState.getInt(WIND2));
            windSpeedEditText.setText(inState.getCharSequence(WIND_SPEED));
            selectedWindSpeedUnit = inState.getString(WIND_UNIT);
            windSpeedSpinner.setSelection(((UnitAdapter) windSpeedSpinner.getAdapter()).getPositionByName(selectedWindSpeedUnit));
        }
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

    private OnWheelChangedListener limitRunwayTrigger = new OnWheelChangedListener() {
        public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
            AbstractWheel rwy1 = getWheel(R.id.rwy1);
            if (wheel.getCurrentItem() == 3)
                rwy1.setViewAdapter(new NumericWheelAdapter(getActivity().getApplicationContext(), 0, 5));
            else {
                rwy1.setViewAdapter(new NumericWheelAdapter(getActivity().getApplicationContext(), 0, 9));
            }
        }
    };

    private OnWheelChangedListener limitWindTrigger = new OnWheelChangedListener() {
        public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
            AbstractWheel wind1 = getWheel(R.id.wind1);
            if (wheel.getCurrentItem() == 3)
                wind1.setViewAdapter(new NumericWheelAdapter(getActivity().getApplicationContext(), 0, 5));
            else {
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

    private CompassDirection getRunwayHeading() {
        return new CompassDirection(getCurrentItem(R.id.rwy0) * 100
                + getCurrentItem(R.id.rwy1) * 10, 0, 0);
    }

    private void calculate() {
        CompassDirection wind = getWind();
        CompassDirection runwayHeading = getRunwayHeading();
        if (inputWS != null) {
            WindComponents windComponents = calculator.calculateWindComponents(
                    runwayHeading, new WindTriangleVector(wind, new Measurement(inputWS, selectedWindSpeedUnit)),
                    selectedWindSpeedUnit);

            NumberFormat format = NumberFormat.getInstance();
            format.setMaximumFractionDigits(0);
            headwindText.setText(format.format(windComponents.getHeadWind().getMagnitude()));
            crosswindText.setText(format.format(windComponents.getCrossWind().getMagnitude()));
        }
    }
}
