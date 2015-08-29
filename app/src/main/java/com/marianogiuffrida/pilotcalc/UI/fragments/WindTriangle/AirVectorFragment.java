package com.marianogiuffrida.pilotcalc.UI.fragments.WindTriangle;

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
import com.marianogiuffrida.pilotcalc.UI.fragments.StatefulFragment;
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

public class AirVectorFragment extends StatefulFragment {
    public static final int ID = 2;
    private static final String TRACK0 = "track0";
    private static final String TRACK1 = "track1";
    private static final String TRACK2 = "track2";
    private static final String WIND0 = "wind0";
    private static final String WIND1 = "wind1";
    private static final String WIND2 = "wind2";
    private static final String GS = "gs";
    private static final String TAS_UNIT = "tas_UNIT";
    private static final String GS_UNIT = "gs_unit";
    private static final String WIND_SPEED = "windspeed";
    private static final String WIND_UNIT = "wind_unit";

    private View rootView;
    private Spinner trueAirspeedSpinner;
    private Spinner groundSpeedSpinner;
    private Spinner windSpeedSpinner;
    private UnitConversionRepository unitConversionsRepository;

    private String selectedGroundSpeedUnit;
    private String selectedTrueAirspeedUnit;
    private String selectedWindSpeedUnit;
    private WindTriangleCalculator calculator;
    private EditText groundSpeedEditText;
    private EditText windSpeedEditText;
    private BigDecimal inputGroundSpeed;
    private BigDecimal inputWS;
    private TextView headingText;
    private TextView tasText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_wind_air_vector, container, false);
        trueAirspeedSpinner = (Spinner) rootView.findViewById(R.id.tasSpinner);
        groundSpeedSpinner = (Spinner) rootView.findViewById(R.id.groundSpeedSpinner);
        windSpeedSpinner = (Spinner) rootView.findViewById(R.id.windSpeedSpinner);

        groundSpeedEditText = (EditText) rootView.findViewById(R.id.groundSpeed);
        windSpeedEditText = (EditText) rootView.findViewById(R.id.windSpeed);
        headingText = (TextView) rootView.findViewById(R.id.heading);
        tasText = (TextView) rootView.findViewById(R.id.tas);

        SqlLiteDataStore dataStore = new SqlLiteDataStore(getActivity().getApplicationContext());
        unitConversionsRepository = new UnitConversionRepository(dataStore);
        calculator = new WindTriangleCalculator(unitConversionsRepository);

        initWheel(R.id.track0, 3);
        getWheel(R.id.track0).addChangingListener(limitTrackTrigger);
        initWheel(R.id.track1, 9);
        initWheel(R.id.track2, 9);

        initWheel(R.id.wind0, 3);
        getWheel(R.id.wind0).addChangingListener(limitWindTrigger);
        initWheel(R.id.wind1, 9);
        initWheel(R.id.wind2, 9);

        selectedGroundSpeedUnit = fillSpinner(groundSpeedSpinner);
        selectedWindSpeedUnit = fillSpinner(windSpeedSpinner);
        selectedTrueAirspeedUnit = fillSpinner(trueAirspeedSpinner);

        groundSpeedEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    inputGroundSpeed = new BigDecimal(s.toString());
                    calculate();
                }else {
                    inputGroundSpeed = null;
                    headingText.setText(null);
                    tasText.setText(null);
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
                }else {
                    inputWS = null;
                    headingText.setText(null);
                    tasText.setText(null);
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

    @Override
    protected void onSaveState(Bundle outState) {
        outState.putInt(TRACK0, getCurrentItem(R.id.track0));
        outState.putInt(TRACK1, getCurrentItem(R.id.track1));
        outState.putInt(TRACK2, getCurrentItem(R.id.track2));
        outState.putInt(WIND0, getCurrentItem(R.id.wind0));
        outState.putInt(WIND1, getCurrentItem(R.id.wind1));
        outState.putInt(WIND2, getCurrentItem(R.id.wind2));
        outState.putString(GS_UNIT, selectedGroundSpeedUnit);
        outState.putString(GS, groundSpeedEditText.getText().toString());
        outState.putString(TAS_UNIT, selectedTrueAirspeedUnit);
        outState.putString(WIND_SPEED, windSpeedEditText.getText().toString());
        outState.putString(WIND_UNIT, selectedWindSpeedUnit);
    }

    @Override
    protected void onRestoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            getWheel(R.id.track0).setCurrentItem(savedInstanceState.getInt(TRACK0));
            getWheel(R.id.track1).setCurrentItem(savedInstanceState.getInt(TRACK1));
            getWheel(R.id.track2).setCurrentItem(savedInstanceState.getInt(TRACK2));
            getWheel(R.id.wind0).setCurrentItem(savedInstanceState.getInt(WIND0));
            getWheel(R.id.wind1).setCurrentItem(savedInstanceState.getInt(WIND1));
            getWheel(R.id.wind2).setCurrentItem(savedInstanceState.getInt(WIND2));

            selectedTrueAirspeedUnit = savedInstanceState.getString(TAS_UNIT);
            trueAirspeedSpinner.setSelection(((UnitAdapter) trueAirspeedSpinner.getAdapter()).getPositionByName(selectedTrueAirspeedUnit));

            groundSpeedEditText.setText(savedInstanceState.getCharSequence(GS));
            selectedGroundSpeedUnit = savedInstanceState.getString(GS_UNIT);
            groundSpeedSpinner.setSelection(((UnitAdapter) groundSpeedSpinner.getAdapter()).getPositionByName(selectedGroundSpeedUnit));

            windSpeedEditText.setText(savedInstanceState.getCharSequence(WIND_SPEED));
            selectedWindSpeedUnit = savedInstanceState.getString(WIND_UNIT);
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

    private OnWheelChangedListener limitTrackTrigger = new OnWheelChangedListener() {
        public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
            AbstractWheel track1 = getWheel(R.id.track1);
            if (wheel.getCurrentItem() == 3)
                track1.setViewAdapter(new NumericWheelAdapter(getActivity().getApplicationContext(), 0, 5));
            else{
                track1.setViewAdapter(new NumericWheelAdapter(getActivity().getApplicationContext(), 0, 9));
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

    private CompassDirection getTrack() {
        return new CompassDirection(getCurrentItem(R.id.track0) * 100
                + getCurrentItem(R.id.track1) * 10 + getCurrentItem(R.id.track2), 0, 0);
    }

    private void calculate() {
        CompassDirection wind = getWind();
        CompassDirection track = getTrack();
        if (inputWS != null && inputGroundSpeed != null) {
            WindTriangleVector airVector = calculator.calculateAirVector(
                    new WindTriangleVector(wind, new Measurement(inputWS, selectedWindSpeedUnit)),
                    new WindTriangleVector(track, new Measurement(inputGroundSpeed, selectedTrueAirspeedUnit)),
                    selectedTrueAirspeedUnit);

            NumberFormat format = NumberFormat.getInstance();
            format.setMaximumFractionDigits(0);
            headingText.setText(format.format(airVector.getDirection().getDegrees()));
            tasText.setText(format.format(airVector.getSpeed().getMagnitude()));
        }
    }
}
