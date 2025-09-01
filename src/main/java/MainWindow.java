import java.util.Objects;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    // Assigned at runtime
    @SuppressWarnings("unused")
    @FXML
    private Button sendButton;

    private Milo milo;

    private final Image userImage = new Image(Objects.requireNonNull(
            this.getClass().getResourceAsStream("/images/DaUser.png")));
    private final Image miloImage = new Image(Objects.requireNonNull(
            this.getClass().getResourceAsStream("/images/DaMilo.png")));

    /**
     * Opens the window and Milo welcomes user with welcome message.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        dialogContainer.getChildren().add(
                DialogBox.getMiloDialog("""
                ____________________________________________________________
                 Hello! I'm Milo!
                 What can I do for you?
                 Type 'help' for the list of commands I can understand!
                ____________________________________________________________
                """, miloImage)
        );
    }

    /** Injects the Milo instance */
    public void setMilo(Milo m) {
        milo = m;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Milo's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = milo.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getMiloDialog(response, miloImage)
        );
        userInput.clear();
    }
}
