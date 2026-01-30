package fr.uit.univparis8.tpair.tpair1.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private static final String URL = "jdbc:postgresql://database-etudiants:5432/epembelefuala";
    private static final String USER = "epembelefuala";
    private static final String PASS = "9q9g6Gvv";

    public static Connection getInstance() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
