package com.marianogiuffrida.pilotcalc;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.marianogiuffrida.pilotcalc.database.UnitConversionDatabase;
import com.marianogiuffrida.pilotcalc.model.ShuntingYardEvaluator;
import com.marianogiuffrida.pilotcalc.model.UnitConversionDescriptor;
import com.marianogiuffrida.pilotcalc.model.UnitConversionHelper;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.List;

public class ShuntingYardEvaluatorTest extends ApplicationTestCase<Application> {

    public ShuntingYardEvaluatorTest() {
        super(Application.class);
    }

    private ArrayList<String> BadFormedExpressions = new ArrayList<String>() {{
        add("ddee");
        add("5**3");
        add("*5");
        add("*");
        add("5+");
        add("5*");
        add("5+34*");
        add("5+34*-");
        add("-+5+34");
        add("-+5*-34");
    }};

    private ArrayList<String> WellFormedExpressions = new ArrayList<String>() {{
        add("5");
        add("-5");
        add("5+7");
        add("5+7*6");
        add("5*7+6");
        add("5*7-6");
        add("-5+34*-5");
        add("5+28÷-7");
        add("5*2*-1");
        add("10÷-2÷-1");
        add("10-2+1");
        add("10+2-1");
    }};

    private ArrayList<Double> ExpectedResults = new ArrayList<Double>() {{
        add(5.0);
        add(-5.0);
        add(12.0);
        add(47.0);
        add(41.0);
        add(29.0);
        add(-175.0);
        add(1.0);
        add(-10.0);
        add(5.0);
        add(9.0);
        add(11.0);
    }};

    public void testShouldReturnFalseWhenExpressionIsNotWellFormed() throws Exception {
        for (String s : BadFormedExpressions) {
            assertFalse(s, ShuntingYardEvaluator.IsWellFormedExpression(s));
        }
    }

    public void testShouldReturnTrueWhenExpressionIsWellFormed() throws Exception {
        for (String s : WellFormedExpressions) {
            assertTrue(s, ShuntingYardEvaluator.IsWellFormedExpression(s));
        }
    }

    public void testShouldReturnCorrectValue() throws Exception {
        for (int i = 0; i < WellFormedExpressions.size(); i++) {
            String s = WellFormedExpressions.get(i);
            Double r = ExpectedResults.get(i);
            assertEquals(String.format("%s -> %.2f", s, r),
                    r,
                    ShuntingYardEvaluator.Evaluate(s),
                    0.01);
        }
    }
}