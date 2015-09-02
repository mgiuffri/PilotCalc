package com.marianogiuffrida.pilotcalc.UI;

import android.text.InputFilter;
import android.text.Spanned;

import java.math.BigDecimal;

/**
 * Created by Mariano on 02/09/2015.
 */
public class RangedInputFilter implements InputFilter {

    private final double min;
    private final double max;

    public RangedInputFilter(double min, double max){
        this.min = min;
        this.max = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            double input = Double.parseDouble(dest.toString() + source.toString());
            if (isInRange(min, max, input))
                return null;
        } catch (NumberFormatException nfe) { }
        return "";
    }

    private boolean isInRange(double min, double max, double input) {
        return (input >= min) && (input <= max);
    }
}
