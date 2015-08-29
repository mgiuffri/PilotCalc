package com.marianogiuffrida.pilotcalc.UI.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marianogiuffrida.helpers.FragmentUtils;
import com.marianogiuffrida.pilotcalc.R;
import com.marianogiuffrida.pilotcalc.UI.notification.OnTitleChangeListener;

public class SplashFragment extends Fragment {

    private OnTitleChangeListener onTitleChangeListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        onTitleChangeListener = FragmentUtils.getParent(this, OnTitleChangeListener.class);
        if(onTitleChangeListener != null) onTitleChangeListener.newTitle(R.string.app_name);
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }
}
