package com.marianogiuffrida.pilotcalc;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.marianogiuffrida.pilotcalc.database.UnitConversionDatabase;

/**
 * Created by Mariano on 12/01/2015.
 */
public class ConversionsFragment extends Fragment {

    public ConversionsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.conversions_fragment, container, false);
        Spinner fromUnit = (Spinner) rootView.findViewById(R.id.conversions_from_spinner);
        UnitConversionDatabase db = new UnitConversionDatabase(getActivity().getApplicationContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, db.getFromUnits());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromUnit.setAdapter(adapter);

        return rootView;
    }
}
