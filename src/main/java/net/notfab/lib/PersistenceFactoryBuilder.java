package net.notfab.lib;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PersistenceFactoryBuilder {

    private String name = "PersistEngine";
    private String database;
    private String username;
    private String password;
    private String ip;
    private int port;
    private String connURL;
    private String driver;
    private String subProtocol;
    private String poolName = "PersistEngine";
    private Dialect dialect;
    private Map<String, String> extraProperties = new HashMap<>();

    PersistenceFactoryBuilder(Dialect dialect) {
        this.driver = dialect.getDriver();
        this.subProtocol = dialect.getSubProtocol();
        this.dialect = dialect;
    }

    public PersistenceFactoryBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PersistenceFactoryBuilder setDatabase(String database) {
        this.database = database;
        return this;
    }

    public PersistenceFactoryBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public PersistenceFactoryBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public PersistenceFactoryBuilder setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public PersistenceFactoryBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public PersistenceFactoryBuilder setConnectionURL(String connectionURL) {
        this.connURL = connectionURL;
        return this;
    }

    public PersistenceFactoryBuilder setDriver(String driver) {
        this.driver = driver;
        return this;
    }

    public PersistenceFactoryBuilder setPoolName(String poolName) {
        this.poolName = poolName;
        return this;
    }

    public PersistenceFactoryBuilder setSubprotocol(String subprotocol) {
        this.subProtocol = subprotocol;
        return this;
    }

    public PersistenceFactoryBuilder addProperty(String name, String value) {
        this.extraProperties.put(name, value);
        return this;
    }

    public PersistenceFactory build() {
        if (connURL == null) {
            connURL = "jdbc:" + subProtocol
                    + "://" + ip + ":"
                    + port + "/" + database
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

        return new PersistenceFactory(this.name, this.dialect, properties);
    }

}