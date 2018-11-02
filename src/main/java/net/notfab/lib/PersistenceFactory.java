package net.notfab.lib;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.Closeable;

public class PersistenceFactory implements Closeable {

    private final String name;
    private final EntityManagerFactory entityManagerFactory;

    public PersistenceFactory(String name) {
        this.name = name;
        this.entityManagerFactory = Persistence.createEntityManagerFactory(name);
    }

    public String getName() {
        return name;
    }

    public PersistEngine getEngine() {
        return new PersistEngine(this.entityManagerFactory.createEntityManager());
    }

    @Override
    public void close() {
        this.entityManagerFactory.close();
    }

}