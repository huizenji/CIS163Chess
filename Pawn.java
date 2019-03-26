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
		
	//check to see if the other spot has a piece for the same player
		if (super.sameTeam(move, board))
			valid = false;

		//Place limitations on white pawns
		if (super.player() == Player.WHITE) {
			/**check for diagonal movement. if it exists and there is no
			 * enemy player, invalid move this is for attacking other
			 * pieces **/
			
			//is this a valid diagonal
			if (Math.abs(move.toColumn - move.fromColumn) == 1) {
				//check for enemy player
				if (board[move.toRow][move.toColumn] != null) {    
					if (board[move.toRow][move.toColumn].player()
							== Player.BLACK) {
						//if an enemy player exists, it's valid
						valid = true;
					}
				} else
					//if nobody is on the spot, invalid move
					valid = false;
				
				//no other diagonal allowed	
			} else if (move.toColumn != move.fromColumn)    
				valid = false;

		//check for a piece of any kind in front and prevent movement
			if (move.fromRow - 2 >= 0)
				if ((board[move.fromRow - 1][move.toColumn] != null ||
						board[move.fromRow - 2][move.toColumn] != null)
						&& move.toColumn == move.fromColumn)
					valid = false;

			/**normal movements**/
			//no movement beyond 2 spaces allowed
			if (move.fromRow - move.toRow > 2)
				valid = false;

			//if the pawn has moved, only allow 1 space
			if (super.isMoved() && move.fromRow - move.toRow != 1)
				valid = false;
		}

		//Place limitations on black pawns
		if (super.player() == Player.BLACK) {
			/**check for diagonal movement. if it exists and there is no
			 * enemy player, invalid move this is for attacking other
			 * pieces **/
			
			//is this a valid diagonal
			if ((move.toColumn - move.fromColumn) == 1 ||
					(move.toColumn - move.fromColumn) == -1) {
				//check for enemy player
				if (board[move.toRow][move.toColumn] != null) {    
					if (board[move.toRow][move.toColumn].player()
							== Player.WHITE) {
						//if an enemy player exists, it's valid
						valid = true;            
					}
				} else
					//if nobody is on the spot, invalid move
					valid = false;
				//no other diagonal allowed	
			} else if (move.toColumn != move.fromColumn)    
				valid = false;

			//check for a piece of any kind in front and prevent movement
			if (move.fromRow + 2 <= 7)
				if ((board[move.fromRow + 1][move.toColumn] != null ||
						board[move.fromRow + 2][move.toColumn] != null)
						&& move.toColumn == move.fromColumn)
					valid = false;

			//normal movements
			//no movement beyond 2 spaces allowed
			if (move.fromRow - move.toRow < -2)
				valid = false;

			//if the pawn has moved, only allow 1 space
			if (super.isMoved() && move.fromRow - move.toRow != -1)
				valid = false;
		}

		//if it's a valid move, set the piece to moved
		if (valid)
			super.setMoved(true);

		return valid;
	}
}
