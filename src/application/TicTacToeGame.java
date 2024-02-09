package application;

import java.util.Random;

public class TicTacToeGame {
	private char[][] board; // Represents the game board, initialized with empty cells ('-')
	private String[][] state; // Represents the state of the game (not used in provided code)
	private char currentPlayer; // Represents the player (either 'X' or 'O') whose turn it currently is
	private char computer; // Represents the symbol ('X' or 'O') assigned to the computer player
	private char human; // Represents the symbol ('X' or 'O') assigned to the human player
	private int loss = 0, win = 0, draw = 0; // Variables to keep track of game outcomes

	// Constructor initializes the game board and assigns players
	public TicTacToeGame(char humanPlayer, char currentPlayer) {
		board = new char[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				board[i][j] = '-';
			}
		}
		this.human = humanPlayer;
		this.computer = (humanPlayer == 'X') ? 'O' : 'X';
		this.currentPlayer = currentPlayer; // X always starts
	}

	// Checks if the game is over (either due to a win or a full board)
	public boolean isGameOver() {
		return (checkForWin() || isBoardFull());
	}

	// Checks if there is a win on the board
	private boolean checkForWin() {
		return (checkRowColDiag('X') || checkRowColDiag('O'));
	}

	// Checks for a win in rows, columns, and diagonals
	private boolean checkRowColDiag(char player) {
		// Check rows, columns, and diagonals
		return (checkLine(player, 0, 0, 0, 1, 0, 2) || checkLine(player, 1, 0, 1, 1, 1, 2)
				|| checkLine(player, 2, 0, 2, 1, 2, 2) || checkLine(player, 0, 0, 1, 0, 2, 0)
				|| checkLine(player, 0, 1, 1, 1, 2, 1) || checkLine(player, 0, 2, 1, 2, 2, 2)
				|| checkLine(player, 0, 0, 1, 1, 2, 2) || checkLine(player, 0, 2, 1, 1, 2, 0));
	}

	// Checks a specific line for a win
	private boolean checkLine(char player, int x1, int y1, int x2, int y2, int x3, int y3) {
		return (board[x1][y1] == player && board[x2][y2] == player && board[x3][y3] == player);
	}

	// Checks if the game board is full
	private boolean isBoardFull() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] == '-') {
					return false;
				}
			}
		}
		return true;
	}

	// Checks if the game board is completely free (all cells are empty)
	public boolean isBoardFree() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] != '-') {
					return false;
				}
			}
		}
		return true; // No empty spaces, board is not free
	}

	public int generateRandomMove() {
		// Count empty cells
		int emptyCount = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] == '-') {
					emptyCount++;
				}
			}
		}

		// Store empty cell coordinates
		int[][] emptyCells = new int[emptyCount][2];
		int index = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] == '-') {
					emptyCells[index][0] = i;
					emptyCells[index][1] = j;
					index++;
				}
			}
		}

		// Select a random empty cell
		Random rand = new Random();
		int randomIndex = rand.nextInt(emptyCount);
		return emptyCells[randomIndex][0] * 3 + emptyCells[randomIndex][1];
	}

	public int bestMove() {
		// Initialize variables to keep track of the best score and move
		int bestScore = Integer.MIN_VALUE;
		int bestMove = -1;
		// Create a 2D array to store the state of each cell during the search
		state = new String[3][3];

		// Check if the board is completely free (no moves made yet)
		if (isBoardFree())
			return 0;

		// Iterate through each cell in the game board
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				// Reset counters for win, loss, and draw
				loss = 0;
				win = 0;
				draw = 0;

				// Check if the current cell is empty
				if (board[i][j] == '-') {
					// Simulate making a move in the current cell for the computer
					board[i][j] = computer;
					// Use the minimax algorithm to calculate the score for this move
					int score = minimax(false);
					// Undo the move to revert the board to its previous state
					board[i][j] = '-';
					// Store the result of the game in the state array
					state[i][j] = getGameResult();

					// Update the best move if the current score is better than the previous best
					// score
					if (score > bestScore) {
						bestScore = score;
						bestMove = i * 3 + j; // Convert 2D index to 1D index
					}
				} else {
					// If the cell is not empty, mark it as "Invalid" in the state array
					state[i][j] = "Invalid";
				}
			}
		}
		// Return the best move found
		return bestMove;
	}

	public String getGameResult() {
		// Create a string to represent the result of the game based on win, loss, and
		// draw
		String result = "";

		// Check if there is a loss and append it to the result string
		if (loss != 0) {
			result += "loss";
		}

		// Check if there is a win and append it to the result string
		if (win != 0) {
			if (!result.isEmpty()) {
				result += "\n";
			}
			result += "win";
		}

		// Check if there is a draw and append it to the result string
		if (draw != 0) {
			if (!result.isEmpty()) {
				result += "\n";
			}
			result += "draw";
		}

		// Return the result string or null if no outcome is present
		return result.isEmpty() ? null : result;
	}

	private int minimax(boolean isMaximizing) {
		// Check if the game is over with a win or loss, or if it's a draw
		if (checkForWin()) {
			// If maximizing, it's a loss for the computer; if minimizing, it's a win for
			// the human
			if (isMaximizing) {
				loss++;
				return -1;
			} else {
				win++;
				return 1;
			}
		} else if (isBoardFull()) {
			// If the board is full and no one has won, it's a draw
			draw++;
			return 0;
		}

		// Recursive part of the minimax algorithm
		if (isMaximizing) {
			// Maximizing player (computer) seeks to maximize the score
			int bestScore = Integer.MIN_VALUE;
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (board[i][j] == '-') {
						// Simulate making a move for the computer
						board[i][j] = computer;
						// Recursively calculate the score for this move
						int score = minimax(false);
						// Undo the move
						board[i][j] = '-';
						// Update the best score
						bestScore = Math.max(score, bestScore);
					}
				}
			}
			return bestScore;
		} else {
			// Minimizing player (human) seeks to minimize the score
			int bestScore = Integer.MAX_VALUE;
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (board[i][j] == '-') {
						// Simulate making a move for the human
						board[i][j] = human;
						// Recursively calculate the score for this move
						int score = minimax(true);
						// Undo the move
						board[i][j] = '-';
						// Update the best score
						bestScore = Math.min(score, bestScore);
					}
				}
			}
			return bestScore;
		}
	}

	public int[] getWinningCombination() {
		int[][] combinations = { { 0, 0, 0, 1, 0, 2 }, { 1, 0, 1, 1, 1, 2 }, { 2, 0, 2, 1, 2, 2 }, { 0, 0, 1, 0, 2, 0 },
				{ 0, 1, 1, 1, 2, 1 }, { 0, 2, 1, 2, 2, 2 }, { 0, 0, 1, 1, 2, 2 }, { 0, 2, 1, 1, 2, 0 } };

		for (int[] combination : combinations) {
			if (board[combination[0]][combination[1]] != '-'
					&& board[combination[0]][combination[1]] == board[combination[2]][combination[3]]
					&& board[combination[0]][combination[1]] == board[combination[4]][combination[5]]) {
				return new int[] { combination[0], combination[1], combination[2], combination[3], combination[4],
						combination[5] };
			}
		}
		return null;
	}

	public void makeMove(int row, int col, char player) {
		if (row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == '-') {
			board[row][col] = player;
		}
	}

	public char getCurrentPlayer() {
		return currentPlayer;
	}

	public void switchPlayer() {
		currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
	}

	public char[][] getBoard() {
		return board;
	}

	public String[][] getState() {
		return state;
	}

	public char getComputer() {
		return computer;
	}
}