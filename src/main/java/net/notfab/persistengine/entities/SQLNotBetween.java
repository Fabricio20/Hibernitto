package net.notfab.persistengine.entities;

import java.util.Arrays;
import java.util.List;

public class SQLNotBetween implements SQLMultiFilter {

    private String field;
    private String value1;
    private String value2;

    public SQLNotBetween(String field, String value1, String value2) {
        this.field = field;
        this.value1 = value1;
        this.value2 = value2;
    }

    public String getField() {
        return this.field;
    }

    @Override
    public List<String> getValues() {
        return Arrays.asList(value1, value2);
    }

    @Override
    public String toString() {
        return getField() + " NOT between ? AND ?";
    }

}