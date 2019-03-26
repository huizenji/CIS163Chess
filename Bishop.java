package Project3;

/**********************************************************************
 * A class that stores the information in, and the movement restriction
 * of a bishop.
 *
 * @author David Butz, Lauren Freeman, Jillian Huizenga
 * Date: 3/26/2019
 *********************************************************************/
public class Bishop extends ChessPiece {

	public Bishop(Player player) {
		super(player);
	}

	public String type() {
		return "Bishop";
	}

	public boolean isValidMove(Move move, IChessPiece[][] board) {

		boolean valid = false;

		//Check to ensure movement is on pure diagonal
		if(Math.abs(move.toRow - move.fromRow) ==
				Math.abs(move.toColumn-move.fromColumn))
			valid = true;

	//check to see if the other spot has a piece for the same player
		if (super.sameTeam(move, board))
			valid = false;

		//check for blocking elements in path to target location
		//Check for movement from left toward right
		if (move.toRow > move.fromRow) {
			//check for movement in the upper diagonal
			if (move.toColumn > move.fromColumn) {
				//check every space for a piece between start
				//and stop locations
				for (int i = 1; i < move.toRow - move.fromRow; i++)
					if (move.fromRow + i<=7 && move.fromColumn + i<=7)
						if (board[move.fromRow + i][move.fromColumn +i]
								!= null)
							valid = false;
			}

			//check for movement in the lower diagonal
			if (move.toColumn < move.fromColumn) {
				//check every space for a piece between start
				//and stop locations
				for (int i = 1; i < move.toRow - move.fromRow; i++)
					if (move.fromRow + i <=7 && move.fromColumn - i>=0)
						if (board[move.fromRow + i][move.fromColumn -i]
								!= null)
							valid = false;
			}
		// Check movement from right to left
		} else {
			//check for blocking elements in path to target location
			//Check for movement from left toward right
			if (move.toColumn > move.fromColumn) {
				//check every space for a piece between start
				//and stop locations
				for (int i = 1; i < move.toColumn-move.fromColumn; i++)
					if (move.fromRow - i >=0 && move.fromColumn + i<=7)
						if (board[move.fromRow - i][move.fromColumn +i]
								!= null)
							valid = false;
			}

			//check for blocking elements in path to target location
			//Check for movement from right toward left
			if (move.toColumn < move.fromColumn) {
				//check every space for a piece between start
				//and stop locations
				for (int i =1; i< move.fromColumn - move.toColumn; i++)
					if (move.fromRow - i >=0 && move.fromColumn - i>=0)
						if (board[move.fromRow - i][move.fromColumn -i]
								!= null)
							valid = false;
			}
		}
		return valid;
	}
}
