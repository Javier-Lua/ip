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

    private Rotom rotom;

    private final Image userImage = new Image(Objects.requireNonNull(
            this.getClass().getResourceAsStream("/images/DaUser.png")));
    private final Image rotomImage = new Image(Objects.requireNonNull(
            this.getClass().getResourceAsStream("/images/DaRotom.png")));

    /**
     * Opens the window and Rotom welcomes user with welcome message.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        dialogContainer.getChildren().add(
                DialogBox.getRotomDialog("""
                ____________________________________________________________
                 Hello! I'm Rotom,
                 your electrifying task helper! ⚡
                 What can I do for you?
                 Type 'help' for the list of commands I understand!
                ____________________________________________________________
                """, rotomImage)
        );
    }

    /** Injects the Rotom instance */
    public void setRotom(Rotom m) {
        rotom = m;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Rotom's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = rotom.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getRotomDialog(response, rotomImage)
        );
        userInput.clear();
    }
}
