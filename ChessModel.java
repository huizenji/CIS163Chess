package Project3;

import java.util.ArrayList;

/**********************************************************************
 * A class that stores the board and rules of chess.
 *
 * @author David Butz, Lauren Freeman, Jillian Huizenga
 * Date: 3/26/2019
 *********************************************************************/
public class ChessModel implements IChessModel {

    /** A two-dimensional array of IChessPiece **/
    private IChessPiece[][] board;

    /** A Player that stores the current player **/
    private Player player;

    /** A GUIcodes object that stores the board status **/
    private GUIcodes status;

    /** An ArrayList of two-dimensional IChessPiece arrays **/
    private ArrayList<IChessPiece[][]> boards = new ArrayList<>();

    /** index value of the ArrayList for the saved board data **/
    private int moveIndex = 0;
    
    /** the total number of rows on the chess board **/
    private final int NUM_ROWS = 8;

    /** the total number of columns on the chess board **/
    private final int NUM_COLS = 8;

    /******************************************************************
     * The default constructor: places the pieces, sets the Player,
     * sets the status, and saves the starting board.
     *****************************************************************/
    public ChessModel() {
        board = new IChessPiece[8][8];
        player = Player.WHITE;

        //places all of the white pieces
        board[7][0] = new Rook(Player.WHITE);
        board[7][1] = new Knight(Player.WHITE);
        board[7][2] = new Bishop(Player.WHITE);
        board[7][3] = new Queen(Player.WHITE);
        board[7][4] = new King(Player.WHITE);
        board[7][5] = new Bishop(Player.WHITE);
        board[7][6] = new Knight(Player.WHITE);
        board[7][7] = new Rook(Player.WHITE);

        //places all of the white pawns
        for (int i = 0; i < 8; i++) {
            board[6][i] = new Pawn(Player.WHITE);
        }

        //places all of the black pieces
        board[0][0] = new Rook(Player.BLACK);
        board[0][1] = new Knight(Player.BLACK);
        board[0][2] = new Bishop(Player.BLACK);
        board[0][3] = new Queen(Player.BLACK);
        board[0][4] = new King(Player.BLACK);
        board[0][5] = new Bishop(Player.BLACK);
        board[0][6] = new Knight(Player.BLACK);
        board[0][7] = new Rook(Player.BLACK);

        //places all of the black pawns
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn(Player.BLACK);
        }

        //the game's status
        status = GUIcodes.NoMessage;

        //Save the base board as first element in ArrayList
        saveMove(board);
    }

    /******************************************************************
     * Returns whether or not the game is over by checkmate.
     *
     * @return {@code true} if complete, {@code false} otherwise.
     *****************************************************************/
    public boolean isComplete() {

        if (inCheck(currentPlayer())) {
            int kingRow = 1;
            int kingCol = 1;
            int enemyRow = 1;
            int enemyCol = 1;

            //find king's location
            for (int row = 0; row < NUM_ROWS; row++)
                for (int col = 0; col < NUM_COLS; col++)
                    if (pieceAt(row, col) != null)
                        if (pieceAt(row, col).type().equals("King"))
                            if (pieceAt(row, col).player() ==
                                    currentPlayer()) {
                                kingRow = row;
                                kingCol = col;
                                break;
                            }

            //try to move King
            for (int row = 0; row < NUM_ROWS; row++) {
                for (int col = 0; col < NUM_COLS; col++) {
                    Move move = new Move(kingRow, kingCol, row, col);
                    if (pieceAt(row, col) != null)
                        if (pieceAt(kingRow, kingCol)
                                .isValidMove(move, board))
                            if (!stillInCheck(move))
                                return false;
                }
            }

            //find threatening piece
            for (int row = 0; row < NUM_ROWS; row++)
                for (int col = 0; col < NUM_COLS; col++) {
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
            for (int row = 0; row < NUM_ROWS; row++)
                for (int col = 0; col < NUM_COLS; col++) {
                    Move move = new Move(row, col, enemyRow, enemyCol);
                    if (pieceAt(row, col) != null)
                        if (pieceAt(row, col).isValidMove(move, board))
                            if (!stillInCheck(move))
                                return false;
                }

            //try to block threatening piece (move everywhere)
            for (int row = 0; row < NUM_ROWS; row++)
                for (int col = 0; col < NUM_COLS; col++)
                    for (int toRow = 0; toRow < NUM_ROWS; toRow++)
                        for (int toCol = 0; toCol < NUM_COLS;
                             toCol++) {
                            Move move = new Move(row, col, toRow,
                                    toCol);
                            if (pieceAt(row, col) != null)
                                if (pieceAt(row, col).isValidMove
                                        (move, board))
                                    if (pieceAt(row, col).player() ==
                                            currentPlayer())
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
     * after a temporary move (used in isComplete()).
     *
     * @param move the move in question
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

    /******************************************************************
     * Returns whether the piece at location {@code [move.fromRow,
     * move.fromColumn]} is allowed to move to location
     * {@code [move.fromRow, move.fromColumn]}.
     *
     * @param move a {@link Project3.Move} object describing the
     *                move to be made.
     * @return {@code true} if the proposed move is valid,
     * 			{@code false} otherwise.
     * @throws IndexOutOfBoundsException if either {@code
     * 			[move.fromRow, move.fromColumn]} or {@code [move.toRow,
     *           move.toColumn]} don't represent valid locations on the
     *           board.
     *****************************************************************/
    public boolean isValidMove(Move move) {
        boolean valid = false;

        //checks if the move uses valid board parameters
        if (move.fromRow < 0 || move.fromColumn < 0 ||
                move.toColumn < 0 || move.toRow < 0 ||
                move.fromRow > NUM_ROWS || move.toRow > NUM_ROWS ||
                move.fromColumn > NUM_COLS || move.toColumn > NUM_COLS) {
            //if move is invalid, throw exception
            throw new IndexOutOfBoundsException();
        }

        //don't allow moves that would put the player in check
        if (stillInCheck(move))
            return false;

        //check if the pieces are able to move this way
        if (board[move.fromRow][move.fromColumn] != null)
            if (board[move.fromRow][move.fromColumn]
                    .isValidMove(move, board))
                return true;

        return valid;
    }

    /******************************************************************
     * Moves the piece from location {@code [move.fromRow,
     * move.fromColumn]} to location {@code [move.fromRow,
     * move.fromColumn]}.  It updates the counters for undo and redo
     * accordingly, allows castles if the parameters are met, and
     * saves the move for the sake of undo and redo.
     *
     * @param move a {@link Project3.Move} object describing the move
     *                to be made.
     * @throws IndexOutOfBoundsException if either {@code
     * 			   [move.fromRow, move.fromColumn]} or {@code
     * 			   [move.toRow, move.toColumn]} don't represent valid
     * 			   locations on the board.
     *****************************************************************/
    public void move(Move move) {

        //checks if the move uses valid board parameters
        if (move.fromRow < 0 || move.fromColumn < 0 ||
                move.toColumn < 0 || move.toRow < 0 ||
                move.fromRow > NUM_ROWS || move.toRow > NUM_ROWS ||
                move.fromColumn > NUM_COLS || move.toColumn > NUM_COLS) {
            //if move is invalid, throw exception
            throw new IndexOutOfBoundsException();
        }

        //increment moveIndex for undo and redo
        if (moveIndex < boards.size() - 1)
            /**must delete everything including current board to
             prevent double record of current board.**/
            for (int i = 0; i < boards.size() - (moveIndex); i++) {
                //delete irrelevant moves
                deleteMove();
            }

        //Queen's side castle
        if (pieceAt(move.fromRow, move.fromColumn) != null)
            if (pieceAt(move.fromRow, move.fromColumn).type()
                    .equals("King") &&
                    move.fromColumn == 4 &&
                    pieceAt(move.fromRow, 0) != null) {
                if (!pieceAt(move.fromRow, 0).isMoved() &&
                        move.toColumn == 2) {
                    //move rook
                    board[move.fromRow][3] = board[move.fromRow][0];
                    board[move.fromRow][0] = null;
                }
            }

        //King's side castle
        if (pieceAt(move.fromRow, move.fromColumn) != null)
            if (pieceAt(move.fromRow, move.fromColumn).type()
                    .equals("King") &&
                    move.fromColumn == 4 &&
                    pieceAt(move.fromRow, 7) != null) {
                if (!pieceAt(move.fromRow, 7).isMoved() &&
                        move.toColumn == 6) {
                    //move rook
                    board[move.fromRow][5] = board[move.fromRow][7];
                    board[move.fromRow][7] = null;
                }
            }

        //actually move the pieces
        board[move.toRow][move.toColumn]
                = board[move.fromRow][move.fromColumn];
        board[move.fromRow][move.fromColumn] = null;

        //record current board
        saveMove(board);
        moveIndex++;
    }
    /******************************************************************
     * Report whether the current player p is in check.
     * @param  p {@link Project3.Move} the Player being checked
     * @return {@code true} if the current player is in check,
     * 			{@code false} otherwise.
     *****************************************************************/
    public boolean inCheck(Player p) {
        boolean valid = false;
        int toRow = 0;
        int toCol = 0;
        
        //locate the location of the king
        for (int row = 0; row < NUM_ROWS; row++)
            for (int col = 0; col < NUM_COLS; col++)
                if (board[row][col] != null && board[row][col].type()
                        .equals("King"))
                    if (pieceAt(row, col).player() == p){
                        toRow = row;
                        toCol = col;
                    }

        //check if any enemy piece is capable of moving to the king
        for (int row = 0; row < NUM_ROWS; row++)
            for (int col = 0; col < NUM_COLS; col++) {
                Move move = new Move(row, col, toRow, toCol);
                if (board[row][col] != null && board[row][col]
                        .isValidMove(move, board))
                    valid = true;
            }

        return valid;
    }

    /******************************************************************
     * A helper method that returns the current player.
     *
     * @return player the player whose turn it currently is
     *****************************************************************/
    public Player currentPlayer() {
        return player;
    }

    /******************************************************************
     * A method that returns what type of piece is at the given row
     * and column coordinates.
     *
     * @param row the row of the piece in question
     *        col the column of the piece in question
     * @return board[row][column] the type of piece on that part of
     *         the board
     *****************************************************************/
    public IChessPiece pieceAt(int row, int column) {
        return board[row][column];
    }

    /******************************************************************
     * A method that passes the turn to the next player.
     *
     *****************************************************************/
    public void setNextPlayer() {
        player = player.next();
    }

    /******************************************************************
     * A method that sets a specific piece to a specific space on the
     * board.
     *
     * @param row the row of the desired space
     *        column the column of the desired space
     *        piece the IChessPiece to be moved to the location
     *****************************************************************/
    public void setPiece(int row, int column, IChessPiece piece) {
        board[row][column] = piece;
    }

    /******************************************************************
     * A method that moves the Black player's pieces. It first tries
     * to remove itself from check, then makes moves to immediately
     * put its opponent in check, then makes an effort to save any
     * endangered pieces, and if there's nothing else to do, it'll
     * just move a random piece as long as the move doesn't put
     * the piece in danger.
     *
     *****************************************************************/
    public void AI() {
        //first try to get out of check (if in check)
        if (inCheck(Player.BLACK))
            getOutOfCheck();

        //try to put opponent in check
        if (!inCheck(Player.BLACK))
            attemptToPutIntoCheck();

        //move an endangered piece
        for (int allyRow = 0; allyRow < NUM_ROWS; allyRow++)
            for (int allyCol = 0; allyCol < NUM_COLS; allyCol++)
                if (pieceAt(allyRow, allyCol) != null)
                    if (pieceAt(allyRow, allyCol).player()
                            == currentPlayer()) {
                        // can white move onto black?
                        if (inDanger(allyRow, allyCol))
                            attemptToRemoveFromDanger
                                    (allyRow, allyCol);
                    }

        //if no obvious threat or chance to check, move a piece
        moveAPiece();

    }

    /******************************************************************
     * A method for the AI that attempts to remove the Black player
     * from check.
     *
     *****************************************************************/
    public void getOutOfCheck() {
        //indexes for the king's location
        int kingRow = 1;
        int kingCol = 1;

        //find King
        for (int row = 0; row < NUM_ROWS; row++)
            for (int col = 0; col < NUM_COLS; col++)
                if (pieceAt(row, col) != null)
                    if (pieceAt(row, col).type().equals("King"))
                        if (pieceAt(row, col).player() ==
                                currentPlayer()) {
                            kingRow = row;
                            kingCol = col;
                            break;
                        }
        //try all possibilities to remove from check
        attemptToRemoveFromDanger(kingRow, kingCol);

    }

    /******************************************************************
     * A method for the AI to move the Black player's pieces. If
     * there's any immediate moves it can make to put the opponent
     * in check, it'll make that move.
     *
     *****************************************************************/
    public void attemptToPutIntoCheck() {

        // move every black piece everywhere
        for (int row = 0; row < NUM_ROWS; row++)
            for (int col = 0; col < NUM_COLS; col++)
                for (int toRow = 0; toRow < NUM_ROWS; toRow++)
                    for (int toCol = 0; toCol < NUM_COLS; toCol++)
                        if (pieceAt(row, col) != null) {
                            if (pieceAt(row, col).player() ==
                                    Player.BLACK) {
                                Move move = new Move
                                        (row, col, toRow, toCol);
                                if (pieceAt(row, col)
                                        .isValidMove(move, board))
                                    if (currentPlayer() ==
                                            Player.BLACK &&
                                            !stillInCheck(move)) {
                                        move(move);
                                        if (inCheck(Player.WHITE))
                                            setNextPlayer();
                                        else
                                            //if move isn't helpful
                                            undo();
                                    }
                            }
                        }

    }

    /******************************************************************
     * A method for the AI to move the Black player's pieces. If
     * any of its pieces are in danger of being taken, it'll do
     * anything in its power to prevent this.
     *
     *****************************************************************/
    public void attemptToRemoveFromDanger(int allyRow, int allyCol) {
        for (int moveRow = 0; moveRow < NUM_ROWS; moveRow++)
            for (int moveCol = 0; moveCol < NUM_COLS; moveCol++)
                if (currentPlayer() == Player.BLACK) {
                    Move newMove = new Move
                            (allyRow, allyCol, moveRow, moveCol);
                    if (isValidMove(newMove) &&
                            !stillInCheck(newMove)) {
                        move(newMove);
                        //if move doesn't remove threat, undo and retry
                        if (inDanger(moveRow, moveCol))
                            undo();
                        else
                            setNextPlayer();
                    }
                }

    }

    /******************************************************************
     * A method for the AI to move the Black player's pieces. If
     * there's no threat of pieces being taken or no easy way to put
     * the opponent in check, try to take an opponent's piece.
     * Prioritizes moves that don't sacrifice pieces, but allows them
     * if there's no better move to be made. If there's no way to
     * remove an opponent's piece, a random piece is moved so long as
     * it's not put into any danger.
     *
     *****************************************************************/
    public void moveAPiece() {
        int dangerEnemyRow = 0;
        int dangerEnemyCol = 0;

        //are any opponent pieces in danger?
        for (int enemyRow = 0; enemyRow < NUM_ROWS; enemyRow++)
            for (int enemyCol = 0; enemyCol < NUM_COLS; enemyCol++)
                if (pieceAt(enemyRow, enemyCol) != null)
                    if (pieceAt(enemyRow, enemyCol).player() ==
                            Player.WHITE)
                        if (inDanger(enemyRow, enemyCol)) {
                            for (int allyRow = 0;
                                 allyRow < NUM_ROWS; allyRow++)
                                for (int allyCol = 0;
                                     allyCol < NUM_COLS; allyCol++)
                                    if (pieceAt(allyRow, allyCol) !=
                                            null)
                                        if
                                        (pieceAt(allyRow, allyCol)
                                                .player() ==
                                                Player.BLACK) {
                                            Move move = new Move
                                                    (allyRow, allyCol,
                                                            enemyRow,
                                                            enemyCol);
                                            if (isValidMove(move) &&
                                                    (currentPlayer() ==
                                                        Player.BLACK) &&
                                                        !stillInCheck
                                                            (move)) {
                                                move(move);
                                                /**will taking their
                                                 * piece put any of our
                                                 * pieces in danger?
                                                  */

                                                if (inDanger(enemyRow,
                                                        enemyCol)) {
                                                    dangerEnemyRow =
                                                            enemyRow;
                                                    dangerEnemyCol =
                                                            enemyCol;
                                                    undo();
                                                } else
                                                    setNextPlayer();

                                            }
                                        }
                        }

        //take a risk if it's the only piece we can take
        for (int allyRow = 0; allyRow < NUM_ROWS; allyRow++)
            for (int allyCol = 0; allyCol < NUM_COLS; allyCol++)
                if (pieceAt(allyRow, allyCol) != null)
                    if (pieceAt(allyRow, allyCol).player() ==
                            Player.BLACK) {
                        Move move = new Move(allyRow, allyCol,
                                dangerEnemyRow, dangerEnemyCol);
                        if (isValidMove(move)&& currentPlayer() ==
                                Player.BLACK &&
                                !stillInCheck(move)) {
                            move(move);
                            setNextPlayer();
                        }
                    }

        //try to put a white piece in danger
        //find black piece to move
        for (int allyRow = 0; allyRow < NUM_ROWS; allyRow++)
            for (int allyCol = 0; allyCol < NUM_COLS; allyCol++)
                if (pieceAt(allyRow, allyCol) != null)
                    if (pieceAt(allyRow, allyCol).player() ==
                            Player.BLACK) {
                        //find a space to move to
                        for (int toRow = 0; toRow < NUM_ROWS; toRow++)
                            for (int toCol = 0; toCol < NUM_COLS;
                                 toCol++) {
                                Move move = new Move(allyRow, allyCol,
                                        toRow, toCol);
                                if (isValidMove(move)) {
                                    /**
                                     * check if this move puts any
                                     * enemy piece in danger
                                     */
                                    for (int enemyRow = 0;
                                         enemyRow < NUM_ROWS;
                                         enemyRow++)
                                        for (int enemyCol = 0;
                                             enemyCol < NUM_COLS;
                                             enemyCol++)
                                            if (pieceAt(enemyRow,
                                                    enemyCol) != null)
                                                if (pieceAt(enemyRow,
                                                        enemyCol)
                                                        .player() ==
                                                        Player.WHITE
                                                        &&
                                                        currentPlayer()
                                                        == Player.BLACK
                                                        &&
                                                        !stillInCheck
                                                                (move))
                                                {
                                                    /** undo and
                                                     * continue
                                                     * moving pieces if
                                                     * enemy is not in
                                                     * danger
                                                     */

                                                    move(move);
                                                    if (!inDanger
                                                            (enemyRow,
                                                             enemyCol))
                                                        undo();
                                                    else
                                                       setNextPlayer();
                                                }
                                }
                            }

                    }

        //just move a piece somewhere (that doesn't put it in danger)
        for (int allyRow = 0; allyRow < NUM_ROWS; allyRow++)
            for (int allyCol = 0; allyCol < NUM_COLS; allyCol++)
                if (pieceAt(allyRow, allyCol) != null)
                    if (pieceAt(allyRow, allyCol).player() ==
                            Player.BLACK) {
                        //find a space to move to
                        for (int toRow = 0; toRow < NUM_ROWS; toRow++)
                            for (int toCol = 0; toCol < NUM_COLS;
                                 toCol++) {
                                Move move = new Move(allyRow, allyCol,
                                        toRow, toCol);
                                if (isValidMove(move) &&
                                        currentPlayer() == Player.BLACK
                                        && !stillInCheck(move)) {
                                    move(move);
                                    //if space is dangerous, move back
                                    if (inDanger(toRow, toCol))
                                        undo();
                                    else
                                        setNextPlayer();

                                }
                            }
                    }

    }

    /******************************************************************
     * A method that checks whether a piece is in danger of being taken
     *
     * @param pieceRow the row location of the piece in question
     *        pieceCol the column location of the piece in question
     * @return {@code true} if in danger, {@code false} otherwise.
     *****************************************************************/
    public boolean inDanger(int pieceRow, int pieceCol) {
        //checks all enemy pieces to see if any can move to this piece
        for (int enemyRow = 0; enemyRow < NUM_ROWS; enemyRow++)
            for (int enemyCol = 0; enemyCol < NUM_COLS; enemyCol++)
                if (pieceAt(enemyRow, enemyCol) != null)
                    if (pieceAt(enemyRow, enemyCol).player() !=
                            pieceAt(pieceRow, pieceCol).player()) {
                        Move move = new Move(enemyRow, enemyCol,
                                pieceRow, pieceCol);
                        if (isValidMove(move)) {
                            return true;
                        }
                    }
        //if none of the enemy pieces can move to this piece
        return false;
    }

    /******************************************************************
     * Add a new move to the game history. This saves the current game
     * board onto the end of an ArrayList, allowing access to older
     * moves and enabling an undo function if desired.
     *
     * @param board import and save the current board
     *****************************************************************/
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
     *****************************************************************/
    private void deleteMove() {
        if (!(boards.size() - 1 <= 0))
            boards.remove(boards.size() - 1);
    }

    /******************************************************************
     * Moves the game back a state by loading the previous move.
     * Adjusts the amount of undoes and redoes possible accordingly.
     *
     *****************************************************************/
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

    /******************************************************************
     * Moves the game forward a state by loading the following move.
     * Adjusts the amount of undoes and redoes possible accordingly.
     *
     *****************************************************************/
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

    /******************************************************************
     * Copies the current state of the board so that it can be returned
     * to with the undo() and redo() methods.
     *
     * @param copyTo the new board for the board state to be copied to
     *        copyFrom the current board to be saved
     *****************************************************************/
    private void cloneBoard(IChessPiece[][] copyTo,
                            IChessPiece[][] copyFrom) {

        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++) {
                if (copyFrom[r][c] != null) {
                    //copies the location and state of the rooks
                    if (copyFrom[r][c].type().equals("Rook")) {
                        copyTo[r][c] =
                                new Rook(copyFrom[r][c].player());
                        copyTo[r][c]
                                .setMoved(copyFrom[r][c].isMoved());
                    }
                    //copies the location and state of the kings
                    if (copyFrom[r][c].type().equals("King")) {
                        copyTo[r][c] =
                                new King(copyFrom[r][c].player());
                        copyTo[r][c]
                                .setMoved(copyFrom[r][c].isMoved());
                    }
                    //copies the location and state of the pawns
                    if (copyFrom[r][c].type().equals("Pawn")) {
                        copyTo[r][c] =
                                new Pawn(copyFrom[r][c].player());
                        copyTo[r][c]
                                .setMoved(copyFrom[r][c].isMoved());
                    } else
                        //copies the location of everything else
                        copyTo[r][c] = copyFrom[r][c];
                }
            }
    }

    /******************************************************************
     * Returns the current status of the game using the GUIcodes.
     *
     * @return status the current game status
     *****************************************************************/
    public GUIcodes getStatus() {
        return status;
    }

    /******************************************************************
     * Sets the current status of the game using the GUIcodes.
     *
     * @param status the desired status of the game
     *****************************************************************/
    public void setStatus(GUIcodes status) {
        this.status = status;
    }

    /******************************************************************
     * Changes what player gets to make a move.
     *
     * @param p the player whose turn it now is
     *****************************************************************/
    private void setPlayer(Player p) {
        player = p;
    }
}
