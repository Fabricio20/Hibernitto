package net.notfab.lib;

import net.notfab.spigot.simpleconfig.Section;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.Closeable;
import java.util.Properties;

public class PersistenceFactory implements Closeable {

    private final String name;
    private final EntityManagerFactory entityManagerFactory;

    public PersistenceFactory(String name) {
        this.name = name;
        this.entityManagerFactory = Persistence.createEntityManagerFactory(name);
    }

    public PersistenceFactory(String name, Section section) {
        this.name = name;

        String url;
        String user = section.getString("User", "anonymous");
        String password = section.getString("Password", "123");
        int minimumIdle = section.getInt("MinimumIdle", 5);
        int maxPoolSize = section.getInt("MaxPoolSize", 100);
        int idleTimeout = section.getInt("IdleTimeout", 30000);
        int leakDetectionThreshold = section.getInt("LeakDetectionThreshold", 30000);
        String poolName = section.getString("PoolName", "PE-" + name);
        String driver = section.getString("Driver", "org.mariadb.jdbc.Driver");
        String testQuery = section.getString("TestQuery", "SELECT 1");

        if (section.contains("URL")) {
            url = section.getString("URL");
        } else {
            url = "jdbc:mariadb://" +
                    section.getString("Ip", "127.0.0.1") + ":" +
                    section.getInt("Port", 3306) + "/" +
                    section.getString("Database") + "?UseUnicode=true&amp;characterEncoding=utf8";
        }

        Properties properties = new Properties();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("javax.persistence.provider", "org.hibernate.jpa.HibernatePersistenceProvider");
        properties.put("javax.persistence.jdbc.driver", driver);
        properties.put("javax.persistence.jdbc.url", url);
        properties.put("javax.persistence.jdbc.user", user);
        properties.put("javax.persistence.jdbc.password", password);
        properties.put("hibernate.hikari.minimumIdle", minimumIdle);
        properties.put("hibernate.hikari.maximumPoolSize", maxPoolSize);
        properties.put("hibernate.hikari.idleTimeout", idleTimeout);
        properties.put("hibernate.connection.provider_class", "org.hibernate.hikaricp.internal.HikariCPConnectionProvider");
        properties.put("hibernate.connection.CharSet", "utf8");
        properties.put("hibernate.connection.characterEncoding", "utf8");
        properties.put("hibernate.connection.useUnicode", "true");
        properties.put("hibernate.hikari.leakDetectionThreshold", leakDetectionThreshold);
        properties.put("hibernate.hikari.poolName", poolName);
        properties.put("hibernate.hikari.connectionTestQuery", testQuery);

        this.entityManagerFactory = Persistence.createEntityManagerFactory(name, properties);
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