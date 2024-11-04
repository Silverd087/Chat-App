package tutoringApp.Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tutoringApp.Utils.DatabaseConnection;

public class UserDao {
	
    public List<String> getAllUsernames(String username) {
        String query = "SELECT username FROM users where username != ?";
        List<String> usernames = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                usernames.add(resultSet.getString("username"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usernames;
    }
    public User authenticateUser(String username, String password) throws UserNotFoundException {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        User user = null;
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(user==null) {
        	throw new UserNotFoundException();
        }
        return user;
    }
    
    public User getUserByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    return new User(
                            resultSet.getString("username"),
                            resultSet.getString("password")
                    );
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public void addUser(User user) throws SQLException {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());

            preparedStatement.executeUpdate();

        }
    }

}