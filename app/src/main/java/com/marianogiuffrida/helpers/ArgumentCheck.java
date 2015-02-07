package com.marianogiuffrida.helpers;

/**
 * Created by Mariano on 7/02/2015.
 */
public class ArgumentCheck {

    public static void IsNotNull(Object o, String argName)
    {
        if(o == null) throw new IllegalArgumentException(argName);
    }

    public static void IsNotNullorEmpty(String s, String argName)
    {
        if(StringUtils.isNullOrEmpty(s)) throw new IllegalArgumentException(argName);
    }


}
