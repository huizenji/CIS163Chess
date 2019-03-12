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

		//check for blocking elements in path to target location
		if(move.toRow > move.fromRow){
			if(move.toColumn > move.fromColumn) {
				for(int i = 1; i < move.toRow - move.fromRow; i++)
					if(board[move.fromRow + i][move.fromColumn + i]
							!= null)
						valid = false;
			}

			if(move.toColumn < move.fromColumn){
				for(int i = 1; i < move.toRow - move.fromRow; i++)
					if(board[move.fromRow + i][move.fromColumn - i]
							!= null)
						valid = false;
			}
		}
		else{
			if(move.toColumn > move.fromColumn) {
				for(int i = 1; i < move.toColumn - move.fromColumn; i++)
					if(board[move.fromRow - i][move.fromColumn + i]
							!= null)
						valid = false;
			}

			if(move.toColumn < move.fromColumn){
				for(int i = 1; i < move.fromColumn - move.toColumn; i++)
					if(board[move.fromRow - i][move.fromColumn - i]
							!= null)
						valid = false;
			}
		}

        // More code is needed
		return valid;
	}
}
