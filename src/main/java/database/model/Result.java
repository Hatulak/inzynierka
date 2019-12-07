package database.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@Entity
public class Result {
    @Id
    @GeneratedValue
    private Long id;
    private Date beginningDate;
    private Date endingDate;
    private String optionsFilePath;
    private String mriOutputFilePath;
    private String outputKSpaceRePath;
    private String outputKSpaceImPath;
    private String outputImageAmpTxtPath;
    private String outputImageAmpBmpPath;
    private String outputImagePhaseBmpPath;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experimentId")
    private Experiment experiment;

    public Result() {

    }
}
