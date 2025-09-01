import java.io.IOException;

import exception.MiloException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Milo/ using FXML.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            Milo milo = new Milo("./src/main/java/milo.txt");
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            // stage.setMaxWidth(417); // Add this if you didn't automatically resize elements
            fxmlLoader.<MainWindow>getController().setMilo(milo); // inject the Milo instance
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load MainWindow FXML", e);
        } catch (MiloException e) {
            System.out.println(e.getMessage());
        }
    }
}
