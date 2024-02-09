package application;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.beans.binding.*;
import javafx.application.*;

public class MainScene extends Scene {

	HBox layout; // Horizontal layout for organizing UI components
	GridPane rightPanel; // GridPane for displaying X or O choice
	String modeName = null; // Variable to store the selected game mode
	RadioButton xButton1; // RadioButton for X selection
	RadioButton turnButton1; // RadioButton for O selection
	TextField roundsField; // TextField for inputting the number of rounds
	TextField nameField1; // TextField for player 1's name
	TextField nameField2; // TextField for player 2's name

	/**
	 * Constructor for the main scene of the Tic Tac Toe game.
	 */
	public MainScene() {
		// Initializing the scene with a vertical box layout
		super(new VBox(60), Screen.getPrimary().getVisualBounds().getWidth(),
				Screen.getPrimary().getVisualBounds().getHeight());
		VBox root = (VBox) getRoot();

		// Adding external CSS stylesheet
		root.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		root.getStyleClass().add("root");
		root.setAlignment(Pos.TOP_CENTER);
		root.setPadding(new Insets(60));

		// Creating a vertical box for the menu
		VBox menu = new VBox(40);
		menu.setAlignment(Pos.CENTER);

		// Array of strings for menu options
		String[] strings = { "Easy Mode", "Advanced Mode", "Two Players Mode", "Start", "Exit" };

		Button[] buttons = new Button[strings.length];
		setupButtons(strings, buttons); // Method call to set up menu buttons
		menu.getChildren().addAll(buttons);

		// Creating the right panel for X or O choice
		rightPanel = new GridPane();
		rightPanel.setVisible(false);

		// Creating a horizontal box layout to organize the menu and right panel
		layout = new HBox();
		layout.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(menu, rightPanel);

		// Creating and styling a label for the welcome message
		Label welcomeLabel = new Label("Tic Tac Toe Game");
		welcomeLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 40));
		welcomeLabel.setStyle("-fx-text-fill: #000000;");

		// Adding components to the root layout
		root.getChildren().addAll(welcomeLabel, layout);
	}

	private void setupButtons(String[] strings, Button[] buttons) {
		// Loop to create buttons with custom styling
		for (int i = 0; i < strings.length; i++) {
			buttons[i] = new Button(strings[i]);
			buttons[i].getStyleClass().add("custom-button");
			buttons[i].setMinSize(200, 50);
		}

		// Action handler for "Easy Mode" button
		buttons[0].setOnAction(e -> {
			rightPanel.setVisible(true);
			modeName = "Easy Mode";
			controlRightPanel();
		});

		// Action handler for "Advanced Mode" button
		buttons[1].setOnAction(e -> {
			rightPanel.setVisible(true);
			modeName = "Advanced Mode";
			controlRightPanel();
		});

		// Action handler for "Two Player Mode" button
		buttons[2].setOnAction(e -> {
			rightPanel.setVisible(true);
			modeName = "Two Player Mode";
			controlRightPanel();
		});

		// Action handler for "Start" button
		buttons[3].setOnAction(e -> {
			try {
				// Check if a game mode has been selected
				if (modeName == null)
					throw new IllegalArgumentException("You must choose the mode first");

				char player1, currentPlayer;

				// Determine the player symbols and starting player based on radio button
				// selections
				if (xButton1.isSelected() && turnButton1.isSelected()) {
					player1 = 'X';
					currentPlayer = 'X';
				} else if (!xButton1.isSelected() && turnButton1.isSelected()) {
					player1 = 'O';
					currentPlayer = 'O';
				} else if (xButton1.isSelected() && !turnButton1.isSelected()) {
					player1 = 'X';
					currentPlayer = 'O';
				} else {
					player1 = 'O';
					currentPlayer = 'X';
				}

				// Get input values for number of rounds and player names
				int numRounds = Integer.parseInt(roundsField.getText());
				String p1Name = nameField1.getText();
				String p2Name = (modeName.equals("Two Player Mode")) ? nameField2.getText() : null;

				// Validate input values
				if (numRounds <= 0)
					throw new IllegalArgumentException("Number of rounds can't be a negative number");

				// Set the result scene based on the selected game mode
				if (modeName.equals("Easy Mode"))
					SceneManager.setScene(new ResultScene(numRounds, 1, player1, currentPlayer, p1Name, "Computer"));
				else if (modeName.equals("Advanced Mode"))
					SceneManager.setScene(new ResultScene(numRounds, 2, player1, currentPlayer, p1Name, "Computer"));
				else
					SceneManager.setScene(new ResultScene(numRounds, 3, player1, currentPlayer, p1Name, p2Name));

			} catch (Exception e2) {
				// Display an alert in case of an error
				SceneManager.showAlert("Error", e2.getMessage());
			}
		});

		// Action handler for "Exit" button
		buttons[4].setOnAction(e -> {
			// Exit the application
			Platform.exit();
			System.exit(0);
		});
	}

	/**
	 * Method to control the right panel based on the selected game mode.
	 */
	private void controlRightPanel() {
		// Adjusting layout spacing for better organization
		layout.setSpacing(120);

		// Clearing previous content in the right panel
		rightPanel.getChildren().clear();

		// Setting padding, gaps, and size for the right panel
		rightPanel.setPadding(new Insets(10));
		rightPanel.setVgap(10);
		rightPanel.setHgap(20);
		rightPanel.setMaxSize(500, 580);
		rightPanel.setPrefSize(500, 580);
		rightPanel.getStyleClass().add("choice-box");

		// Displaying the selected mode name
		Label modeLabel = new Label(modeName);
		modeLabel.getStyleClass().add("custom-label");
		GridPane.setHalignment(modeLabel, HPos.CENTER);
		rightPanel.add(modeLabel, 0, 0, 2, 1);

		// Player 1 choices (X or O)
		Label choiceLabel1 = new Label("Choose X or O for Player 1");
		choiceLabel1.getStyleClass().add("sub-label");
		rightPanel.add(choiceLabel1, 0, 1, 2, 1);

		// Radio buttons for X and O choices for Player 1
		xButton1 = new RadioButton();
		RadioButton oButton1 = new RadioButton();
		ToggleGroup group1 = new ToggleGroup();
		xButton1.setToggleGroup(group1);
		oButton1.setToggleGroup(group1);
		xButton1.setSelected(true);
		createImageView("/pictures/x.png", xButton1, 70);
		createImageView("/pictures/o.png", oButton1, 70);

		HBox player1Box = new HBox(10, xButton1, oButton1);
		player1Box.setAlignment(Pos.CENTER);
		rightPanel.add(player1Box, 0, 2, 2, 1);

		// Player 2 choices (X or O) or Computer choice
		Label choiceLabel2 = new Label(
				modeName.equals("Two Player Mode") ? "Choose X or O for Player 2" : "Choose X or O for Computer");
		choiceLabel2.getStyleClass().add("sub-label");
		rightPanel.add(choiceLabel2, 0, 3, 2, 1);

		// Radio buttons for X and O choices for Player 2 or Computer
		RadioButton xButton2 = new RadioButton();
		RadioButton oButton2 = new RadioButton();
		ToggleGroup group2 = new ToggleGroup();
		xButton2.setToggleGroup(group2);
		oButton2.setToggleGroup(group2);
		oButton2.setSelected(true);

		createImageView("/pictures/x.png", xButton2, 70);
		createImageView("/pictures/o.png", oButton2, 70);

		HBox player2Box = new HBox(10, xButton2, oButton2);
		player2Box.setAlignment(Pos.CENTER);
		rightPanel.add(player2Box, 0, 4, 2, 1);

		// Player names and additional choices based on Two Player Mode
		Label nameLabel1 = new Label(modeName.equals("Two Player Mode") ? "Player 1's Name" : "Player Name");
		nameLabel1.getStyleClass().add("sub-label");
		rightPanel.add(nameLabel1, 0, 5);

		nameField1 = new TextField();
		nameField1.setPromptText(modeName.equals("Two Player Mode") ? "Enter Player 1's name" : "Enter Player name");
		nameField1.setPrefWidth(200);
		rightPanel.add(nameField1, 1, 5);

		// Linking radio button selections between players
		group1.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal == xButton1) {
				oButton2.setSelected(true);
			} else {
				xButton2.setSelected(true);
			}
		});

		group2.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal == xButton2) {
				oButton1.setSelected(true);
			} else {
				xButton1.setSelected(true);
			}
		});

		// Additional UI components for Two Player Mode
		if (modeName.equals("Two Player Mode")) {
			Label nameLabel2 = new Label("Player 2's Name");
			nameLabel2.getStyleClass().add("sub-label");
			rightPanel.add(nameLabel2, 0, 6);

			nameField2 = new TextField();
			nameField2.setPromptText("Enter Player 2's name");
			nameField2.setPrefWidth(200);
			rightPanel.add(nameField2, 1, 6);
		}

		// Number of rounds field
		Label roundsLabel = new Label("Number of Rounds");
		roundsLabel.getStyleClass().add("sub-label");
		rightPanel.add(roundsLabel, 0, modeName.equals("Two Player Mode") ? 7 : 6);

		roundsField = new TextField();
		roundsField.setPromptText("Enter number of rounds");
		rightPanel.add(roundsField, 1, modeName.equals("Two Player Mode") ? 7 : 6);

		// Starting player selection
		Label startLabel = new Label("Choose who starts the game");
		startLabel.getStyleClass().add("sub-label");
		rightPanel.add(startLabel, 0, modeName.equals("Two Player Mode") ? 8 : 7, 2, 1);

		turnButton1 = new RadioButton("Player 1");
		RadioButton turnButton2 = modeName.equals("Two Player Mode") ? new RadioButton("Player 2")
				: new RadioButton("Computer");

		ToggleGroup startGroup = new ToggleGroup();
		turnButton1.setToggleGroup(startGroup);
		turnButton2.setToggleGroup(startGroup);
		turnButton1.setSelected(true);

		// Creating image views for radio buttons
		createImageView("/pictures/Button.png", turnButton1, 30);
		createImageView("/pictures/Button.png", turnButton2, 30);

		HBox startBox = new HBox(10, turnButton1, turnButton2);
		startBox.setAlignment(Pos.CENTER);
		rightPanel.add(startBox, 0, modeName.equals("Two Player Mode") ? 9 : 8, 2, 1);
	}

	private void createImageView(String path, RadioButton button, int size) {
		ImageView image = new ImageView(new Image(getClass().getResourceAsStream(path)));
		image.setPreserveRatio(true);
		image.setFitHeight(size);
		image.opacityProperty().bind(Bindings.when(button.selectedProperty()).then(1.0).otherwise(0.5));
		button.setGraphic(image);
		button.getStyleClass().add("custom-radiobutton");
	}

}
