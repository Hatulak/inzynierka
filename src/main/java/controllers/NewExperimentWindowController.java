package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

@Slf4j
public class NewExperimentWindowController {
    @FXML
    private TextField newExperimentNumberOfTreesTextField;
    @FXML
    private TextField newExperimentMapSizeTextField;
    @FXML
    private TextField newExperimentMapSizeTreeOneTextField;
    @FXML
    private TextField newExperimentMapSizeTreeTwoTextField;
    @FXML
    private TextField newExperimentMapSizeTreeThreeTextField;
    @FXML
    private TextField newExperimentDimensionTextField;
    @FXML
    private TextField newExperimentNameTextField;
    @FXML
    private TextField newExperimentMRIObjectTextField;
    @FXML
    private TextField newExperimentMRIMethodTextField;
    @FXML
    private Button newExperimentMRIObjectFileButton;
    @FXML
    private TextField newExperimentMRIDimensionTextField;
    @FXML
    private TextField newExperimentTRTextField;
    @FXML
    private TextField newExperimentTETextField;
    @FXML
    private TextField newExperimentACQTimeTextField;
    @FXML
    private TextField newExperimentFATextField;
    @FXML
    private TextField newExperimentKindOfRfPulseTextField;
    @FXML
    private TextField newExperimentRfPulseTimeTextField;
    @FXML
    private TextField newExperimentRRfPulseNOfLobesTextField;
    @FXML
    private TextField newExperimentRfPulseBwTextField;
    @FXML
    private TextField newExperimentRfPulsePointsTextField;
    @FXML
    private TextField newExperimentRfSliceSelAxisTextField;
    @FXML
    private TextField newExperimentRfSliceSelThicknessTextField;
    @FXML
    private TextField newExperimentRfSliceSelFractionTextField;
    @FXML
    private TextField newExperimentRfSliceSelGradientTextField;
    @FXML
    private TextField newExperimentChangeRfPulseFlowStepTextField;
    @FXML
    private TextField newExperimentRfPulseFlowStepFactorTextField;
    @FXML
    private TextField newExperimentChangeBeforeACQFlowStepTextField;
    @FXML
    private TextField newExperimentBeforeACQFlowStepFactorTextField;
    @FXML
    private TextField newExperimentChangeAfterACQFlowStepTextField;
    @FXML
    private TextField newExperimentAfterACQFlowStepFactorTextField;
    @FXML
    private TextField newExperimentMRIFlowGmnXTextField;
    @FXML
    private TextField newExperimentMRIFlowGmnYTextField;
    @FXML
    private TextField newExperimentMRIFlowGmnZTextField;
    @FXML
    private TextField newExperimentMRIFlowGmnSliceSelTextField;
    @FXML
    private Button newExperimentCancelButton;
    @FXML
    private Button newExperimentSaveButton;
    @FXML
    private Button newExperimentDefaultValuesButton;
    @FXML
    private Button newExperimentReadFileWithOptionsButton;

    private File mriObjectFileDesc;
    private File mriObjectFileFlow;
    private File mriObjectFileType;


    @FXML
    public void loadMRIObjectFileDesc(ActionEvent actionEvent) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("bundles.messages");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(resourceBundle.getString("new.experiment.mri.simulation.object.file.open.title"));
        mriObjectFileDesc = fileChooser.showOpenDialog(null);
    }

    @FXML
    public void loadMRIObjectFileFlow(ActionEvent actionEvent) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("bundles.messages");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(resourceBundle.getString("new.experiment.mri.simulation.object.file.open.title"));
        mriObjectFileFlow = fileChooser.showOpenDialog(null);
    }

    @FXML
    public void loadMRIObjectFileType(ActionEvent actionEvent) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("bundles.messages");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(resourceBundle.getString("new.experiment.mri.simulation.object.file.open.title"));
        mriObjectFileFlow = fileChooser.showOpenDialog(null);
    }

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) newExperimentCancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void loadDefaultData(ActionEvent event) {
        //Todo
    }

    @FXML
    void readFileWithOptions(ActionEvent event) {
        ResourceBundle messagesBundle = ResourceBundle.getBundle("bundles.messages");
        //fixme - powrócić do filechoosera jak na testowym pliku bedzie git
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle(messagesBundle.getString("new.experiment.load.file.with.options"));
//        File fileWithOptions = fileChooser.showOpenDialog(null);
        try {
//            List<String> fileLines = Files.readAllLines(Paths.get(fileWithOptions.getPath()));
            List<String> fileLines = Files.readAllLines(Paths.get("E:\\Repozytoria\\inzynierka\\inout\\in\\ISBI2012\\addEllipse\\optionsMRI_addEllipse.txt")); //a to usunąć
            List<String> experimentParameters = getExperimentParametersFromBundle();
            for (String line : fileLines) {
                if (line.split("#").length == 0) continue;
                line = line.split("#")[0];
                if (line.startsWith("#")) continue;
                for (String experimentParameter : experimentParameters) {
                    if (experimentParameter.length() > line.length()) continue;
                    String[] split = line.split("\\s");
                    if (split[0].matches(experimentParameter)) {
                        //znaczy że nie zakomentowane -> bierzemy
                        setParameterIntoTextField(experimentParameter, split[split.length - 1]);
                    }
                }
            }
        } catch (IOException e) {
            //Todo - pokazać jakiś message że błąd z plikiem
            e.printStackTrace();
        }
    }

    private void setParameterIntoTextField(String experimentParameter, String value) {
        log.debug("Setting experiment parameter( " + experimentParameter + " )  with value ( " + value + " )");

        ResourceBundle textFields = ResourceBundle.getBundle("bundles.parameters_into_textfields");

        Class<NewExperimentWindowController> myClass = NewExperimentWindowController.class;
        try {
            Field field = myClass.getDeclaredField(textFields.getString(experimentParameter));
            TextField textField = (TextField) field.get(this);
            textField.setText(value);
        } catch (NoSuchFieldException e) {
            if (experimentParameter.matches("MRI_OBJECT_FILE")) {
                //nie ma tekiego fielda bo tutaj dajemy pliki
                //todo
            } else {
                //todo - dać info że nie ma tekiego fielda
                e.printStackTrace();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void saveNewExperiment(ActionEvent event) {
        //Todo
    }

    private List<String> getExperimentParametersFromBundle() {
        List<String> experimentParameters = new LinkedList<>();
        ResourceBundle parametersBunle = ResourceBundle.getBundle("bundles.experiment_parameters");
        Enumeration<String> keys = parametersBunle.getKeys();
        while (keys.hasMoreElements()) {
            experimentParameters.add(parametersBunle.getString(keys.nextElement()));
        }
        return experimentParameters;
    }

}

