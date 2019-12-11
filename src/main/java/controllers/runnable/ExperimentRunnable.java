package controllers.runnable;

import controllers.MainWindowController;
import database.model.Experiment;
import database.model.Result;
import database.model.Status;
import database.repository.ExperimentRepository;
import database.repository.ResultRepository;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import utils.CommonConstants;
import viewmodel.ExperimentTableRow;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.ResourceBundle;

@Slf4j
public class ExperimentRunnable implements Runnable {

    private Experiment experiment;
    private MainWindowController mainWindowController;

    public ExperimentRunnable(Experiment experiment, MainWindowController mainWindowController) {
        this.experiment = experiment;
        this.mainWindowController = mainWindowController;
    }

    @Override
    public void run() {
        Result result = prepareResultEntity();
        experiment.setStatus(Status.RUNNING);
        ExperimentRepository.merge(experiment);

        mainWindowController.refeshExperimentList();
        ExperimentTableRow experimentTR = null;
        ObservableList<ExperimentTableRow> experimentsList = mainWindowController.getExperimentsObservableList();
        for (ExperimentTableRow experimentTableRow : experimentsList) {
            if (experimentTableRow.getId() == experiment.getId()) {
                experimentTR = experimentTableRow;
                break;
            }

        }
        File outputFile = new File(result.getMriOutputFilePath());
        try {
            outputFile.createNewFile();
        } catch (IOException e) {
            log.error("Error during file creation, experiment id: " + experiment.getId() + " result id: " + result.getId());
            e.printStackTrace();
        }
        ProcessBuilder builder = new ProcessBuilder("lbm.exe", result.getOptionsFilePath())
                .directory(new File(System.getProperty("user.home") + "\\MRISimulatorDB"));
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
                if (Thread.currentThread().isInterrupted()) {
                    //CHECK IF THREAD IS INTERRUPTED (CANCEL TASK)
                    fileWriter.close();
                    FileUtils.deleteDirectory(new File(result.getOptionsFilePath()).getParentFile());
                    experiment.setStatus(Status.CANCELLED);
                    ExperimentRepository.merge(experiment);
                    ResultRepository.delete(result);
                    return;
                }
                os.write('\n');
                System.out.println(line);
                fileWriter.write(line + "\n");
                if (line.matches("Please press the any key to continue")) {
                    os.write('\n');
                    break;
                }
                if (line.startsWith("test-")) {
                    line = line.replaceAll("[^0-9]+", " "); //Zmiana wszystkich znaków oprócz liczb na spacje
                    line = line.trim(); //usuwanie nadmiarowych spacji
                    String[] split = line.split(" ");
                    int currentIndex = Integer.parseInt(split[0]);
                    int endIndex = Integer.parseInt(split[1]);
                    experimentsList.remove(experimentTR);
                    experimentTR.setProgress(currentIndex + "\\" + endIndex);
                    experimentsList.add(experimentTR);
                }
            }
            fileWriter.close();
            result.setEndingDate(new Date());
            ResultRepository.merge(result);
            experiment.setStatus(Status.DONE);
            ExperimentRepository.merge(experiment);
            mainWindowController.refeshExperimentList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Result prepareResultEntity() {
        Result result = ResultRepository.save(Result.builder()
                .experiment(experiment)
                .beginningDate(new Date())
                .build());
        try {
            String optionsFilePath = System.getProperty("user.home") + "\\MRISimulatorDB\\out\\" + experiment.getId() + "\\"
                    + result.getId() + "\\" + CommonConstants.OPTIONS + experiment.getName() + "_" + experiment.getId() + "_" + result.getId() + CommonConstants.TXT;
            File optionsFile = new File(optionsFilePath);
            optionsFile.getParentFile().mkdirs();

            Path copy = Files.copy(Paths.get(Paths.get(experiment.getOptionsFilePath()).toFile().getAbsolutePath()),
                    Paths.get(optionsFile.getParentFile().getAbsolutePath() + "\\" + CommonConstants.OPTIONS + experiment.getName() + "_" + experiment.getId() + "_" + result.getId() + CommonConstants.TXT));
            optionsFile = new File(copy.toAbsolutePath().toString());
            ResourceBundle parameters = ResourceBundle.getBundle("bundles.experiment_parameters");
            FileWriter fileWriter = new FileWriter(optionsFile, true);
            fileWriter.write(parameters.getString("output.kspace.re.file.txt") + " " +
                    System.getProperty("user.home") + "\\MRISimulatorDB\\out\\" + experiment.getId() + "\\"
                    + result.getId() + "\\" + experiment.getName() + "_" + experiment.getId() + "_" + result.getId() + CommonConstants.KSPACE_RE_TXT + "\n");

            fileWriter.write(parameters.getString("output.kspace.im.file.txt") + " " +
                    System.getProperty("user.home") + "\\MRISimulatorDB\\out\\" + experiment.getId() + "\\"
                    + result.getId() + "\\" + experiment.getName() + "_" + experiment.getId() + "_" + result.getId() + CommonConstants.KSPACE_IM_TXT + "\n");

            fileWriter.write(parameters.getString("output.image.amp.file.txt") + " " +
                    System.getProperty("user.home") + "\\MRISimulatorDB\\out\\" + experiment.getId() + "\\"
                    + result.getId() + "\\" + experiment.getName() + "_" + experiment.getId() + "_" + result.getId() + CommonConstants.IMAGE_AMP_TXT + "\n");

            fileWriter.write(parameters.getString("output.image.amp.file.bmp") + " " +
                    System.getProperty("user.home") + "\\MRISimulatorDB\\out\\" + experiment.getId() + "\\"
                    + result.getId() + "\\" + experiment.getName() + "_" + experiment.getId() + "_" + result.getId() + CommonConstants.IMAGE_AMP_BMP + "\n");

            fileWriter.write(parameters.getString("output.image.phase.file.bmp") + " " +
                    System.getProperty("user.home") + "\\MRISimulatorDB\\out\\" + experiment.getId() + "\\"
                    + result.getId() + "\\" + experiment.getName() + "_" + experiment.getId() + "_" + result.getId() + CommonConstants.IMAGE_PHASE_BMP + "\n");
            fileWriter.close();
            result.setOptionsFilePath(copy.toAbsolutePath().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        result.setMriOutputFilePath(System.getProperty("user.home") + "\\MRISimulatorDB\\out\\" + experiment.getId() + "\\" + result.getId() + "\\" + "mriOutput.txt");
        result.setOutputKSpaceRePath(System.getProperty("user.home") + "\\MRISimulatorDB\\out\\" + experiment.getId() + "\\"
                + result.getId() + "\\" + experiment.getName() + "_" + experiment.getId() + "_" + result.getId() + CommonConstants.KSPACE_RE_TXT + CommonConstants.TXT);
        result.setOutputKSpaceImPath(System.getProperty("user.home") + "\\MRISimulatorDB\\out\\" + experiment.getId() + "\\"
                + result.getId() + "\\" + experiment.getName() + "_" + experiment.getId() + "_" + result.getId() + CommonConstants.KSPACE_IM_TXT + CommonConstants.TXT);
        result.setOutputImageAmpTxtPath(System.getProperty("user.home") + "\\MRISimulatorDB\\out\\" + experiment.getId() + "\\"
                + result.getId() + "\\" + experiment.getName() + "_" + experiment.getId() + "_" + result.getId() + CommonConstants.IMAGE_AMP_TXT + CommonConstants.TXT);
        result.setOutputImageAmpBmpPath(System.getProperty("user.home") + "\\MRISimulatorDB\\out\\" + experiment.getId() + "\\"
                + result.getId() + "\\" + experiment.getName() + "_" + experiment.getId() + "_" + result.getId() + CommonConstants.IMAGE_AMP_BMP + CommonConstants.SLICE_BMP);
        result.setOutputImagePhaseBmpPath(System.getProperty("user.home") + "\\MRISimulatorDB\\out\\" + experiment.getId() + "\\"
                + result.getId() + "\\" + experiment.getName() + "_" + experiment.getId() + "_" + result.getId() + CommonConstants.IMAGE_PHASE_BMP + CommonConstants.SLICE_BMP);

        ResultRepository.merge(result);

        return ResultRepository.findById(result.getId());
    }
}
