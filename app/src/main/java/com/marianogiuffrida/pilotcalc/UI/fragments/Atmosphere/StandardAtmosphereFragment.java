package com.marianogiuffrida.pilotcalc.UI.fragments.Atmosphere;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marianogiuffrida.helpers.FragmentUtils;
import com.marianogiuffrida.pilotcalc.R;
import com.marianogiuffrida.pilotcalc.UI.BackButtonHandledFragment;
import com.marianogiuffrida.pilotcalc.UI.notification.OnSelectionListener;
import com.marianogiuffrida.pilotcalc.UI.notification.OnTitleChangeListener;

public class StandardAtmosphereFragment extends BackButtonHandledFragment implements OnSelectionListener {
    private View rootView;
    private OnTitleChangeListener onTitleChangeListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_atmosphere, container, false);

        if(savedInstanceState == null){
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.atmosphere_frame_container, new AtmosphereCalculationPickerFragment())
                    .commit();
        }

        onTitleChangeListener = FragmentUtils.getParent(this, OnTitleChangeListener.class);
        if(onTitleChangeListener != null) onTitleChangeListener.newTitle(R.string.AtmosphereCalculator);

        return rootView;
    }

    @Override
    public void onNewSelection(int position) {
        switch (position) {
            case StandardTemperatureFragment.ID:
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.atmosphere_frame_container, new StandardTemperatureFragment())
                        .addToBackStack("atmosphere")
                        .commit();
                break;
            case TemperatureFromMachFragment.ID:
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.atmosphere_frame_container, new TemperatureFromMachFragment())
                        .addToBackStack("atmosphere")
                        .commit();
                break;
            case TemperatureFromAltitudeFragment.ID:
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.atmosphere_frame_container, new TemperatureFromAltitudeFragment())
                        .addToBackStack("atmosphere")
                        .commit();
                break;
            case PressureFragment.ID:
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.atmosphere_frame_container, new PressureFragment())
                        .addToBackStack("atmosphere")
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
