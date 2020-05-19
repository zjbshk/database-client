package org.jasonsui.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class AppEnum {


    @AllArgsConstructor
    public static enum TypeEnum{
        ORACLE("ORACLE"),MYSQL("MYSQL"),PG("PG");
        @Getter
        private String code;
    }

    public static enum Events{
        SELECT_DB_TYPE,RELOAD_DB_CONFIG;
    }
}
