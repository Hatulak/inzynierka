package viewmodel;

import javafx.beans.property.SimpleStringProperty;


public class ExperimentTableRow {

    private SimpleStringProperty name;
    private SimpleStringProperty status;
    private SimpleStringProperty action;
    private SimpleStringProperty result;

    public ExperimentTableRow(String name, String status, String action, String result) {
        this.name = new SimpleStringProperty(name);
        this.status = new SimpleStringProperty(status);
        this.action = new SimpleStringProperty(action);
        this.result = new SimpleStringProperty(result);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getAction() {
        return action.get();
    }

    public SimpleStringProperty actionProperty() {
        return action;
    }

    public void setAction(String action) {
        this.action.set(action);
    }

    public String getResult() {
        return result.get();
    }

    public SimpleStringProperty resultProperty() {
        return result;
    }

    public void setResult(String result) {
        this.result.set(result);
    }
}
