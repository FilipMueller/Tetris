import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Board  extends JPanel implements KeyListener {

    private static final int BOARD_WIDTH = 10;

    private static final int BOARD_HEIGHT = 20;

    private static final int BLOCK_SIZE = 30;

    private static final int FPS = 60;

    private static final int delay = 1000 / FPS;

    private static final int BOARD_LEFT_WALL = 30;

    private static final int BOARD_RIGHT_WALL = 240;

    private static final int NORMAL = 600;

    private static final int FAST = 50;

    private int delayTimeForMovement = NORMAL;

    private long beginTime;

    private final ArrayList<Shape> oldShapes = new ArrayList<>();

    private Square[] floor = new Square[BOARD_WIDTH];

    Shape currentShape = new Shape();

    public Board() {
        floorSquares(floor);
        Timer looper = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (System.currentTimeMillis() - beginTime > delayTimeForMovement) {
                    if (!atBottom()) currentShape.moveDown();
                    beginTime = System.currentTimeMillis();
                }
                repaint();
            }
        });
        looper.start();
    }

    public static void floorSquares(Square[] squares) {
        int x = 0;
        for (int i = 0; i < 10; i++) {
            squares[i] = new Square(null);
            squares[i].setX(x);
            squares[i].setY(570);
            x += 30;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (atBottom()) {
            updateBottom();
            oldShapes.add(currentShape);
            currentShape = new Shape();
        }

        if (!oldShapes.isEmpty()) {
            for (Shape s : oldShapes) {
                s.draw(g);
            }
        }

        currentShape.draw(g);

        g.setColor(Color.WHITE);
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            g.drawLine(0, BLOCK_SIZE * row, BLOCK_SIZE * BOARD_WIDTH, BLOCK_SIZE * row);
        }
        for (int column = 0; column < BOARD_WIDTH + 1; column++) {
            g.drawLine(column * BLOCK_SIZE, 0, column * BLOCK_SIZE, BLOCK_SIZE * BOARD_HEIGHT);
        }
    }

    public void updateBottom() {
        for (int i = 0; i < currentShape.squareMatrix.length; i++) {
            int lowest = 1000;
            for (int j = 0; j < currentShape.squareMatrix[0].length; j++) {
                if (currentShape.squareMatrix[i][j].getColor() != null) {
                    if (currentShape.squareMatrix[i][j].getY() < lowest) {
                        lowest = currentShape.squareMatrix[i][j].getY();
                        for (int k = 0; k < 10; k++) {
                            if (floor[k].getX() == currentShape.squareMatrix[i][j].getX()) {
                                floor[k].setY(lowest - 30);
                            }
                        }
                    }
                }
            }
        }
    }

    public int findY(int x) {
        for (int k = 0; k < 10; k++) {
            if (floor[k].getX() == x) {
                return floor[k].getY();
            }
        }
        return -1;
    }

    public boolean atBottom() {
        for (int i = 0; i < currentShape.squareMatrix.length; i++) {
            for (int j = 0; j < currentShape.squareMatrix[0].length; j++) {
                if (currentShape.squareMatrix[i][j].getColor() != null) {
                    if (currentShape.squareMatrix[i][j].getY() == findY(currentShape.squareMatrix[i][j].getX())){
                        return true;
                    }
                }
            }
        }
        return false;
    }




    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT && currentShape.furthestLeft() >= BOARD_LEFT_WALL) {
            currentShape.moveLeft();
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && currentShape.furthestRight() <= BOARD_RIGHT_WALL) {
            currentShape.moveRight();
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN && !atBottom()) {
            delayTimeForMovement = FAST;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            delayTimeForMovement = NORMAL;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            delayTimeForMovement = NORMAL;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            delayTimeForMovement = NORMAL;
        }
    }
}
