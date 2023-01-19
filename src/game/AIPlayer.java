package game;

import java.util.Random;

public class AIPlayer {
	private int[] smallHoles; // Starting state of the small holes
	private int[] bigHoles; // Starting state of the big holes
	private int extraTurns; // counts how many extra turns occurred
	private final static int searchDepth=5; //How many steps forward the computer will simulate

	public AIPlayer(int[] smallHoles, int[] bigHoles) {
		this.smallHoles = smallHoles.clone();
		this.bigHoles = bigHoles.clone();
		extraTurns = 0;

	}

	public int chooseMove() {
		int[] options = { -1000000, -1000000, -1000000, -1000000, -1000000, -1000000 }; // Each player controls 6 holes
		GameState base = new GameState(smallHoles, bigHoles);
		GameState temp = new GameState(base);
		int maxOption, maxValue;                  
		for (int i = 11; i >= 6; i--) { // Calc max option from each branch
			temp = new GameState(base);
			if(base.getSmallHoles()[i]>0) {
			temp = GameState.moveStones(base, i, Player.Player2);
			if (!checkEndGame(temp.getSmallHoles().clone()))
				options[i - 6] = minimax(temp.getSmallHoles().clone(), temp.getBigHoles().clone(), searchDepth, false,0,0,-10000,10000);
			}

		}
//		for (int i = 0; i <= 5; i++) {
//			System.out.println("Options[" + (i + 6) + "]:" + options[i]);
//		}
		maxValue = options[0]; // Assume its the first option (hole 6)(range 6-11)
		maxOption = 6;
		for (int i = 1; i <= 5; i++) {
			if (options[i] > maxValue) {
				maxValue = options[i];
				maxOption = i + 6;
			}
		}
		return maxOption;

	}

	public int minimax(int[] smallHoles, int[] bigHoles, int depth, boolean maxPlayer,int extraTurns,int steals,int alpha,int beta) {
		int eval;
		int extraT=0;
		int steal=0;
		GameState base = new GameState(smallHoles,bigHoles);
		GameState temp = new GameState(base);

		if (depth <= 0 || checkEndGame(smallHoles)) {
//			System.out.println("Outcome:" + bigHoles[0] + "  " + bigHoles[1]+" Extra:"+extraTurns+" Steals:"+steals);
			eval=bigHoles[0]-bigHoles[1]+steals;
			return eval;

		}
		if (maxPlayer) {
			int maxEval = (-10000);
			for (int i = 6; i <= 11; i++) { // holes 6 to 11 are the holes controlled by player 2
				extraT=0;
				if (base.getSmallHoles()[i] > 0) { // Check if the hole has at least 1 stone
					temp = GameState.moveStones(base, i, Player.Player2);
					if(temp.isExtraTurn()) { //Check if that move caused an extra turn
						extraT++;
					}
					if(temp.isDidSteal()) { //Check if that move caused a steal
						steal++;
					}
					eval = minimax(temp.getSmallHoles().clone(), temp.getBigHoles().clone(), depth - 1, !maxPlayer,extraTurns+extraT,steals+steal,alpha,beta);
					maxEval = Math.max(eval, maxEval);
					alpha = Math.max(alpha, eval);
					if(beta<=alpha)
						break;
				}
			}
			return maxEval;

		} else {
			int minEval = (10000);
			for (int i = 0; i <= 5; i++) { // holes 0 to 5 are the holes controlled by player 1
				if (base.getSmallHoles()[i] > 0) {// Check if the hole has at least 1 stone

					temp = GameState.moveStones(base, i, Player.Player1);
					if(temp.isExtraTurn()) {//Check if that move caused an extra turn
						extraT--;
					}
					if(temp.isDidSteal()) {//Check if that move caused an extra turn
						steal--;
					}
					eval = minimax(temp.getSmallHoles().clone(), temp.getBigHoles().clone(), depth - 1, !maxPlayer,extraTurns+extraT,steals+steal,alpha,beta);
					minEval = Math.min(eval, minEval);
					beta=Math.min(beta, eval);
					if(beta<=alpha)
						break;
				}
			}
			return minEval;

		}

	}

	public boolean checkEndGame(int[] smallHoles) { // check if one row is stone free
		int sum1 = 0;
		int sum2 = 0;
		for (int i = 0; i < 12; i++) {
			if (i <= 5)
				sum1 += this.smallHoles[i];
			else
				sum2 += this.smallHoles[i];
		}
		if (sum1 == 0 || sum2 == 0)
			return true;
		return false;

	}

}
