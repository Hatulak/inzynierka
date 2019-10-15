package database.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Builder
@AllArgsConstructor
@Entity
public class Experiment {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String optionsFilePath;

    public Experiment() {
    }

    public Experiment(String name, String optionsFilePath) {
        this.name = name;
        this.optionsFilePath = optionsFilePath;
    }
}
