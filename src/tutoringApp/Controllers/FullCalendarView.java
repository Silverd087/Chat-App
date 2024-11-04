package tutoringApp.Controllers;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import tutoringApp.Models.AnchorPaneNode;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;


public class FullCalendarView {

    private ArrayList<AnchorPaneNode> allCalendarDays = new ArrayList<>(35);
    private VBox view;
    private Text calendarTitle;
    private YearMonth currentYearMonth;
    private GridPane calendar; // Added the calendar field
    private CalendarController calendarController;

    // Add getters and setters for yearMonth and calendarController
    public YearMonth getYearMonth() {
        return currentYearMonth;
    }

    public void setYearMonth(YearMonth yearMonth) {
        this.currentYearMonth = yearMonth;
        // Add code here to update the calendar view with the new yearMonth
    }

    public CalendarController getCalendarController() {
        return calendarController;
    }

    public void setCalendarController(CalendarController calendarController) {
        this.calendarController = calendarController;
        // Add code here to set up any necessary communication between the FullCalendarView and the CalendarController
    }
    /**
     * Create a calendar view
     * @param yearMonth year month to create the calendar of
     */
    public FullCalendarView(YearMonth yearMonth,CalendarController calendarController,String username) {
    	currentYearMonth = yearMonth;
    	this.calendarController = calendarController;        
    	// Create the calendar grid pane
        calendar = new GridPane();
        calendar.setPrefSize(680, 490);
        calendar.setGridLinesVisible(true);
        // Create rows and columns with anchor panes for the calendar
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                AnchorPaneNode ap = new AnchorPaneNode();
                ap.setPrefSize(200,200);
                calendar.add(ap,j,i);
                allCalendarDays.add(ap);
                ap.setOnMouseClicked(event -> {
                    LocalDate date = ap.getDate();
                    System.out.println("Anchor pane clicked! the date is : "+date);
                    if (date != null) {
                        calendarController.displayEventsForDateAndName(date, username);
                    }
                });
            }
        }
        // Days of the week labels
        Text[] dayNames = new Text[]{ new Text("Sunday"), new Text("Monday"), new Text("Tuesday"),
                                            new Text("Wednesday"), new Text("Thursday"), new Text("Friday"),
                                            new Text("Saturday") };
        GridPane dayLabels = new GridPane();
        dayLabels.setPrefWidth(600);
        Integer col = 0;
        for (Text txt : dayNames) {
            AnchorPane ap = new AnchorPane();
            ap.setPrefSize(200, 10);
            ap.setBottomAnchor(txt, 5.0);
            ap.getChildren().add(txt);
            dayLabels.add(ap, col++, 0);
        }
        // Create calendarTitle and buttons to change current month
        calendarTitle = new Text();
        Button previousMonth = new Button("<<");
        previousMonth.setOnAction(e -> previousMonth(username));
        Button nextMonth = new Button(">>");
        nextMonth.setOnAction(e -> nextMonth(username));
        HBox titleBar = new HBox(previousMonth, calendarTitle, nextMonth);
        titleBar.setAlignment(Pos.BASELINE_CENTER);
        // Populate calendar with the appropriate day numbers
        populateCalendar(yearMonth,username);
        // Create the calendar view
        view = new VBox(titleBar, dayLabels, calendar);
    }

    /**
     * Set the days of the calendar to correspond to the appropriate date
     * @param yearMonth year and month of month to render
     */
    public void populateCalendar(YearMonth yearMonth, String username) {
        // Get the date we want to start with on the calendar
        LocalDate calendarDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);
        // Dial back the day until it is SUNDAY (unless the month starts on a sunday)
        while (!calendarDate.getDayOfWeek().toString().equals("SUNDAY") ) {
            calendarDate = calendarDate.minusDays(1);
        }
        // Populate the calendar with day numbers
        for (AnchorPaneNode ap : allCalendarDays) {
            if (ap.getChildren().size()!= 0) {
                ap.getChildren().remove(0);
            }
            Text txt = new Text(String.valueOf(calendarDate.getDayOfMonth()));
            ap.setDate(calendarDate); // Set the user data of the AnchorPaneNode
            ap.setTopAnchor(txt, 5.0);
            ap.setLeftAnchor(txt, 5.0);
            ap.getChildren().add(txt);
            calendarDate = calendarDate.plusDays(1);

            // Check if there's a CalendarEvent for this date and if the username matches
            if (calendarController.checkForCalendarEvent(ap.getDate(), username)) {
                ap.setStyle("-fx-background-color: red;"); // Set the background color to red
            } else {
                ap.setStyle("-fx-background-color: white;"); // Set the background color to white
            }
        }
        // Change the title of the calendar
        calendarTitle.setText(yearMonth.getMonth().toString() + " " + String.valueOf(yearMonth.getYear()));
    }

    /**
     * Move the month back by one. Repopulate the calendar with the correct dates.
     */
    private void previousMonth(String username) {
        currentYearMonth = currentYearMonth.minusMonths(1);
        populateCalendar(currentYearMonth,username);
    }

    /**
     * Move the month forward by one. Repopulate the calendar with the correct dates.
     */
    private void nextMonth(String username) {
        currentYearMonth = currentYearMonth.plusMonths(1);
        populateCalendar(currentYearMonth,username);
    }

    public VBox getView() {
        return view;
    }

    public ArrayList<AnchorPaneNode>  getAllCalendarDays() {
        return allCalendarDays;
    }

    public void setAllCalendarDays(ArrayList<AnchorPaneNode> allCalendarDays) {
        this.allCalendarDays = allCalendarDays;
    }
}