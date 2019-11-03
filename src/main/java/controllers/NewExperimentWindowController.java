package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
public class NewExperimentWindowController {

    public static final String DESC_TXT = "_desc.txt";
    public static final String FLOW_TXT = "_flow.txt";
    public static final String TYPE_TXT = "_type.txt";


    @FXML
    public Label descFileLabel;
    @FXML
    public Label flowFileLabel;
    @FXML
    public Label typeFileLabel;
    @FXML
    public CheckBox newExperimentWriteKspaceImTxtCheckBox;
    @FXML
    public CheckBox newExperimentWriteKspaceReTxtCheckBox;
    @FXML
    public CheckBox newExperimentWriteImageAmpTxtCheckBox;
    @FXML
    public CheckBox newExperimentWriteImageAmpBmpCheckBox;
    @FXML
    public CheckBox newExperimentWriteImagePhaseBmpCheckBox;
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

    private File fileWithOptions;
    private File mriObjectFileDesc;
    private File mriObjectFileFlow;
    private File mriObjectFileType;


    @FXML
    public void loadMRIObjectFileDesc(ActionEvent actionEvent) {
        ResourceBundle messages = ResourceBundle.getBundle("bundles.messages");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(messages.getString("new.experiment.mri.simulation.object.file.open.title"));
        mriObjectFileDesc = fileChooser.showOpenDialog(null);
        if (!mriObjectFileDesc.exists()) {
            descFileLabel.setText(messages.getString("desc.file.label.error"));
            descFileLabel.setTextFill(Color.RED);
        } else {
            descFileLabel.setText(messages.getString("desc.file.label.fine"));
            descFileLabel.setTextFill(Color.BLACK);
        }
    }

    @FXML
    public void loadMRIObjectFileFlow(ActionEvent actionEvent) {
        ResourceBundle messages = ResourceBundle.getBundle("bundles.messages");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(messages.getString("new.experiment.mri.simulation.object.file.open.title"));
        mriObjectFileFlow = fileChooser.showOpenDialog(null);
        if (!mriObjectFileFlow.exists()) {
            flowFileLabel.setText(messages.getString("desc.file.label.error"));
            flowFileLabel.setTextFill(Color.RED);
        } else {
            flowFileLabel.setText(messages.getString("desc.file.label.fine"));
            flowFileLabel.setTextFill(Color.BLACK);
        }
    }

    @FXML
    public void loadMRIObjectFileType(ActionEvent actionEvent) {
        ResourceBundle messages = ResourceBundle.getBundle("bundles.messages");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(messages.getString("new.experiment.mri.simulation.object.file.open.title"));
        mriObjectFileType = fileChooser.showOpenDialog(null);
        if (!mriObjectFileType.exists()) {
            typeFileLabel.setText(messages.getString("desc.file.label.error"));
            typeFileLabel.setTextFill(Color.RED);
        } else {
            typeFileLabel.setText(messages.getString("desc.file.label.fine"));
            typeFileLabel.setTextFill(Color.BLACK);
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) newExperimentCancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void loadDefaultData(ActionEvent event) {
        ResourceBundle defaultParametersValues = ResourceBundle.getBundle("bundles.default_parameters");

        List<String> experimentParameters = getExperimentParametersFromBundle();

        experimentParameters.forEach(eP -> {
            try {
                setParameter(eP, defaultParametersValues.getString(eP));
            } catch (MissingResourceException e) {
                log.warn("While loading default data: " + e.getMessage());
            }
        });
    }

    @FXML
    void readFileWithOptions(ActionEvent event) {
        ResourceBundle messagesBundle = ResourceBundle.getBundle("bundles.messages");
        //fixme - powrócić do filechoosera jak na testowym pliku bedzie git
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(messagesBundle.getString("new.experiment.load.file.with.options"));
        fileWithOptions = fileChooser.showOpenDialog(null);
//        fileWithOptions = new File(String.valueOf(Paths.get("E:\\Repozytoria\\inzynierka\\inout\\in\\ISBI2012\\addEllipse\\optionsMRI_addEllipse.txt"))); //a to usunąć
        try {
            List<String> fileLines = Files.readAllLines(Paths.get(fileWithOptions.getPath()));
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
            //Todo - pokazać jakiś message że błąd z plikiem
            log.error("Problem with file: " + fileWithOptions.getPath());
            e.printStackTrace();
        }
    }

    private void setParameter(String experimentParameter, String value) {
        log.debug("Setting experiment parameter( " + experimentParameter + " )  with value ( " + value + " )");
        ResourceBundle fields = ResourceBundle.getBundle("bundles.parameters_into_fields");

        Class<NewExperimentWindowController> myClass = NewExperimentWindowController.class;
        if (fields.getString(experimentParameter).contains("TextField")) {
            try {
                Field field = myClass.getDeclaredField(fields.getString(experimentParameter));
                TextField textField = (TextField) field.get(this);
                textField.setText(value);
            } catch (NoSuchFieldException e) {
                if (experimentParameter.matches("MRI_OBJECT_FILE")) {
                    log.debug("In catch - MRI_OBJECT_FILE setting");
                    //nie ma tekiego fielda bo tutaj dajemy pliki
                    ResourceBundle messages = ResourceBundle.getBundle("bundles.messages");

                    String descFilePath = value + DESC_TXT;
                    String flowFilePath = value + FLOW_TXT;
                    String typeFilePath = value + TYPE_TXT;

                    File descFile = new File(fileWithOptions.getParent() + "\\" + Paths.get(descFilePath).getFileName());
                    if (!descFile.exists()) {
                        descFileLabel.setText(messages.getString("desc.file.label.error"));
                        descFileLabel.setTextFill(Color.RED);
                    } else {
                        mriObjectFileDesc = descFile;
                        descFileLabel.setText(messages.getString("desc.file.label.fine"));
                        descFileLabel.setTextFill(Color.BLACK);
                    }
                    File flowFile = new File(fileWithOptions.getParent() + "\\" + Paths.get(flowFilePath).getFileName());
                    if (!flowFile.exists()) {
                        flowFileLabel.setText(messages.getString("desc.file.label.error"));
                        flowFileLabel.setTextFill(Color.RED);
                    } else {
                        mriObjectFileFlow = flowFile;
                        flowFileLabel.setText(messages.getString("desc.file.label.fine"));
                        flowFileLabel.setTextFill(Color.BLACK);
                    }
                    File typeFile = new File(fileWithOptions.getParent() + "\\" + Paths.get(typeFilePath).getFileName());
                    if (!typeFile.exists()) {
                        typeFileLabel.setText(messages.getString("desc.file.label.error"));
                        typeFileLabel.setTextFill(Color.RED);
                    } else {
                        mriObjectFileType = typeFile;
                        typeFileLabel.setText(messages.getString("desc.file.label.fine"));
                        typeFileLabel.setTextFill(Color.BLACK);
                    }
                } else {
                    //todo - dać info że nie ma tekiego fielda
                    log.warn(e.getMessage());
                }
            } catch (IllegalAccessException e) {
                log.warn(e.getMessage());
            }
        } else if (fields.getString(experimentParameter).contains("CheckBox")) {
            try {
                Field field = myClass.getDeclaredField(fields.getString(experimentParameter));
                CheckBox checkBox = (CheckBox) field.get(this);
                checkBox.setSelected(value.matches("1"));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    void saveNewExperiment(ActionEvent event) {

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

