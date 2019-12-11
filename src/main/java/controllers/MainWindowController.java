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
import javafx.scene.layout.HBox;
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

    @FXML
    private TableColumn<ExperimentTableRow, String> tableColumnExperimentCreationDate;

    @FXML
    private TableColumn<ExperimentTableRow, String> tableColumnExperimentProgress;

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
        assert tableColumnExperimentCreationDate != null : "fx:id=\"tableColumnExperimentCreationDate\" was not injected: check your FXML file 'main_window.fxml'.";

        tableColumnExperimentId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnExperimentName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnExperimentStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableColumnExperimentAction.setCellValueFactory(new PropertyValueFactory<>("action"));
        tableColumnExperimentResults.setCellValueFactory(new PropertyValueFactory<>("result"));
        tableColumnExperimentProgress.setCellValueFactory(new PropertyValueFactory<>("progress"));
        tableColumnExperimentCreationDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));


        //KOLUMNA ACTION
        Callback<TableColumn<ExperimentTableRow, String>, TableCell<ExperimentTableRow, String>> actionsCellFactory = param -> {
            return new TableCell<ExperimentTableRow, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final Button startButton = new Button(resources.getString("experiment.action.start"));
                        final Button editButton = new Button(resources.getString("experiment.action.edit"));

                        startButton.setOnAction(e -> {
                            ExperimentTableRow experimentTableRow = getTableView().getItems().get(getIndex());
                            log.info("Starting task with id: " + experimentTableRow.getId());
                            Experiment experiment = ExperimentRepository.findById(experimentTableRow.getId());
                            startProcessingExperiment(experiment);
                        });
                        editButton.setOnAction(e -> {
                            ExperimentTableRow experimentTableRow = getTableView().getItems().get(getIndex());
                            Experiment experiment = ExperimentRepository.findById(experimentTableRow.getId());
                            editExperiment(experiment);
                        });
                        HBox hbox = new HBox(startButton, editButton);
                        hbox.setSpacing(5);
                        setGraphic(hbox);
                        setText(null);
                    }
                }
            };
        };
        tableColumnExperimentAction.setCellFactory(actionsCellFactory);

        //KOLUMNA RESULTS
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


        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);

        initializeExperimentsList();
        tableExperiments.setItems(experimentsObservableList);

    }

    private void editExperiment(Experiment experiment) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/experiment_edition_window.fxml"), resources);
            Parent root = loader.load();

            ExperimentEditionWindowController experimentEditionWindowController = loader.getController();
            experimentEditionWindowController.setMainWindowController(this);
            experimentEditionWindowController.setExperiment(experiment);
            experimentEditionWindowController.init();

            Stage stage = new Stage();
            stage.setTitle(resources.getString("experiment.edit"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showExperimentResults(Experiment experiment) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/results_list_window.fxml"), resources);
            Parent root = loader.load();

            ResultsListWindowController resultsListWindowController = loader.getController();
            resultsListWindowController.setMainWindowController(this);
            resultsListWindowController.setExperiment(experiment);
            resultsListWindowController.init();

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
//        experimentsObservableList.clear();
        ExperimentRepository.getAll().forEach(e -> {
            for (ExperimentTableRow experimentTableRow : experimentsObservableList) {
                if (experimentTableRow.getId() == e.getId()) {
                    experimentTableRow.setName(e.getName());
                    experimentTableRow.setStatus(e.getStatus().toString());
                }
            }
//            experimentsObservableList.add(new ExperimentTableRow(e.getId(), e.getName(), e.getStatus().toString(), ""));
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
            list.add(new ExperimentTableRow(e.getId(), e.getName(), e.getStatus().toString(), "", e.getCreationDate()));
        });
        experimentsObservableList.addAll(list);
    }

    public void addExperimentToExperimentsList(Experiment experiment) {
        experimentsObservableList.add(new ExperimentTableRow(experiment.getId(), experiment.getName(), experiment.getStatus().toString(), "", experiment.getCreationDate()));
    }

    public ObservableList<ExperimentTableRow> getExperimentsObservableList() {
        return experimentsObservableList;
    }

}

