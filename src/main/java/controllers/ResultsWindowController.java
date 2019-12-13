package controllers;

import database.model.Experiment;
import database.model.Result;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import lombok.extern.slf4j.Slf4j;
import utils.CommonConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

@Slf4j
public class ResultsWindowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    public TextArea mriOptionsFileTextArea;

    @FXML
    private TextArea programOutputTextArea;

    @FXML
    private ImageView imageAmpImageView;

    @FXML
    private TextArea imageAmpTextArea;

    @FXML
    private ImageView imagePhaseImageView;

    @FXML
    private TextArea kSpaceReTextArea;

    @FXML
    private TextArea kSpaceImTextArea;

    @FXML
    public Tab kspaceImTab;

    @FXML
    public Tab kspaceReTab;

    @FXML
    public Tab imagePhaseTab;

    @FXML
    public Tab imageAmpBmpTab;

    @FXML
    public Tab imageAmpTxtTab;

    @FXML
    public Tab programOutputTab;

    @FXML
    public Tab optionsFileTab;

    @FXML
    public TabPane tabPane;

    private MainWindowController mainWindowController;
    private Experiment experiment;
    private Result result;

    public void init() {
        try {
            initMriOptionsFileTextArea();
            initProgramOutputTextArea();
            initImageAmpTextArea();
            initKSpaceReTextArea();
            initKSpaceImTextArea();
            initImageAmpImageView();
            initImagePhaseImageView();
        } catch (NoSuchFieldException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(resources.getString("warning"));
            alert.setHeaderText(resources.getString("warning.header"));
            alert.setContentText(resources.getString("warning.context") + "\n" + e.getMessage());
            alert.showAndWait();
        }
    }

    private void initMriOptionsFileTextArea() throws NoSuchFieldException {
        try {
            List<String> strings = Files.readAllLines(Paths.get(result.getOptionsFilePath()));
            StringBuilder string = new StringBuilder();
            for (String s : strings)
                string.append(s).append("\n");
            mriOptionsFileTextArea.setText(string.toString());
        } catch (NoSuchFileException e) {
            throw new NoSuchFieldException(e.getMessage());
        } catch (IOException e) {
            log.error("Error during MriOptionsFile loading, path:" + result.getOptionsFilePath());
            log.error(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
            tabPane.getTabs().remove(optionsFileTab);
        }
    }

    private void initImagePhaseImageView() throws NoSuchFieldException {
        try {
            FileInputStream fis = new FileInputStream(new File(result.getOutputImagePhaseBmpPath()));
            Image read = new Image(fis, imagePhaseImageView.getFitWidth(), imagePhaseImageView.getFitHeight(), true, true);
            imagePhaseImageView.setImage(read);
        } catch (FileNotFoundException e) {
            log.info("File not found, it means, that checkbox for writing image phase bmp wasn't checked");
            tabPane.getTabs().remove(imagePhaseTab);
        }
    }

    private void initImageAmpImageView() throws NoSuchFieldException {
        try {
            FileInputStream fis = new FileInputStream(new File(result.getOutputImageAmpBmpPath()));
            Image read = new Image(fis, imageAmpImageView.getFitWidth(), imageAmpImageView.getFitHeight(), true, true);
            imageAmpImageView.setImage(read);
        } catch (FileNotFoundException e) {
            log.info("File not found, it means, that checkbox for writing image amp bmp wasn't checked");
            tabPane.getTabs().remove(imageAmpBmpTab);
        }
    }

    private void initKSpaceReTextArea() {
        try {
            List<String> strings = Files.readAllLines(Paths.get(result.getOutputKSpaceRePath()));
            StringBuilder string = new StringBuilder();
            for (String s : strings)
                string.append(s).append("\n");
            kSpaceReTextArea.setText(string.toString());
        } catch (NoSuchFileException e) {
            log.info("File not found, it means, that checkbox for writing kspace re txt wasn't checked");
            tabPane.getTabs().remove(kspaceReTab);
        } catch (IOException e) {
            log.error("Error during KSpaceReFile loading, path:" + result.getOutputKSpaceRePath());
            log.error(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
    }

    private void initKSpaceImTextArea() {
        try {
            List<String> strings = Files.readAllLines(Paths.get(result.getOutputKSpaceImPath()));
            StringBuilder string = new StringBuilder();
            for (String s : strings)
                string.append(s).append("\n");
            kSpaceImTextArea.setText(string.toString());
        } catch (NoSuchFileException e) {
            log.info("File not found, it means, that checkbox for writing kspace im txt wasn't checked");
            tabPane.getTabs().remove(kspaceImTab);
        } catch (IOException e) {
            log.error("Error during KSpaceImFile loading, path:" + result.getOutputKSpaceImPath());
            log.error(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
    }

    private void initImageAmpTextArea() {
        try {
            List<String> strings = Files.readAllLines(Paths.get(result.getOutputImageAmpTxtPath()));
            StringBuilder string = new StringBuilder();
            for (String s : strings)
                string.append(s).append("\n");
            imageAmpTextArea.setText(string.toString());
        } catch (NoSuchFileException e) {
            log.info("File not found, it means, that checkbox for writing image amp txt wasn't checked");
            tabPane.getTabs().remove(imageAmpTxtTab);
        } catch (IOException e) {
            log.error("Error during ImageAmpTxtFile loading, path:" + result.getOutputImageAmpTxtPath());
            log.error(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
    }

    private void initProgramOutputTextArea() throws NoSuchFieldException {
        try {
            List<String> strings = Files.readAllLines(Paths.get(result.getMriOutputFilePath()));
            StringBuilder string = new StringBuilder();
            for (String s : strings)
                string.append(s).append("\n");
            programOutputTextArea.setText(string.toString());
        } catch (NoSuchFileException e) {
            throw new NoSuchFieldException(e.getMessage());
        } catch (IOException e) {
            log.error("Error during mriOutputFile loading, path:" + result.getMriOutputFilePath());
            log.error(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
            tabPane.getTabs().remove(programOutputTab);
        }
    }

    public void setMainWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    @FXML
    public void saveOptionsFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName(CommonConstants.OPTIONS + experiment.getName() + "_" + result.getId() + CommonConstants.TXT);

        File fileToSave = fileChooser.showSaveDialog(null);

        if (fileToSave != null) {
            try {
                Files.write(fileToSave.toPath(), mriOptionsFileTextArea.getText().getBytes());
            } catch (IOException e) {
                log.error("Error during OptionsFile saving, file path:" + result.getOptionsFilePath() + " FileToSave Path: " + fileToSave.getAbsolutePath());
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void saveProgramOutput(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName(CommonConstants.OUTPUT + experiment.getName() + "_" + result.getId() + CommonConstants.TXT);

        File fileToSave = fileChooser.showSaveDialog(null);

        if (fileToSave != null) {
            try {
                Files.write(fileToSave.toPath(), programOutputTextArea.getText().getBytes());
            } catch (IOException e) {
                log.error("Error during ProgramOutput saving, file path:" + result.getMriOutputFilePath() + " FileToSave Path: " + fileToSave.getAbsolutePath());
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void saveImageAmpTxt(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName(experiment.getName() + "_" + result.getId() + CommonConstants.IMAGE_AMP_TXT + CommonConstants.TXT);

        File fileToSave = fileChooser.showSaveDialog(null);

        if (fileToSave != null) {
            try {
                Files.write(fileToSave.toPath(), imageAmpTextArea.getText().getBytes());
            } catch (IOException e) {
                log.error("Error during ImageAmpTxt saving, file path:" + result.getOutputImageAmpTxtPath() + " FileToSave Path: " + fileToSave.getAbsolutePath());
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void saveImageAmpBmp(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("BMP files (*.bmp)", "*.bmp");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName(experiment.getName() + "_" + result.getId() + CommonConstants.IMAGE_AMP_BMP + CommonConstants.SLICE_BMP);

        File fileToSave = fileChooser.showSaveDialog(null);

        if (fileToSave != null) {
            try {
                Files.copy(Paths.get(result.getOutputImageAmpBmpPath()), Paths.get(fileToSave.getAbsolutePath()));
            } catch (IOException e) {
                log.error("Error during ImageAmpBmp saving, file path:" + result.getOutputImageAmpBmpPath() + " FileToSave Path: " + fileToSave.getAbsolutePath());
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void saveImagePhaseBmp(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("BMP files (*.bmp)", "*.bmp");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName(experiment.getName() + "_" + result.getId() + CommonConstants.IMAGE_PHASE_BMP + CommonConstants.SLICE_BMP);

        File fileToSave = fileChooser.showSaveDialog(null);

        if (fileToSave != null) {
            try {
                Files.copy(Paths.get(result.getOutputImagePhaseBmpPath()), Paths.get(fileToSave.getAbsolutePath()));
            } catch (IOException e) {
                log.error("Error during ImagePhaseBmp saving, file path:" + result.getOutputImagePhaseBmpPath() + " FileToSave Path: " + fileToSave.getAbsolutePath());
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void saveKspaceReTxt(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName(experiment.getName() + "_" + result.getId() + CommonConstants.KSPACE_RE_TXT + CommonConstants.TXT);

        File fileToSave = fileChooser.showSaveDialog(null);

        if (fileToSave != null) {
            try {
                Files.write(fileToSave.toPath(), kSpaceReTextArea.getText().getBytes());
            } catch (IOException e) {
                log.error("Error during KspaceReTxt saving, file path:" + result.getOutputKSpaceRePath() + " FileToSave Path: " + fileToSave.getAbsolutePath());
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void saveKspaceImTxt(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName(experiment.getName() + "_" + result.getId() + CommonConstants.KSPACE_IM_TXT + CommonConstants.TXT);

        File fileToSave = fileChooser.showSaveDialog(null);

        if (fileToSave != null) {
            try {
                Files.write(fileToSave.toPath(), kSpaceImTextArea.getText().getBytes());
            } catch (IOException e) {
                log.error("Error during KspaceImTxt saving, file path:" + result.getOutputKSpaceImPath() + " FileToSave Path: " + fileToSave.getAbsolutePath());
                e.printStackTrace();
            }
        }
    }
}
