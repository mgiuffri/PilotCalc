package com.marianogiuffrida.pilotcalc.UI.fragments.Atmosphere;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.marianogiuffrida.helpers.FragmentUtils;
import com.marianogiuffrida.pilotcalc.R;
import com.marianogiuffrida.pilotcalc.UI.fragments.WindTriangle.AirVectorFragment;
import com.marianogiuffrida.pilotcalc.UI.fragments.WindTriangle.GroundVectorFragment;
import com.marianogiuffrida.pilotcalc.UI.fragments.WindTriangle.InFlightWindFragment;
import com.marianogiuffrida.pilotcalc.UI.fragments.WindTriangle.WindComponentsFragment;
import com.marianogiuffrida.pilotcalc.UI.notification.OnSelectionListener;

/**
 * Created by Mariano on 15/08/2015.
 */
public class AtmosphereCalculationPickerFragment extends Fragment {

    private View rootView;
    private Button standardTemperature;
    private Button temperatureMach;
    private Button temperaturePA;
    private Button pressure;
    private OnSelectionListener mListener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_atmosphere_choose_calculation, container, false);
        standardTemperature = (Button) rootView.findViewById(R.id.standardTemperature);
        temperatureMach = (Button) rootView.findViewById(R.id.outsideTemperatureMach);
        temperaturePA = (Button) rootView.findViewById(R.id.outsideTemperaturePressureAltitude);
        pressure = (Button) rootView.findViewById(R.id.pressure);

        standardTemperature.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onNewSelection(StandardTemperatureFragment.ID);
                    }
                }
        );

        temperatureMach.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onNewSelection(TemperatureFromMachFragment.ID);
                    }
                }
        );

        temperaturePA.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onNewSelection(TemperatureFromAltitudeFragment.ID);
                    }
                }
        );

        pressure.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onNewSelection(PressureFragment.ID);
                    }
                }
        );

        mListener = FragmentUtils.getParent(this, OnSelectionListener.class);
        return rootView;
    }
}
