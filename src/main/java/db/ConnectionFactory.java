package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String URL = System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/entregasgw");
    private static final String USUARIO = System.getenv().getOrDefault("DB_USER", "postgres");
    private static final String SENHA = System.getenv().getOrDefault("DB_PASSWORD", "postgres");

    private ConnectionFactory() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
