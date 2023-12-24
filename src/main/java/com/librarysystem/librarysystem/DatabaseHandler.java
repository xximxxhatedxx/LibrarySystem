package com.librarysystem.librarysystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseHandler extends Config {
    Connection dbConnection;

    public Connection getDbConnection()
            throws ClassNotFoundException, SQLException{
        String connectionString = "jdbc:mysql:" + dbHost
                + ":" + dbPort + "/" + dbName;

        Class.forName("com.mysql.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

        return dbConnection;
    }

    public void createBook(String author, String name, String genre, int number)
            throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO books (author, name, genre, number) VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setString(1, author);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, genre);
        preparedStatement.setInt(4, number);

        preparedStatement.executeUpdate();
    }
}
