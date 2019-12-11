package controllers;

import database.model.Experiment;
import database.model.Result;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.extern.slf4j.Slf4j;

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

    }

    @FXML
    public void saveProgramOutput(ActionEvent actionEvent) {

    }

    @FXML
    public void saveImageAmpTxt(ActionEvent actionEvent) {

    }

    @FXML
    public void saveImageAmpBmp(ActionEvent actionEvent) {

    }

    @FXML
    public void saveImagePhaseBmp(ActionEvent actionEvent) {

    }

    @FXML
    public void saveKspaceReTxt(ActionEvent actionEvent) {

    }

    @FXML
    public void saveKspaceImTxt(ActionEvent actionEvent) {

    }
}
