package controllers;

import database.model.Experiment;
import database.model.Result;
import database.repository.ResultRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;
import viewmodel.ResultTableRow;

import java.io.IOException;
import java.util.ResourceBundle;

@Slf4j
public class ResultsListWindowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private TableView<ResultTableRow> tableViewResults;

    @FXML
    private TableColumn<ResultTableRow, Long> tableColumnResultId;

    @FXML
    private TableColumn<ResultTableRow, String> tableColumnResultBeginningDate;

    @FXML
    private TableColumn<ResultTableRow, String> tableColumnResultEndingDate;

    @FXML
    private TableColumn<ResultTableRow, String> tableColumnResultDuration;

    @FXML
    private TableColumn<ResultTableRow, String> tableColumnResultResults;

    private MainWindowController mainWindowController;
    private Experiment experiment;
    private ObservableList<ResultTableRow> resultTableRowObservableList = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        tableColumnResultId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnResultBeginningDate.setCellValueFactory(new PropertyValueFactory<>("beginningDate"));
        tableColumnResultEndingDate.setCellValueFactory(new PropertyValueFactory<>("endingDate"));
        tableColumnResultDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        tableColumnResultResults.setCellValueFactory(new PropertyValueFactory<>("results"));

        Callback<TableColumn<ResultTableRow, String>, TableCell<ResultTableRow, String>> actionsCellFactory = param -> {
            return new TableCell<ResultTableRow, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final Button actionButton = new Button(ResourceBundle.getBundle("bundles.messages").getString("results.list.window.results"));
                        actionButton.setOnAction(e -> {
                            ResultTableRow resultTableRow = getTableView().getItems().get(getIndex());
                            Result result = ResultRepository.findById(resultTableRow.getId());
                            showResult(result);
                        });
                        setGraphic(actionButton);
                        setText(null);
                    }
                }
            };
        };
        tableColumnResultResults.setCellFactory(actionsCellFactory);

        tableViewResults.setItems(resultTableRowObservableList);
    }

    private void showResult(Result result) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/results_window.fxml"), resources);
            Parent root = loader.load();

            ResultsWindowController resultsWindowController = loader.getController();
            resultsWindowController.setMainWindowController(this.mainWindowController);
            resultsWindowController.setExperiment(experiment);
            resultsWindowController.setResult(result);
            resultsWindowController.init();

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

    public void init() {
        ResultRepository.findByExperimentId(experiment.getId())
                .forEach(result -> {
                    if (result.getEndingDate() != null)
                        resultTableRowObservableList.add(new ResultTableRow(result.getId(), result.getBeginningDate(), result.getEndingDate()));
                });
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    public void setMainWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

}
