package net.notfab.lib.entities;

import net.notfab.lib.Dialect;

public interface SQLFilter {

    String getField();

    String getValue();

    String toString(Dialect dialect);

}