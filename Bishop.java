package Project3;

public class Bishop extends ChessPiece {

	public Bishop(Player player) {
		super(player);
	}

	public String type() {
		return "Bishop";
	}
	
	public boolean isValidMove(Move move, IChessPiece[][] board) {

		if(move.toRow - move.fromRow == move.toColumn-move.fromColumn||
		 move.toRow-move.fromRow==(move.toColumn-move.fromColumn)*(-1))
		return true;
        // More code is needed
		return false;
		
	}
}
