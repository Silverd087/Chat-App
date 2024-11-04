package tutoringApp.Models;

import tutoringApp.Utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TutorDao {
    private Connection connection;

    public TutorDao() {
        connection = DatabaseConnection.getConnection();
    }

    public Tutor findByUsername(String username) {
        String query = "SELECT * FROM tutors WHERE username =?";
        Tutor tutor = null;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                tutor = new Tutor();
                tutor.setId(resultSet.getInt("id"));
                tutor.setUsername(resultSet.getString("username"));
                tutor.setPassword(resultSet.getString("password"));
                tutor.setCourses(resultSet.getString("courses").split(","));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tutor;
    }

    public List<Tutor> findAll() {
        String query = "SELECT * FROM tutors";
        List<Tutor> tutors = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                Tutor tutor = new Tutor();
                tutor.setId(resultSet.getInt("id"));
                tutor.setUsername(resultSet.getString("username"));
                tutor.setPassword(resultSet.getString("password"));
                tutor.setCourses(resultSet.getString("courses").split(","));
                tutors.add(tutor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tutors;
    }

    public void save(Tutor tutor) {
        String query = "INSERT INTO tutors (username, password, courses) VALUES (?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, tutor.getUsername());
            stmt.setString(2, tutor.getPassword());
            stmt.setString(3, String.join(",", tutor.getCourses()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Tutor tutor) {
        String query = "UPDATE tutors SET username =?, password =?, courses =? WHERE id =?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, tutor.getUsername());
            stmt.setString(2, tutor.getPassword());
            stmt.setString(3, String.join(",", tutor.getCourses()));
            stmt.setInt(4, tutor.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String query = "DELETE FROM tutors WHERE id =?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkIfUserIsTutor(String username) {
        String query = "SELECT COUNT(*) FROM tutors WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}