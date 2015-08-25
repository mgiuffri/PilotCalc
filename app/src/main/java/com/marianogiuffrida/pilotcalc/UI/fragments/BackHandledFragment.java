package com.marianogiuffrida.pilotcalc.UI.fragments;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by Mariano on 25/08/2015.
 */

public abstract class BackHandledFragment extends Fragment {
    protected BackHandlerInterface backHandlerInterface;

    public abstract String getTagText();

    public abstract boolean onBackPressed();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!(getActivity() instanceof BackHandlerInterface)) {
            throw new ClassCastException("Hosting activity must implement BackHandlerInterface");
        } else {
            backHandlerInterface = (BackHandlerInterface) getActivity();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // Mark this fragment as the selected Fragment.
        backHandlerInterface.setSelectedFragment(this);
    }

    public interface BackHandlerInterface {
        public void setSelectedFragment(BackHandledFragment backHandledFragment);
    }
}
