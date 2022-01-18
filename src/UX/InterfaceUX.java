package UX;

import Constants.BoardConstants;
import Models.Board;
import Models.Move;
import Models.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InterfaceUX extends JLayeredPane implements MouseListener, MouseMotionListener {
    private JPanel board;
    private JLabel movingPiece;
    private final Board internalLogicBoard;
    private int fromI;
    private int fromJ;
    private int toI;
    private int toJ;

    public InterfaceUX(Board b) {
        this.internalLogicBoard = b;
        this.fromI = -1;
        this.fromJ = -1;
        this.toI = -1;
        this.toJ = -1;

        initializeBoard();
    }

    private void initializeBoard() {
        this.board = new JPanel();

        this.board.setLayout(new GridLayout(8, 8));

        this.board.setBackground(Color.LIGHT_GRAY);
        this.board.setBounds(0, 0, BoardConstants.BOARD_SIZE, BoardConstants.BOARD_SIZE);

        this.board.addMouseListener(this);
        this.board.addMouseMotionListener(this);

        Piece[][] pieces = this.internalLogicBoard.getBoard();

        int j = 0;
        for (Piece[] row : pieces) {
            int i = 0;
            for (Piece p : row) {
                JPanel square = new JPanel(new BorderLayout());
                square.setBackground((i + j) % 2 == 0 ? Color.red : Color.white);

                String pieceStr = p == null ? "" : p.toString();
                square.add(new JLabel(pieceStr, SwingConstants.CENTER));
                this.board.add(square);
                i++;
            }
            j++;
        }

        this.board.setPreferredSize(new Dimension(500, 500));

        JFrame frame = new JFrame("Chess Board");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(this.board);
        frame.setSize(500, 500);
        frame.setResizable(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void movePiece(Move move) {
        JPanel labelToMove = (JPanel) this.board.getComponent((move.getFromI() * 8) + move.getFromJ());
        JPanel labelToReplace = (JPanel) this.board.getComponent((move.getToI() * 8) + move.getToJ());

        labelToReplace.removeAll();
        labelToReplace.add(labelToMove.getComponent(0));
        labelToMove.removeAll();

        labelToMove.revalidate();
        labelToReplace.revalidate();
        this.board.repaint();
    }

    /*
     **  Add the selected chess piece to the dragging layer, so it can be moved
     */
    public void mousePressed(MouseEvent e) {
        movingPiece = null;
        Component c = this.board.findComponentAt(e.getX(), e.getY());

        if (c instanceof JPanel) {
            return;
        }

        int[] moveCords = getIJFromComponent(e.getX(), e.getY());
        this.fromI = moveCords[0];
        this.fromJ = moveCords[1];

        this.movingPiece = (JLabel) c;
        this.movingPiece.setLocation(e.getX(), e.getY());

        add(this.movingPiece, JLayeredPane.DRAG_LAYER);
        setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }

    /*
     **  Move the chess piece around
     */
    public void mouseDragged(MouseEvent me) {
        if (this.movingPiece == null) {
            return;
        }

        this.movingPiece.setLocation(me.getX(), me.getY());
    }

    /*
     **  Drop the chess piece back onto the chess board
     */
    public void mouseReleased(MouseEvent e) {
        setCursor(null);

        if (this.movingPiece == null) {
            return;
        }

        //  Make sure the chess piece is no longer painted on the layered pane
        this.movingPiece.setVisible(false);
        remove(this.movingPiece);
        this.movingPiece.setVisible(true);

        Component c = this.board.findComponentAt(e.getX(), e.getY());

        int[] moveCords = getIJFromComponent(e.getX(), e.getY());
        this.toI = moveCords[0];
        this.toJ = moveCords[1];

        Container parent;
        if (c instanceof JPanel) {
            parent = (Container) c;
        } else {
            parent = c.getParent();
        }
        parent.removeAll();
        parent.add(this.movingPiece);
        parent.revalidate();
        this.movingPiece = null;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public Move getNextMove() {
        System.out.println(this.fromI + " " + this.fromJ + " " + this.toI + " " + this.toJ);
        if (this.toI > -1 && this.toJ > -1 && this.fromI > -1 && this.fromJ > -1) {
            Move m = new Move(this.fromI, this.fromJ, this.toI, this.toJ, 0);

            this.fromI = -1;
            this.fromJ = -1;
            this.toI = -1;
            this.toJ = -1;

            return m;
        }
        return null;
    }

    private int[] getIJFromComponent(int x, int y) {
        Dimension gridSize = this.board.getComponent(0).getSize();

        int i = Math.floorDiv(y, gridSize.width);
        int j = Math.floorDiv(x, gridSize.height);

        return new int[]{i, j};
    }
}
