package controllers.runnable;

import controllers.MainWindowController;
import database.model.Experiment;
import database.model.Status;
import database.repository.ExperimentRepository;
import lombok.extern.slf4j.Slf4j;

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
        File outputFile = new File(experiment.getMriOutputFilePath());
        try {
            outputFile.createNewFile();
        } catch (IOException e) {
            log.error("Error during file creation, experiment id: " + experiment.getId());
            e.printStackTrace();
        }
        ProcessBuilder builder = new ProcessBuilder("lbm.exe", experiment.getOptionsFilePath())
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
            mainWindowController.refeshExperimentList();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
