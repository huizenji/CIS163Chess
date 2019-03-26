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

		//Test against movements larger than 1 space
		if(Math.abs(move.toRow - move.fromRow) > 1 ||
		Math.abs(move.toColumn - move.fromColumn) > 1)
			valid = false;

		//Test for castle
		//If the king has not yet moved...
		if(!super.isMoved())
			//Test to make sure King is not leaving base row
			if(move.toRow - move.fromRow == 0)
				//Queen's side castle
					if(move.toColumn == 2 &&
					//... and the rook has not moved
					!board[move.toRow][0].isMoved()){
						valid = true;

						//check for blocking element
						for(int i = move.fromColumn - 1;
							i >= move.toColumn - 1;	i--){
							//if there is a piece in the way, not valid
							if(board[move.toRow][i] != null)
								valid = false;
						}

					}
				//King's side castle
					else if(move.toColumn == 6 &&
					//... and the rook has not moved
					!board[move.fromRow][7].isMoved()){
						valid = true;

						//check for blocking element
						for(int i = move.fromColumn + 1;
							i < move.toColumn; i++){
							//if there is a piece in the way, not valid
							if(board[move.toRow][i] != null)
								valid = false;
						}
					}

	//check to see if the other spot has a piece for the same player
		if (super.sameTeam(move, board))
			valid = false;

		//if it's a valid move, set the piece to moved
		if(valid)
			super.setMoved(true);

		return valid;
	}
}
