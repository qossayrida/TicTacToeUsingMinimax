package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class SceneManager {

	// Reference to the primary stage of the application.
	private static Stage primaryStage;

	// Setter method to set the primary stage.
	public static void setPrimaryStage(Stage stage) {
		primaryStage = stage;
	}

	// Set the main scene using a custom MainScene class.
	public static void setMainScene() {
		primaryStage.setScene(new MainScene());
	}

	// Set a general scene for the primary stage.
	public static void setScene(Scene scene) {
		primaryStage.setScene(scene);
	}

	// Display an alert with specified title and content
	// Because I used it in all the scenes i put it here to avoid repeating it in
	// every scene
	public static void showAlert(String title, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

}
