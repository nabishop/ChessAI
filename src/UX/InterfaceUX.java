package UX;

import Constants.BoardConstants;
import Models.Board;
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

    public InterfaceUX(Board b) {
        this.internalLogicBoard = b;

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

    public void movePiece(int fromI, int fromJ, int toI, int toJ) {

    }

    /*
     **  Add the selected chess piece to the dragging layer, so it can be moved
     */
    public void mousePressed(MouseEvent e) {
        System.out.println("MOUSE PRESSED: " + this.movingPiece);

        movingPiece = null;
        Component c = this.board.findComponentAt(e.getX(), e.getY());

        if (c instanceof JPanel) {
            return;
        }

        this.movingPiece = (JLabel) c;
        this.movingPiece.setLocation(e.getX() , e.getY() );

        add(this.movingPiece, JLayeredPane.DRAG_LAYER);
        setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }

    /*
     **  Move the chess piece around
     */
    public void mouseDragged(MouseEvent me) {
        System.out.println("MOUSE DRAGGED: " + this.movingPiece);

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
        System.out.println("MOUSE RELEASED: " + this.movingPiece);

        if (this.movingPiece == null) {
            return;
        }

        //  Make sure the chess piece is no longer painted on the layered pane
        this.movingPiece.setVisible(false);
        remove(this.movingPiece);
        this.movingPiece.setVisible(true);

        //  The drop location should be within the bounds of the chess board
        int xMax = this.movingPiece.getWidth() - this.movingPiece.getWidth();
        int x = Math.min(e.getX(), xMax);
        x = Math.max(x, 0);

        int yMax = this.movingPiece.getHeight() - this.movingPiece.getHeight();
        int y = Math.min(e.getY(), yMax);
        y = Math.max(y, 0);

        Component c = this.board.findComponentAt(e.getX(), e.getY());
        System.out.println(c.toString());

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

        for (Component co : this.board.getComponents()) {
            System.out.println(co.toString());
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
