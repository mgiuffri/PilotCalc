package com.marianogiuffrida.pilotcalc.UI.fragments.WindTriangle;

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

/**
 * Created by Mariano on 17/08/2015.
 */
public class WindFragment extends BackButtonHandledFragment implements OnSelectionListener {

    private View rootView;
    private OnTitleChangeListener onTitleChangeListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_wind, container, false);

        if(savedInstanceState == null){
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.wind_frame_container, new WindCalculationPickerFragment())
                    .commit();
        }
        onTitleChangeListener = FragmentUtils.getParent(this, OnTitleChangeListener.class);
        if(onTitleChangeListener != null) onTitleChangeListener.newTitle(R.string.WindCalculator);

        return rootView;
    }

    @Override
    public void onNewSelection(int position) {
        switch (position) {
            case InFlightWindFragment.ID:
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.wind_frame_container, new InFlightWindFragment())
                        .addToBackStack("wind")
                        .commit();
                break;
            case GroundVectorFragment.ID:
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.wind_frame_container, new GroundVectorFragment())
                        .addToBackStack("wind")
                        .commit();
                break;
            case AirVectorFragment.ID:
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.wind_frame_container, new AirVectorFragment())
                        .addToBackStack("wind")
                        .commit();
                break;
            case WindComponentsFragment.ID:
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.wind_frame_container, new WindComponentsFragment())
                        .addToBackStack("wind")
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


