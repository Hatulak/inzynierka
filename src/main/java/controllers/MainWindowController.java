package controllers;

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

import java.io.*;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

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

        Callback<TableColumn<ExperimentTableRow, String>, TableCell<ExperimentTableRow, String>> cellFactory = param -> {
            final TableCell<ExperimentTableRow, String> cell = new TableCell<ExperimentTableRow, String>() {
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
                            experiment.setStatus(Status.PROCESSING);
                            ExperimentRepository.merge(experiment);
                            startProcessingExperiment(experiment);
                        });
                        setGraphic(actionButton);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        tableColumnExperimentAction.setCellFactory(cellFactory);

        initializeExperimentsList();
        tableExperiments.setItems(experimentsObservableList);

    }

    private void startProcessingExperiment(Experiment experiment) {
        String optionsFilePath = experiment.getOptionsFilePath();
        Thread thread1 = new Thread(() -> {
            File outputFile = new File(experiment.getMriOutputFilePath());
            try {
                outputFile.createNewFile();
            } catch (IOException e) {
                log.error("Error during file creation, experiment id: " + experiment.getId());
                e.printStackTrace();
            }
            ProcessBuilder builder = new ProcessBuilder("lbm.exe", optionsFilePath)
                    .directory(new File("C:\\Users\\Przemek\\MRISimulatorDB")); //todo - tak nie może być, trzeba naprawić
            Process process = null;
            try {
                process = builder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStream is = process.getInputStream();
            OutputStream os = process.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            try {
                FileWriter fileWriter = new FileWriter(outputFile);
                while ((line = reader.readLine()) != null) {
                    os.write('\n');
                    System.out.println(line);
                    fileWriter.write(line + "\n");
                    if (line.matches("Please press the any key to continue")) {
                        os.write('\n');
                        break;
                    }
                    //todo - tu pewnie jakaś obsługa progress bara czy coś takiego
                }
                fileWriter.close();
                experiment.setStatus(Status.DONE);
                ExperimentRepository.merge(experiment);
                refeshExperimentList();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        thread1.start();
        experiment.setStatus(Status.RUNNING);
        ExperimentRepository.merge(experiment);
        refeshExperimentList();
    }

    private void refeshExperimentList() {
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

