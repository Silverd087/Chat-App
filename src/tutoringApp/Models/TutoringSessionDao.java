package tutoringApp.Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import tutoringApp.Models.TutoringSession;
import tutoringApp.Utils.DatabaseConnection;

public class TutoringSessionDao {

    public List<TutoringSession> getAllTutoringSessions() {
        List<TutoringSession> tutoringSessions = new ArrayList<>();
        String query = "SELECT * FROM tutoring_sessions";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("idTutoringSession");
                String name = resultSet.getString("tutorName");
                String course = resultSet.getString("course");
                String chapter = resultSet.getString("chapter");
                String time = resultSet.getString("time");
                String date = resultSet.getString("date");
                String status = resultSet.getString("status");

                TutoringSession tutoringSession = new TutoringSession(id,name, course, chapter, time, date,status);
                tutoringSessions.add(tutoringSession);
                System.out.println(tutoringSession);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tutoringSessions;
    }

    public void addTutoringSession(String name, String course, String chapter, String time, LocalDate date) {
        String dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String query = "INSERT INTO tutoring_sessions (tutorName, course, chapter, time, date, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, name);
            statement.setString(2, course);
            statement.setString(3, chapter);
            statement.setString(4, time);
            statement.setString(5, dateString);
            statement.setString(6, "not booked");

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean updateTutoringSession(TutoringSession tutoringSession) {
        try (Connection connection = DatabaseConnection.getConnection()){
            String sql = "UPDATE tutoring_sessions SET tutorName = ?, course = ?, chapter = ?, time = ?, date = ?, status = ? WHERE idTutoringSession = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, tutoringSession.getTutorName());
            statement.setString(2, tutoringSession.getCourse());
            statement.setString(3, tutoringSession.getChapter());
            statement.setString(4, tutoringSession.getTime());
            statement.setString(5, tutoringSession.getDate());
            statement.setString(6, tutoringSession.getStatus());
            statement.setInt(7, tutoringSession.getId());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error updating tutoring session: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteTutoringSession(TutoringSession tutoringSession) {
        try(Connection connection = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM tutoring_sessions WHERE idTutoringSession = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, tutoringSession.getId());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting tutoring session: " + e.getMessage());
            return false;
        }
    }
    
    

}