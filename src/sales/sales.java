package sales;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author DEAdMEAt
 */
public class sales extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("salesFXML.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.getIcons().add(new Image("img/log.jpg"));
        scene.getStylesheets().add("css/general.css");
        stage.show();
        stage.setResizable(false);

    }

    public static void main(String[] args) {
        launch(args);
    }

}
