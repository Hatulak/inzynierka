package controllers;

import database.model.Experiment;
import database.model.Result;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import java.nio.file.Paths;
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

    private MainWindowController mainWindowController;
    private Experiment experiment;
    private Result result;

    public void init() {
        initMriOptionsFileTextArea();
        initProgramOutputTextArea();
        initImageAmpTextArea();
        initKSpaceReTextArea();
        initKSpaceImTextArea();
        initImageAmpImageView();
        initImagePhaseImageView();
    }

    private void initMriOptionsFileTextArea() {
        try {
            List<String> strings = Files.readAllLines(Paths.get(result.getOptionsFilePath()));
            StringBuilder string = new StringBuilder();
            for (String s : strings)
                string.append(s).append("\n");
            mriOptionsFileTextArea.setText(string.toString());
        } catch (IOException e) {
            log.error("Error during MriOptionsFile loading, path:" + result.getOptionsFilePath());
            e.printStackTrace();
        }
    }

    private void initImagePhaseImageView() {
        try {
            FileInputStream fis = new FileInputStream(new File(result.getOutputImagePhaseBmpPath()));
            Image read = new Image(fis, imagePhaseImageView.getFitWidth(), imagePhaseImageView.getFitHeight(), true, true);
            imagePhaseImageView.setImage(read);
        } catch (FileNotFoundException e) {
            log.error("Error during ImagePhaseBmp loading, path:" + result.getOutputImagePhaseBmpPath());
            e.printStackTrace();
        }
    }

    private void initImageAmpImageView() {
        try {
            FileInputStream fis = new FileInputStream(new File(result.getOutputImageAmpBmpPath()));
            Image read = new Image(fis, imageAmpImageView.getFitWidth(), imageAmpImageView.getFitHeight(), true, true);
            imageAmpImageView.setImage(read);
        } catch (FileNotFoundException e) {
            log.error("Error during ImageAmpBmp loading, path:" + result.getOutputImageAmpBmpPath());
            e.printStackTrace();
        }
    }

    private void initKSpaceReTextArea() {
        try {
            List<String> strings = Files.readAllLines(Paths.get(result.getOutputKSpaceRePath()));
            StringBuilder string = new StringBuilder();
            for (String s : strings)
                string.append(s).append("\n");
            kSpaceReTextArea.setText(string.toString());
        } catch (IOException e) {
            log.error("Error during KSpaceReFile loading, path:" + result.getOutputKSpaceRePath());
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
        } catch (IOException e) {
            log.error("Error during KSpaceImFile loading, path:" + result.getOutputKSpaceImPath());
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
        } catch (IOException e) {
            log.error("Error during ImageAmpTxtFile loading, path:" + result.getOutputImageAmpTxtPath());
            e.printStackTrace();
        }
    }

    private void initProgramOutputTextArea() {
        try {
            List<String> strings = Files.readAllLines(Paths.get(result.getMriOutputFilePath()));
            StringBuilder string = new StringBuilder();
            for (String s : strings)
                string.append(s).append("\n");
            programOutputTextArea.setText(string.toString());
        } catch (IOException e) {
            log.error("Error during mriOutputFile loading, path:" + result.getMriOutputFilePath());
            e.printStackTrace();
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
