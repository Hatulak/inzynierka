package controllers;

import database.model.Experiment;
import database.model.Status;
import database.repository.ExperimentRepository;
import database.utils.CommonConstants;
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
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
public class NewExperimentWindowController {



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

    private MainWindowController mainWindowController;

    private File fileWithOptions;
    private File readedMriObjectFileDesc;
    private File readedMriObjectFileFlow;
    private File readedMriObjectFileType;


    @FXML
    public void loadMRIObjectFileDesc(ActionEvent actionEvent) {
        ResourceBundle messages = ResourceBundle.getBundle("bundles.messages");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(messages.getString("new.experiment.mri.simulation.object.file.open.title"));
        readedMriObjectFileDesc = fileChooser.showOpenDialog(null);
        if (!readedMriObjectFileDesc.exists()) {
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
        readedMriObjectFileFlow = fileChooser.showOpenDialog(null);
        if (!readedMriObjectFileFlow.exists()) {
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
        readedMriObjectFileType = fileChooser.showOpenDialog(null);
        if (!readedMriObjectFileType.exists()) {
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(messagesBundle.getString("new.experiment.load.file.with.options"));
        fileWithOptions = fileChooser.showOpenDialog(null);
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

    @FXML
    void saveNewExperiment(ActionEvent event) {
        if (validateFields()) {
            Experiment experiment = Experiment.builder()
                    .name(newExperimentNameTextField.getText())
                    .status(Status.CREATED)
                    .creationDate(new Date())
                    .build();
            Experiment savedExperiment = ExperimentRepository.save(experiment);

            savedExperiment.setOptionsFilePath(createOptionsFile(savedExperiment.getId()));
            savedExperiment.setDescFilePath(readedMriObjectFileDesc.getAbsolutePath());
            savedExperiment.setFlowFilePath(readedMriObjectFileFlow.getAbsolutePath());
            savedExperiment.setTypeFilePath(readedMriObjectFileType.getAbsolutePath());
//            savedExperiment.setMriOutputFilePath(System.getProperty("user.home") + "\\MRISimulatorDB\\out\\" + savedExperiment.getId() + "\\" +"mriOutput.txt");
//            savedExperiment.setOutputKSpaceRePath(System.getProperty("user.home") + "\\MRISimulatorDB\\out\\" + savedExperiment.getId() + "\\" + newExperimentNameTextField.getText() + "_" + savedExperiment.getId() + CommonConstants.KSPACE_RE_TXT + CommonConstants.TXT);
//            savedExperiment.setOutputKSpaceImPath(System.getProperty("user.home") + "\\MRISimulatorDB\\out\\" + savedExperiment.getId() + "\\" + newExperimentNameTextField.getText() + "_" + savedExperiment.getId() + CommonConstants.KSPACE_IM_TXT + CommonConstants.TXT);
//            savedExperiment.setOutputImageAmpTxtPath(System.getProperty("user.home") + "\\MRISimulatorDB\\out\\" + savedExperiment.getId() + "\\" + newExperimentNameTextField.getText() + "_" + savedExperiment.getId() + CommonConstants.IMAGE_AMP_TXT + CommonConstants.TXT);
//            savedExperiment.setOutputImageAmpBmpPath(System.getProperty("user.home") + "\\MRISimulatorDB\\out\\" + savedExperiment.getId() + "\\" + newExperimentNameTextField.getText() + "_" + savedExperiment.getId() + CommonConstants.IMAGE_AMP_BMP + CommonConstants.SLICE_BMP);
//            savedExperiment.setOutputImagePhaseBmpPath(System.getProperty("user.home") + "\\MRISimulatorDB\\out\\" + savedExperiment.getId() + "\\" + newExperimentNameTextField.getText() + "_" + savedExperiment.getId() + CommonConstants.IMAGE_PHASE_BMP + CommonConstants.SLICE_BMP);
            ExperimentRepository.merge(savedExperiment);
            mainWindowController.addExperimentToExperimentsList(savedExperiment);
            closeWindow();
        }
    }

    private String createOptionsFile(Long id) {
        String path = System.getProperty("user.home") + "\\MRISimulatorDB\\in\\" + id + "\\"
                + "options_" + newExperimentNameTextField.getText() + "_" + id + CommonConstants.TXT;
        File optionsFile = new File(path);
        optionsFile.getParentFile().mkdirs();
        try {
            optionsFile.createNewFile();
            Path descCopyPath = Files.copy(Paths.get(readedMriObjectFileDesc.getAbsolutePath()),
                    Paths.get(optionsFile.getParentFile().getAbsolutePath() + "\\" + newExperimentNameTextField.getText() + "_" + id + CommonConstants.DESC_TXT));
            readedMriObjectFileDesc = new File(descCopyPath.toUri());

            Path flowCopyPath = Files.copy(Paths.get(readedMriObjectFileFlow.getAbsolutePath()),
                    Paths.get(optionsFile.getParentFile().getAbsolutePath() + "\\" + newExperimentNameTextField.getText() + "_" + id + CommonConstants.FLOW_TXT));
            readedMriObjectFileFlow = new File(flowCopyPath.toUri());

            Path typeCopyPath = Files.copy(Paths.get(readedMriObjectFileType.getAbsolutePath()),
                    Paths.get(optionsFile.getParentFile().getAbsolutePath() + "\\" + newExperimentNameTextField.getText() + "_" + id + CommonConstants.TYPE_TXT));
            readedMriObjectFileType = new File(typeCopyPath.toUri());

            ResourceBundle parameters = ResourceBundle.getBundle("bundles.experiment_parameters");
            FileWriter fileWriter = new FileWriter(optionsFile);
            fileWriter.write(parameters.getString("number.of.trees") + " " + newExperimentNumberOfTreesTextField.getText() + "\n");
            fileWriter.write(parameters.getString("map.size") + " " + newExperimentMapSizeTextField.getText() + "\n");
            fileWriter.write(parameters.getString("map.size.one") + " " + newExperimentMapSizeTreeOneTextField.getText() + "\n");
            fileWriter.write(parameters.getString("map.size.two") + " " + newExperimentMapSizeTreeTwoTextField.getText() + "\n");
            fileWriter.write(parameters.getString("map.size.three") + " " + newExperimentMapSizeTreeThreeTextField.getText() + "\n");
            fileWriter.write(parameters.getString("dimension") + " " + newExperimentDimensionTextField.getText() + "\n");
            fileWriter.write("SIMULATE_MRI 1"); //TODO - TUTAJ UWAGA JEST HARDCODED
            fileWriter.write(parameters.getString("mri.object") + " " + newExperimentMRIObjectTextField.getText() + "\n");
            fileWriter.write(parameters.getString("mri.object.file") + " " + optionsFile.getParentFile().getAbsolutePath() + "\\" + newExperimentNameTextField.getText() + "_" + id + "\n");
            fileWriter.write(parameters.getString("mri.method") + " " + newExperimentMRIMethodTextField.getText() + "\n");
            fileWriter.write(parameters.getString("mri.dimension") + " " + newExperimentMRIDimensionTextField.getText() + "\n");
            fileWriter.write(parameters.getString("tr") + " " + newExperimentTRTextField.getText() + "\n");
            fileWriter.write(parameters.getString("te") + " " + newExperimentTETextField.getText() + "\n");
            fileWriter.write(parameters.getString("acq.time") + " " + newExperimentACQTimeTextField.getText() + "\n");
            fileWriter.write(parameters.getString("fa") + " " + newExperimentFATextField.getText() + "\n");
            fileWriter.write(parameters.getString("kind.of.rf.pulse") + " " + newExperimentKindOfRfPulseTextField.getText() + "\n");
            fileWriter.write(parameters.getString("rf.pulse.time") + " " + newExperimentRfPulseTimeTextField.getText() + "\n");
            fileWriter.write(parameters.getString("rf.pulse.n.of.lobes") + " " + newExperimentRRfPulseNOfLobesTextField.getText() + "\n");
            fileWriter.write(parameters.getString("rf.pulse.bw") + " " + newExperimentRfPulseBwTextField.getText() + "\n");
            fileWriter.write(parameters.getString("rf.pulse.points") + " " + newExperimentRfPulsePointsTextField.getText() + "\n");
            fileWriter.write(parameters.getString("rf.slice.sel.axis") + " " + newExperimentRfSliceSelAxisTextField.getText() + "\n");
            fileWriter.write(parameters.getString("rf.slice.sel.thickness") + " " + newExperimentRfSliceSelThicknessTextField.getText() + "\n");
            fileWriter.write(parameters.getString("rf.slice.sel.fraction") + " " + newExperimentRfSliceSelFractionTextField.getText() + "\n");
            fileWriter.write(parameters.getString("rf.slice.sel.gradient") + " " + newExperimentRfSliceSelGradientTextField.getText() + "\n");
            fileWriter.write(parameters.getString("mri.flow.gmn.x") + " " + newExperimentMRIFlowGmnXTextField.getText() + "\n");
            fileWriter.write(parameters.getString("mri.flow.gmn.y") + " " + newExperimentMRIFlowGmnYTextField.getText() + "\n");
            fileWriter.write(parameters.getString("mri.flow.gmn.z") + " " + newExperimentMRIFlowGmnZTextField.getText() + "\n");
            fileWriter.write(parameters.getString("mri.flow.gmn.slice.sel") + " " + newExperimentMRIFlowGmnSliceSelTextField.getText() + "\n");
            fileWriter.write(parameters.getString("write.kspace.re.txt") + " " + (newExperimentWriteKspaceReTxtCheckBox.isSelected() ? "1" : "0") + "\n");
//            String output = System.getProperty("user.home") + "\\MRISimulatorDB\\out\\" + id + "\\" + newExperimentNameTextField.getText() + "_" + id + CommonConstants.KSPACE_RE_TXT + "\n";
//            new File(output).getParentFile().mkdirs();
//            fileWriter.write(parameters.getString("output.kspace.re.file.txt") + " " +
//                    System.getProperty("user.home") + "\\MRISimulatorDB\\out\\" + id + "\\" + newExperimentNameTextField.getText() + "_" + id + CommonConstants.KSPACE_RE_TXT + "\n");
            fileWriter.write(parameters.getString("write.kspace.im.txt") + " " + (newExperimentWriteKspaceImTxtCheckBox.isSelected() ? "1" : "0") + "\n");
//            fileWriter.write(parameters.getString("output.kspace.im.file.txt") + " " +
//                    System.getProperty("user.home") + "\\MRISimulatorDB\\out\\" + id + "\\" + newExperimentNameTextField.getText() + "_" + id + CommonConstants.KSPACE_IM_TXT + "\n");
            fileWriter.write(parameters.getString("write.image.amp.txt") + " " + (newExperimentWriteImageAmpTxtCheckBox.isSelected() ? "1" : "0") + "\n");
//            fileWriter.write(parameters.getString("output.image.amp.file.txt") + " " +
//                    System.getProperty("user.home") + "\\MRISimulatorDB\\out\\" + id + "\\" + newExperimentNameTextField.getText() + "_" + id + CommonConstants.IMAGE_AMP_TXT + "\n");
            fileWriter.write(parameters.getString("write.image.amp.bmp") + " " + (newExperimentWriteImageAmpBmpCheckBox.isSelected() ? "1" : "0") + "\n");
//            fileWriter.write(parameters.getString("output.image.amp.file.bmp") + " " +
//                    System.getProperty("user.home") + "\\MRISimulatorDB\\out\\" + id + "\\" + newExperimentNameTextField.getText() + "_" + id + CommonConstants.IMAGE_AMP_BMP + "\n");
            fileWriter.write(parameters.getString("write.image.phase.bmp") + " " + (newExperimentWriteImagePhaseBmpCheckBox.isSelected() ? "1" : "0") + "\n");
//            fileWriter.write(parameters.getString("output.image.phase.file.bmp") + " " +
//                    System.getProperty("user.home") + "\\MRISimulatorDB\\out\\" + id + "\\" + newExperimentNameTextField.getText() + "_" + id + CommonConstants.IMAGE_PHASE_BMP + "\n");
            fileWriter.write(parameters.getString("change.rf.pulse.flow.step") + " " + newExperimentChangeRfPulseFlowStepTextField.getText() + "\n");
            fileWriter.write(parameters.getString("rf.pulse.flow.step.factor") + " " + newExperimentRfPulseFlowStepFactorTextField.getText() + "\n");
            fileWriter.write(parameters.getString("change.before.acq.flow.step") + " " + newExperimentChangeBeforeACQFlowStepTextField.getText() + "\n");
            fileWriter.write(parameters.getString("before.acq.flow.step.factor") + " " + newExperimentBeforeACQFlowStepFactorTextField.getText() + "\n");
            fileWriter.write(parameters.getString("change.after.acq.flow.step") + " " + newExperimentChangeAfterACQFlowStepTextField.getText() + "\n");
            fileWriter.write(parameters.getString("after.acq.flow.step.factor") + " " + newExperimentAfterACQFlowStepFactorTextField.getText() + "\n");

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return optionsFile.getAbsolutePath();
    }

    private boolean validateFields() {
        //todo - walidacja elemwentów
        return true;
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
            if (experimentParameter.matches("MRI_OBJECT_FILE")) {
                log.debug("In catch - MRI_OBJECT_FILE setting");
                //nie ma tekiego fielda bo tutaj dajemy pliki
                ResourceBundle messages = ResourceBundle.getBundle("bundles.messages");

                String descFilePath = value + CommonConstants.DESC_TXT;
                String flowFilePath = value + CommonConstants.FLOW_TXT;
                String typeFilePath = value + CommonConstants.TYPE_TXT;

                File descFile = new File(fileWithOptions.getParent() + "\\" + Paths.get(descFilePath).getFileName());
                if (!descFile.exists()) {
                    descFileLabel.setText(messages.getString("desc.file.label.error"));
                    descFileLabel.setTextFill(Color.RED);
                } else {
                    readedMriObjectFileDesc = descFile;
                    descFileLabel.setText(messages.getString("desc.file.label.fine"));
                    descFileLabel.setTextFill(Color.BLACK);
                }
                File flowFile = new File(fileWithOptions.getParent() + "\\" + Paths.get(flowFilePath).getFileName());
                if (!flowFile.exists()) {
                    flowFileLabel.setText(messages.getString("desc.file.label.error"));
                    flowFileLabel.setTextFill(Color.RED);
                } else {
                    readedMriObjectFileFlow = flowFile;
                    flowFileLabel.setText(messages.getString("desc.file.label.fine"));
                    flowFileLabel.setTextFill(Color.BLACK);
                }
                File typeFile = new File(fileWithOptions.getParent() + "\\" + Paths.get(typeFilePath).getFileName());
                if (!typeFile.exists()) {
                    typeFileLabel.setText(messages.getString("desc.file.label.error"));
                    typeFileLabel.setTextFill(Color.RED);
                } else {
                    readedMriObjectFileType = typeFile;
                    typeFileLabel.setText(messages.getString("desc.file.label.fine"));
                    typeFileLabel.setTextFill(Color.BLACK);
                }
            }
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

    public MainWindowController getMainWindowController() {
        return mainWindowController;
    }

    public void setMainWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    private void closeWindow() {
        Stage stage = (Stage) newExperimentSaveButton.getScene().getWindow();
        stage.close();
    }
}

