package net.notfab.persistengine;

import net.notfab.persistengine.entities.SQLFilter;
import net.notfab.persistengine.entities.SQLWhere;
import org.hibernate.internal.SessionImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "unchecked", "SqlDialectInspection"})
public class PersistEngine implements AutoCloseable {

    private EntityManager em;

    protected PersistEngine(EntityManager em) {
        this.em = em;
    }

    /**
     * Inserts or updates an object.
     * @param object The object.
     */
    public boolean persist(Object object) {
        try {
            em.getTransaction().begin();
            ((SessionImpl) em).saveOrUpdate(object);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Fetches a persisted entity from the database.
     * @param clazz Class of the entity
     * @param o Filter object(s) or primary key
     * @param <T> Type
     * @return Entity from DB
     */
    public <T> T get(Class<T> clazz, Object o) {
        if (o instanceof SQLWhere) return this.get(clazz, (SQLWhere) o); // SQLWhere
        if (isArray(o)) return this.get(clazz, (SQLFilter[]) o); // Array of SQLFilters
        if (o instanceof SQLFilter) return this.get(clazz, new SQLFilter[]{(SQLFilter) o}); // SQLFilter
        em.getTransaction().begin();
        T resp = em.find(clazz, o);
        em.getTransaction().commit();
        return resp;
    }

    /**
     * Fetches a persisted entity from the database.
     * @param clazz Class of the entity
     * @param where SQLWhere object (filter).
     * @param <T> Type
     * @return Entity from DB
     */
    public <T> T get(Class<T> clazz, SQLWhere where) {
        String queryStr = "SELECT * from " + clazz.getSimpleName() + where.toString();
        Query query = em.createNativeQuery(queryStr, clazz);
        where.prepare(query);
        query.setFirstResult(0);
        query.setMaxResults(1);
        return query.getResultList().isEmpty() ? null : (T) query.getResultList().get(0);
    }

    /**
     * Fetches a persisted entity from the database with filters.
     * @param clazz Class of the entity
     * @param filters SQL Filters see {@link net.notfab.persistengine.entities}
     * @param <T> Type
     * @return Persisted entity if found, null otherwise.
     */
    public <T> T get(Class<T> clazz, SQLFilter... filters) {
        SQLWhere where = new SQLWhere(filters);
        String queryStr = "SELECT * from " + clazz.getSimpleName() + where.toString();
        Query query = em.createNativeQuery(queryStr, clazz);
        where.prepare(query);
        query.setFirstResult(0);
        query.setMaxResults(1);
        return query.getResultList().isEmpty() ? null : (T) query.getResultList().get(0);
    }

    // --------------------------------------------------

    /**
     * Fetches a list of persisted entities from the database with filters.
     * @param clazz Class of the entity
     * @param where SQLWhere object (filter).
     * @param <T> Type
     * @return Persisted list of entities if found, null otherwise.
     */
    public <T> List<T> getList(Class<T> clazz, SQLWhere where) {
        String queryStr = "SELECT * from " + clazz.getSimpleName() + where.toString();
        Query query = em.createNativeQuery(queryStr, clazz);
        query = where.prepare(query);
        query.setFirstResult(0);
        query.setMaxResults(50);
        return (List<T>) query.getResultList();
    }

    /**
     * Fetches a list of persisted entities from the database with filters.
     * @param clazz Class of the entity
     * @param filters SQL Filters see {@link net.notfab.persistengine.entities}
     * @param <T> Type
     * @return Persisted list of entities if found, null otherwise.
     */
    public <T> List<T> getList(Class<T> clazz, SQLFilter... filters) {
        SQLWhere where = new SQLWhere(filters);
        String queryStr = "SELECT * from " + clazz.getSimpleName() + where.toString();
        Query query = em.createNativeQuery(queryStr, clazz);
        query = where.prepare(query);
        query.setFirstResult(0);
        query.setMaxResults(50);
        return (List<T>) query.getResultList();
    }

    /**
     * Deletes an entity from the database.
     * @param object The entity.
     */
    public void delete(Object object) {
        if(object == null) return;
        em.getTransaction().begin();
        em.remove(em.merge(object));
        em.getTransaction().commit();
    }

    /**
     * Refreshes an entity with latest data from the database.
     * @param o The entity.
     */
    public void refresh(Object o) {
        em.refresh(o);
    }

    /**
     * Closes the connection pool and entity manager.
     */
    public void close() {
        this.em.close();
    }

    private boolean isArray(Object obj) {
        return obj != null && obj.getClass().isArray();
    }

}