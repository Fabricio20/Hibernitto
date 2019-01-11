package net.notfab.lib.entities;

import net.notfab.lib.Dialect;

import java.util.Arrays;
import java.util.List;

public class SQLBetween implements SQLMultiFilter {

    private String field;
    private String value1;
    private String value2;
    private String not = "";

    public SQLBetween(String field, String value1, String value2) {
        this.field = field;
        this.value1 = value1;
        this.value2 = value2;
    }

    public SQLBetween(boolean equals, String field, String value1, String value2) {
        this.field = field;
        this.value1 = value1;
        this.value2 = value2;
        if (!equals) {
            this.not = " NOT";
        }
    }

    public String getField() {
        return this.field;
    }

    @Override
    public List<String> getValues() {
        return Arrays.asList(value1, value2);
    }

    @Override
    public String toString(Dialect dialect) {
        return getField() + not + " BETWEEN ? AND ?";
    }

}