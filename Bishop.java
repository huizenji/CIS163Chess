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

        if(Math.abs(move.toRow - move.fromRow) ==
                Math.abs(move.toColumn-move.fromColumn))
            valid = true;

        //check to see if the other spot has a piece for the same player
        if (super.sameTeam(move, board))
            valid = false;

        //check for blocking elements in path to target location
        if (move.toRow > move.fromRow) {
            if (move.toColumn > move.fromColumn) {
                for (int i = 1; i < move.toRow - move.fromRow; i++)
                    if (move.fromRow + i <= 7 && move.fromColumn + i <= 7)
                        if (board[move.fromRow + i][move.fromColumn + i]
                                != null)
                            valid = false;
            }

            if (move.toColumn < move.fromColumn) {
                for (int i = 1; i < move.toRow - move.fromRow; i++)
                    if (move.fromRow + i <= 7 && move.fromColumn - i >= 0)
                        if (board[move.fromRow + i][move.fromColumn - i]
                                != null)
                            valid = false;
            }
        } else {
            if (move.toColumn > move.fromColumn) {
                for (int i = 1; i < move.toColumn - move.fromColumn; i++)
                    if (move.fromRow - i >= 0 && move.fromColumn + i <= 7)
                        if (board[move.fromRow - i][move.fromColumn + i]
                                != null)
                            valid = false;
            }

            if (move.toColumn < move.fromColumn) {
                for (int i = 1; i < move.fromColumn - move.toColumn; i++)
                    if (move.fromRow - i >= 0 && move.fromColumn - i >= 0)
                    if (board[move.fromRow - i][move.fromColumn - i]
                            != null)
                        valid = false;
            }
        }
        return valid;
    }
}
