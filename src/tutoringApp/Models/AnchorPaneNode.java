package tutoringApp.Models;

import java.time.LocalDate;

import javafx.scene.layout.AnchorPane;

public class AnchorPaneNode extends AnchorPane {
    private LocalDate date;

    public AnchorPaneNode() {
        super();
    }

    public AnchorPaneNode(LocalDate date) {
        super();
        this.date = date;
        setUserData(date);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
        setUserData(date);
    }
}