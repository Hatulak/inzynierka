package viewmodel;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ExperimentTableRow {

    private LongProperty id;
    private StringProperty name;
    private StringProperty status;
    private StringProperty result;
    private StringProperty progress;
    private StringProperty creationDate;

    private Button startButton;
    private Button editButton;
    private Button deleteButton;
    private Button cancelButton;

    public ExperimentTableRow() {
    }

    public ExperimentTableRow(Long id, String name, String status, String result, Date creationDate) {
        this.id = new SimpleLongProperty(id);
        this.name = new SimpleStringProperty(name);
        this.status = new SimpleStringProperty(status);
        this.result = new SimpleStringProperty(result);
        this.progress = new SimpleStringProperty("");
        this.creationDate = new SimpleStringProperty(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(creationDate));
    }

    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getResult() {
        return result.get();
    }

    public StringProperty resultProperty() {
        return result;
    }

    public void setResult(String result) {
        this.result.set(result);
    }

    public String getProgress() {
        return progress.get();
    }

    public StringProperty progressProperty() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress.set(progress);
    }

    public String getCreationDate() {
        return creationDate.get();
    }

    public StringProperty creationDateProperty() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate.set(creationDate);
    }

    public void setButtons(Button startButton, Button editButton, Button deleteButton, Button cancelButton) {
        this.startButton = startButton;
        this.editButton = editButton;
        this.deleteButton = deleteButton;
        this.cancelButton = cancelButton;
    }


    public void disableButtons() {
        startButton.setDisable(true);
        editButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    public void enableButtons() {
        startButton.setDisable(false);
        editButton.setDisable(false);
        deleteButton.setDisable(false);
    }

    public void disableCancel() {
        cancelButton.setDisable(true);
    }

    public void enableCancel() {
        cancelButton.setDisable(false);
    }
}
