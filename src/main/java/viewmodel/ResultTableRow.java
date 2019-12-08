package viewmodel;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ResultTableRow {
    private LongProperty id;
    private StringProperty beginnigDate;
    private StringProperty endingDate;
    private StringProperty duration;

    public ResultTableRow() {
    }

    public ResultTableRow(Long id, Date beginnigDate, Date endingDate) {
        this.id = new SimpleLongProperty(id);
        this.beginnigDate = new SimpleStringProperty(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(beginnigDate));
        this.endingDate = new SimpleStringProperty(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(endingDate));
        long diff = endingDate.getTime() - beginnigDate.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
        String durationString = "";
        if (diffHours > 0) durationString = diffHours + " h " + diffMinutes + " m " + diffSeconds + " s";
        else if (diffMinutes > 0) durationString = diffMinutes + " m " + diffSeconds + " s";
        else if (diffSeconds > 0) durationString = diffSeconds + " s";
        this.duration = new SimpleStringProperty(durationString);
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

    public String getBeginnigDate() {
        return beginnigDate.get();
    }

    public StringProperty beginnigDateProperty() {
        return beginnigDate;
    }

    public void setBeginnigDate(String beginnigDate) {
        this.beginnigDate.set(beginnigDate);
    }

    public String getEndingDate() {
        return endingDate.get();
    }

    public StringProperty endingDateProperty() {
        return endingDate;
    }

    public void setEndingDate(String endingDate) {
        this.endingDate.set(endingDate);
    }

    public String getDuration() {
        return duration.get();
    }

    public StringProperty durationProperty() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration.set(duration);
    }
}
