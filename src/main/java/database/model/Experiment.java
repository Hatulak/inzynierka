package database.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@Entity
public class Experiment {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Date creationDate;
    private Status status;
    private String optionsFilePath;
    private String descFilePath;
    private String flowFilePath;
    private String typeFilePath;
//    private String mriOutputFilePath;
//    private String outputKSpaceRePath;
//    private String outputKSpaceImPath;
//    private String outputImageAmpTxtPath;
//    private String outputImageAmpBmpPath;
//    private String outputImagePhaseBmpPath;

//    @OneToMany(cascade = CascadeType.ALL,orphanRemoval =  true)
//    private List<Result> resultList;


    public Experiment() {
        this.status = Status.CREATED;
        this.creationDate = new Date();
    }

    public Experiment(String name, String optionsFilePath, String descFilePath, String flowFilePath, String typeFilePath) {
        this.name = name;
        this.optionsFilePath = optionsFilePath;
        this.descFilePath = descFilePath;
        this.flowFilePath = flowFilePath;
        this.typeFilePath = typeFilePath;
        this.status = Status.CREATED;
        this.creationDate = new Date();
    }

    public Experiment(String name, String optionsFilePath) {
        this.name = name;
        this.optionsFilePath = optionsFilePath;
        this.status = Status.CREATED;
        this.creationDate = new Date();
    }
}
