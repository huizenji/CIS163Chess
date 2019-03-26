package Project3;

/**********************************************************************
 * A class that stores the information in, and the movement restriction
 * of a knight.
 *
 * @author David Butz, Lauren Freeman, Jillian Huizenga
 * Date: 3/26/2019
 *********************************************************************/
public class Knight extends ChessPiece {

	public Knight(Player player) {
		super(player);
	}

	public String type() {
		return "Knight";
	}

	public boolean isValidMove(Move move, IChessPiece[][] board){
		boolean valid = true;

		//valid move condition for knight: 2 one way, 1 perpindicular
		// Test for movement within row within range
		if(!((Math.abs(move.toRow  - move.fromRow) == 1 ||
				Math.abs(move.toRow  - move.fromRow) == 2)&&

				//Test for movement within column range
				(Math.abs(move.toColumn  - move.fromColumn) == 1 ||
				Math.abs(move.toColumn  - move.fromColumn) == 2) &&

				//Test for ratio of row/column movements
				(Math.abs((move.toColumn - move.fromColumn) -
						(move.toRow - move.fromRow)) == 1 ||
				Math.abs((move.toColumn - move.fromColumn) -
						(move.toRow - move.fromRow)) == 3)))
			valid = false;

	//check to see if the other spot has a piece for the same player
		if (super.sameTeam(move, board))
			valid = false;

		return valid;
		
	}

}
