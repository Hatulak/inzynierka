import controllers.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

@Slf4j
public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {
//        Zmiana lokalizacji dla testu
        Locale.setDefault(new Locale("en"));
        ResourceBundle resourceBundle = ResourceBundle.getBundle("bundles.messages");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main_window.fxml"), resourceBundle);
        Parent root = loader.load();

        MainWindowController mainWindowController = loader.getController();

        primaryStage.setTitle(resourceBundle.getString("main.window.title"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnHidden(event -> mainWindowController.shutdown());
        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(800);
        primaryStage.show();
    }

    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            log.error("Unknown error: " + e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }
}


