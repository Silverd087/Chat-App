package tutoringApp.Models;

import tutoringApp.Utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CalendarEventDao {
    private Connection connection;

    public CalendarEventDao() {
        connection = DatabaseConnection.getConnection();
    }

    public CalendarEvent findById(int id) {
        String query = "SELECT * FROM calendar_events WHERE idcalendar_events =?";
        CalendarEvent calendarEvent = null;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                calendarEvent = new CalendarEvent();
                calendarEvent.setId(resultSet.getInt("idcalendar_events"));
                calendarEvent.setTuteeName(resultSet.getString("tutee_name"));
                calendarEvent.setTutorName(resultSet.getString("tutor_name"));
                calendarEvent.setCourse(resultSet.getString("course"));
                calendarEvent.setDate(resultSet.getString("date"));
                calendarEvent.setTime(resultSet.getString("time"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return calendarEvent;
    }

    public List<CalendarEvent> findAll() {
        String query = "SELECT * FROM calendar_events";
        List<CalendarEvent> calendarEvents = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                CalendarEvent calendarEvent = new CalendarEvent();
                calendarEvent.setId(resultSet.getInt("idcalendar_events"));
                calendarEvent.setTuteeName(resultSet.getString("tutee_name"));
                calendarEvent.setTutorName(resultSet.getString("tutor_name"));
                calendarEvent.setCourse(resultSet.getString("course"));
                calendarEvent.setDate(resultSet.getString("date"));
                calendarEvent.setTime(resultSet.getString("time"));
                calendarEvents.add(calendarEvent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return calendarEvents;
    }

    public void save(CalendarEvent calendarEvent) {
        String query = "INSERT INTO calendar_events (tutee_name, tutor_name, course, date, time) VALUES (?,?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, calendarEvent.getTuteeName());
            stmt.setString(2, calendarEvent.getTutorName());
            stmt.setString(3, calendarEvent.getCourse());
            stmt.setString(4, calendarEvent.getDate());
            stmt.setString(5, calendarEvent.getTime());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(CalendarEvent calendarEvent) {
        String query = "UPDATE calendar_events SET tutee_name =?, tutor_name =?, course =?, date =?, time =? WHERE idcalendar_events =?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, calendarEvent.getTuteeName());
            stmt.setString(2, calendarEvent.getTutorName());
            stmt.setString(3, calendarEvent.getCourse());
            stmt.setString(4, calendarEvent.getDate());
            stmt.setString(5, calendarEvent.getTime());
            stmt.setLong(6, calendarEvent.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String query = "DELETE FROM calendar_events WHERE idcalendar_events =?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<CalendarEvent> findByDateAndName(String date, String name) {
        String query = "SELECT * FROM calendar_events WHERE date =? AND (tutee_name =? OR tutor_name =?)";
        List<CalendarEvent> calendarEvents = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, date);
            stmt.setString(2, name);
            stmt.setString(3, name);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                CalendarEvent calendarEvent = new CalendarEvent();
                calendarEvent.setId(resultSet.getInt("idcalendar_events"));
                calendarEvent.setTuteeName(resultSet.getString("tutee_name"));
                calendarEvent.setTutorName(resultSet.getString("tutor_name"));
                calendarEvent.setCourse(resultSet.getString("course"));
                calendarEvent.setDate(resultSet.getString("date"));
                calendarEvent.setTime(resultSet.getString("time"));
                calendarEvents.add(calendarEvent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return calendarEvents;
    }
    public CalendarEvent getCalendarEventByTutoringSession(TutoringSession tutoringSession, String username) {
        String query = "SELECT * FROM calendar_events WHERE (tutee_name = ? OR tutor_name = ?) AND course = ? AND date = ? AND time = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, username);
            statement.setString(3, tutoringSession.getCourse());
            statement.setString(4, tutoringSession.getDate());
            statement.setString(5, tutoringSession.getTime());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                CalendarEvent calendarEvent = new CalendarEvent();
                calendarEvent.setId(resultSet.getInt("idcalendar_events"));
                calendarEvent.setTuteeName(resultSet.getString("tutee_name"));
                calendarEvent.setTutorName(resultSet.getString("tutor_name"));
                calendarEvent.setCourse(resultSet.getString("course"));
                calendarEvent.setDate(resultSet.getString("date"));
                calendarEvent.setTime(resultSet.getString("time"));
                return calendarEvent;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving CalendarEvent by TutoringSession and username: " + e.getMessage());
            return null;
        }
    }
    
    
    public boolean deleteCalendarEvent(CalendarEvent calendarEvent) {
        String query = "DELETE FROM calendar_events WHERE idcalendar_events = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, calendarEvent.getId());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting CalendarEvent: " + e.getMessage());
            return false;
        }
    }
}