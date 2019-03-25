package Project3;

import java.util.ArrayList;

/**********************************************************************
 * A class that stores the board and rules of chess
 *
 * @author David Butz, Lauren Freeman, Jillian Huizenga
 * Date: 3/26/2019
 *********************************************************************/
public class ChessModel implements IChessModel {

    /** A two-dimensional array of IChessPiece **/
    private IChessPiece[][] board;

    /** A Player that stores the current player **/
    private Player player;

    /** A GUIcodes that stores the board status **/
    private GUIcodes status;

    /** An ArrayList of two-dimensional IChessPiece arrays **/
    private ArrayList<IChessPiece[][]> boards = new ArrayList<>();

    /** index value of the ArrayList for the saved board data **/
    private int moveIndex = 0;

    /******************************************************************
     * The default constructor: places the pieces, sets the Player,
     * sets the status, and saves the starting board
     *****************************************************************/
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

        status = GUIcodes.NoMessage;

        //Save the base board as first element in ArrayList
        saveMove(board);
    }

    /******************************************************************
     * Returns whether or not the game is over by checkmate
     * @return a boolean that indicates if the game is over or not
     *****************************************************************/
    public boolean isComplete() {

        if (inCheck(currentPlayer())) {
            int kingRow = 1;
            int kingCol = 1;
            int enemyRow = 1;
            int enemyCol = 1;

            //find king's location
            for (int row = 0; row < numRows(); row++)
                for (int col = 0; col < numColumns(); col++)
                    if (pieceAt(row, col) != null)
                        if (pieceAt(row, col).type().equals("King"))
                            if (pieceAt(row, col).player() ==
                                    currentPlayer()) {
                                kingRow = row;
                                kingCol = col;
                                break;
                            }

            //try to move King
            for (int row = 0; row < numRows(); row++) {
                for (int col = 0; col < numColumns(); col++) {
                    Move move = new Move(kingRow, kingCol, row, col);
                    if (pieceAt(row, col) != null)
                        if (pieceAt(kingRow, kingCol)
                                .isValidMove(move, board))
                            if (!stillInCheck(move))
                                return false;
                }
            }

            //find threatening piece
            for (int row = 0; row < numRows(); row++)
                for (int col = 0; col < numColumns(); col++) {
                    Move move = new Move(row, col, kingRow, kingCol);
                    if (pieceAt(row, col) != null)
                        if (pieceAt(row, col).player() !=
                                currentPlayer())
                            if (pieceAt(row, col)
                                    .isValidMove(move, board)) {
                                enemyCol = col;
                                enemyRow = row;
                            }
                }

            //try to take threatening piece
            for (int row = 0; row < numRows(); row++)
                for (int col = 0; col < numColumns(); col++) {
                    Move move = new Move(row, col, enemyRow, enemyCol);
                    if (pieceAt(row, col) != null)
                        if (pieceAt(row, col).isValidMove(move, board))
                            if (!stillInCheck(move))
                                return false;
                }

            //try to block threatening piece (move everywhere)
            for (int row = 0; row < numRows(); row++)
                for (int col = 0; col < numColumns(); col++)
                    for (int toRow = 0; toRow < numRows(); toRow++)
                        for (int toCol = 0; toCol < numColumns();
                             toCol++) {
                            Move move = new Move(row, col, toRow,
                                    toCol);
                            if (pieceAt(row, col) != null)
                                if (pieceAt(row, col).isValidMove
                                        (move, board))
                                    if (pieceAt(row, col).player()
                                            == currentPlayer())
                                        if (!stillInCheck(move))
                                            return false;
                        }
            status = GUIcodes.Checkmate;
            return true;
        }
        return false;
    }

    /******************************************************************
     * A method that returns whether or not a Player is still in check
     * after a temporary move (used in isComplete())
     * @param move a Move object
     * @return a boolean that indicates if a Player will remain in
     *          check
     *****************************************************************/
    public boolean stillInCheck(Move move) {
        move(move);
        if (!inCheck(currentPlayer())) {
            undo();
            return false;
        }
        undo();
        return true;
    }


    public boolean isValidMove(Move move) {
        boolean valid = false;

        Move kings = new Move(move.fromRow, 0, move.fromRow, 3);
        Move queens = new Move(move.fromRow, 0, move.fromRow, 3);

        if (stillInCheck(move))
            return false;

        if (board[move.fromRow][move.fromColumn] != null)
            if (board[move.fromRow][move.fromColumn].isValidMove(move, board))
                return true;

        return valid;
    }

    public void move(Move move) {

        //increment moveIndex for undo and redo
        if (moveIndex < boards.size() - 1)
            //must delete everything including current board to prevent
            //double record of current board.
            for (int i = 0; i < boards.size() - (moveIndex); i++) {
                //delete irrelevant moves
                deleteMove();
            }

        //Queen's side castle
        if (pieceAt(move.fromRow, move.fromColumn) != null)
            if (pieceAt(move.fromRow, move.fromColumn).type().equals("King")
                    && move.fromColumn == 4 &&
                    pieceAt(move.fromRow, 0) != null) {
                if (!pieceAt(move.fromRow, 0).isMoved()
                        && move.toColumn == 2) {
                    board[move.fromRow][3] = board[move.fromRow][0];//move rook
                    board[move.fromRow][0] = null;
                }
            }

        //King's side castle
        if (pieceAt(move.fromRow, move.fromColumn) != null)
            if (pieceAt(move.fromRow, move.fromColumn).type().equals("King")
                    && move.fromColumn == 4 &&
                    pieceAt(move.fromRow, 7) != null) {
                if (!pieceAt(move.fromRow, 7).isMoved()
                        && move.toColumn == 6) {
                    board[move.fromRow][5] = board[move.fromRow][7];//move rook
                    board[move.fromRow][7] = null;
                }
            }

        board[move.toRow][move.toColumn] = board[move.fromRow][move.fromColumn];
        board[move.fromRow][move.fromColumn] = null;

        //record current board
        saveMove(board);
        moveIndex++;
    }

    public boolean inCheck(Player p) {
        boolean valid = false;
        int toRow = 0;
        int toCol = 0;
        for (int row = 0; row < numRows(); row++)
            for (int col = 0; col < numColumns(); col++)
                if (board[row][col] != null && board[row][col].type()
                        .equals("King"))
                    if (pieceAt(row, col).player() == currentPlayer()){
                        toRow = row;
                        toCol = col;
                    }

        for (int row = 0; row < numRows(); row++)
            for (int col = 0; col < numColumns(); col++) {
                Move move = new Move(row, col, toRow, toCol);
                if (board[row][col] != null && board[row][col]
                        .isValidMove(move, board))
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

        //first try to get out of check (if in check)
        if (inCheck(Player.BLACK))
            getOutOfCheck();

        //try to put opponent in check
        if (!inCheck(Player.BLACK))
            attemptToPutIntoCheck();

        //move an endangered piece
        for (int allyRow = 0; allyRow < numRows(); allyRow++)
            for (int allyCol = 0; allyCol < numColumns(); allyCol++)
                if (pieceAt(allyRow, allyCol) != null)
                    if (pieceAt(allyRow, allyCol).player() == currentPlayer()) {
                        // can white move onto black?
                        if (inDanger(allyRow, allyCol))
                            attemptToRemoveFromDanger(allyRow, allyCol);
                    }

        //if no obvious threat or chance to check, move a piece
        moveAPiece();

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

    public void getOutOfCheck() {
        int kingRow = 1;
        int kingCol = 1;
        int enemyRow = 1;
        int enemyCol = 1;

        //find King
        for (int row = 0; row < numRows(); row++)
            for (int col = 0; col < numColumns(); col++)
                if (pieceAt(row, col) != null)
                    if (pieceAt(row, col).type().equals("King"))
                        if (pieceAt(row, col).player() == currentPlayer()) {
                            kingRow = row;
                            kingCol = col;
                            break;
                        }

        //try to move King
        for (int row = 0; row < numRows(); row++) {
            for (int col = 0; col < numColumns(); col++)
                if (currentPlayer() == Player.BLACK) {
                    Move move = new Move(kingRow, kingCol, row, col);
                    if (pieceAt(row, col) != null)
                        if (pieceAt(kingRow, kingCol).isValidMove(move, board))
                            if (!stillInCheck(move)) {
                                move(move);
                                setPlayer(Player.WHITE);
                            }
                }
        }

        //find threatening piece
        for (int row = 0; row < numRows(); row++)
            for (int col = 0; col < numColumns(); col++) {
                Move move = new Move(row, col, kingRow, kingCol);
                if (pieceAt(row, col) != null)
                    if (pieceAt(row, col).player() != currentPlayer())
                        if (pieceAt(row, col).isValidMove(move, board)) {
                            enemyCol = col;
                            enemyRow = row;
                            break;
                        }
            }

        //try to take threatening piece
        for (int row = 0; row < numRows(); row++)
            for (int col = 0; col < numColumns(); col++)
                if (currentPlayer() == Player.BLACK) {
                    Move move = new Move(row, col, enemyRow, enemyCol);
                    if (pieceAt(row, col) != null)
                        if (pieceAt(row, col).isValidMove(move, board))
                            if (!stillInCheck(move)) {
                                move(move);
                                setPlayer(Player.WHITE);
                            }
                }

        //try to block threatening piece (just move every piece everywhere)
        for (int row = 0; row < numRows(); row++)
            for (int col = 0; col < numColumns(); col++)
                for (int toRow = 0; toRow < numRows(); toRow++)
                    for (int toCol = 0; toCol < numColumns(); toCol++)
                        if (currentPlayer() == Player.BLACK) {
                            Move move = new Move(row, col, toRow, toCol);
                            if (pieceAt(row, col) != null)
                                if (pieceAt(row, col).isValidMove(move, board))
                                    if (pieceAt(row, col).player() == currentPlayer())
                                        if (!stillInCheck(move)) {
                                            move(move);
                                            setPlayer(Player.WHITE);
                                        }
                        }
    }

    public void attemptToPutIntoCheck() {

        // move every black piece everywhere
        for (int row = 0; row < numRows(); row++)
            for (int col = 0; col < numColumns(); col++)
                for (int toRow = 0; toRow < numRows(); toRow++)
                    for (int toCol = 0; toCol < numColumns(); toCol++)
                        if (currentPlayer() == Player.BLACK) {
                            Move move = new Move(row, col, toRow, toCol);
                            if (pieceAt(row, col) != null)
                                if (pieceAt(row, col).isValidMove(move, board))
                                    if (pieceAt(row, col).player() == Player.BLACK) {
                                        move(move);
                                        if (inCheck(Player.WHITE) || isComplete())
                                            setPlayer(Player.WHITE);
                                        else
                                            undo();
                                    }
                        }

    }

    public void attemptToRemoveFromDanger(int allyRow, int allyCol) {
        for (int moveRow = 0; moveRow < numRows(); moveRow++)
            for (int moveCol = 0; moveCol < numColumns(); moveCol++)
                if (currentPlayer() == Player.BLACK) {
                    Move newMove = new Move(allyRow, allyCol, moveRow, moveCol);
                    move(newMove);
                    if (inDanger(moveRow, moveCol))
                        undo();
                    else
                        setPlayer(Player.WHITE);
                }

    }


public void moveAPiece() {
        int dangerEnemyRow = 0;
        int dangerEnemyCol = 0;

        //are any opponent pieces in danger?
        for (int enemyRow = 0; enemyRow < numRows(); enemyRow++)
            for (int enemyCol = 0; enemyCol < numColumns(); enemyCol++)
                if (pieceAt(enemyRow, enemyCol) != null)
                    if (pieceAt(enemyRow, enemyCol).player() == player.WHITE)
                        if (inDanger(enemyRow, enemyCol)) {
                            for (int allyRow = 0; allyRow < numRows(); allyRow++)
                                for (int allyCol = 0; allyCol < numColumns(); allyCol++)
                                    if (pieceAt(allyRow, allyCol) != null)
                                        if (pieceAt(allyRow, allyCol).player() == player.BLACK) {
                                            Move move = new Move(allyRow, allyCol, enemyRow, enemyCol);
                                            if (isValidMove(move)) {
                                                move(move);
                                                //will taking their piece put any of our pieces in danger?
                                                if (inDanger(enemyRow, enemyCol))
                                                    undo();
                                                else
                                                    dangerEnemyRow = enemyRow;
                                                dangerEnemyCol = enemyCol;
                                                break;

                                            }
                                        }
                        }

        //take a risk if it's the only piece we can take
        for (int allyRow = 0; allyRow < numRows(); allyRow++)
            for (int allyCol = 0; allyCol < numColumns(); allyCol++)
                if (pieceAt(allyRow, allyCol) != null)
                    if (pieceAt(allyRow, allyCol).player() == player.WHITE) {
                        Move move = new Move(allyRow, allyCol, dangerEnemyRow, dangerEnemyCol);
                        if (isValidMove(move)) {
                            move(move);
                            break;
                        }
                    }

        //try to put a white piece in danger
        //find black piece to move
        for (int allyRow = 0; allyRow < numRows(); allyRow++)
            for (int allyCol = 0; allyCol < numColumns(); allyCol++)
                if (pieceAt(allyRow, allyCol) != null)
                    if (pieceAt(allyRow, allyCol).player() == player.BLACK) {
                        //find a space to move to
                        for (int toRow = 0; toRow < numRows(); toRow++)
                            for (int toCol = 0; toCol < numColumns(); toCol++) {
                                Move move = new Move(allyRow, allyCol, toRow, toCol);
                                if (isValidMove(move)) {
                                    //check if this move puts any enemy piece in danger
                                    for (int enemyRow = 0; enemyRow < numRows(); enemyRow++)
                                        for (int enemyCol = 0; enemyCol < numColumns(); enemyCol++)
                                            if (pieceAt(enemyRow, enemyCol) != null)
                                                if (pieceAt(enemyRow, enemyCol).player() == player.WHITE) {
                                                    //undo and continue moving pieces if enemy is not in danger
                                                    move(move);
                                                    if (!inDanger(enemyRow, enemyCol))
                                                        undo();
                                                    else
                                                        break;
                                                }
                                }
                            }

                    }
        //just move a piece somewhere (that doesn't put it in danger)
        for (int allyRow = 0; allyRow < numRows(); allyRow++)
            for (int allyCol = 0; allyCol < numColumns(); allyCol++)
                if (pieceAt(allyRow, allyCol) != null)
                    if (pieceAt(allyRow, allyCol).player() == player.BLACK) {
                        //find a space to move to
                        for (int toRow = 0; toRow < numRows(); toRow++)
                            for (int toCol = 0; toCol < numColumns(); toCol++) {
                                Move move = new Move(allyRow, allyCol, toRow, toCol);
                                if (isValidMove(move)) {
                                    move(move);
                                    if (inDanger(toRow, toCol))
                                        undo();
                                    else
                                        break;

                                }
                            }
                    }

    }

    public boolean inDanger(int pieceRow, int pieceCol) {
        for (int enemyRow = 0; enemyRow < numRows(); enemyRow++)
            for (int enemyCol = 0; enemyCol < numColumns(); enemyCol++)
                if (pieceAt(enemyRow, enemyCol) != null)
                    if (pieceAt(enemyRow, enemyCol).player() != pieceAt(pieceRow, pieceCol).player()) {
                        Move move = new Move(enemyRow, enemyCol, pieceRow, pieceCol);
                        if (isValidMove(move)) {
                            return true;
                        }
                    }
        return false;
    }

    /******************************************************************
     * Add a new move to the game history. This saves the current game
     * board onto the end of an ArrayList, allowing access to older
     * moves and enabling an undo function if desired.
     *
     * @param board import and save the current board
     */
    private void saveMove(IChessPiece[][] board) {
        //save the board to newboard
        IChessPiece[][] newboard = new IChessPiece[8][8];

        cloneBoard(newboard, board);
        boards.add(newboard);
    }

    /******************************************************************
     * Removes only the last element of the saved boards. This is
     * intended to be used on a loop to eliminate all recorded moves
     * after a backed up point: when a new move has been made, delete
     * the old game and continue from the new point
     */
    private void deleteMove() {
        if (!(boards.size() - 1 <= 0))
            boards.remove(boards.size() - 1);
    }

    public void undo() {
        //temporarily, do nothing if no moves are made
        if (boards.size() == 1) ;
        else {
            //go to previous move
            moveIndex--;

            //load previous move
            IChessPiece[][] temp = new IChessPiece[8][8];
            cloneBoard(temp, boards.get(moveIndex));

            board = temp;
        }
    }

    public void redo() {
        //temporarily do nothing if request is higher than last move
        if (boards.size() <= moveIndex) ;
        else {
            //go to next move
            moveIndex++;

            //load next move
            IChessPiece[][] temp = new IChessPiece[8][8];
            cloneBoard(temp, boards.get(moveIndex));

            board = temp;
        }
    }

    private void cloneBoard(IChessPiece[][] copyTo, IChessPiece[][] copyFrom) {

        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++) {
                if (copyFrom[r][c] != null) {
                    if (copyFrom[r][c].type().equals("Rook")) {
                        copyTo[r][c] = new Rook(copyFrom[r][c].player());
                        copyTo[r][c].setMoved(copyFrom[r][c].isMoved());
                    }
                    if (copyFrom[r][c].type().equals("King")) {
                        copyTo[r][c] = new King(copyFrom[r][c].player());
                        copyTo[r][c].setMoved(copyFrom[r][c].isMoved());
                    }
                    if (copyFrom[r][c].type().equals("Pawn")) {
                        copyTo[r][c] = new Pawn(copyFrom[r][c].player());
                        copyTo[r][c].setMoved(copyFrom[r][c].isMoved());
                    } else
                        copyTo[r][c] = copyFrom[r][c];
                }
            }
    }
    
    public GUIcodes getStatus() {
        return status;
    }

    public void setStatus(GUIcodes status) {
        this.status = status;
    }

    public void setPlayer(Player p) {
        player = p;
    }
}
