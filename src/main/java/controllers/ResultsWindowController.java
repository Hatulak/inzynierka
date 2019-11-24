package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class ResultsWindowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea programOutputTextArea;

    @FXML
    private ImageView imageAmpImageView;

    @FXML
    private TextArea imageAmpTextArea;

    @FXML
    private ImageView imagePhaseImageView;

    @FXML
    private TextArea imagePhaseTextArea;

    @FXML
    private TextArea kSpaceReTextArea;

    @FXML
    private TextArea kSpaceImTextArea;

    @FXML
    void initialize() {
        assert programOutputTextArea != null : "fx:id=\"programOutputTextArea\" was not injected: check your FXML file 'results_window.fxml'.";
        assert imageAmpImageView != null : "fx:id=\"imageAmpImageView\" was not injected: check your FXML file 'results_window.fxml'.";
        assert imageAmpTextArea != null : "fx:id=\"imageAmpTextArea\" was not injected: check your FXML file 'results_window.fxml'.";
        assert imagePhaseImageView != null : "fx:id=\"imagePhaseImageView\" was not injected: check your FXML file 'results_window.fxml'.";
        assert imagePhaseTextArea != null : "fx:id=\"imagePhaseTextArea\" was not injected: check your FXML file 'results_window.fxml'.";
        assert kSpaceReTextArea != null : "fx:id=\"kSpaceReTextArea\" was not injected: check your FXML file 'results_window.fxml'.";
        assert kSpaceImTextArea != null : "fx:id=\"kSpaceImTextArea\" was not injected: check your FXML file 'results_window.fxml'.";

    }
}
