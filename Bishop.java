package Project3;

public class Bishop extends ChessPiece {

	public Bishop(Player player) {
		super(player);
	}

	public String type() {
		return "Bishop";
	}
	
	public boolean isValidMove(Move move, IChessPiece[][] board) {

		boolean valid = false;
		
		if(move.toRow - move.fromRow == move.toColumn-move.fromColumn||
		 move.toRow-move.fromRow==(move.toColumn-move.fromColumn)*(-1))
		valid = true;

		//check to see if the other spot has a piece for the same player
		if (super.sameTeam(move, board))
			valid = false;

        // More code is needed
		return valid;
	}
}
