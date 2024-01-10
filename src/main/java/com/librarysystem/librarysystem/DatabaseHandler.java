package com.librarysystem.librarysystem;

import java.sql.*;
import java.util.*;

public class DatabaseHandler extends Config {
//    Connection dbConnection = null;

    public Connection getDbConnection()
            throws SQLException{
        if (dbConnection != null) return dbConnection;
        String connectionString = "jdbc:mysql://" + dbHost
                + ":" + dbPort + "/" + dbName;
        return dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
    }

    public void createBook(String author, String name, String genre, int number)
            throws SQLException {
        String query = "INSERT INTO books (author, name, genre, number) VALUES (?,?,?,?)";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setString(1, author);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, genre);
        preparedStatement.setInt(4, number);

        preparedStatement.executeUpdate();
    }

    public Book getBookById(int id) throws SQLException{
        String query = "SELECT * FROM books WHERE idbooks = ? LIMIT 1";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resSet = preparedStatement.executeQuery();
        if (!resSet.next()) return null;

        return new Book(
                id,
                resSet.getString("author"),
                resSet.getString("name"),
                resSet.getString("genre"),
                resSet.getInt("number")
        );
    }

    public Book[] getBooksByAuthor(String author) throws SQLException{
        String query = "SELECT * FROM books WHERE author LIKE ?";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setString(1, "%" + author + "%");
        ResultSet resSet = preparedStatement.executeQuery();

        List<Book> books = new ArrayList<Book>();
        while (resSet.next()){
            books.add(new Book(
                    resSet.getInt("idbooks"),
                    resSet.getString("author"),
                    resSet.getString("name"),
                    resSet.getString("genre"),
                    resSet.getInt("number")
            ));
        }

        return books.toArray(new Book[0]);
    }

    public Book[] getLastBooks(int number) throws SQLException{
        String query = "SELECT * FROM books ORDER BY idbooks DESC LIMIT 20 OFFSET ?";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setInt(1, number);
        ResultSet resSet = preparedStatement.executeQuery();

        List<Book> books = new ArrayList<Book>();
        while (resSet.next()){
            books.add(new Book(
                    resSet.getInt("idbooks"),
                    resSet.getString("author"),
                    resSet.getString("name"),
                    resSet.getString("genre"),
                    resSet.getInt("number")
            ));
        }

        return books.toArray(new Book[0]);
    }
}
