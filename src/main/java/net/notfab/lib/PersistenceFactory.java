package net.notfab.lib;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.Closeable;
import java.util.Properties;

public class PersistenceFactory implements Closeable {

    private final String name;
    private final EntityManagerFactory entityManagerFactory;
    private final Dialect dialect;

    public PersistenceFactory(String name, Dialect dialect) {
        this.name = name;
        this.dialect = dialect;
        this.entityManagerFactory = Persistence.createEntityManagerFactory(name);
    }

    public PersistenceFactory(String name, Dialect dialect, Properties properties) {
        this.name = name;
        this.dialect = dialect;
        this.entityManagerFactory = Persistence.createEntityManagerFactory(name, properties);
    }

    public String getName() {
        return name;
    }

    public PersistEngine getEngine() {
        return new PersistEngine(this.entityManagerFactory.createEntityManager(), this.dialect);
    }

    @Override
    public void close() {
        this.entityManagerFactory.close();
    }

    public static PersistenceFactoryBuilder builder(Dialect dialect) {
        return new PersistenceFactoryBuilder(dialect);
    }

}