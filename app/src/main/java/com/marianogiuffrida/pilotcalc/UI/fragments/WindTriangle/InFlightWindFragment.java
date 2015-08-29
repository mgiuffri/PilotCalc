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

public class InFlightWindFragment extends StatefulFragment {
    public static final int ID = 0;
    private static final String HEADING0 = "heading0";
    private static final String HEADING1 = "heading1";
    private static final String HEADING2 = "heading2";
    private static final String TRACK0 = "track0";
    private static final String TRACK1 = "track1";
    private static final String TRACK2 = "track2";
    private static final String TAS = "tas";
    private static final String TAS_UNIT = "tas_UNIT";
    private static final String GS = "GS";
    private static final String GS_UNIT = "gs_unit";
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
    private EditText tasEditText;
    private EditText groundSpeedEditText;
    private BigDecimal inputTas;
    private BigDecimal inputGS;
    private TextView windDirectionText;
    private TextView windSpeedText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_wind_in_flight, container, false);
        trueAirspeedSpinner = (Spinner) rootView.findViewById(R.id.tasSpinner);
        groundSpeedSpinner = (Spinner) rootView.findViewById(R.id.groundSpeedSpinner);
        windSpeedSpinner = (Spinner) rootView.findViewById(R.id.windSpeedSpinner);

        tasEditText = (EditText) rootView.findViewById(R.id.tas);
        groundSpeedEditText = (EditText) rootView.findViewById(R.id.groundSpeed);
        windDirectionText = (TextView) rootView.findViewById(R.id.windDirection);
        windSpeedText = (TextView) rootView.findViewById(R.id.windSpeed);

        SqlLiteDataStore dataStore = new SqlLiteDataStore(getActivity().getApplicationContext());
        unitConversionsRepository = new UnitConversionRepository(dataStore);
        calculator = new WindTriangleCalculator(unitConversionsRepository);

        initWheel(R.id.heading0, 3);
        getWheel(R.id.heading0).addChangingListener(limitHeadingTrigger);
        initWheel(R.id.heading1, 9);
        initWheel(R.id.heading2, 9);

        initWheel(R.id.track0, 3);
        getWheel(R.id.track0).addChangingListener(limitTrackTrigger);
        initWheel(R.id.track1, 9);
        initWheel(R.id.track2, 9);

        selectedGroundSpeedUnit = fillSpinner(groundSpeedSpinner);
        selectedWindSpeedUnit = fillSpinner(windSpeedSpinner);
        selectedTrueAirspeedUnit = fillSpinner(trueAirspeedSpinner);

        tasEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    inputTas = new BigDecimal(s.toString());
                    calculate();
                } else {
                    inputTas = null;
                    windDirectionText.setText(null);
                    windSpeedText.setText(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        groundSpeedEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    inputGS = new BigDecimal(s.toString());
                    calculate();
                } else {
                    inputGS = null;
                    windDirectionText.setText(null);
                    windSpeedText.setText(null);
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
    public void onSaveState(Bundle outState) {
        outState.putInt(TRACK0, getCurrentItem(R.id.track0));
        outState.putInt(TRACK1, getCurrentItem(R.id.track1));
        outState.putInt(TRACK2, getCurrentItem(R.id.track2));
        outState.putInt(HEADING0, getCurrentItem(R.id.heading0));
        outState.putInt(HEADING1, getCurrentItem(R.id.heading1));
        outState.putInt(HEADING2, getCurrentItem(R.id.heading2));
        outState.putString(GS, groundSpeedEditText.getText().toString());
        outState.putString(GS_UNIT, selectedGroundSpeedUnit);
        outState.putString(TAS, tasEditText.getText().toString());
        outState.putString(TAS_UNIT, selectedTrueAirspeedUnit);
        outState.putString(WIND_UNIT, selectedWindSpeedUnit);
    }
    @Override
    protected void onRestoreState(Bundle inState) {
        if (inState != null) {
            getWheel(R.id.track0).setCurrentItem(inState.getInt(TRACK0));
            getWheel(R.id.track1).setCurrentItem(inState.getInt(TRACK1));
            getWheel(R.id.track2).setCurrentItem(inState.getInt(TRACK2));
            getWheel(R.id.heading0).setCurrentItem(inState.getInt(HEADING0));
            getWheel(R.id.heading1).setCurrentItem(inState.getInt(HEADING1));
            getWheel(R.id.heading2).setCurrentItem(inState.getInt(HEADING2));
            tasEditText.setText(inState.getCharSequence(TAS));
            selectedTrueAirspeedUnit = inState.getString(TAS_UNIT);
            trueAirspeedSpinner.setSelection(((UnitAdapter) trueAirspeedSpinner.getAdapter()).getPositionByName(selectedTrueAirspeedUnit));

            groundSpeedEditText.setText(inState.getCharSequence(GS));
            selectedGroundSpeedUnit = inState.getString(GS_UNIT);
            groundSpeedSpinner.setSelection(((UnitAdapter) groundSpeedSpinner.getAdapter()).getPositionByName(selectedGroundSpeedUnit));

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

    private OnWheelChangedListener limitHeadingTrigger = new OnWheelChangedListener() {
        public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
            AbstractWheel heading1 = getWheel(R.id.heading1);
            if (wheel.getCurrentItem() == 3)
                heading1.setViewAdapter(new NumericWheelAdapter(getActivity().getApplicationContext(), 0, 5));
            else {
                heading1.setViewAdapter(new NumericWheelAdapter(getActivity().getApplicationContext(), 0, 9));
            }
        }
    };

    private OnWheelChangedListener limitTrackTrigger = new OnWheelChangedListener() {
        public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
            AbstractWheel track1 = getWheel(R.id.track1);
            if (wheel.getCurrentItem() == 3)
                track1.setViewAdapter(new NumericWheelAdapter(getActivity().getApplicationContext(), 0, 5));
            else {
                track1.setViewAdapter(new NumericWheelAdapter(getActivity().getApplicationContext(), 0, 9));
            }
        }
    };


    private AbstractWheel getWheel(int id) {
        return (AbstractWheel) rootView.findViewById(id);
    }

    private int getCurrentItem(int id) {
        return getWheel(id).getCurrentItem();
    }

    private CompassDirection getTrack() {
        return new CompassDirection(getCurrentItem(R.id.track0) * 100
                + getCurrentItem(R.id.track1) * 10 + getCurrentItem(R.id.track2), 0, 0);
    }

    private CompassDirection getHeading() {
        return new CompassDirection(getCurrentItem(R.id.heading0) * 100
                + getCurrentItem(R.id.heading1) * 10 + getCurrentItem(R.id.heading2), 0, 0);
    }

    private void calculate() {
        CompassDirection track = getTrack();
        CompassDirection heading = getHeading();
        if (inputGS != null && inputTas != null) {
            WindTriangleVector wind = calculator.calculateWindVector(
                    new WindTriangleVector(track, new Measurement(inputGS, selectedGroundSpeedUnit)),
                    new WindTriangleVector(heading, new Measurement(inputTas, selectedTrueAirspeedUnit)),
                    selectedWindSpeedUnit);

            NumberFormat format = NumberFormat.getInstance();
            format.setMaximumFractionDigits(0);
            windDirectionText.setText(format.format(wind.getDirection().getDegrees()));
            windSpeedText.setText(format.format(wind.getSpeed().getMagnitude()));
        }
    }
}
