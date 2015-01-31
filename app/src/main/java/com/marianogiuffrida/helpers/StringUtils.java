package com.marianogiuffrida.helpers;

/**
 * Created by Mariano on 31/01/2015.
 */
public class StringUtils {

    public static boolean isNull(String s){
        return s == null;
    }

    public static boolean isEmpty(String s) {
        return s.length()==0;
    }

    public static boolean isNullOrEmpty(String s){
        return isNull(s)|| isEmpty(s);
    }
}
