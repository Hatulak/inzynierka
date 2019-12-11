package controllers;

import database.model.Experiment;
import database.model.Status;
import database.repository.ExperimentRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
public class ExperimentEditionWindowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField experimentNumberOfTreesTextField;

    @FXML
    private TextField experimentMapSizeTextField;

    @FXML
    private TextField experimentMapSizeTreeOneTextField;

    @FXML
    private TextField experimentMapSizeTreeTwoTextField;

    @FXML
    private TextField experimentMapSizeTreeThreeTextField;

    @FXML
    private TextField experimentDimensionTextField;

    @FXML
    private TextField experimentMRIMethodTextField;

    @FXML
    private TextField experimentMRIDimensionTextField;

    @FXML
    private TextField experimentTRTextField;

    @FXML
    private TextField experimentTETextField;

    @FXML
    private TextField experimentACQTimeTextField;

    @FXML
    private TextField experimentFATextField;

    @FXML
    private TextField experimentKindOfRfPulseTextField;

    @FXML
    private TextField experimentRfPulseTimeTextField;

    @FXML
    private TextField experimentRRfPulseNOfLobesTextField;

    @FXML
    private TextField experimentRfPulseBwTextField;

    @FXML
    private TextField experimentRfPulsePointsTextField;

    @FXML
    private TextField experimentRfSliceSelAxisTextField;

    @FXML
    private TextField experimentRfSliceSelThicknessTextField;

    @FXML
    private TextField experimentRfSliceSelFractionTextField;

    @FXML
    private TextField experimentRfSliceSelGradientTextField;

    @FXML
    private TextField experimentChangeRfPulseFlowStepTextField;

    @FXML
    private TextField experimentRfPulseFlowStepFactorTextField;

    @FXML
    private TextField experimentChangeBeforeACQFlowStepTextField;

    @FXML
    private TextField experimentBeforeACQFlowStepFactorTextField;

    @FXML
    private TextField experimentChangeAfterACQFlowStepTextField;

    @FXML
    private TextField experimentAfterACQFlowStepFactorTextField;

    @FXML
    private TextField experimentMRIFlowGmnXTextField;

    @FXML
    private TextField experimentMRIFlowGmnYTextField;

    @FXML
    private TextField experimentMRIFlowGmnZTextField;

    @FXML
    private TextField experimentMRIFlowGmnSliceSelTextField;

    @FXML
    private CheckBox experimentWriteKspaceReTxtCheckBox;

    @FXML
    private CheckBox experimentWriteKspaceImTxtCheckBox;

    @FXML
    private CheckBox experimentWriteImageAmpTxtCheckBox;

    @FXML
    private CheckBox experimentWriteImageAmpBmpCheckBox;

    @FXML
    private CheckBox experimentWriteImagePhaseBmpCheckBox;

    @FXML
    private Button experimentCancelButton;

    @FXML
    private Button experimentSaveButton;

    @FXML
    private Button experimentResetButton;

    private MainWindowController mainWindowController;
    private Experiment experiment;
    private Map<String, String> parametersWithoutFieldsMap = new HashMap<>();

    public void init() {
        readFileWithOptions(null);
    }

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) experimentCancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void resetData(ActionEvent event) {
        readFileWithOptions(null);
    }

    @FXML
    void saveExperiment(ActionEvent event) {
        if (validateFields()) {
            updateOptionsFile();
            experiment.setStatus(Status.EDITED);
            ExperimentRepository.merge(experiment);
            mainWindowController.refeshExperimentList();
            closeWindow();
        }
    }

    private void updateOptionsFile() {
        File optionsFile = new File(experiment.getOptionsFilePath());
        try {
            FileWriter fileWriter = new FileWriter(optionsFile, false);
            ResourceBundle parameters = ResourceBundle.getBundle("bundles.experiment_parameters");
            fileWriter.write(parameters.getString("number.of.trees") + " " + experimentNumberOfTreesTextField.getText() + "\n");
            fileWriter.write(parameters.getString("map.size") + " " + experimentMapSizeTextField.getText() + "\n");
            fileWriter.write(parameters.getString("map.size.one") + " " + experimentMapSizeTreeOneTextField.getText() + "\n");
            fileWriter.write(parameters.getString("map.size.two") + " " + experimentMapSizeTreeTwoTextField.getText() + "\n");
            fileWriter.write(parameters.getString("map.size.three") + " " + experimentMapSizeTreeThreeTextField.getText() + "\n");
            fileWriter.write(parameters.getString("dimension") + " " + experimentDimensionTextField.getText() + "\n");
            fileWriter.write(parameters.getString("simulate.mri") + " " + parametersWithoutFieldsMap.get(parameters.getString("simulate.mri")) + "\n");
            fileWriter.write(parameters.getString("mri.object") + " " + parametersWithoutFieldsMap.get(parameters.getString("mri.object")) + "\n");
            fileWriter.write(parameters.getString("mri.object.file") + " " + parametersWithoutFieldsMap.get(parameters.getString("mri.object.file")) + "\n");
            fileWriter.write(parameters.getString("mri.method") + " " + experimentMRIMethodTextField.getText() + "\n");
            fileWriter.write(parameters.getString("mri.dimension") + " " + experimentMRIDimensionTextField.getText() + "\n");
            fileWriter.write(parameters.getString("tr") + " " + experimentTRTextField.getText() + "\n");
            fileWriter.write(parameters.getString("te") + " " + experimentTETextField.getText() + "\n");
            fileWriter.write(parameters.getString("acq.time") + " " + experimentACQTimeTextField.getText() + "\n");
            fileWriter.write(parameters.getString("fa") + " " + experimentFATextField.getText() + "\n");
            fileWriter.write(parameters.getString("kind.of.rf.pulse") + " " + experimentKindOfRfPulseTextField.getText() + "\n");
            fileWriter.write(parameters.getString("rf.pulse.time") + " " + experimentRfPulseTimeTextField.getText() + "\n");
            fileWriter.write(parameters.getString("rf.pulse.n.of.lobes") + " " + experimentRRfPulseNOfLobesTextField.getText() + "\n");
            fileWriter.write(parameters.getString("rf.pulse.bw") + " " + experimentRfPulseBwTextField.getText() + "\n");
            fileWriter.write(parameters.getString("rf.pulse.points") + " " + experimentRfPulsePointsTextField.getText() + "\n");
            fileWriter.write(parameters.getString("rf.slice.sel.axis") + " " + experimentRfSliceSelAxisTextField.getText() + "\n");
            fileWriter.write(parameters.getString("rf.slice.sel.thickness") + " " + experimentRfSliceSelThicknessTextField.getText() + "\n");
            fileWriter.write(parameters.getString("rf.slice.sel.fraction") + " " + experimentRfSliceSelFractionTextField.getText() + "\n");
            fileWriter.write(parameters.getString("rf.slice.sel.gradient") + " " + experimentRfSliceSelGradientTextField.getText() + "\n");
            fileWriter.write(parameters.getString("mri.flow.gmn.x") + " " + experimentMRIFlowGmnXTextField.getText() + "\n");
            fileWriter.write(parameters.getString("mri.flow.gmn.y") + " " + experimentMRIFlowGmnYTextField.getText() + "\n");
            fileWriter.write(parameters.getString("mri.flow.gmn.z") + " " + experimentMRIFlowGmnZTextField.getText() + "\n");
            fileWriter.write(parameters.getString("mri.flow.gmn.slice.sel") + " " + experimentMRIFlowGmnSliceSelTextField.getText() + "\n");
            fileWriter.write(parameters.getString("write.kspace.re.txt") + " " + (experimentWriteKspaceReTxtCheckBox.isSelected() ? "1" : "0") + "\n");
            fileWriter.write(parameters.getString("write.kspace.im.txt") + " " + (experimentWriteKspaceImTxtCheckBox.isSelected() ? "1" : "0") + "\n");
            fileWriter.write(parameters.getString("write.image.amp.txt") + " " + (experimentWriteImageAmpTxtCheckBox.isSelected() ? "1" : "0") + "\n");
            fileWriter.write(parameters.getString("write.image.amp.bmp") + " " + (experimentWriteImageAmpBmpCheckBox.isSelected() ? "1" : "0") + "\n");
            fileWriter.write(parameters.getString("write.image.phase.bmp") + " " + (experimentWriteImagePhaseBmpCheckBox.isSelected() ? "1" : "0") + "\n");
            fileWriter.write(parameters.getString("change.rf.pulse.flow.step") + " " + experimentChangeRfPulseFlowStepTextField.getText() + "\n");
            fileWriter.write(parameters.getString("rf.pulse.flow.step.factor") + " " + experimentRfPulseFlowStepFactorTextField.getText() + "\n");
            fileWriter.write(parameters.getString("change.before.acq.flow.step") + " " + experimentChangeBeforeACQFlowStepTextField.getText() + "\n");
            fileWriter.write(parameters.getString("before.acq.flow.step.factor") + " " + experimentBeforeACQFlowStepFactorTextField.getText() + "\n");
            fileWriter.write(parameters.getString("change.after.acq.flow.step") + " " + experimentChangeAfterACQFlowStepTextField.getText() + "\n");
            fileWriter.write(parameters.getString("after.acq.flow.step.factor") + " " + experimentAfterACQFlowStepFactorTextField.getText() + "\n");

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validateFields() {
        return true; //TODO - walidacja
    }

    public void setMainWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }


    void readFileWithOptions(ActionEvent event) {
        try {
            List<String> fileLines = Files.readAllLines(Paths.get(experiment.getOptionsFilePath()));
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
                        setParameter(experimentParameter, split[split.length - 1]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void setParameter(String experimentParameter, String value) {
        log.debug("Setting experiment parameter( " + experimentParameter + " )  with value ( " + value + " )");
        ResourceBundle fields = ResourceBundle.getBundle("bundles.edit_parameters_into_fields");

        Class<ExperimentEditionWindowController> myClass = ExperimentEditionWindowController.class;
        if (fields.getString(experimentParameter).contains("TextField")) {
            try {
                Field field = myClass.getDeclaredField(fields.getString(experimentParameter));
                TextField textField = (TextField) field.get(this);
                textField.setText(value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.warn(e.getMessage());
                e.printStackTrace();
            }
        } else if (fields.getString(experimentParameter).contains("CheckBox")) {
            try {
                Field field = myClass.getDeclaredField(fields.getString(experimentParameter));
                CheckBox checkBox = (CheckBox) field.get(this);
                checkBox.setSelected(value.matches("1"));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.warn(e.getMessage());
                e.printStackTrace();
            }
        } else {
            parametersWithoutFieldsMap.put(experimentParameter, value);
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) experimentSaveButton.getScene().getWindow();
        stage.close();
    }
}
