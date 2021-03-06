package Project3;

import org.junit.Test;

import static org.junit.Assert.*;

/**********************************************************************
 * A class that tests the ChessModel class with JUnits
 *
 * @author David Butz, Lauren Freeman, Jillian Huizenga
 * Date: 3/26/2019
 *********************************************************************/
public class ChessTest {

    // tests pawn movement
    @Test
    public void testPawn() {
        ChessModel model = new ChessModel();
        Move valid = new Move(1, 1, 3, 1);
        Move notValid = new Move(3, 1, 5, 1);
        if (model.isValidMove(valid))
            model.move(valid);
        if (model.isValidMove(notValid))
            model.move(notValid);
        assertTrue(model.pieceAt(3, 1).type().equals("Pawn"));
    }

    // tests rook movement
    @Test
    public void testRook() {
        ChessModel model = new ChessModel();
        Move pawn = new Move(6, 0, 4, 0);
        Move forward = new Move(7, 0, 5, 0);
        Move sideways = new Move(5, 0, 5, 5);
        Move notValid = new Move(5, 5, 7, 7);
        if (model.isValidMove(pawn))
            model.move(pawn);
        if (model.isValidMove(forward))
            model.move(forward);
        if (model.isValidMove(sideways))
            model.move(sideways);
        if (model.isValidMove(notValid))
            model.move(notValid);
        assertTrue(model.pieceAt(5, 5).type().equals("Rook"));
    }

    // tests knight movement
    @Test
    public void testKnight() {
        ChessModel model = new ChessModel();
        Move valid = new Move(0, 6, 2, 5);
        Move notValid = new Move(2, 5, 1, 3);
        if (model.isValidMove(valid))
            model.move(valid);
        if (model.isValidMove(notValid))
            model.move(notValid);
        assertTrue(model.pieceAt(2, 5).type().equals("Knight"));
    }

    // tests bishop movement
    @Test
    public void testBishop() {
        ChessModel model = new ChessModel();
        Move pawn = new Move(6, 3, 5, 3);
        Move valid = new Move(7, 2, 2, 7);
        Move notValid = new Move(2, 7, 2, 0);
        if (model.isValidMove(pawn))
            model.move(pawn);
        if (model.isValidMove(valid))
            model.move(valid);
        if (model.isValidMove(notValid))
            model.move(notValid);
        assertTrue(model.pieceAt(2, 7).type().equals("Bishop"));

    }

    // test queen movement
    @Test
    public void testQueen() {
        ChessModel model = new ChessModel();

        // Rook
        Move pawn = new Move(6, 3, 4, 3);
        Move forward = new Move(7, 3, 5, 3);
        Move sideways = new Move(5, 3, 5, 5);
        Move notValid = new Move(5, 5, 2, 6);
        if (model.isValidMove(pawn))
            model.move(pawn);
        if (model.isValidMove(forward))
            model.move(forward);
        if (model.isValidMove(sideways))
            model.move(sideways);
        if (model.isValidMove(notValid))
            model.move(notValid);
        assertTrue(model.pieceAt(5, 5).type().equals("Queen"));

        // Bishop
        Move valid = new Move(5, 5, 4, 4);
        if (model.isValidMove(valid))
            model.move(valid);
        assertTrue(model.pieceAt(4, 4).type().equals("Queen"));
    }

    // test king movement
    @Test
    public void testKing() {
        ChessModel model = new ChessModel();
        Move pawn = new Move(1, 4, 3, 4);
        Move pawn2 = new Move(1, 5, 3, 5);
        Move valid = new Move(0, 4, 1, 4);
        Move valid2 = new Move(1, 4, 1, 5);
        Move notValid = new Move(1, 5, 3, 5);
        if (model.isValidMove(pawn))
            model.move(pawn);
        if (model.isValidMove(pawn2))
            model.move(pawn2);
        if (model.isValidMove(valid))
            model.move(valid);
        if (model.isValidMove(valid2))
            model.move(valid2);
        if (model.isValidMove(notValid))
            model.move(notValid);
        assertTrue(model.pieceAt(1, 5).type().equals("King"));
    }

    // test castling movements
    @Test
    public void testCastling() {
        ChessModel model = new ChessModel();
        Move pawn = new Move(1, 6, 2, 6);
        Move bishop = new Move(0, 5, 1, 6);
        Move knight = new Move(0, 6, 2, 7);
        Move king = new Move(0, 4, 0, 6);
        if (model.isValidMove(pawn))
            model.move(pawn);
        if (model.isValidMove(bishop))
            model.move(bishop);
        if (model.isValidMove(knight))
            model.move(knight);
        if (model.isValidMove(king))
            model.move(king);
        assertTrue(model.pieceAt(0, 6).type().equals("King"));
        assertTrue(model.pieceAt(0, 5).type().equals("Rook"));
    }

    // test the undo method
    @Test
    public void testUndo() {
        ChessModel model = new ChessModel();
        Move m = new Move(1, 2, 3, 2);
        if (model.isValidMove(m))
            model.move(m);
        model.undo();
        assertTrue(model.pieceAt(1, 2).type().equals("Pawn"));
    }

    // test the redo method
    @Test
    public void testRedo() {
        ChessModel model = new ChessModel();
        Move m = new Move(1, 2, 3, 2);
        if (model.isValidMove(m))
            model.move(m);
        model.undo();
        model.redo();
        assertTrue(model.pieceAt(3, 2).type().equals("Pawn"));
    }

    // test the inCheck() method
    @Test
    public void testInCheck() {
        ChessModel model = new ChessModel();
        Move pawn = new Move(6, 5, 5, 5);
        Move pawn2 = new Move(1, 4, 3, 4);
        Move pawn3 = new Move(6, 6, 4, 6);
        Move pawn4 = new Move(6, 3, 5, 3);
        Move queen = new Move(0, 3, 4, 7);
        model.move(pawn);
        model.move(pawn2);
        model.move(pawn3);
        model.move(pawn4);
        model.move(queen);
        assertTrue(model.inCheck(Player.WHITE));
    }

    // test the isComplete() method
    @Test
    public void testIsComplete() {
        ChessModel model = new ChessModel();
        Move pawn = new Move(6, 5, 4, 5);
        Move pawn2 = new Move(1, 4, 3, 4);
        Move pawn3 = new Move(6, 6, 4, 6);
        Move queen = new Move(0, 3, 4, 7);
        model.move(pawn);
        model.move(pawn2);
        model.move(pawn3);
        model.move(queen);
        assertTrue(model.isComplete());
    }

    // test other methods in ChessModel (set/getStatus, setNextPlayer)
    @Test
    public void testExtra(){
        ChessModel model = new ChessModel();
        model.setStatus(GUIcodes.Checkmate);
        model.setNextPlayer();
        assertTrue(model.getStatus() == GUIcodes.Checkmate);
        assertTrue(model.currentPlayer() == Player.BLACK);
    }

}
