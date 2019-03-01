package Project3;

public class King extends ChessPiece {

	public King(Player player) {
		super(player);
	}

	public String type() {
		return "King";
	}
	
	public boolean isValidMove(Move move, IChessPiece[][] board) {
		boolean valid = true;
        // More code is needed

		if(move.toRow - move.fromRow > 1 ||
		move.toRow - move.fromRow < -1||
		move.toColumn - move.fromColumn > 1 ||
		move.toColumn - move.fromColumn < -1)
			valid = false;
		
		//check to see if the other spot has a piece for the same player
		if (super.sameTeam(move, board))
			valid = false;

		//allow castle
		if(move.fromColumn == 4)
			if(move.toRow - move.fromRow == 0 &&
					(move.toColumn == 2 || move.toColumn == 6))
			valid = true;

		return valid;
	}
}
