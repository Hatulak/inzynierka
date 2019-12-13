import controllers.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {
//        Zmiana lokalizacji dla testu
//        Locale.setDefault(new Locale("en"));
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

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}


