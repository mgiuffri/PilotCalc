package com.marianogiuffrida.pilotcalc.UI.fragments.Altimetry;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.marianogiuffrida.helpers.FragmentUtils;
import com.marianogiuffrida.pilotcalc.R;
import com.marianogiuffrida.pilotcalc.UI.fragments.Atmosphere.PressureFragment;
import com.marianogiuffrida.pilotcalc.UI.fragments.Atmosphere.StandardTemperatureFragment;
import com.marianogiuffrida.pilotcalc.UI.fragments.Atmosphere.TemperatureFromAltitudeFragment;
import com.marianogiuffrida.pilotcalc.UI.fragments.Atmosphere.TemperatureFromMachFragment;
import com.marianogiuffrida.pilotcalc.UI.notification.OnSelectionListener;

/**
 * Created by Mariano on 15/08/2015.
 */
public class AltimetryCalculationPickerFragment extends Fragment {

    private View rootView;
    private Button pressureAltitude;
    private Button densityAltitude;
    private Button trueAltitude;
    private OnSelectionListener mListener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_altimetry_choose_calculation, container, false);
        pressureAltitude = (Button) rootView.findViewById(R.id.pressureAltitude);
        densityAltitude = (Button) rootView.findViewById(R.id.densityAltitude);
        trueAltitude = (Button) rootView.findViewById(R.id.trueAltitude);

        pressureAltitude.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onNewSelection(PressureAltitudeFragment.ID);
                    }
                }
        );

        densityAltitude.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onNewSelection(DensityAltitudeFragment.ID);
                    }
                }
        );

        trueAltitude.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onNewSelection(TrueAltitudeFragment.ID);
                    }
                }
        );

        mListener = FragmentUtils.getParent(this, OnSelectionListener.class);
        return rootView;
    }
}
