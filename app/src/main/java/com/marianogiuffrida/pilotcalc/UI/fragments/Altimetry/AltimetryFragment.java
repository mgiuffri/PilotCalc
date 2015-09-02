package com.marianogiuffrida.pilotcalc.UI.fragments.Altimetry;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marianogiuffrida.helpers.FragmentUtils;
import com.marianogiuffrida.pilotcalc.R;
import com.marianogiuffrida.pilotcalc.UI.BackButtonHandledFragment;
import com.marianogiuffrida.pilotcalc.UI.fragments.Atmosphere.AtmosphereCalculationPickerFragment;
import com.marianogiuffrida.pilotcalc.UI.fragments.Atmosphere.PressureFragment;
import com.marianogiuffrida.pilotcalc.UI.fragments.Atmosphere.StandardTemperatureFragment;
import com.marianogiuffrida.pilotcalc.UI.fragments.Atmosphere.TemperatureFromAltitudeFragment;
import com.marianogiuffrida.pilotcalc.UI.fragments.Atmosphere.TemperatureFromMachFragment;
import com.marianogiuffrida.pilotcalc.UI.notification.OnSelectionListener;
import com.marianogiuffrida.pilotcalc.UI.notification.OnTitleChangeListener;

public class AltimetryFragment extends BackButtonHandledFragment implements OnSelectionListener {
    private View rootView;
    private OnTitleChangeListener onTitleChangeListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_altimetry, container, false);

        if(savedInstanceState == null){
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.altimetry_frame_container, new AltimetryCalculationPickerFragment())
                    .commit();
        }

        onTitleChangeListener = FragmentUtils.getParent(this, OnTitleChangeListener.class);
        if(onTitleChangeListener != null) onTitleChangeListener.newTitle(R.string.AltimetryCalculator);

        return rootView;
    }

    @Override
    public void onNewSelection(int position) {
        switch (position) {
            case PressureAltitudeFragment.ID:
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.altimetry_frame_container, new PressureAltitudeFragment())
                        .addToBackStack("altimetry")
                        .commit();
                break;
            case DensityAltitudeFragment.ID:
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.altimetry_frame_container, new DensityAltitudeFragment())
                        .addToBackStack("altimetry")
                        .commit();
                break;
            case TrueAltitudeFragment.ID:
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.altimetry_frame_container, new TrueAltitudeFragment())
                        .addToBackStack("altimetry")
                        .commit();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onBackPressed() {
        if(getChildFragmentManager().getBackStackEntryCount() > 0){
            getChildFragmentManager().popBackStack();
            return true;
        }
        return false;
    }
}
