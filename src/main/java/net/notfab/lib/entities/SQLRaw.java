package net.notfab.lib.entities;

import net.notfab.lib.Dialect;

/**
 * Represents a raw filter, type-your-own-where style.
 */
public class SQLRaw implements SQLFilter {

    private String query;

    public SQLRaw(String query) {
        this.query = query;
    }

    @Override
    public String getField() {
        return null;
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public String toString(Dialect dialect) {
        return this.query;
    }

}