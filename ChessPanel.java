package Project3;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChessPanel extends JPanel {

    private JButton[][] board;
    private ChessModel model;
    private JLabel turn;
    private JLabel enableAI;

    private ImageIcon wRook;
    private ImageIcon wBishop;
    private ImageIcon wQueen;
    private ImageIcon wKing;
    private ImageIcon wPawn;
    private ImageIcon wKnight;

    private ImageIcon bRook;
    private ImageIcon bBishop;
    private ImageIcon bQueen;
    private ImageIcon bKing;
    private ImageIcon bPawn;
    private ImageIcon bKnight;

    private boolean firstTurnFlag;
    private int fromRow;
    private int toRow;
    private int fromCol;
    private int toCol;
    private int numMoves;
    private int numUndos;
    private int useAI;
    // declare other instance variables as needed

    private listener listener;
    private JButton undoButton;
    private JButton redoButton;

    public ChessPanel() {
        model = new ChessModel();
        board = new JButton[model.numRows()][model.numColumns()];
        turn = new JLabel(model.currentPlayer() + "'s turn");

        useAI = JOptionPane.showConfirmDialog(null, "Enable AI?",
                "Opponent", JOptionPane.YES_NO_OPTION);
        if (useAI == 0)
            enableAI = new JLabel("AI: ENABLED");
        else
            enableAI = new JLabel("AI: DISABLED");

        listener = new listener();

        undoButton = new JButton("UNDO");
        redoButton = new JButton("REDO");
        undoButton.addActionListener(listener);
        redoButton.addActionListener(listener);
        undoButton.setEnabled(false);
        redoButton.setEnabled(false);

        createIcons();

        JPanel boardPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(model.numRows(),
                model.numColumns(), 1, 1));

        for (int r = 0; r < model.numRows(); r++) {
            for (int c = 0; c < model.numColumns(); c++) {
                if (model.pieceAt(r, c) == null) {
                    board[r][c] = new JButton("", null);
                    board[r][c].addActionListener(listener);
                } else if (model.pieceAt(r, c).player() ==
                        Player.WHITE) {
                    placeWhitePieces(r, c);
                } else if (model.pieceAt(r, c).player() ==
                        Player.BLACK)
                    placeBlackPieces(r, c);

                setBackGroundColor(r, c);
                boardPanel.add(board[r][c]);
            }
        }
        add(boardPanel, BorderLayout.WEST);
        boardPanel.setPreferredSize(new Dimension(600, 600));

        buttonPanel.add(turn);
        buttonPanel.add(undoButton);
        buttonPanel.add(redoButton);
        buttonPanel.add(enableAI);
        add(buttonPanel, BorderLayout.NORTH);
        buttonPanel.setPreferredSize(new Dimension(100, 200));

        firstTurnFlag = true;
        numMoves = 0;
        numUndos = 0;
    }

    private void setBackGroundColor(int r, int c) {
        if ((c % 2 == 1 && r % 2 == 0) ||
                (c % 2 == 0 && r % 2 == 1)) {
            board[r][c].setBackground(Color.LIGHT_GRAY);
        } else if ((c % 2 == 0 && r % 2 == 0) ||
                (c % 2 == 1 && r % 2 == 1)) {
            board[r][c].setBackground(Color.WHITE);
        }
    }

    private void placeWhitePieces(int r, int c) {
        if (model.pieceAt(r, c).type().equals("Pawn")) {
            board[r][c] = new JButton(null, wPawn);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("Rook")) {
            board[r][c] = new JButton(null, wRook);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("Knight")) {
            board[r][c] = new JButton(null, wKnight);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("Bishop")) {
            board[r][c] = new JButton(null, wBishop);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("Queen")) {
            board[r][c] = new JButton(null, wQueen);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("King")) {
            board[r][c] = new JButton(null, wKing);
            board[r][c].addActionListener(listener);
        }
    }

    private void placeBlackPieces(int r, int c) {
        if (model.pieceAt(r, c).type().equals("Pawn")) {
            board[r][c] = new JButton(null, bPawn);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("Rook")) {
            board[r][c] = new JButton(null, bRook);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("Knight")) {
            board[r][c] = new JButton(null, bKnight);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("Bishop")) {
            board[r][c] = new JButton(null, bBishop);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("Queen")) {
            board[r][c] = new JButton(null, bQueen);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("King")) {
            board[r][c] = new JButton(null, bKing);
            board[r][c].addActionListener(listener);
        }
    }

    private void createIcons() {
        // Sets the Image for white player pieces
        wPawn = new ImageIcon("./src/Project3/wPawn.png");
        wRook = new ImageIcon("./src/Project3/wRook.png");
        wBishop = new ImageIcon("./src/Project3/wBishop.png");
        wQueen = new ImageIcon("./src/Project3/wQueen.png");
        wKing = new ImageIcon("./src/Project3/wKing.png");
        wPawn = new ImageIcon("./src/Project3/wPawn.png");
        wKnight = new ImageIcon("./src/Project3/wKnight.png");

        // Sets the Image for black player pieces
        bPawn = new ImageIcon("./src/Project3/bPawn.png");
        bRook = new ImageIcon("./src/Project3/bRook.png");
        bBishop = new ImageIcon("./src/Project3/bBishop.png");
        bQueen = new ImageIcon("./src/Project3/bQueen.png");
        bKing = new ImageIcon("./src/Project3/bKing.png");
        bPawn = new ImageIcon("./src/Project3/bPawn.png");
        bKnight = new ImageIcon("./src/Project3/bKnight.png");
    }

    // method that updates the board
    private void displayBoard() {

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++)
                if (model.pieceAt(r, c) == null)
                    board[r][c].setIcon(null);
                else if (model.pieceAt(r, c).player() ==
                        Player.WHITE) {
                    if (model.pieceAt(r, c).type().equals("Pawn"))
                        board[r][c].setIcon(wPawn);

                    if (model.pieceAt(r, c).type().equals("Rook"))
                        board[r][c].setIcon(wRook);

                    if (model.pieceAt(r, c).type().equals("Knight"))
                        board[r][c].setIcon(wKnight);

                    if (model.pieceAt(r, c).type().equals("Bishop"))
                        board[r][c].setIcon(wBishop);

                    if (model.pieceAt(r, c).type().equals("Queen"))
                        board[r][c].setIcon(wQueen);

                    if (model.pieceAt(r, c).type().equals("King"))
                        board[r][c].setIcon(wKing);

                } else if (model.pieceAt(r, c).player() ==
                        Player.BLACK) {
                    if (model.pieceAt(r, c).type().equals("Pawn"))
                        board[r][c].setIcon(bPawn);

                    if (model.pieceAt(r, c).type().equals("Rook"))
                        board[r][c].setIcon(bRook);

                    if (model.pieceAt(r, c).type().equals("Knight"))
                        board[r][c].setIcon(bKnight);

                    if (model.pieceAt(r, c).type().equals("Bishop"))
                        board[r][c].setIcon(bBishop);

                    if (model.pieceAt(r, c).type().equals("Queen"))
                        board[r][c].setIcon(bQueen);

                    if (model.pieceAt(r, c).type().equals("King"))
                        board[r][c].setIcon(bKing);

                }
        }
        repaint();

        if (numMoves > 0)
            undoButton.setEnabled(true);
        else
            undoButton.setEnabled(false);

        if (numUndos > 0)
            redoButton.setEnabled(true);
        else
            redoButton.setEnabled(false);

        turn.setText(model.currentPlayer() + "'s turn");

        if (model.isComplete()) {
            model.setNextPlayer();
            JOptionPane.showMessageDialog(
                    null,
                    "Checkmate! " + model.currentPlayer() + " wins!");
            model.setNextPlayer();
        }

    }

    // inner class that represents action listener for buttons
    private class listener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            for (int r = 0; r < model.numRows(); r++)
                for (int c = 0; c < model.numColumns(); c++)
                    if (board[r][c] == event.getSource())
                        if (firstTurnFlag == true) {
                            fromRow = r;
                            fromCol = c;
                            firstTurnFlag = false;
                        } else {
                            toRow = r;
                            toCol = c;
                            firstTurnFlag = true;
                            Move m = new Move(fromRow, fromCol,
                                    toRow, toCol);
                            if ((model.isValidMove(m)))
                                if (model.pieceAt(fromRow, fromCol)
                                        .player() ==
                                        model.currentPlayer())
                                    if (model.getStatus() !=
                                            GUIcodes.Checkmate) {
                                        model.move(m);

                                        numMoves++;
                                        numUndos = 0;

                                        model.setNextPlayer();

                                        if (model.inCheck(model
                                                .currentPlayer())) {
                                            JOptionPane
                                                 .showMessageDialog(
                                                 null,
                                                 model.currentPlayer()
                                                         + " is in " +
                                                         "check!");
                                        }

                                        if (useAI == 0) {
                                            model.AI();
                                            numMoves++;
                                            numUndos = 0;
                                        }
                                    }
                        }

            if (event.getSource().equals(undoButton)) {
                model.undo();
                numUndos++;
                numMoves--;
                model.setNextPlayer();
                if (model.getStatus() == GUIcodes.Checkmate) {
                    model.setStatus(GUIcodes.NoMessage);
                }
            }

            if (event.getSource().equals(redoButton)) {
                model.redo();
                numUndos--;
                numMoves++;
                model.setNextPlayer();
                if (model.inCheck(model.currentPlayer())) {
                    JOptionPane.showMessageDialog(
                            null,
                            model.currentPlayer() + " is in check!");
                }

            }

            displayBoard();
        }
    }

}
