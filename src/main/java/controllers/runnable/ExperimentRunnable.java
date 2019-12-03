package controllers.runnable;

import controllers.MainWindowController;
import database.model.Experiment;
import database.model.Status;
import database.repository.ExperimentRepository;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;
import viewmodel.ExperimentTableRow;

import java.io.*;

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
        File outputFile = new File(experiment.getMriOutputFilePath());
        try {
            outputFile.createNewFile();
        } catch (IOException e) {
            log.error("Error during file creation, experiment id: " + experiment.getId());
            e.printStackTrace();
        }
        ProcessBuilder builder = new ProcessBuilder("lbm.exe", experiment.getOptionsFilePath())
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
                os.write('\n');
                System.out.println(line);
                fileWriter.write(line + "\n");
                if (line.matches("Please press the any key to continue")) {
                    os.write('\n');
                    break;
                }
                if (line.startsWith("test-")) {
                    String[] split = line.split(" ");
                    int currentIndex = Integer.parseInt(split[2].substring(0, split[2].length() - 1));
                    int endIndex = Integer.parseInt(split[3].substring(0, split[3].length() - 1));
                    experimentsList.remove(experimentTR);
                    experimentTR.setProgress(currentIndex + "\\" + endIndex);
                    experimentsList.add(experimentTR);
                }
            }
            fileWriter.close();
            experiment.setStatus(Status.DONE);
            ExperimentRepository.merge(experiment);
            mainWindowController.refeshExperimentList();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
