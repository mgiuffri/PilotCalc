package com.marianogiuffrida.helpers;

import junit.framework.TestCase;

/**
 * Created by Mariano on 16/07/2015.
 */
public class StringUtilsTest extends TestCase {

    public void testShouldReturnTrueWhenCheckingIsNullString() {
        assertTrue(StringUtils.isNull(null));
        assertTrue(StringUtils.isNullOrEmpty(null));
        assertTrue(StringUtils.isNullOrWhiteSpace(null));
    }

    public void testShouldReturnFalseWhenChecingNullString() {
        assertFalse(StringUtils.isNull("ciao"));
    }

    public void testShouldReturnTrueWhenCheckingIsEmptyString() {
        assertTrue(StringUtils.isEmpty(""));
    }

    public void testShouldReturnFalseWhenCheckingIsEmptyString() {
        assertFalse(StringUtils.isEmpty("ciao"));
        assertFalse(StringUtils.isNullOrEmpty("ciao"));
    }

    public void testShouldReturnTrueWhenCheckingIsWhiteSpacesOnlyString() {
        assertTrue(StringUtils.isNullOrWhiteSpace("   "));
    }

    public void testShouldReturnFalseWhenCheckingIsWhiteSpaceOnlyString() {
        assertFalse(StringUtils.isNullOrWhiteSpace("ciao"));
    }
}