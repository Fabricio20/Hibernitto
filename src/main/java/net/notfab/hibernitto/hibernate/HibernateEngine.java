package net.notfab.hibernitto.hibernate;

import net.notfab.hibernitto.core.Dialect;
import net.notfab.hibernitto.core.entities.SQLFilter;
import net.notfab.hibernitto.core.entities.SQLWhere;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Entity;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.Table;
import java.util.List;

public class HibernateEngine implements AutoCloseable {

    private Session session;
    private Dialect dialect;

    public HibernateEngine(Session session, Dialect dialect) {
        this.session = session;
        this.dialect = dialect;
    }

    /**
     * Inserts or updates an object.
     *
     * @param object The object.
     */
    public void save(Object object) {
        EntityTransaction transaction = session.getTransaction();
        transaction.begin();
        session.saveOrUpdate(object);
        transaction.commit();
    }

    /**
     * Fetches an entity from the database.
     *
     * @param clazz Class of the entity
     * @param o     Filter object(s) or primary key
     * @param <T>   Type
     * @return Entity from DB
     */
    public <T> T get(Class<T> clazz, Object o) {
        if (o instanceof SQLWhere) return this.get(clazz, (SQLWhere) o); // SQLWhere
        if (isArray(o)) return this.get(clazz, (SQLFilter[]) o); // Array of SQLFilters
        if (o instanceof SQLFilter) return this.get(clazz, new SQLFilter[]{(SQLFilter) o}); // SQLFilter
        return session.find(clazz, o);
    }

    /**
     * Fetches an entity from the database.
     *
     * @param clazz Class of the entity
     * @param where SQLWhere object (filter).
     * @param <T>   Type
     * @return Entity from DB
     */
    public <T> T get(Class<T> clazz, SQLWhere where) {
        String queryStr = "SELECT * from " + this.getClassName(clazz) + where.toString();
        Query query = session.createNativeQuery(queryStr, clazz);
        where.prepare(query);
        query.setFirstResult(0);
        query.setMaxResults(1);
        return query.getResultList().isEmpty() ? null : (T) query.getResultList().get(0);
    }

    /**
     * Fetches an entity from the database with filters.
     *
     * @param clazz   Class of the entity
     * @param filters SQL Filters see {@link net.notfab.hibernitto.core.entities}
     * @param <T>     Type
     * @return Entity if found, null otherwise.
     */
    public <T> T get(Class<T> clazz, SQLFilter... filters) {
        SQLWhere where = new SQLWhere(filters);
        String queryStr = "SELECT * from " + this.getClassName(clazz) + where.toString();
        Query query = session.createNativeQuery(queryStr, clazz);
        where.prepare(query);
        query.setFirstResult(0);
        query.setMaxResults(1);
        return query.getResultList().isEmpty() ? null : (T) query.getResultList().get(0);
    }

    // --------------------------------------------------

    /**
     * Fetches a list of entities from the database with filters.
     *
     * @param clazz       Class of the entity
     * @param firstResult First result
     * @param maxResults  Max results to return
     * @param where       SQLWhere object (filter)
     * @param <T>         Type
     * @return List of entities if found, null otherwise.
     */
    public <T> List<T> getList(Class<T> clazz, int firstResult, int maxResults, SQLWhere where) {
        String queryStr = "SELECT * from " + this.getClassName(clazz) + where.toString();
        Query query = session.createNativeQuery(queryStr, clazz);
        query = where.prepare(query);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return (List<T>) query.getResultList();
    }

    /**
     * Fetches a list of entities from the database with filters.
     * <p>
     * Note: This has a default limit of 50 results.
     *
     * @param clazz Class of the entity
     * @param where SQLWhere object (filter)
     * @param <T>   Type
     * @return List of entities if found, null otherwise.
     */
    public <T> List<T> getList(Class<T> clazz, SQLWhere where) {
        return getList(clazz, 0, 50, where);
    }

    /**
     * Fetches a list of entities from the database with filters.
     * <p>
     * Note: This has a default limit of 50 results.
     *
     * @param clazz   Class of the entity
     * @param filters SQL Filters see {@link net.notfab.hibernitto.core.entities}
     * @param <T>     Type
     * @return List of entities if found, null otherwise.
     */
    public <T> List<T> getList(Class<T> clazz, SQLFilter... filters) {
        return getList(clazz, 0, 50, filters);
    }

    /**
     * Fetches a list of entities from the database with filters.
     *
     * @param clazz      Class of the entity
     * @param maxResults Max results to return
     * @param filters    SQL Filters see {@link net.notfab.hibernitto.core.entities}
     * @param <T>        Type
     * @return List of entities if found, null otherwise.
     */
    public <T> List<T> getList(Class<T> clazz, int maxResults, SQLFilter... filters) {
        return getList(clazz, 0, maxResults, filters);
    }

    /**
     * Fetches a list of entities from the database with filters.
     *
     * @param clazz       Class of the entity
     * @param firstResult First result
     * @param maxResults  Max results to return
     * @param filters     SQL Filters see {@link net.notfab.hibernitto.core.entities}
     * @param <T>         Type
     * @return List of entities if found, null otherwise.
     */
    public <T> List<T> getList(Class<T> clazz, int firstResult, int maxResults, SQLFilter... filters) {
        return getList(clazz, firstResult, maxResults, new SQLWhere(filters));
    }

    /**
     * Creates a native query via Hibernate.
     *
     * @param query - The query.
     * @return Query.
     */
    public Query createNativeQuery(String query) {
        return session.createNativeQuery(query);
    }

    /**
     * Creates a native query via Hibernate.
     *
     * @param query - The query.
     * @param clazz - The entity.
     * @return Query.
     */
    public Query createNativeQuery(String query, Class<?> clazz) {
        return session.createNativeQuery(query, clazz);
    }

    /**
     * Deletes an entity from the database.
     *
     * @param object The entity.
     */
    public void remove(Object object) {
        if (object == null) return;
        Transaction transaction = session.getTransaction();
        transaction.begin();
        session.remove(session.merge(object));
        transaction.commit();
    }

    /**
     * Refreshes an entity with latest data from the database.
     *
     * @param o The entity.
     */
    public void refresh(Object o) {
        session.refresh(o);
    }

    /**
     * Detaches this entity from the Session.
     *
     * @param o The entity.
     */
    public void detach(Object o) {
        session.detach(o);
    }

    /**
     * Unwraps a wrapped entity.
     *
     * @param o The entity class.
     * @return unwrapped entity.
     */
    public <T> T unwrap(Class<T> o) {
        return session.unwrap(o);
    }

    /**
     * Closes the session.
     */
    public void close() {
        this.session.close();
    }

    private boolean isArray(Object obj) {
        return obj != null && obj.getClass().isArray();
    }

    private String getClassName(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = clazz.getDeclaredAnnotation(Table.class);
            if (!table.name().equals(""))
                return table.name();
        }
        Entity entity = clazz.getDeclaredAnnotation(Entity.class);
        return entity.name().equals("") ? clazz.getSimpleName() : entity.name();
    }

    public Dialect getDialect() {
        return dialect;
    }

}