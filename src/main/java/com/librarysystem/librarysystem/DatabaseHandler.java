package com.librarysystem.librarysystem;

import javafx.collections.ObservableList;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    public boolean statusBook(String author, String name) throws SQLException {
        String query = "SELECT active FROM books WHERE author = ? AND name = ?";
        try (PreparedStatement preparedStatement = getDbConnection().prepareStatement(query)) {
            preparedStatement.setString(1, author);
            preparedStatement.setString(2, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int activeStatus = resultSet.getInt("active");
                    return activeStatus == 1;
                } else {
                    return false;
                }
            }
        }
    }
    public void inactiveBook(String author, String name) throws SQLException {
        String query = "UPDATE books SET active = 0 WHERE author = ? AND name = ?";
        try (PreparedStatement preparedStatement = getDbConnection().prepareStatement(query)) {
            preparedStatement.setString(1, author);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();
        }
    }
    public void activateBook(String author, String name) throws SQLException {
        String query = "UPDATE books SET active = 1 WHERE author = ? AND name = ?";
        try (PreparedStatement preparedStatement = getDbConnection().prepareStatement(query)) {
            preparedStatement.setString(1, author);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();
        }
    }

    public ResultSet getBooksByAuthor(String author, ObservableList<Genre> genres) throws SQLException{
        String condition =
                genres.isEmpty() ? "" :
                "AND btg.idgenre IN (" +
                IntStream.range(0, genres.size()).mapToObj(i -> "?")
                .collect(Collectors.joining(", ")) + ") ";

        String query = "SELECT SQL_CALC_FOUND_ROWS books.idbooks, books.author, books.name, group_concat(gen.name SEPARATOR \", \") as genres FROM books " +
                "INNER JOIN books_to_genres AS btg ON books.idbooks = btg.idbook " +
                "INNER JOIN genres AS gen ON gen.idgenre = btg.idgenre " +
                "WHERE books.author LIKE ? " + condition +
                "GROUP BY books.idbooks " +
                "ORDER BY CASE WHEN books.author LIKE ? THEN 0 ELSE 1 END";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(
                query,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
        );

        preparedStatement.setString(1, "%" + author + "%");
        int parameterIndex = 2;
        for(Genre genre: genres){
            preparedStatement.setInt(parameterIndex++, genre.id);
        }
        preparedStatement.setString(parameterIndex, author + "%");

        return preparedStatement.executeQuery();
    }

    public ResultSet getBooksByName(String name, ObservableList<Genre> genres) throws SQLException{
        String condition =
                genres.isEmpty() ? "" :
                "AND btg.idgenre IN (" +
                IntStream.range(0, genres.size()).mapToObj(i -> "?")
                .collect(Collectors.joining(", ")) + ") ";

        String query = "SELECT SQL_CALC_FOUND_ROWS books.idbooks, books.author, books.name, group_concat(gen.name SEPARATOR \", \") as genres FROM books " +
                "INNER JOIN books_to_genres AS btg ON books.idbooks = btg.idbook " +
                "INNER JOIN genres AS gen ON gen.idgenre = btg.idgenre " +
                "WHERE books.name LIKE ? " + condition +
                "GROUP BY books.idbooks " +
                "ORDER BY CASE WHEN books.name LIKE ? THEN 0 ELSE 1 END";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(
                query,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
        );

        preparedStatement.setString(1, "%" + name + "%");
        int parameterIndex = 2;
        for(Genre genre: genres){
            preparedStatement.setInt(parameterIndex++, genre.id);
        }
        preparedStatement.setString(parameterIndex, name + "%");

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

    public ResultSet getLastBooks(ObservableList<Genre> genres) throws SQLException{
        String condition =
                genres.isEmpty() ? "" :
                "WHERE btg.idgenre IN (" +
                IntStream.range(0, genres.size()).mapToObj(i -> "?")
                .collect(Collectors.joining(", ")) + ") ";

        String query = "SELECT SQL_CALC_FOUND_ROWS books.idbooks, books.author, books.name, group_concat(gen.name SEPARATOR \", \") as genres FROM books " +
                "INNER JOIN books_to_genres AS btg ON books.idbooks = btg.idbook " +
                "INNER JOIN genres AS gen ON gen.idgenre = btg.idgenre " +
                condition + "GROUP BY books.idbooks ORDER BY books.idbooks DESC";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(
                query,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
        );
        int parameterIndex = 1;
        for(Genre genre: genres){
            preparedStatement.setInt(parameterIndex++, genre.id);
        }

        return preparedStatement.executeQuery();
    }
    public int addUser(String name, String surname, String email, String password) throws SQLException {
        String query = "INSERT INTO users (name, surname, email, password) VALUES (?,?,?,?)";

        try (Connection connection = getDbConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, password);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        }
    }

//    public User getUserById(int id) throws SQLException{
//        String query = "SELECT * FROM users WHERE idusers = ? LIMIT 1";
//
//        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
//        preparedStatement.setInt(1, id);
//        ResultSet resSet = preparedStatement.executeQuery();
//        if (!resSet.next()) return null;
//
//        return new User(
//                id,
//                resSet.getString("name"),
//                resSet.getString("surname"),
//                resSet.getString("email"),
//                resSet.getString("password")
//        );
//    }
//    public User[] getUserBySurname(String surname) throws SQLException{
//        String query = "SELECT * FROM users WHERE surname LIKE ?";
//
//        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
//        preparedStatement.setString(1, surname.trim());
//        ResultSet resSet = preparedStatement.executeQuery();
//
//        List<User> users = new ArrayList<User>();
//        while (resSet.next()){
//            users.add(new User(
//                    resSet.getInt("idusers"),
//                    resSet.getString("name"),
//                    resSet.getString("surname"),
//                    resSet.getString("email"),
//                    resSet.getString("password")
//            ));
//        }
//
//        return users.toArray(new User[0]);
//    }
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
                        resSet.getString("password"),
                        resSet.getBoolean("isAdmin")
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
