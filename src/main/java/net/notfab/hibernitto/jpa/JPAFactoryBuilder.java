package net.notfab.hibernitto.jpa;

import net.notfab.hibernitto.core.Dialect;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class JPAFactoryBuilder {

    private String name = "Hibernitto";
    private String database;
    private String username;
    private String password;
    private String ip;
    private int port;
    private String connURL;
    private String driver;
    private String subProtocol;
    private String poolName = "Hibernitto";
    private Dialect dialect;
    private Map<String, String> extraProperties = new HashMap<>();

    JPAFactoryBuilder(Dialect dialect) {
        this.driver = dialect.getDriver();
        this.subProtocol = dialect.getSubProtocol();
        this.dialect = dialect;
    }

    public JPAFactoryBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public JPAFactoryBuilder setDatabase(String database) {
        this.database = database;
        return this;
    }

    public JPAFactoryBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public JPAFactoryBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public JPAFactoryBuilder setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public JPAFactoryBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public JPAFactoryBuilder setConnectionURL(String connectionURL) {
        this.connURL = connectionURL;
        return this;
    }

    public JPAFactoryBuilder setDriver(String driver) {
        this.driver = driver;
        return this;
    }

    public JPAFactoryBuilder setPoolName(String poolName) {
        this.poolName = poolName;
        return this;
    }

    public JPAFactoryBuilder setSubprotocol(String subprotocol) {
        this.subProtocol = subprotocol;
        return this;
    }

    public JPAFactoryBuilder addProperty(String name, String value) {
        this.extraProperties.put(name, value);
        return this;
    }

    public JPAFactory build() {
        if (connURL == null) {
            connURL = "jdbc:" + subProtocol
                    + ":" + ((ip != null) ? "//" + ip : "")
                    + ((port != 0) ? ":" + port : "")
                    + dialect.getDatabaseSeparator() + database
                    + "?UseUnicode=true&amp;characterEncoding=utf8";
        }
        Properties properties = new Properties();
        properties.put("hibernate.hbm2ddl.auto", "validate");
        properties.put("javax.persistence.provider", "org.hibernate.jpa.HibernatePersistenceProvider");
        properties.put("javax.persistence.jdbc.driver", driver);
        properties.put("javax.persistence.jdbc.url", connURL);
        properties.put("javax.persistence.jdbc.user", username);
        properties.put("javax.persistence.jdbc.password", password);
        properties.put("hibernate.hikari.minimumIdle", 2);
        properties.put("hibernate.hikari.maximumPoolSize", 16);
        properties.put("hibernate.hikari.idleTimeout", 30000);
        properties.put("hibernate.connection.provider_class", "org.hibernate.hikaricp.internal.HikariCPConnectionProvider");
        properties.put("hibernate.connection.CharSet", "utf8");
        properties.put("hibernate.connection.characterEncoding", "utf8");
        properties.put("hibernate.connection.useUnicode", "true");
        properties.put("hibernate.hikari.leakDetectionThreshold", 30000);
        properties.put("hibernate.hikari.poolName", poolName);

        for (Map.Entry<String, String> entry : this.extraProperties.entrySet()) {
            properties.put(entry.getKey(), entry.getValue());
        }

        return new JPAFactory(this.name, this.dialect, properties);
    }

}