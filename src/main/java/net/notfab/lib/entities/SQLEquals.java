package net.notfab.lib.entities;

import net.notfab.lib.Dialect;

public class SQLEquals implements SQLFilter {

    private String field;
    private String value;
    private String not = " =";

    public SQLEquals(String field, String value) {
        this.field = field;
        this.value = value;
    }

    public SQLEquals(boolean equals, String field, String value) {
        this.field = field;
        this.value = value;
        if (!equals) {
            this.not = " !=";
        }
    }

    public String getField() {
        return this.field;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString(Dialect dialect) {
        return getField() + not + " ?";
    }

}