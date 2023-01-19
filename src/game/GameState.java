package game;

public class GameState {
	private int[] smallHoles;
	private int[] bigHoles;
	private boolean extraTurn;
	private boolean didSteal;

	public GameState(int[] smallHoles, int[] bigHoles) {
		this.smallHoles = smallHoles.clone();
		this.bigHoles = bigHoles.clone();
		extraTurn = false;
		didSteal = false;
	}

	public GameState(GameState state) {
		this.smallHoles = state.getSmallHoles().clone();
		this.bigHoles = state.getBigHoles().clone();
		this.extraTurn = state.isExtraTurn();
		this.didSteal = state.isDidSteal();
	}

	public int[] getSmallHoles() {
		return smallHoles;
	}

	public int[] getBigHoles() {
		return bigHoles;
	}

	public boolean isExtraTurn() {
		return extraTurn;
	}

	public boolean isDidSteal() {
		return didSteal;
	}

	public void setExtraTurn(boolean extraTurn) {
		this.extraTurn = extraTurn;
	}

	public void setDidSteal(boolean didSteal) {
		this.didSteal = didSteal;
	}

	public static GameState moveStones(GameState board, int position, Player currentP) { // Simulate the chosen move
		GameState newState = new GameState(board);
		int stones = newState.getSmallHoles()[position]; // Get stone count at the chosen hole
		newState.getSmallHoles()[position] = 0; // Clear the chosen hole
		for (int i = 0; i < stones; i++) {
			position++;

			if (position == 6 && currentP == Player.Player1) {
				newState.getBigHoles()[1]++;
				stones--;
				if (i == stones) {
					newState.setExtraTurn(true);
					break;
				}

			}
			if (position == 12) {
				position = 0;
				if (currentP == Player.Player2) {
					newState.getBigHoles()[0]++;
					stones--;
					if (i == stones) {
						newState.setExtraTurn(true);
						break;
					}
				}
			}
			newState.getSmallHoles()[position]++;
			if (i + 1 == stones) {// Check if its last iteration
				newState = checkSteal(newState, position, currentP);
			}
		}

		return newState;

	}

	public static GameState checkSteal(GameState board, int position, Player currentP) { // Check if the the last stone
																							// lands in an empty
																							// hole and the opposite
																							// hole has stones. In that
																							// case, the player takes
																							// all the stones
		int mirrorPos = 11 - position;
		int sum = 0;
		if (board.getSmallHoles()[position] == 1 && board.getSmallHoles()[mirrorPos] != 0) {
			if ((position <= 5 && currentP == Player.Player1) || (position >= 6 && currentP == Player.Player2)) {
				sum += 1 + board.getSmallHoles()[mirrorPos];
				board.getSmallHoles()[mirrorPos] = 0;
				board.getSmallHoles()[position] = 0;
				board.setDidSteal(true);
				if (currentP == Player.Player1)
					board.getBigHoles()[1] += sum;
				else
					board.getBigHoles()[0] += sum;

			}

		}

		return board;

	}

}
