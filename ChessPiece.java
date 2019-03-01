package Project3;

public abstract class ChessPiece implements IChessPiece {

    private Player owner;

    protected ChessPiece(Player player) {
        this.owner = player;
    }

    public abstract String type();

    public Player player() {
        return owner;
    }

    public boolean isValidMove(Move move, IChessPiece[][] board) {
        boolean valid = false;

        //  THIS IS A START... More coding needed

        if (((move.fromRow == move.toRow) && (move.fromColumn == move.toColumn)) == false)
            return valid;

        return false;
    }
    
    public boolean sameTeam(Move move, IChessPiece[][] board){

		//check to see if the other spot has a piece
		if (board[move.toRow][move.toColumn] != null)
			//if it has a piece, is it the same team
			if ((board[move.toRow][move.toColumn].player() ==
				board[move.fromRow][move.fromColumn].player()))
				return true;
			;

		return false;
	}
}
