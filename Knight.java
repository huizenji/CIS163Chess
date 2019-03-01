package Project3;

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
		//Test for valid movement then invert value to set valid = false
		// Test for movement within row within range
		if(!((move.toRow  - move.fromRow == 1 ||
				move.toRow - move.fromRow == -1||
				move.toRow  - move.fromRow == 2 ||
				move.toRow  - move.fromRow == -2)&&

				//Test for movement within column range
				(move.toColumn  - move.fromColumn == 1 ||
				move.toColumn - move.fromColumn == -1||
				move.toColumn  - move.fromColumn == 2 ||
				move.toColumn  - move.fromColumn == -2) &&

				//Test for ratio of row/column movements
				((move.toColumn - move.fromColumn) -
						(move.toRow - move.fromRow) == 1 ||
				(move.toColumn - move.fromColumn) -
						(move.toRow - move.fromRow) == -1||
				(move.toColumn - move.fromColumn) -
						(move.toRow - move.fromRow) == 3 ||
				(move.toColumn - move.fromColumn) -
						(move.toRow - move.fromRow) == -3)))
			valid = false;

		//check to see if the other spot has a piece for the same player
		if (super.sameTeam(move, board))
			valid = false;

        // More code is needed
		return valid;
		
	}

}
