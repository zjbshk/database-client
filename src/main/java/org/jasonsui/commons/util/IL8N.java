package org.jasonsui.commons.util;

import java.util.ResourceBundle;

public class IL8N {

    private final static ResourceBundle bundle = ResourceBundle.getBundle("il8n.messages");

    public static String get(String key) {
        return bundle.getString(key);
    }

}
