package net.notfab.lib.entities;

public class SQLEquals implements SQLFilter {

    private String field;
    private String value;

    public SQLEquals(String field, String value) {
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return this.field;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return getField() + " = ?";
    }

}