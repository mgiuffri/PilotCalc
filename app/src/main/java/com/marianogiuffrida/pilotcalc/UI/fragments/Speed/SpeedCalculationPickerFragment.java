package com.marianogiuffrida.pilotcalc.UI.fragments.Speed;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.marianogiuffrida.helpers.FragmentUtils;
import com.marianogiuffrida.pilotcalc.R;
import com.marianogiuffrida.pilotcalc.UI.fragments.Altimetry.DensityAltitudeFragment;
import com.marianogiuffrida.pilotcalc.UI.fragments.Altimetry.PressureAltitudeFragment;
import com.marianogiuffrida.pilotcalc.UI.fragments.Altimetry.TrueAltitudeFragment;
import com.marianogiuffrida.pilotcalc.UI.notification.OnSelectionListener;

/**
 * Created by Mariano on 15/08/2015.
 */
public class SpeedCalculationPickerFragment extends Fragment {

    private View rootView;
    private Button trueAirspeed;
    private Button machFromAltitude;
    private Button machFromTemperature;
    private Button soundSpeed;
    private OnSelectionListener mListener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_speed_choose_calculation, container, false);
        trueAirspeed = (Button) rootView.findViewById(R.id.trueAirspeed);
        machFromAltitude = (Button) rootView.findViewById(R.id.machFromAltitude);
        machFromTemperature = (Button) rootView.findViewById(R.id.machFromTemperature);
        soundSpeed = (Button) rootView.findViewById(R.id.soundSpeed);

        trueAirspeed.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onNewSelection(TrueAirspeedFragment.ID);
                    }
                }
        );

        machFromAltitude.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onNewSelection(MachFromAltitudeFragment.ID);
                    }
                }
        );

        machFromTemperature.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onNewSelection(MachFromTemperatureFragment.ID);
                    }
                }
        );

        soundSpeed.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onNewSelection(SoundSpeedFragment.ID);
                    }
                }
        );

        mListener = FragmentUtils.getParent(this, OnSelectionListener.class);
        return rootView;
    }
}
