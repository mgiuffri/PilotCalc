package com.marianogiuffrida.pilotcalc.UI.fragments.WindTriangle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marianogiuffrida.pilotcalc.R;
import com.marianogiuffrida.pilotcalc.UI.fragments.BackHandledFragment;
import com.marianogiuffrida.pilotcalc.UI.notification.OnSelectionListener;

/**
 * Created by Mariano on 17/08/2015.
 */
public class WindFragment extends BackHandledFragment implements OnSelectionListener {

    private View rootView;

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

        return rootView;
    }

    @Override
    public void onNewSelection(int position) {
        switch (position) {
            case InFlightWindFragment.ID:
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.wind_frame_container, new InFlightWindFragment())
                        .addToBackStack("ciao")
                        .commit();
                break;
            case GroundVectorFragment.ID:
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.wind_frame_container, new GroundVectorFragment())
                        .addToBackStack("ciao")
                        .commit();
                break;
            case AirVectorFragment.ID:
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.wind_frame_container, new AirVectorFragment())
                        .addToBackStack("ciao")
                        .commit();
                break;
            case WindComponentsFragment.ID:
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.wind_frame_container, new WindComponentsFragment())
                        .addToBackStack("ciao")
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


