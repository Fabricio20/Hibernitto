package net.notfab.persistengine.entities;

import javax.persistence.Query;
import java.util.Arrays;
import java.util.List;

public class SQLWhere {

    private List<SQLFilter> filterList;

    public SQLWhere(SQLFilter... filters) {
        this.filterList = Arrays.asList(filters);
    }

    @Override
    public String toString() {
        StringBuilder where = new StringBuilder();
        filterList.forEach(q -> {
            where.append(" AND ").append(q.toString());
        });
        return where.toString().replaceFirst(" AND ", " where ");
    }

    public Query prepare(Query query) {
        final int[] i = {1};
        filterList.forEach(f -> {
            if (f instanceof SQLMultiFilter) {
                SQLMultiFilter multi = (SQLMultiFilter) f;
                for (String value : multi.getValues()) {
                    query.setParameter(i[0], value);
                    i[0]++;
                }
            } else {
                query.setParameter(i[0], f.getValue());
                i[0]++;
            }
        });
        return query;
    }

}
