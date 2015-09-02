package com.marianogiuffrida.pilotcalc.UI.fragments.Speed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marianogiuffrida.helpers.FragmentUtils;
import com.marianogiuffrida.pilotcalc.R;
import com.marianogiuffrida.pilotcalc.UI.BackButtonHandledFragment;
import com.marianogiuffrida.pilotcalc.UI.fragments.Altimetry.AltimetryCalculationPickerFragment;
import com.marianogiuffrida.pilotcalc.UI.fragments.Altimetry.TrueAltitudeFragment;
import com.marianogiuffrida.pilotcalc.UI.fragments.Atmosphere.PressureFragment;
import com.marianogiuffrida.pilotcalc.UI.fragments.Atmosphere.StandardTemperatureFragment;
import com.marianogiuffrida.pilotcalc.UI.fragments.Atmosphere.TemperatureFromAltitudeFragment;
import com.marianogiuffrida.pilotcalc.UI.fragments.Atmosphere.TemperatureFromMachFragment;
import com.marianogiuffrida.pilotcalc.UI.notification.OnSelectionListener;
import com.marianogiuffrida.pilotcalc.UI.notification.OnTitleChangeListener;

public class SpeedFragment extends BackButtonHandledFragment implements OnSelectionListener {
    private View rootView;
    private OnTitleChangeListener onTitleChangeListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_speed, container, false);

        if(savedInstanceState == null){
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.speed_frame_container, new SpeedCalculationPickerFragment())
                    .commit();
        }

        onTitleChangeListener = FragmentUtils.getParent(this, OnTitleChangeListener.class);
        if(onTitleChangeListener != null) onTitleChangeListener.newTitle(R.string.SpeedCalculator);

        return rootView;
    }

    @Override
    public void onNewSelection(int position) {
        switch (position) {
            case TrueAirspeedFragment.ID:
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.speed_frame_container, new TrueAirspeedFragment())
                        .addToBackStack("speed")
                        .commit();
                break;
            case MachFromAltitudeFragment.ID:
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.speed_frame_container, new MachFromAltitudeFragment())
                        .addToBackStack("speed")
                        .commit();
                break;
            case MachFromTemperatureFragment.ID:
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.speed_frame_container, new MachFromTemperatureFragment())
                        .addToBackStack("speed")
                        .commit();
                break;
            case SoundSpeedFragment.ID:
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.speed_frame_container, new SoundSpeedFragment())
                        .addToBackStack("speed")
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
