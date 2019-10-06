package net.notfab.hibernitto.core.entities;

import net.notfab.hibernitto.core.Dialect;

public interface SQLFilter {

    String getField();

    String getValue();

    String toString(Dialect dialect);

}