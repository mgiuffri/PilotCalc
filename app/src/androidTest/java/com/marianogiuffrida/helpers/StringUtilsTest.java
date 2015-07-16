package com.marianogiuffrida.helpers;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Created by Mariano on 16/07/2015.
 */
public class StringUtilsTest extends TestCase {

    @Test
    public void testShouldReturnTrueWhenCheckingIsNullString() {
        assertTrue(StringUtils.isNull(null));
        assertTrue(StringUtils.isNullOrEmpty(null));
        assertTrue(StringUtils.isNullOrWhiteSpace(null));
    }

    @Test
    public void testShouldReturnFalseWhenChecingNullString() {
        assertFalse(StringUtils.isNull("ciao"));
    }

    @Test
    public void testShouldReturnTrueWhenCheckingIsEmptyString() {
        assertTrue(StringUtils.isEmpty(""));
    }

    @Test
    public void testShouldReturnFalseWhenCheckingIsEmptyString() {
        assertFalse(StringUtils.isEmpty("ciao"));
        assertFalse(StringUtils.isNullOrEmpty("ciao"));
    }

    @Test
    public void testShouldReturnTrueWhenCheckingIsWhiteSpacesOnlyString() {
        assertTrue(StringUtils.isNullOrWhiteSpace("   "));
    }

    @Test
    public void testShouldReturnFalseWhenCheckingIsWhiteSpaceOnlyString() {
        assertFalse(StringUtils.isNullOrWhiteSpace("ciao"));
    }
}