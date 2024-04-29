package com.librarysystem.librarysystem;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.*;

public class DatabaseHandler extends Config {
    Connection dbConnection = null;

    public Connection getDbConnection()
            throws SQLException{
        if (dbConnection != null) return dbConnection;
        String connectionString = "jdbc:mysql://" + dbHost
                + ":" + dbPort + "/" + dbName;
        return dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
    }

    public int createBook(String author, String name, int number)
            throws SQLException {
        String query = "INSERT INTO books (author, name, number) VALUES (?,?,?);";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setString(1, author);
        preparedStatement.setString(2, name);
        preparedStatement.setInt(3, number);

        preparedStatement.executeUpdate();

        Statement st = getDbConnection().createStatement();
        ResultSet resSet = st.executeQuery("SELECT LAST_INSERT_ID() as ind;");
        if (resSet.next()) return resSet.getInt("ind");
        return 0;
    }
    public void deleteBook(String author, String name) throws SQLException {
//        String query = "DELETE FROM books WHERE author = ? AND name = ?";
//
//        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
//        preparedStatement.setString(1, author);
//        preparedStatement.setString(2, name);
//
//        preparedStatement.executeUpdate();
        System.out.println("Button clicked for element " + name + " " + author);
    }

    public ResultSet getBooksByAuthor(String author) throws SQLException{
        String query = "SELECT SQL_CALC_FOUND_ROWS * FROM books WHERE author LIKE ? ORDER BY CASE WHEN author LIKE ? THEN 0 ELSE 1 END";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(
                query,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
        );
        preparedStatement.setString(1, "%" + author + "%");
        preparedStatement.setString(2, author + "%");

        return preparedStatement.executeQuery();
    }

    public ResultSet getBooksByName(String name) throws SQLException{
        String query = "SELECT SQL_CALC_FOUND_ROWS * FROM books WHERE name LIKE ? ORDER BY CASE WHEN name LIKE ? THEN 0 ELSE 1 END";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(
                query,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
        );
        preparedStatement.setString(1, "%" + name + "%");
        preparedStatement.setString(2, name + "%");

        return preparedStatement.executeQuery();
    }

    public int getDbLength() throws SQLException {
        String query = "SELECT FOUND_ROWS() as length;";
        Statement st = getDbConnection().createStatement();
        ResultSet rs = st.executeQuery(query);
        if (rs.next())
            return rs.getInt("length");
        return 0;
    }

    public ResultSet getLastBooks() throws SQLException{
        String query = "SELECT SQL_CALC_FOUND_ROWS * FROM books ORDER BY idbooks DESC";

        Statement statement = getDbConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);

        return statement.executeQuery(query);
    }

    public void addUser(String name, String surname, String email, String password)
            throws SQLException {
        String query = "INSERT INTO users (name, surname, email, password) VALUES (?,?,?,?)";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, surname);
        preparedStatement.setString(3, email);
        preparedStatement.setString(4, password);

        preparedStatement.executeUpdate();

    }
    public User getUserById(int id) throws SQLException{
        String query = "SELECT * FROM users WHERE idusers = ? LIMIT 1";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resSet = preparedStatement.executeQuery();
        if (!resSet.next()) return null;

        return new User(
                id,
                resSet.getString("name"),
                resSet.getString("surname"),
                resSet.getString("email"),
                resSet.getString("password")
        );
    }
    public User[] getUserBySurname(String surname) throws SQLException{
        String query = "SELECT * FROM users WHERE surname LIKE ?";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setString(1, surname.trim());
        ResultSet resSet = preparedStatement.executeQuery();

        List<User> users = new ArrayList<User>();
        while (resSet.next()){
            users.add(new User(
                    resSet.getInt("idusers"),
                    resSet.getString("name"),
                    resSet.getString("surname"),
                    resSet.getString("email"),
                    resSet.getString("password")
            ));
        }

        return users.toArray(new User[0]);
    }
    public User getUserByEmail(String email, String password) throws SQLException{
        String query = "SELECT * FROM users WHERE email LIKE ? LIMIT 1";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setString(1, email.trim());
        ResultSet resSet = preparedStatement.executeQuery();

        if (resSet.next()){
            if (resSet.getString("password").equals(password))
                return new User(
                        resSet.getInt("idusers"),
                        resSet.getString("name"),
                        resSet.getString("surname"),
                        resSet.getString("email"),
                        resSet.getString("password")
                );

        }
        return null;
    }
    public ResultSet getBookByUser(User user) throws SQLException{
        String query = "SELECT books.* FROM users_to_books INNER JOIN books ON users_to_books.idbooks = books.idbooks WHERE users_to_books.idusers = ?";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(
                query,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
        );

        preparedStatement.setInt(1, user.getId());
        return preparedStatement.executeQuery();
    }

    public Genre[] getGenres() throws SQLException {
        String query = "SELECT * FROM genres";

        Statement statement = getDbConnection().createStatement();

        ResultSet resSet = statement.executeQuery(query);
        List<Genre> genres = new ArrayList<Genre>();
        while(resSet.next()){
            genres.add(new Genre(
                    resSet.getInt("idgenre"),
                    resSet.getString("name")
            ));
        }
        return genres.toArray(new Genre[0]);
    }

    public void addBooksToGenre(int idbook, int idgenre) throws SQLException {
        String query = "INSERT INTO books_to_genres (idbook, idgenre) VALUES (?,?)";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setInt(1, idbook);
        preparedStatement.setInt(2, idgenre);

        preparedStatement.executeUpdate();
    }
}
