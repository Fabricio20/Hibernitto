package net.notfab.lib.entities;

import java.util.Arrays;
import java.util.List;

public class SQLLimit implements SQLFilter {

    private int limit1;
    private int limit2;

    public SQLLimit(int limit1, int limit2) {
        this.limit1 = limit1;
        this.limit2 = limit2;
    }

    @Override
    public String toString() {
        return "LIMIT ?,?";
    }

    public List<Integer> getValues() {
        return Arrays.asList(limit1, limit2);
    }

    @Override
    @Deprecated
    public String getField() {
        return null;
    }

    @Override
    @Deprecated
    public String getValue() {
        return null;
    }

}
