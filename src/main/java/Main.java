import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {
//        Zmiana lokalizacji dla testu
//        Locale.setDefault(new Locale("en"));

//        Setting bundle file for messages used in GUI
        ResourceBundle resourceBundle = ResourceBundle.getBundle("bundles.messages");
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/fxml/main_window.fxml"), resourceBundle);
        primaryStage.setTitle(resourceBundle.getString("main.window.title"));
        primaryStage.setScene(new Scene(anchorPane));
        primaryStage.show();
    }

    public static void main(String[] args) throws IOException {
        launch(args);

//
//        final PipedOutputStream output = new PipedOutputStream();
//        final PipedInputStream input = new PipedInputStream(output);


//        Thread thread1 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                ProcessBuilder builder = new ProcessBuilder("lbm.exe", "inout\\in\\ISBI2012\\addEllipse\\optionsMRI_addEllipse.txt", ">", "test.txt")
//                        .directory(new File("E:\\Repozytoria\\inzynierka\\"));
//                Process process = null;
//                try {
//                    process = builder.start();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
////                InputStream is = process.getInputStream();
////                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
////                String line;
////                try {
////                    while ((line = reader.readLine()) != null) {
////                        output.write(line.getBytes());
////                    }
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
//
//            }
//        });
//
//        Thread thread2 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    int data = input.read();
//                    while (data != -1) {
//                        System.out.print((char) data);
//                        data = input.read();
//                    }
//                } catch (IOException e) {
//                }
//            }
//        });

//        thread1.start();
//        thread2.start();


//        Thread thread1 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                ProcessBuilder builder = new ProcessBuilder("lbm.exe", "inout\\in\\ISBI2012\\addEllipse\\optionsMRI_addEllipse.txt", "1>", "test.txt")
//                        .directory(new File("E:\\Repozytoria\\inzynierka\\"))
//                        .redirectInput(ProcessBuilder.Redirect.INHERIT)
//                        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
//                        .redirectError(ProcessBuilder.Redirect.INHERIT);
//                Process process = null;
//                try {
//                    process = builder.start();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
////                InputStream is = process.getInputStream();
////                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
////                String line;
////                try {
////                    while ((line = reader.readLine()) != null) {
////                        output.write(line.getBytes());
////                    }
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
//
//            }
//        });
//
//
//        thread1.start();


//        Runtime rt = Runtime.getRuntime();
//        Process proc = rt.exec("cmd /c E:\\Repozytoria\\inzynierka\\lbm.exe E:\\Repozytoria\\inzynierka\\inout\\in\\ISBI2012\\addEllipse\\optionsMRI_addEllipse.txt");
//
//        // Any error message?
//        Thread errorGobbler
//                = new Thread(new StreamGobbler(proc.getErrorStream(), System.err));
//
//        // Any output?
//        Thread outputGobbler
//                = new Thread(new StreamGobbler(proc.getInputStream(), System.out));
//
//        errorGobbler.start();
//        outputGobbler.start();
//
//        // Any error?
//        try {
//            int exitVal = proc.waitFor();
//            errorGobbler.join();   // Handle condition where the
//            outputGobbler.join();  // process ends before the threads finish
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


//        try {
//            Process process = new ProcessBuilder("start","/high","E:\\Repozytoria\\inzynierka\\lbm.exe", "E:\\Repozytoria\\inzynierka\\inout\\in\\ISBI2012\\bif\\optionsFLOWMRI_bif.txt")
//                    .redirectOutput(ProcessBuilder.Redirect.INHERIT)
//                    .redirectInput(ProcessBuilder.Redirect.INHERIT)
//                    .redirectError(ProcessBuilder.Redirect.INHERIT)
//                    .start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        try {
//            Process process = new ProcessBuilder("ping", "google.com", "-t")
//                    .redirectOutput(ProcessBuilder.Redirect.INHERIT)
//                    .start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}


