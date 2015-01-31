package com.marianogiuffrida.pilotcalc.model;

import junit.framework.TestCase;

import java.io.IOException;
import java.util.ArrayList;

public class ShuntingYardEvaluatorTest extends TestCase {

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
        add("7÷7-1");
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
        add(0.0);
    }};

    public void testShouldReturnFalseWhenExpressionIsNotWellFormed() throws Exception {
        for (String s : BadFormedExpressions) {
            assertFalse(s, ShuntingYardEvaluator.isWellFormedExpression(s));
        }
    }

    public void testShouldReturnTrueWhenExpressionIsWellFormed() throws Exception {
        for (String s : WellFormedExpressions) {
            assertTrue(s, ShuntingYardEvaluator.isWellFormedExpression(s));
        }
    }

    public void testShouldReturnCorrectValue() throws Exception {
        for (int i = 0; i < WellFormedExpressions.size(); i++) {
            String s = WellFormedExpressions.get(i);
            Double r = ExpectedResults.get(i);
            assertEquals(String.format("%s -> %.2f", s, r),
                    r,
                    ShuntingYardEvaluator.evaluate(s),
                    0.01);
        }
    }

    public void testCalculateThrowsWhenBadFormedExpressions() {
        for (String s : BadFormedExpressions) {
            Exception e = null;
            try {
                ShuntingYardEvaluator.calculate(s);
            } catch (IllegalArgumentException ex) {
                e = ex;
            } catch (IOException e1) {
                e = e1;
            }
            assertNotNull(e);
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    public void testCalculateReturnsCorrectValue() throws Exception {
        for (int i = 0; i < WellFormedExpressions.size(); i++) {
            String s = WellFormedExpressions.get(i);
            Double r = ExpectedResults.get(i);
            assertEquals(String.format("%s -> %.2f", s, r),
                    r,
                    ShuntingYardEvaluator.calculate(s),
                    0.01);
        }
    }
}