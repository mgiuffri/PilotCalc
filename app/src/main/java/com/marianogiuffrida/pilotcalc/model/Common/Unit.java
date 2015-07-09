package com.marianogiuffrida.pilotcalc.model.Common;

/**
 * Created by Mariano on 3/02/2015.
 */
public class Unit {

    public final String Name;
    public final String Symbol;

    public Unit(String name, String symbol) {
        Name = name;
        Symbol = symbol;
    }

    @Override
    public String toString() {
        return Symbol;
    }
}