package net.notfab.lib;

public enum Dialect {

    MySQL("com.mysql.jdbc.jdbc2.optional.MysqlDataSource", "mysql"),
    MariaDB("org.mariadb.jdbc.MariaDbDataSource", "mariadb"),
    SQLServer("com.microsoft.sqlserver.jdbc.SQLServerDataSource", "sqlserver"),
    Oracle("oracle.jdbc.pool.OracleDataSource", "oracle:thin:"),
    Postgre("org.postgresql.ds.PGSimpleDataSource", "postgresql"),
    SQLite("org.sqlite.SQLiteDataSource", "sqlite"),
    H2("org.h2.jdbcx.JdbcDataSource", "h2");

    private final String driver;
    private final String subProtocol;

    Dialect(String driver, String subProtocol) {
        this.driver = driver;
        this.subProtocol = subProtocol;
    }

    public String getDriver() {
        return this.driver;
    }

    public String getSubProtocol() {
        return this.subProtocol;
    }

    public boolean isCompatible(Dialect dialect) {
        return this == MySQL && dialect == MariaDB || this == MariaDB && dialect == MySQL;
    }

    public String getDatabaseSeparator() {
        if (this == Dialect.H2) {
            return ":";
        } else if(this == Dialect.Oracle) {
            return "@";
        } else {
            return "/";
        }
    }

}