package net.notfab.hibernitto.jpa;

import net.notfab.hibernitto.core.Dialect;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.Closeable;
import java.util.Properties;

public class JPAFactory implements Closeable {

    private final String name;
    private final EntityManagerFactory entityManagerFactory;
    private final Dialect dialect;

    public JPAFactory(String name, Dialect dialect) {
        this.name = name;
        this.dialect = dialect;
        this.entityManagerFactory = Persistence.createEntityManagerFactory(name);
    }

    public JPAFactory(String name, Dialect dialect, Properties properties) {
        this.name = name;
        this.dialect = dialect;
        this.entityManagerFactory = Persistence.createEntityManagerFactory(name, properties);
    }

    public String getName() {
        return name;
    }

    public JPAEngine getEngine() {
        return new JPAEngine(this.entityManagerFactory.createEntityManager(), this.dialect);
    }

    @Override
    public void close() {
        this.entityManagerFactory.close();
    }

    public static JPAFactoryBuilder builder(Dialect dialect) {
        return new JPAFactoryBuilder(dialect);
    }

}