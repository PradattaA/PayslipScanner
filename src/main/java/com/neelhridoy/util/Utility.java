package com.neelhridoy.util;

import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Author: Pradatta Adhikary - GIDS/2090131/IK
 * Date: 5/23/2016
 * Time: 5:29 PM
 */

public class Utility {
    public static final String RESOURCE_BUNDLE = "resourcebundle";
    private static ResourceBundle resourceBundle;

    static {
        init();
    }

    public static void init() {
        Locale locale = Locale.getDefault();
        resourceBundle = PropertyResourceBundle.getBundle(RESOURCE_BUNDLE, locale);
    }

    public static String getString(String key) {
        return resourceBundle.getString(key);
    }
}
