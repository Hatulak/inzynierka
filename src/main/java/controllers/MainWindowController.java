package controllers;

import controllers.runnable.ExperimentRunnable;
import database.model.Experiment;
import database.model.Result;
import database.model.Status;
import database.repository.ExperimentRepository;
import database.repository.ResultRepository;
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
import org.apache.commons.io.FileUtils;
import viewmodel.ExperimentTableRow;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    private ThreadPoolExecutor threadPoolExecutor;
    private Map<Long, Future> submittedTasks = new HashMap<>();

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
                        final Button deleteButton = new Button(resources.getString("experiment.action.delete"));
                        final Button cancelButton = new Button(resources.getString("experiment.action.cancel"));

                        getTableView().getItems().get(getIndex()).setButtons(startButton, editButton, deleteButton, cancelButton);

                        startButton.setOnAction(e -> {
                            ExperimentTableRow experimentTableRow = getTableView().getItems().get(getIndex());
                            experimentTableRow.disableButtons();
                            log.info("Starting task with id: " + experimentTableRow.getId());
                            Experiment experiment = ExperimentRepository.findById(experimentTableRow.getId());
                            startProcessingExperiment(experiment);
                        });
                        editButton.setOnAction(e -> {
                            ExperimentTableRow experimentTableRow = getTableView().getItems().get(getIndex());
                            Experiment experiment = ExperimentRepository.findById(experimentTableRow.getId());
                            editExperiment(experiment);
                        });
                        deleteButton.setOnAction(e -> {
                            ExperimentTableRow experimentTableRow = getTableView().getItems().get(getIndex());
                            Experiment experiment = ExperimentRepository.findById(experimentTableRow.getId());
                            deleteExperiment(experiment);
                            refeshExperimentList();
                        });
                        cancelButton.setOnAction(e -> {
                            ExperimentTableRow experimentTableRow = getTableView().getItems().get(getIndex());
                            Experiment experiment = ExperimentRepository.findById(experimentTableRow.getId());
                            experimentTableRow.disableCancel();
                            cancelExperiment(experiment);
                        });

                        if (getTableView().getItems().get(getIndex()).getStatus().matches(String.valueOf(Status.RUNNING))) {
                            getTableView().getItems().get(getIndex()).disableButtons();
                            getTableView().getItems().get(getIndex()).enableCancel();
                        } else if (getTableView().getItems().get(getIndex()).getStatus().matches(String.valueOf(Status.IN_QUEUE))) {
                            getTableView().getItems().get(getIndex()).disableButtons();
                            getTableView().getItems().get(getIndex()).disableCancel();
                        } else {
                            getTableView().getItems().get(getIndex()).enableButtons();
                            getTableView().getItems().get(getIndex()).disableCancel();
                        }
                        HBox hbox = new HBox(startButton, editButton, deleteButton, cancelButton);
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

        if (Runtime.getRuntime().availableProcessors() == 1)
            threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        else
            threadPoolExecutor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() - 1, Runtime.getRuntime().availableProcessors() - 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

        initializeExperimentsList();
        tableExperiments.setItems(experimentsObservableList);

    }

    private void cancelExperiment(Experiment experiment) {
        Future future = getSubmittedTask(experiment.getId());
        if (future != null)
            if (future.isDone() || future.isCancelled()) {
                removeSubmittedTask(experiment.getId());
                return;
            } else System.out.println("???");
        getSubmittedTask(experiment.getId()).cancel(true); //fixme - tutaj cos wywala
        removeSubmittedTask(experiment.getId());
    }

    private void deleteExperiment(Experiment experiment) {
        try {
            FileUtils.deleteDirectory(new File(experiment.getOptionsFilePath()).getParentFile());
            List<Result> byExperimentId = ResultRepository.findByExperimentId(experiment.getId());
            if (byExperimentId.size() > 0)
                FileUtils.deleteDirectory(new File(byExperimentId.get(0).getOptionsFilePath()).getParentFile().getParentFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        ExperimentRepository.delete(experiment);
        for (ExperimentTableRow experimentTableRow : experimentsObservableList) {
            if (experimentTableRow.getId() == experiment.getId()) {
                experimentsObservableList.remove(experimentTableRow);
                break;
            }
        }
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
            stage.setResizable(false);
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
            stage.setMinHeight(400);
            stage.setMinWidth(600);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startProcessingExperiment(Experiment experiment) {
        log.info("Adding experiment: " + experiment.getId() + " to queue");
        experiment.setStatus(Status.IN_QUEUE);
        ExperimentRepository.merge(experiment);
        refeshExperimentList();

        Future<?> submit = threadPoolExecutor.submit(new ExperimentRunnable(experiment, this));
        putSubmittedTask(experiment.getId(), submit);
    }

    public void refeshExperimentList() {
        ExperimentRepository.getAll().forEach(e -> {
            for (ExperimentTableRow experimentTableRow : experimentsObservableList) {
                if (experimentTableRow.getId() == e.getId()) {
                    experimentTableRow.setName(e.getName());
                    experimentTableRow.setStatus(e.getStatus().toString());
                    if (experimentTableRow.getStatus().matches(String.valueOf(Status.IN_QUEUE))) {
                        experimentTableRow.disableCancel();
                        experimentTableRow.disableButtons();
                    }
                }
            }
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
            stage.setResizable(false);
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

    public synchronized Map<Long, Future> getSubmittedTasks() {
        return submittedTasks;
    }

    public synchronized Future getSubmittedTask(Long experimentId) {
        return submittedTasks.get(experimentId);
    }

    public synchronized void removeSubmittedTask(Long experimentId) {
        submittedTasks.remove(experimentId);
    }

    public synchronized void putSubmittedTask(Long experimentId, Future future) {
        submittedTasks.put(experimentId, future);
    }


    public void shutdown() {
        List<Runnable> queueList = new LinkedList<>();
        threadPoolExecutor.getQueue().drainTo(queueList);
        experimentsObservableList.forEach(tr -> {
            if (tr.getStatus().matches(String.valueOf(Status.IN_QUEUE))) {
                Experiment byId = ExperimentRepository.findById(tr.getId());
                byId.setStatus(Status.CANCELLED);
                ExperimentRepository.merge(byId);
            }
        });
        threadPoolExecutor.shutdownNow();
    }
}

