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
    private Long id;
    private String name;
    private Status status;
    private String optionsFilePath;
    private String descFilePath;
    private String flowFilePath;
    private String typeFilePath;
    private String mriOutputFilePath;
    private String outputKSpaceRePath;
    private String outputKSpaceImPath;
    private String outputImageAmpTxtPath;
    private String outputImageAmpBmpPath;
    private String outputImagePhaseBmpPath;


    public Experiment() {
        this.status = Status.CREATED;
    }

    public Experiment(String name, String optionsFilePath, String descFilePath, String flowFilePath, String typeFilePath) {
        this.name = name;
        this.optionsFilePath = optionsFilePath;
        this.descFilePath = descFilePath;
        this.flowFilePath = flowFilePath;
        this.typeFilePath = typeFilePath;
        this.status = Status.CREATED;
    }

    public Experiment(String name, String optionsFilePath) {
        this.name = name;
        this.optionsFilePath = optionsFilePath;
        this.status = Status.CREATED;
    }

    @Override
    public String toString() {
        return "Experiment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", optionsFilePath='" + optionsFilePath + '\'' +
                ", descFilePath='" + descFilePath + '\'' +
                ", flowFilePath='" + flowFilePath + '\'' +
                ", typeFilePath='" + typeFilePath + '\'' +
                ", mriOutputFilePath='" + mriOutputFilePath + '\'' +
                ", outputKSpaceRePath='" + outputKSpaceRePath + '\'' +
                ", outputKSpaceImPath='" + outputKSpaceImPath + '\'' +
                ", outputImageAmpTxtPath='" + outputImageAmpTxtPath + '\'' +
                ", outputImageAmpBmpPath='" + outputImageAmpBmpPath + '\'' +
                ", outputImagePhaseBmpPath='" + outputImagePhaseBmpPath + '\'' +
                '}';
    }
}
