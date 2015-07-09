package com.marianogiuffrida.pilotcalc.UI.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.marianogiuffrida.pilotcalc.model.comm.Unit;

import java.util.List;

/**
 * Created by Mariano on 3/02/2015.
 */
public class UnitAdapter extends ArrayAdapter<Unit> {

    private final List<Unit> units;

    public UnitAdapter(Context context, int resource, List<Unit> units) {
        super(context,resource, units);
        this.units = units;
    }

    public int getPositionByName(String name){
        for(Unit u : units){
            if(u.Name.equals(name))
                return units.indexOf(u);
        }
        return -1;
    }

    public Unit getItemByName(String name) {
        for (Unit i : units){
            if(i.Name.equals(name)){
                return i;
            }
        }
        return null;
    }

}
