package Project3;

import java.util.ArrayList;

public class ChessModel implements IChessModel {
	private IChessPiece[][] board;
	private Player player;

		private ArrayList<IChessPiece[][]> boards = new ArrayList<>();

	// declare other instance variables as needed
	//moveIndex controls the index value of the ArrayList for the saved
	//board data.
	private int moveIndex = 0;

	public ChessModel() {
		board = new IChessPiece[8][8];
		player = Player.WHITE;

		board[7][0] = new Rook(Player.WHITE);
		board[7][1] = new Knight(Player.WHITE);
		board[7][2] = new Bishop(Player.WHITE);
		board[7][3] = new Queen(Player.WHITE);
		board[7][4] = new King(Player.WHITE);
		board[7][5] = new Bishop(Player.WHITE);
		board[7][6] = new Knight(Player.WHITE);
		board[7][7] = new Rook(Player.WHITE);

		for (int i = 0; i < 8; i++) {
			board[6][i] = new Pawn(Player.WHITE);
		}

		board[0][0] = new Rook(Player.BLACK);
		board[0][1] = new Knight(Player.BLACK);
		board[0][2] = new Bishop(Player.BLACK);
		board[0][3] = new Queen(Player.BLACK);
		board[0][4] = new King(Player.BLACK);
		board[0][5] = new Bishop(Player.BLACK);
		board[0][6] = new Knight(Player.BLACK);
		board[0][7] = new Rook(Player.BLACK);

		for (int i = 0; i < 8; i++) {
			board[1][i] = new Pawn(Player.BLACK);
		}
	}

	public boolean isComplete() {
		boolean valid = false;
		return valid;
	}

	public boolean isValidMove(Move move) {
		boolean valid = false;

		Move kings = new Move(move.fromRow, 0, move.fromRow, 3);
		Move queens = new Move(move.fromRow, 0, move.fromRow, 3);

		if (board[move.fromRow][move.fromColumn] != null)
			if (board[move.fromRow][move.fromColumn].isValidMove(move, board))
				return true;

		return valid;
	}

	public void move(Move move) {
		
//increment moveIndex for undo and redo
		if(moveIndex < boards.size() - 1)
			//must delete everything including current board to prevent
			//double record of current board.
			for(int i = 0; i < boards.size() - (2 + moveIndex); i++) {
				//delete irrelevant moves
				deleteMove();
			}
		moveIndex++;

		//record current board
		saveMove(board);

		//Queen's side castle
		if(pieceAt(move.fromRow, move.fromColumn).type().equals("King")
				&& move.fromColumn == 4 &&
				pieceAt(move.fromRow, 0) != null){
				if(!pieceAt(move.fromRow, 0).isMoved()
				&& move.toColumn == 2){
					board[move.fromRow][3] = board[move.fromRow][0];//move rook
					board[move.fromRow][0] = null;
		}
	}

		//King's side castle
		if(pieceAt(move.fromRow, move.fromColumn).type().equals("King")
				&& move.fromColumn == 4 &&
				pieceAt(move.fromRow, 7)!= null){
				if(!pieceAt(move.fromRow, 7).isMoved()
				&& move.toColumn == 6) {
					board[move.fromRow][5] = board[move.fromRow][7];//move rook
					board[move.fromRow][7] = null;
				}
		}

		board[move.toRow][move.toColumn] = board[move.fromRow][move.fromColumn];
		board[move.fromRow][move.fromColumn] = null;
	}

	public boolean inCheck(Player p) {
		boolean valid = false;
		int toRow = 0;
		int toCol = 0;
		for (int row = 0; row < numRows(); row++)
			for (int col = 0; col < numColumns(); col++)
				if (board[row][col] != null && board[row][col].type()
						.equals("King"))
					if (pieceAt(row, col).player() == currentPlayer()) {
						toRow = row;
						toCol = col;
					}

		for (int row = 0; row < numRows(); row++)
			for (int col = 0; col < numColumns(); col++) {
				Move move = new Move(row, col, toRow, toCol);
				if (board[row][col] != null && board[row][col].isValidMove(move, board))
					valid = true;
			}

		return valid;
	}


	public Player currentPlayer() {
		return player;
	}

	public int numRows() {
		return 8;
	}

	public int numColumns() {
		return 8;
	}

	public IChessPiece pieceAt(int row, int column) {
		return board[row][column];
	}

	public void setNextPlayer() {
		player = player.next();
	}

	public void setPiece(int row, int column, IChessPiece piece) {
		board[row][column] = piece;
	}

	public void AI() {
		/*
		 * Write a simple AI set of rules in the following order.
		 * a. Check to see if you are in check.
		 * 		i. If so, get out of check by moving the king or placing a piece to block the check
		 *
		 * b. Attempt to put opponent into check (or checkmate).
		 * 		i. Attempt to put opponent into check without losing your piece
		 *		ii. Perhaps you have won the game.
		 *
		 *c. Determine if any of your pieces are in danger,
		 *		i. Move them if you can.
		 *		ii. Attempt to protect that piece.
		 *
		 *d. Move a piece (pawns first) forward toward opponent king
		 *		i. check to see if that piece is in danger of being removed, if so, move a different piece.
		 */

	}
	/******************************************************************
	 * Add a new move to the game history. This saves the current game
	 * board onto the end of an ArrayList, allowing access to older
	 * moves and enabling an undo function if desired.
	 *
	 * @param board import and save the current board
	 */
	private void saveMove(IChessPiece[][] board){
		//save the board to newboard
		IChessPiece[][] newboard = new IChessPiece[8][8];
			for(int r = 0; r < 8; r++)
				for(int c = 0; c < 8; c++) {
					newboard[r][c] = board[r][c];
					if(board[r][c]!= null)
						newboard[r][c].setMoved(board[r][c].isMoved());
				}

			boards.add(newboard);
	}

	/******************************************************************
	 * Removes only the last element of the saved boards. This is
	 * intended to be used on a loop to eliminate all recorded moves
	 * after a backed up point: when a new move has been made, delete
	 * the old game and continue from the new point
	 */
	private void deleteMove(){
		if(!(boards.size()-1 <= 0))
		boards.remove(boards.size()-1);
	}
}
