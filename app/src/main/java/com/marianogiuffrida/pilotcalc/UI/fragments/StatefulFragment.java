package com.marianogiuffrida.pilotcalc.UI.fragments;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by Mariano on 26/08/2015.
 * Largely based on this article:
 * http://inthecheesefactory.com/blog/best-approach-to-keep-android-fragment-state/en
 */

public class StatefulFragment extends Fragment {

    public static final String INTERNAL_STATE = "internalState";
    Bundle savedState;

    public StatefulFragment() {
        if(getArguments() == null) {
            setArguments(new Bundle());
        }

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(!restoreStateFromArguments()) {
            onFirstTimeLaunched();
        }
    }

    protected void onFirstTimeLaunched() {
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveStateToArguments();
    }

    public void onDestroyView() {
        super.onDestroyView();
        saveStateToArguments();
    }

    private void saveStateToArguments() {
        if(getView() != null) {
            savedState = saveState();
        }

        if(savedState != null) {
            Bundle b = getArguments();
            if(b != null) {
                b.putBundle(INTERNAL_STATE, savedState);
            }
        }

    }

    private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
        if(b != null) {
            savedState = b.getBundle(INTERNAL_STATE);
            if(savedState != null) {
                restoreState();
                return true;
            }
        }

        return false;
    }

    private void restoreState() {
        if(savedState != null) {
            onRestoreState(savedState);
        }

    }

    protected void onRestoreState(Bundle savedInstanceState) {
    }

    private Bundle saveState() {
        Bundle state = new Bundle();
        onSaveState(state);
        return state;
    }

    protected void onSaveState(Bundle outState) {
    }
}
