package com.marianogiuffrida.pilotcalc.UI.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.marianogiuffrida.helpers.FragmentUtils;
import com.marianogiuffrida.pilotcalc.R;
import com.marianogiuffrida.pilotcalc.UI.notification.IProvideResult;
import com.marianogiuffrida.pilotcalc.UI.notification.OnSelectionListener;

/**
 * Created by Mariano on 15/08/2015.
 */
public class WindCalculationPickerFragment extends Fragment {

    private View rootView;
    private Button inflightWindButton;
    private Button groundVectorButton;
    private Button airVectorButton;
    private Button componentsButton;
    private OnSelectionListener mListener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_wind_choose_calculation, container, false);
        inflightWindButton = (Button) rootView.findViewById(R.id.inflightwind);
        groundVectorButton = (Button) rootView.findViewById(R.id.groundvector);
        airVectorButton = (Button) rootView.findViewById(R.id.airvector);
        componentsButton = (Button) rootView.findViewById(R.id.windcomponents);

        inflightWindButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onNewSelection(InFlightWindFragment.ID);
                    }
                }
        );

        groundVectorButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onNewSelection(GroundVectorFragment.ID);
                    }
                }
        );

        airVectorButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onNewSelection(AirVectorFragment.ID);
                    }
                }
        );

        componentsButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onNewSelection(WindComponentsFragment.ID);
                    }
                }
        );

        mListener = FragmentUtils.getParent(this, OnSelectionListener.class);
        return rootView;
    }
}
