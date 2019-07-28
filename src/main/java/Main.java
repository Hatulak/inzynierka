import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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

    public static void main(String[] args) {
        launch(args);
    }

}
