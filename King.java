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

		//Test for castle
		if(move.fromColumn == 4 && !super.isMoved())
			if(move.toRow - move.fromRow == 0)
				//Queen's side castle
					if(move.toColumn == 2 &&
					!board[move.toRow][0].isMoved()){
						valid = true;

						//check for blocking element
						for(int i = move.fromColumn - 1; i >= move.toColumn - 1;
							i--){
							if(board[move.toRow][i] != null)
								valid = false;
						}

					}
				//King's side castle
					else if(move.toColumn == 6 &&
					!board[move.fromRow][7].isMoved()){
						valid = true;

						//check for blocking element
						for(int i = move.fromColumn + 1; i < move.toColumn;
						i++){
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
