package org.jasonsui.commons.util;

import javafx.fxml.FXMLLoader;


public class FXMLLoadUtil {
    public static FXMLLoader load(String fxmlURI) {
        return new FXMLLoader(FXMLLoadUtil.class.getResource(fxmlURI));
    }
}
