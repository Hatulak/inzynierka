package controllers;

import controllers.runnable.ExperimentRunnable;
import database.model.Experiment;
import database.model.Status;
import database.repository.ExperimentRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;
import viewmodel.ExperimentTableRow;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class MainWindowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MenuItem menuItemNewExperiment;

    @FXML
    private MenuItem menuItemClose;

    @FXML
    private TableView<ExperimentTableRow> tableExperiments;

    @FXML
    private TableColumn<ExperimentTableRow, Long> tableColumnExperimentId;

    @FXML
    private TableColumn<ExperimentTableRow, String> tableColumnExperimentName;

    @FXML
    private TableColumn<ExperimentTableRow, String> tableColumnExperimentStatus;

    @FXML
    private TableColumn<ExperimentTableRow, String> tableColumnExperimentAction;

    @FXML
    private TableColumn<ExperimentTableRow, String> tableColumnExperimentResults;

    private ObservableList<ExperimentTableRow> experimentsObservableList = FXCollections.observableArrayList();

    private ExecutorService executorService;

    @FXML
    void initialize() {
        assert menuItemNewExperiment != null : "fx:id=\"menuItemNewExperiment\" was not injected: check your FXML file 'main_window.fxml'.";
        assert menuItemClose != null : "fx:id=\"menuItemClose\" was not injected: check your FXML file 'main_window.fxml'.";
        assert tableExperiments != null : "fx:id=\"tableExperiments\" was not injected: check your FXML file 'main_window.fxml'.";
        assert tableColumnExperimentId != null : "fx:id=\"tableColumnExperimentId\" was not injected: check your FXML file 'main_window.fxml'.";
        assert tableColumnExperimentName != null : "fx:id=\"tableColumnExperimentName\" was not injected: check your FXML file 'main_window.fxml'.";
        assert tableColumnExperimentStatus != null : "fx:id=\"tableColumnExperimentStatus\" was not injected: check your FXML file 'main_window.fxml'.";
        assert tableColumnExperimentAction != null : "fx:id=\"tableColumnExperimentAction\" was not injected: check your FXML file 'main_window.fxml'.";
        assert tableColumnExperimentResults != null : "fx:id=\"tableColumnExperimentResults\" was not injected: check your FXML file 'main_window.fxml'.";

        tableColumnExperimentId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnExperimentName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnExperimentStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableColumnExperimentAction.setCellValueFactory(new PropertyValueFactory<>("action"));
        tableColumnExperimentResults.setCellValueFactory(new PropertyValueFactory<>("result"));

        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);

        Callback<TableColumn<ExperimentTableRow, String>, TableCell<ExperimentTableRow, String>> actionsCellFactory = param -> {
            return new TableCell<ExperimentTableRow, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final Button actionButton = new Button(resources.getString("experiment.action.start"));
                        actionButton.setOnAction(e -> {
                            ExperimentTableRow experimentTableRow = getTableView().getItems().get(getIndex());
                            log.info("Starting task with id: " + experimentTableRow.getId());
                            Experiment experiment = ExperimentRepository.findById(experimentTableRow.getId());
                            startProcessingExperiment(experiment);
                        });
                        setGraphic(actionButton);
                        setText(null);
                    }
                }
            };
        };
        tableColumnExperimentAction.setCellFactory(actionsCellFactory);

        Callback<TableColumn<ExperimentTableRow, String>, TableCell<ExperimentTableRow, String>> resultsCellFactory = param -> {
            return new TableCell<ExperimentTableRow, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final Button actionButton = new Button(resources.getString("experiment.results"));
                        actionButton.setOnAction(e -> {
                            ExperimentTableRow experimentTableRow = getTableView().getItems().get(getIndex());
                            Experiment experiment = ExperimentRepository.findById(experimentTableRow.getId());
                            showExperimentResults(experiment);
                        });
                        setGraphic(actionButton);
                        setText(null);
                    }
                }
            };
        };
        tableColumnExperimentResults.setCellFactory(resultsCellFactory);

        initializeExperimentsList();
        tableExperiments.setItems(experimentsObservableList);

    }

    private void showExperimentResults(Experiment experiment) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/results_window.fxml"), resources);
            Parent root = loader.load();

            ResultsWindowController resultsWindowController = loader.getController();
            resultsWindowController.setMainWindowController(this);
            resultsWindowController.setExperiment(experiment);
            resultsWindowController.init();

            Stage stage = new Stage();
            stage.setTitle(resources.getString("experiment.results"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startProcessingExperiment(Experiment experiment) {
        experiment.setStatus(Status.IN_QUEUE);
        ExperimentRepository.merge(experiment);
        refeshExperimentList();
        executorService.submit(new ExperimentRunnable(experiment, this));
    }

    public void refeshExperimentList() {
        experimentsObservableList.clear();
        ExperimentRepository.getAll().forEach(e -> {
            experimentsObservableList.add(new ExperimentTableRow(e.getId(), e.getName(), e.getStatus().toString(), ""));
        });

    }

    @FXML
    public void openNewExperimentWindow(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/new_experiment_window.fxml"), resources);
            Parent root = loader.load();

            NewExperimentWindowController newExperimentWindowController = loader.getController();
            newExperimentWindowController.setMainWindowController(this);

            Stage stage = new Stage();
            stage.setTitle(resources.getString("new.experiment.title"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeExperimentsList() {
        List<ExperimentTableRow> list = new LinkedList<>();
        ExperimentRepository.getAll().forEach(e -> {
            list.add(new ExperimentTableRow(e.getId(), e.getName(), e.getStatus().toString(), ""));
        });
        experimentsObservableList.addAll(list);
    }

    public void addExperimentToExperimentsList(Experiment experiment) {
        experimentsObservableList.add(new ExperimentTableRow(experiment.getId(), experiment.getName(), experiment.getStatus().toString(), ""));
    }
}

