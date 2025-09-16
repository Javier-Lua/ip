import java.io.IOException;

import exception.RotomException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Rotom/ using FXML.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            Rotom rotom = new Rotom("./src/main/java/rotom.txt");
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("Rotom Chatbot");
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            // stage.setMaxWidth(417); // Add this if you didn't automatically resize elements
            fxmlLoader.<MainWindow>getController().setRotom(rotom); // inject the Rotom instance
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load MainWindow FXML", e);
        } catch (RotomException e) {
            System.out.println(e.getMessage());
        }
    }
}
