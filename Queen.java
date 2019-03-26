package Project3;

/**********************************************************************
 * This code is strictly unchanged from what was provided to us by the
 * class instructor. It checks to see if the requested move is valid
 * for the bishop and for the rook. If it is valid for either one, it 
 * returns a boolean "True" value. Otherwise, it returns "False".
 */

public class Queen extends ChessPiece {

    public Queen(Player player) {
        super(player);

    }

    public String type() {
        return "Queen";

    }

    public boolean isValidMove(Move move, IChessPiece[][] board) {
        Bishop move1 = new Bishop(board[move.fromRow][move.fromColumn].player());
        Rook move2 = new Rook(board[move.fromRow][move.fromColumn].player());
        return (move1.isValidMove(move, board) || move2.isValidMove(move, board));
    }
}
