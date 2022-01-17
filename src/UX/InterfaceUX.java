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
    private int xAdjustment;
    private int yAdjustment;
    private JLabel movingPiece;

    public InterfaceUX() {
        initializeBoard();
    }

    private void initializeBoard() {
        this.board = new JPanel();

        this.board.setLayout(new GridLayout(8, 8));

        this.board.setBackground(Color.LIGHT_GRAY);
        this.board.setBounds(0, 0, BoardConstants.BOARD_SIZE, BoardConstants.BOARD_SIZE);

        this.board.addMouseListener(this);
        this.board.addMouseMotionListener(this);

        Board b = new Board(); // temp board used for modeling our UX
        Piece[][] pieces = b.getBoard();

        int j = 0;
        for (Piece[] row : pieces) {
            int i = 0;
            for (Piece p : row) {
                JPanel square = new JPanel(new BorderLayout());
                square.setBackground((i + j) % 2 == 0 ? Color.red : Color.white);

                if (p != null) {
                    square.add(new JLabel(p.toString(), SwingConstants.CENTER));
                }
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

        Point parentLocation = c.getParent().getLocation();
        this.xAdjustment = parentLocation.x - e.getX();
        this.yAdjustment = parentLocation.y - e.getY();
        this.movingPiece = (JLabel) c;
        this.movingPiece.setLocation(e.getX() + this.xAdjustment, e.getY() + this.yAdjustment);

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

        //  The drag location should be within the bounds of the chess board

        int x = me.getX() + this.xAdjustment;
        int xMax = this.movingPiece.getWidth() - this.movingPiece.getWidth();
        x = Math.min(x, xMax);
        x = Math.max(x, 0);

        int y = me.getY() + this.yAdjustment;
        int yMax = this.movingPiece.getHeight() - this.movingPiece.getHeight();
        y = Math.min(y, yMax);
        y = Math.max(y, 0);

        this.movingPiece.setLocation(x, y);
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
            parent.removeAll();
            parent.add(this.movingPiece);
        } else {
            parent = c.getParent();
            parent.remove(0);
            parent.add(this.movingPiece);
            parent.revalidate();
        }
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
}