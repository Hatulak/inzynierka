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
    private String descFilePath;
    private String flowFilePath;
    private String typeFilePath;
    private String mriOutputFilePath;


    public Experiment() {
    }

    public Experiment(String name, String optionsFilePath, String descFilePath, String flowFilePath, String typeFilePath) {
        this.name = name;
        this.optionsFilePath = optionsFilePath;
        this.descFilePath = descFilePath;
        this.flowFilePath = flowFilePath;
        this.typeFilePath = typeFilePath;
    }

    public Experiment(String name, String optionsFilePath) {
        this.name = name;
        this.optionsFilePath = optionsFilePath;
    }
}
