package net.notfab.hibernitto.core.entities;

import net.notfab.hibernitto.core.Dialect;

public class SQLLike implements SQLFilter {

    private String field;
    private String value;
    private String not = "";

    public SQLLike(String field, String value) {
        this.field = field;
        this.value = value;
    }

    public SQLLike(boolean equals, String field, String value) {
        this.field = field;
        this.value = value;
        if (!equals) {
            this.not = " NOT";
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
        return getField() + not + " LIKE ?";
    }

}