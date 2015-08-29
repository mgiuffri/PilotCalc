package com.marianogiuffrida.pilotcalc.UI;

import android.os.Bundle;

import com.marianogiuffrida.pilotcalc.UI.fragments.StatefulFragment;

/**
 * Created by Mariano on 25/08/2015.
 */

public abstract class BackButtonHandledFragment extends StatefulFragment {
    protected OnSelectedFragmentListener onSelectedFragmentListener;

    public abstract boolean onBackPressed();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!(getActivity() instanceof OnSelectedFragmentListener)) {
            throw new ClassCastException("Hosting activity must implement BackHandlerInterface");
        } else {
            onSelectedFragmentListener = (OnSelectedFragmentListener) getActivity();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Mark this fragment as the selected Fragment.
        onSelectedFragmentListener.setSelectedFragment(this);
    }

    public interface OnSelectedFragmentListener {
        void setSelectedFragment(BackButtonHandledFragment backHandledFragment);
    }
}
