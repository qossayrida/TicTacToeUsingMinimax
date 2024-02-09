package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;

public class ResultScene extends Scene {

	private int numRounds, mode, round = 1;
	private char player1, startPlayer;
	private int winsPlayer1 = 0, winsComputer = 0;
	private Button nextRoundButton, backButton;
	private String player1Name, player2Name;
	private Label scoreX, scoreO, roundIndicator, turnIndicator, winnerLabel;
	private VBox layout;
	private GridPane gameBoard, gameState;
	private Button[][] buttons; // Buttons for the Tic-Tac-Toe grid
	private Label[][] labels;
	private TicTacToeGame game;
	Pane pane;

	/**
	 * Constructor for the ResultScene, representing the result and progress of the
	 * Tic Tac Toe game. numRounds The total number of rounds to be played mode The
	 * game mode (1: Easy, 2: Advanced, 3: Two Players) player1 The symbol for
	 * Player 1 (X or O) startPlayer The player who starts the game (X or O) p1Name
	 * The name of Player 1 p2Name The name of Player 2 (null for computer in Two
	 * Players mode)
	 */
	public ResultScene(int numRounds, int mode, char player1, char startPlayer, String p1Name, String p2Name) {
		// Setting up the main layout as a VBox
		super(new VBox(30), Screen.getPrimary().getVisualBounds().getWidth(),
				Screen.getPrimary().getVisualBounds().getHeight() - 20);
		layout = (VBox) getRoot();
		layout.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		layout.getStyleClass().add("background");
		layout.setAlignment(Pos.TOP_CENTER);
		layout.setPadding(new Insets(40));

		// Initializing score and round information
		initializeScoreAndRounds(numRounds, mode, player1, startPlayer, p1Name, p2Name);

		// Creating and styling the welcome label
		Label welcomeLabel = new Label("Tic Tac Toe Game");
		welcomeLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 40));
		welcomeLabel.setStyle("-fx-text-fill: #000000;");
		layout.getChildren().add(welcomeLabel);

		// Creating a VBox for the score board
		VBox scoreBoard = new VBox(10);
		scoreX = new Label(p1Name + ": 0");
		scoreO = new Label(p2Name + ": 0");
		scoreX.setStyle("-fx-font-size: 20px; -fx-text-fill: black;");
		scoreO.setStyle("-fx-font-size: 20px; -fx-text-fill: black;");
		scoreBoard.getChildren().addAll(scoreX, scoreO);
		scoreBoard.setAlignment(Pos.CENTER);

		// Creating a VBox for round information
		VBox roundBox = new VBox(10);
		roundBox.setAlignment(Pos.CENTER);
		roundBox.setStyle("-fx-padding: 20px;");
		roundIndicator = new Label("Round: 1/" + numRounds);
		if (startPlayer == player1)
			turnIndicator = new Label("Turn: " + p1Name);
		else
			turnIndicator = new Label("Turn: " + p2Name);
		turnIndicator.setStyle("-fx-font-size: 20px; -fx-text-fill: black;");
		roundIndicator.setStyle("-fx-font-size: 20px; -fx-text-fill: black;");
		roundBox.getChildren().add(roundIndicator);
		if (mode == 3)
			roundBox.getChildren().add(turnIndicator);

		// Creating an HBox for the center content, including the game board and
		// additional components
		HBox centerHBox = new HBox(150);
		centerHBox.setPadding(new Insets(20));
		centerHBox.setAlignment(Pos.CENTER);

		// Initializing the game board and drawing the grid
		initializeGameBoard();
		pane = new Pane();
		drawGrid(360, 360);

		// Checking the game mode for additional UI components
		if (mode == 2) {
			initializeGameState(); // Additional game state information for Advanced Mode
			VBox boardVBox = new VBox(50);
			boardVBox.setAlignment(Pos.TOP_CENTER);
			Label gameBoardLabel = new Label("Game Board");
			gameBoardLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 28));
			gameBoardLabel.setStyle("-fx-text-fill: #000000;");
			boardVBox.getChildren().addAll(gameBoardLabel, new StackPane(gameBoard, pane));

			VBox stateVBox = new VBox(80);
			stateVBox.setAlignment(Pos.TOP_CENTER);
			Label gameStateLabel = new Label("Game State");
			gameStateLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 28));
			gameStateLabel.setStyle("-fx-text-fill: #000000;");
			stateVBox.getChildren().addAll(gameStateLabel, gameState);
			centerHBox.getChildren().addAll(scoreBoard, boardVBox, stateVBox, roundBox);
		} else {
			centerHBox.setPadding(new Insets(60));
			centerHBox.getChildren().addAll(scoreBoard, new StackPane(gameBoard, pane), roundBox);
		}

		// Initializing the Tic Tac Toe game instance
		game = new TicTacToeGame(player1, startPlayer);

		// Making the computer's move if the computer starts the game
		if (this.startPlayer == game.getComputer()) {
			if (mode == 2)
				makeComputerMoveAdvanced();
			else if (mode == 1)
				makeComputerMoveRandom();
		}

		// Creating a label for displaying the winner
		winnerLabel = new Label();
		winnerLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 20));

		// Adding components to the layout
		layout.getChildren().addAll(centerHBox, winnerLabel);

		// Initializing the next round button
		initializeNextRoundButton();
	}

	private void initializeScoreAndRounds(int numRounds, int mode, char player1, char currentPlayer, String p1Name,
			String p2Name) {
		this.numRounds = numRounds;
		this.mode = mode;
		this.player1Name = p1Name;
		this.player2Name = p2Name;
		this.startPlayer = currentPlayer;
		this.player1 = player1;
	}

	private void initializeGameBoard() {
		// Creating a GridPane to represent the Tic Tac Toe game board
		gameBoard = new GridPane();
		gameBoard.setAlignment(Pos.CENTER);
		gameBoard.setHgap(15); // Horizontal gap between buttons
		gameBoard.setVgap(15); // Vertical gap between buttons
		buttons = new Button[3][3];

		// Creating buttons for each cell in the grid
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Button button = new Button();
				gameBoard.add(button, j, i);
				buttons[i][j] = button;

				// Styling the button with CSS class and size preferences
				button.getStyleClass().add("square");
				button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // Allow the button to grow
				button.setPrefSize(102, 102);

				// Adding an action handler to handle button clicks
				int row = i, col = j;
				button.setOnAction(e -> {
					// Checking if the selected cell is empty and the game is not over
					if (game.getBoard()[row][col] == '-' && !game.isGameOver()) {
						// Making a move, updating the UI, and switching players
						game.makeMove(row, col, game.getCurrentPlayer());
						updateButtonUI(button, game.getCurrentPlayer());
						game.switchPlayer();
						checkGameState();

						// If the game is not over, updating the turn indicator and making computer
						// moves
						if (!game.isGameOver()) {
							if (game.getCurrentPlayer() == player1)
								turnIndicator.setText("Turn: " + player1Name);
							else
								turnIndicator.setText("Turn: " + player2Name);

							// Making computer moves based on the game mode
							if (mode == 2)
								makeComputerMoveAdvanced();
							else if (mode == 1)
								makeComputerMoveRandom();
						}
					}
				});
			}
		}
	}

	/**
	 * Checks the current state of the Tic Tac Toe game and updates the UI
	 * accordingly. If the game is over, it checks for a winner or a draw and
	 * updates the scores and winner label. If the maximum number of rounds is
	 * reached, it displays an alert with the final winner.
	 */
	private void checkGameState() {
		if (game.isGameOver()) {
			// Retrieving the winning combination, if any
			int[] winningCombination = game.getWinningCombination();

			if (winningCombination != null) {
				// Drawing the winning line and determining the winner
				drawWinningLine(winningCombination);
				char winner = game.getBoard()[winningCombination[0]][winningCombination[1]];

				// Updating scores and winner label
				if (winner == player1) {
					winsPlayer1++;
					winnerLabel.setText(player1Name + " wins!");
				} else {
					winsComputer++;
					winnerLabel.setText(player2Name + " wins!");
				}
				scoreX.setText(player1Name + ": " + winsPlayer1);
				scoreO.setText(player2Name + ": " + winsComputer);
			} else {
				// Handling the case of a draw
				winnerLabel.setText("It's a draw!");
			}

			// Enabling the next round button if the maximum number of rounds is not reached
			if (round < numRounds) {
				
				if(winsPlayer1>(numRounds/2) ||winsComputer>(numRounds/2) )
					showAlert();
				
				nextRoundButton.setDisable(false);
			} else {
				// Displaying an alert with the final winner if all rounds are completed
				showAlertWithWinner();
			}
		}
	}

	/**
	 * Makes a random move for the computer player in the Tic Tac Toe game. Checks
	 * if the game is not over and generates a random move, updating the UI
	 * accordingly. Switches players, updates the turn indicator, and checks the
	 * game state.
	 */
	private void makeComputerMoveRandom() {
		if (!game.isGameOver()) {
			int index = game.generateRandomMove();
			if (index != -1) {
				game.makeMove(index / 3, index % 3, game.getCurrentPlayer());
				Button button = buttons[index / 3][index % 3];
				updateButtonUI(button, game.getCurrentPlayer());
				checkGameState();
				game.switchPlayer();

				if (game.getCurrentPlayer() == player1)
					turnIndicator.setText("Turn: " + player1Name);
				else
					turnIndicator.setText("Turn: " + player2Name);
			}
		}
	}

	/**
	 * Makes an advanced move for the computer player in the Tic Tac Toe game using
	 * a prediction algorithm. Checks if the game is not over, determines the best
	 * move, updates the UI, and checks the game state. Switches players and updates
	 * the turn indicator accordingly.
	 */
	private void makeComputerMoveAdvanced() {
		if (!game.isGameOver()) {

			int index = game.bestMove();
			if (index != -1) {
				printPredictions(index);
				game.makeMove(index / 3, index % 3, game.getCurrentPlayer());
				updateButtonUI(buttons[index / 3][index % 3], game.getCurrentPlayer());
				game.switchPlayer();
				checkGameState();
			}

			if (game.getCurrentPlayer() == player1)
				turnIndicator.setText("Turn: " + player1Name);
			else
				turnIndicator.setText("Turn: " + player2Name);
		}
	}

	/**
	 * Prints the predictions made by the advanced computer move algorithm on the
	 * UI. Updates the labels representing the game state with predictions and
	 * highlights the result. Also, applies styling for the "Invalid" state if
	 * encountered.
	 */
	private void printPredictions(int result) {
		if (!game.isBoardFree()) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					labels[i][j].setText(game.getState()[i][j]);

					// Highlighting the cell with the predicted move result
					if (i == result / 3 && j == result % 3) {
						labels[i][j].getStyleClass().add("square-result");
					} else if (game.getState()[i][j].equals("Invalid")) {
						// Applying styling for cells with "Invalid" state
						labels[i][j].getStyleClass().remove("square-result");
						labels[i][j].getStyleClass().add("square-Invalid");
					} else {
						// Removing styling for cells without the predicted move result or "Invalid"
						// state
						labels[i][j].getStyleClass().removeAll("square-result", "square-Invalid");
					}
				}
			}
		}
	}

	private void updateButtonUI(Button button, char player) {
		ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/pictures/" + player + player + ".png")));
		image.setPreserveRatio(true);
		image.setFitHeight(100);
		button.setStyle("-fx-background-color: transparent;");
		button.setPadding(Insets.EMPTY); // Remove padding
		button.setBorder(Border.EMPTY); // Remove border
		button.setGraphic(image);
	}

	/**
	 * Initializes the game state grid for displaying advanced move predictions.
	 * Creates labels for each cell and sets their initial styling.
	 */
	private void initializeGameState() {
		gameState = new GridPane();
		gameState.setAlignment(Pos.CENTER);
		gameState.setHgap(15); // Horizontal gap between buttons
		gameState.setVgap(15); // Vertical gap between buttons
		labels = new Label[3][3];

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				labels[i][j] = new Label();
				labels[i][j].setAlignment(Pos.CENTER);
				gameState.add(labels[i][j], j, i);
				labels[i][j].getStyleClass().add("square");
				labels[i][j].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // Allow the button to grow
				labels[i][j].setPrefSize(90, 90);
			}
		}
	}

	/**
	 * Initializes the "Next Round" button and "Back to Main" button. Disables the
	 * "Next Round" button initially and sets event handlers for button clicks.
	 */
	private void initializeNextRoundButton() {
		nextRoundButton = new Button("Next Round");
		backButton = new Button("Back to main");
		nextRoundButton.setDisable(true);
		nextRoundButton.getStyleClass().add("custom-button");
		backButton.getStyleClass().add("custom-button");

		nextRoundButton.setOnAction(e -> {
			round++;
			nextRoundButton.setDisable(true);
			resetGame();
			roundIndicator.setText("Round: " + round + "/" + numRounds);
			winnerLabel.setText("");
		});
		backButton.setOnAction(e -> SceneManager.setMainScene());

		HBox controlBox = new HBox(20, backButton, nextRoundButton);
		controlBox.setAlignment(Pos.CENTER);
		layout.getChildren().add(controlBox);
	}

	/**
	 * Resets the game state to start a new round. Clears the game board, updates
	 * the turn indicator, and makes a computer move if needed.
	 */
	private void resetGame() {
		game = new TicTacToeGame(player1, startPlayer);
		if (game.getCurrentPlayer() == player1)
			turnIndicator.setText("Turn: " + player1Name);
		else
			turnIndicator.setText("Turn: " + player2Name);

		pane.getChildren().clear();
		drawGrid(360, 360);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				buttons[i][j].setStyle("");
				buttons[i][j].setGraphic(null);
				buttons[i][j].getStyleClass().add("square");
				buttons[i][j].setPadding(new Insets(8, 8, 8, 8));

				if (mode == 2) {
					labels[i][j].setText(null);
					labels[i][j].getStyleClass().clear();
					labels[i][j].getStyleClass().add("square");
				}
			}
		}

		if (startPlayer == game.getComputer()) {
			if (mode == 2)
				makeComputerMoveAdvanced();
			else if (mode == 1)
				makeComputerMoveRandom();
		}
	}

	/**
	 * Displays an alert with the final game result. Shows the winner or declares a
	 * draw based on the number of wins for each player.
	 */
	private void showAlertWithWinner() {
		String winner;
		if (winsPlayer1 > winsComputer) {
			winner = player1Name + " wins the game!";
		} else if (winsComputer > winsPlayer1) {
			winner = player2Name + " wins the game!";
		} else {
			winner = "The game is a draw!";
		}

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Game Over");
		alert.setHeaderText(null);
		alert.setContentText(winner);
		alert.showAndWait();
	}

	
	private void showAlert() {
		String winner="The game was end\n";
		if (winsPlayer1 > winsComputer) {
			winner += player1Name + " wins the game!";
		} else if (winsComputer > winsPlayer1) {
			winner += player2Name + " wins the game!";
		} else {
			winner += "The game is a draw!";
		}
		winner += "\nEnd the game?";

		Alert confirmation = new Alert(AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText(null);
        confirmation.setContentText(winner);

        // Customize the buttons (OK for delete, CANCEL for cancel)
        ButtonType buttonTypeOK = new ButtonType("OK");
        ButtonType buttonTypeCancel = new ButtonType("Cancel");
        confirmation.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);
        ButtonType result = confirmation.showAndWait().orElse(ButtonType.CANCEL);

        if (result == buttonTypeOK) {
        	SceneManager.setMainScene();
        }   	 
	}
	/**
	 * Draws a line on the game board to indicate the winning combination. Takes the
	 * winning combination array as input and calculates the start and end
	 * coordinates.
	 */
	private void drawWinningLine(int[] winningCombination) {
		double cellSize = 102;

		int startRow = winningCombination[0];
		int startCol = winningCombination[1];
		int endRow = winningCombination[4];
		int endCol = winningCombination[5];

		double startX = buttons[startRow][startCol].getLayoutX() + cellSize / 2;
		double startY = buttons[startRow][startCol].getLayoutY() + cellSize / 2;
		double endX = buttons[endRow][endCol].getLayoutX() + cellSize / 2;
		double endY = buttons[endRow][endCol].getLayoutY() + cellSize / 2;

		Line line = new Line(startX, startY, endX, endY);
		line.setStrokeWidth(5);
		line.setStroke(Color.RED);

		pane.getChildren().add(line);
	}

	/**
	 * Draws the initial Tic-Tac-Toe grid on the game board. Takes width and height
	 * parameters and calculates the positions for horizontal and vertical lines.
	 */
	public void drawGrid(double W, double H) {
		pane.setMouseTransparent(true);
		// Calculate the positions for the lines based on the parameters W and H
		double w_third = W / 3;
		double h_third = H / 3;

		// Draw horizontal lines
		for (int i = 1; i < 3; i++) {
			Line hLine = new Line(0, i * h_third, W, i * h_third);
			hLine.setStrokeWidth(3);
			pane.getChildren().add(hLine);
		}

		// Draw vertical lines
		for (int i = 1; i < 3; i++) {
			Line vLine = new Line(i * w_third, 0, i * w_third, H);
			vLine.setStrokeWidth(3);
			pane.getChildren().add(vLine);
		}
	}

}
