package Project3;

public class Rook extends ChessPiece {

	public Rook(Player player) {
		
		super(player);
		
	}

	public String type() {
		
		return "Rook";
		
	}
	
	// determines if the move is valid for a rook piece
	public boolean isValidMove(Move move, IChessPiece[][] board) {


		boolean valid = true;

		if(!(move.toRow - move.fromRow == 0 ||
		move.toColumn - move.fromColumn == 0))
        // More code is needed
			valid = false;

		//Check the axis for blocking elements
		if(move.toRow > move.fromRow){
			for(int i = move.fromRow + 1; i < move.toRow; i++)
				if(board[i][move.fromColumn] != null)
					valid = false;
		}

		if(move.toColumn > move.fromColumn){
			for(int i = move.fromColumn + 1; i < move.toColumn; i++)
				if(board[move.toRow][i] != null)
					valid = false;
		}

		if(move.toRow < move.fromRow){
			for(int i = move.fromRow - 1; i > move.toRow; i--)
				if(board[i][move.fromColumn] != null)
					valid = false;
		}

		if(move.toColumn < move.fromColumn){
			for(int i = move.fromColumn - 1; i > move.toColumn; i--)
				if(board[move.toRow][i] != null)
					valid = false;
		}

		//castling concerns
		if(board[move.fromRow][move.fromColumn].type().equals("King")
		&& super.isMoved())
			valid = false;



		//check to see if the other spot has a piece for the same player
		if (super.sameTeam(move, board))
			valid = false;

		//if it's a valid move, set the piece to moved
		if(valid)
			super.setMoved(true);

        return valid;


		
	}
	
}
