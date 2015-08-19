package com.marianogiuffrida.pilotcalc.UI.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;

import com.marianogiuffrida.pilotcalc.R;

import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelChangedListener;
import antistatic.spinnerwheel.adapters.NumericWheelAdapter;

public class InFlightWindFragment extends Fragment {
    public static final int ID  = 0;

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_in_flight_wind, container, false);
        initWheel(R.id.heading0, 3);
        initWheel(R.id.heading1, 5);
        initWheel(R.id.heading2, 9);

        initWheel(R.id.track0, 3);
        initWheel(R.id.track1, 5);
        initWheel(R.id.track2, 9);

        return rootView;
    }

    private void initWheel(int id, int maxValue) {
        AbstractWheel wheel = getWheel(id);
        wheel.setViewAdapter(new NumericWheelAdapter(this.getActivity().getApplicationContext(), 0, maxValue));
        wheel.setCurrentItem(0);
//        OnWheelChangedListener changedListener;
//        wheel.addChangingListener(changedListener);
//      changedListener  wheel.addScrollingListener(scrolledListener);
        wheel.setVisibleItems(3);
        wheel.setCyclic(true);
        wheel.setInterpolator(new AnticipateOvershootInterpolator());
    }

    private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
        public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
        }
    };

    private AbstractWheel getWheel(int id) {
        return (AbstractWheel) rootView.findViewById(id);
    }
}
