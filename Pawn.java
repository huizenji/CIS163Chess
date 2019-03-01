package Project3;

public class Pawn extends ChessPiece {

    public Pawn(Player player) {
        super(player);
    }

    public String type() {
        return "Pawn";
    }

    // determines if the move is valid for a pawn piece
    public boolean isValidMove(Move move, IChessPiece[][] board) {

        boolean valid = true;
        // More code is needed
        
        //check to see if the other spot has a piece for the same player
		if (super.sameTeam(move, board))
			valid = false;
        
        return valid;
    }
}
