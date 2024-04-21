import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Board  extends JPanel implements KeyListener {

    private static final int BOARD_WIDTH = 10;

    private static final int BOARD_HEIGHT = 20;

    private static final int BLOCK_SIZE = 30;

    private static final int FPS = 60;

    private static final int delay = 1000 / FPS;

    private static final int NORMAL = 600;

    private static final int FAST = 50;

    private final int[] xRotationCounterClockwise = {0, 1};

    private final int[] yRotationCounterClockwise = {-1, 0};

    private int delayTimeForMovement = NORMAL;

    private long beginTime;

    private final Square[][] board = new Square[BOARD_HEIGHT][BOARD_WIDTH];

    Shape currentShape = new Shape();

    public Board() {
        Timer looper = new Timer(delay, _ -> {
            if (System.currentTimeMillis() - beginTime > delayTimeForMovement) {
                if (!checkIfHasNeighbour(2)) currentShape.moveDown();
                beginTime = System.currentTimeMillis();
            }
            repaint();
        });
        looper.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (checkIfHasNeighbour(2)) {
            fillArray();
            currentShape = new Shape();
        }

        checkIfRowIsFilled();
        drawBoard(g);
        currentShape.draw(g);

        g.setColor(Color.WHITE);
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            g.drawLine(0, BLOCK_SIZE * row, BLOCK_SIZE * BOARD_WIDTH, BLOCK_SIZE * row);
        }
        for (int column = 0; column < BOARD_WIDTH + 1; column++) {
            g.drawLine(column * BLOCK_SIZE, 0, column * BLOCK_SIZE, BLOCK_SIZE * BOARD_HEIGHT);
        }
    }

    public void rotate() {
        int row = Integer.parseInt(currentShape.centerPoint.substring(0, 1));
        int col = Integer.parseInt(currentShape.centerPoint.substring(1));
        int centerX = currentShape.squareMatrix[row][col].getX();
        int centerY = currentShape.squareMatrix[row][col].getY();

        Square[][] tempMatrix = new Square[currentShape.squareMatrix.length][currentShape.squareMatrix[0].length];

        int counter = 0;

        for (int i = 0; i < currentShape.squareMatrix.length; i++) {
            for (int j = 0; j < currentShape.squareMatrix[0].length; j++) {
                if (currentShape.squareMatrix[i][j].getColor() != null) {
                    int x = currentShape.squareMatrix[i][j].getX() - centerX;
                    int y = currentShape.squareMatrix[i][j].getY() - centerY;

                    int newX = (xRotationCounterClockwise[0] * x) + (xRotationCounterClockwise[1] * y);
                    int newY = (yRotationCounterClockwise[0] * x) + (yRotationCounterClockwise[1] * y);

                    tempMatrix[i][j] = new Square(null);
                    tempMatrix[i][j].setX(newX + centerX);
                    tempMatrix[i][j].setY(newY + centerY);

                    if (tempMatrix[i][j].getX() > 9 || tempMatrix[i][j].getX() < 0) {
                        counter++;
                    }
                }
            }
        }
        if (counter == 0) {
            for (int i = 0; i < currentShape.squareMatrix.length; i++) {
                for (int j = 0; j < currentShape.squareMatrix[0].length; j++) {
                    if (currentShape.squareMatrix[i][j].getColor() != null) {
                        currentShape.squareMatrix[i][j].setX(tempMatrix[i][j].getX());
                        currentShape.squareMatrix[i][j].setY(tempMatrix[i][j].getY());
                    }
                }
            }
        }
    }

    public void checkIfRowIsFilled() {
        int rowNumber = 0;
        for (Square[] squareArray : board) {
            int filledSquares = 0;
            for (Square square : squareArray) {
                if (square != null) {
                    filledSquares++;
                }
            }
            if (filledSquares == 10) {
                emptyFilledRow(rowNumber);
            }
            rowNumber++;
        }
    }

    public void emptyFilledRow(int rowNumber) {
        for (int i = 0; i < 10; i++) {
            board[rowNumber][i] = null;
        }

        for (int i = rowNumber - 1; i >= 0; i--) {
            for (int j = 0; j < 10; j++) {
                if (board[i][j] != null) {
                    board[i + 1][j] = new Square(board[i][j].getColor());
                    board[i + 1][j].setX(board[i][j].getX());
                    board[i + 1][j].setY(board[i][j].getY() + 1);
                    board[i][j] = null;
                }
            }
        }
    }


    public void drawBoard(Graphics g) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                if (board[row][col] != null) {
                    g.setColor(board[row][col].getColor());
                    g.fillRect(board[row][col].getX() * BLOCK_SIZE, board[row][col].getY() * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
    }

    public boolean checkIfHasNeighbour(int direction) {
        for (Square[] squareArray : currentShape.squareMatrix) {
            for (Square square : squareArray) {
                if (square.getColor() != null) {
                    int x = square.getX();
                    int y = square.getY();
                    switch (direction) {
                        case 0: //check left
                            if (square.getX() >= 9 || board[square.getY()][square.getX() + 1] != null) {
                                return true;
                            }
                            break;
                        case 1: //check right
                            if (x == 0 || board[y][x - 1] != null) {
                                return true;
                            }
                            break;
                        case 2: //check bottom
                            if (y == board.length - 1 || board[y + 1][x] != null) {
                                return true;
                            }
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid dircetion");
                    }
                }
            }
        }
        return false;
    }

    public void fillArray() {
        for (Square[] squareArray : currentShape.squareMatrix) {
            for (Square square : squareArray) {
                int x = square.getX();
                int y = square.getY();
                if (square.getColor() != null) {
                    board[y][x] = new Square(square.getColor());
                    board[y][x].setX(x);
                    board[y][x].setY(y);
                }
            }
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            rotate();
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT && (!checkIfHasNeighbour(1))) {
            currentShape.moveLeft();
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && (!checkIfHasNeighbour(0))) {
            currentShape.moveRight();
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN && !checkIfHasNeighbour(2)) {
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
