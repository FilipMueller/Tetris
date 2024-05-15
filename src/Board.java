import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

public class Board  extends JPanel implements KeyListener {

    private static final int BOARD_WIDTH = 10;

    private static final int BOARD_HEIGHT = 20;

    private static final int BLOCK_SIZE = 30;

    private static final int FPS = 60;

    private static final int delay = 1000 / FPS;

    private static int NORMAL = 700;

    private static final int FAST = 40;

    private final int[] xRotationCounterClockwise = {0, 1};

    private final int[] yRotationCounterClockwise = {-1, 0};

    private final int[] xRotationClockwise = {0, -1};

    private final int[] yRotationClockwise = {1, 0};

    private int delayTimeForMovement = NORMAL;

    private long beginTime;

    private int score = 0;

    private boolean pause = false;

    private boolean gameIsOver = false;

    private final Square[][] board = new Square[BOARD_HEIGHT][BOARD_WIDTH];

    private Shape currentShape = new Shape();

    private int paintBackground = 0;

    private final Timer looper;

    private Square[][] I_OFFSET_DATA = new Square[4][5];

    public Board() {
        looper = new Timer(delay, _ -> {
            if (System.currentTimeMillis() - beginTime > delayTimeForMovement) {
                //if (!checkIfShapeHasNeighbour(currentShape, 2) && !pause) currentShape.moveDown();
                beginTime = System.currentTimeMillis();
            }
            if (!pause) {
                paintBackground = 0;
                repaint();
            }

            if (pause && paintBackground == 0) {
                repaint();
                paintBackground = 1;
            }

        });
        looper.start();
    }

    private void Awake()
    {

        I_OFFSET_DATA[0][0] = new Square(null);
        I_OFFSET_DATA[0][0].setX(0);
        I_OFFSET_DATA[0][0].setY(0);
        I_OFFSET_DATA[0][1] = new Square(null);
        I_OFFSET_DATA[0][1].setX(-1);
        I_OFFSET_DATA[0][1].setY(0);
        I_OFFSET_DATA[0][2] = new Square(null);
        I_OFFSET_DATA[0][2].setX(2);
        I_OFFSET_DATA[0][2].setY(0);
        I_OFFSET_DATA[0][3] = new Square(null);
        I_OFFSET_DATA[0][3].setX(-1);
        I_OFFSET_DATA[0][3].setY(0);
        I_OFFSET_DATA[0][4] = new Square(null);
        I_OFFSET_DATA[0][4].setX(2);
        I_OFFSET_DATA[0][4].setY(0);

        I_OFFSET_DATA[1][0] = new Square(null);
        I_OFFSET_DATA[1][0].setX(-1);
        I_OFFSET_DATA[1][0].setY(0);
        I_OFFSET_DATA[1][1] = new Square(null);
        I_OFFSET_DATA[1][1].setX(0);
        I_OFFSET_DATA[1][1].setY(0);
        I_OFFSET_DATA[1][2] = new Square(null);
        I_OFFSET_DATA[1][2].setX(0);
        I_OFFSET_DATA[1][2].setY(0);
        I_OFFSET_DATA[1][3] = new Square(null);
        I_OFFSET_DATA[1][3].setX(0);
        I_OFFSET_DATA[1][3].setY(1);
        I_OFFSET_DATA[1][4] = new Square(null);
        I_OFFSET_DATA[1][4].setX(0);
        I_OFFSET_DATA[1][4].setY(-2);

        I_OFFSET_DATA[2][0] = new Square(null);
        I_OFFSET_DATA[2][0].setX(-1);
        I_OFFSET_DATA[2][0].setY(1);
        I_OFFSET_DATA[2][1] = new Square(null);
        I_OFFSET_DATA[2][1].setX(1);
        I_OFFSET_DATA[2][1].setY(1);
        I_OFFSET_DATA[2][2] = new Square(null);
        I_OFFSET_DATA[2][2].setX(-2);
        I_OFFSET_DATA[2][2].setY(1);
        I_OFFSET_DATA[2][3] = new Square(null);
        I_OFFSET_DATA[2][3].setX(1);
        I_OFFSET_DATA[2][3].setY(0);
        I_OFFSET_DATA[2][4] = new Square(null);
        I_OFFSET_DATA[2][4].setX(-2);
        I_OFFSET_DATA[2][4].setY(0);

        I_OFFSET_DATA[3][0] = new Square(null);
        I_OFFSET_DATA[3][0].setX(0);
        I_OFFSET_DATA[3][0].setY(1);
        I_OFFSET_DATA[3][1] = new Square(null);
        I_OFFSET_DATA[3][1].setX(0);
        I_OFFSET_DATA[3][1].setY(1);
        I_OFFSET_DATA[3][2] = new Square(null);
        I_OFFSET_DATA[3][2].setX(0);
        I_OFFSET_DATA[3][2].setY(1);
        I_OFFSET_DATA[3][3] = new Square(null);
        I_OFFSET_DATA[3][3].setX(0);
        I_OFFSET_DATA[3][3].setY(-1);
        I_OFFSET_DATA[3][4] = new Square(null);
        I_OFFSET_DATA[3][4].setX(0);
        I_OFFSET_DATA[3][4].setY(2);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Awake();
        Font fontScore = new Font("fontScore", Font.PLAIN, 20);
        Font fontp = new Font("fontp", Font.PLAIN, 16);
        Font fontPause = new Font("fontPause", Font.BOLD, 50);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (pause) {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, 300, 600);
        }

        if (checkIfShapeHasNeighbour(currentShape, 2)) {
            fillBoard();
            currentShape = new Shape();
            if (boardLimitReached()) {
                gameover(g);
                return;
            }
            NORMAL--;
        }

        if (HighScoreDBManager.loadHighScore() < score) {
            HighScoreDBManager.saveHighScore(score);
        }

        g.setColor(Color.YELLOW);
        g.setFont(fontScore);
        g.drawString("Highscore", 315, 150);
        g.drawString(HighScoreDBManager.loadHighScore() + "", 315, 175);
        g.setColor(Color.WHITE);
        g.drawString("Score", 315, 300);
        g.drawString(score + "", 315, 325);
        g.setFont(fontp);
        g.drawString( "'P' = pause", 315, 480);

        checkIfBoardHasFilledRow();
        drawBoard(g);
        currentShape.draw(g);
        drawGameField(g);

        if (pause) {
            g.setColor(Color.RED);
            g.setFont(fontPause);
            g.drawString("PAUSED", 48, 280);
        }
    }

    public void gameover(Graphics g) {
        gameIsOver = true;
        score = 0;
        looper.stop();
        drawGameField(g);
        drawBoard(g);
        Font font = new Font("font", Font.BOLD, 50);
        Font fontTwo = new Font("font2", Font.ITALIC, 45);
        g.setColor(Color.BLACK);
        g.setFont(font);
        g.drawString("GAME OVER", 50, 279);
        g.drawString("GAME OVER", 49, 280);
        g.drawString("GAME OVER", 50, 281);
        g.drawString("GAME OVER", 51, 280);
        g.setColor(Color.RED);
        g.drawString("GAME OVER", 50, 280);
        g.setFont(fontTwo);
        g.setColor(Color.BLACK);
        g.drawString(score + "", 160, 324);
        g.drawString(score + "", 159, 325);
        g.drawString(score + "", 160, 326);
        g.drawString(score + "", 161, 325);
        g.setColor(Color.YELLOW);
        g.drawString(score + "", 160, 325);
        g.setColor(Color.BLACK);
        g.drawString( "'R' = Restart", 80, 359);
        g.drawString( "'R' = Restart", 79, 360);
        g.drawString( "'R' = Restart", 80, 361);
        g.drawString( "'R' = Restart", 81, 360);
        g.setColor(Color.YELLOW);
        g.drawString( "'R' = Restart", 80, 360);
    }


    public void drawGameField(Graphics g) {
        g.setColor(Color.WHITE);
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            g.drawLine(0, BLOCK_SIZE * row, BLOCK_SIZE * BOARD_WIDTH, BLOCK_SIZE * row);
        }
        for (int column = 0; column < BOARD_WIDTH + 1; column++) {
            g.drawLine(column * BLOCK_SIZE, 0, column * BLOCK_SIZE, BLOCK_SIZE * BOARD_HEIGHT);
        }
    }



    public void rotate(int direction) {
        int row = Integer.parseInt(currentShape.centerPoint.substring(0, 1));
        int col = Integer.parseInt(currentShape.centerPoint.substring(1));
        int centerX = currentShape.squareMatrix[row][col].getX();
        int centerY = currentShape.squareMatrix[row][col].getY();

        final int oldRotationIndex = currentShape.currentRotation;

        if (direction == 1) {
            if (currentShape.currentRotation == 0) {
                currentShape.currentRotation = 3;
            } else {
                currentShape.currentRotation -= 1;
            }
        } else {
            if (currentShape.currentRotation == 3) {
                currentShape.currentRotation = 0;
            } else {
                currentShape.currentRotation += 1;
            }
        }

        final int newRotationIndex = currentShape.currentRotation;

        Shape tempShape = new Shape(currentShape.random, currentShape.currentRotation);
        tempShape.centerPoint = currentShape.centerPoint;
        for (int testIndex = 0; testIndex < 4; testIndex++) {
            for (int i = 0; i < currentShape.squareMatrix.length; i++) {
                for (int j = 0; j < currentShape.squareMatrix[0].length; j++) {
                    if (currentShape.squareMatrix[i][j].getColor() != null) {
                        int relativeX = currentShape.squareMatrix[i][j].getX() - centerX;
                        int relativeY = currentShape.squareMatrix[i][j].getY() - centerY;
                        int newX;
                        int newY;


                        if (direction == 1) {
                            newX = (xRotationCounterClockwise[0] * relativeX) + (xRotationCounterClockwise[1] * relativeY);
                            newY = (yRotationCounterClockwise[0] * relativeX) + (yRotationCounterClockwise[1] * relativeY);
                        } else {
                            newX = (xRotationClockwise[0] * relativeX) + (xRotationClockwise[1] * relativeY);
                            newY = (yRotationClockwise[0] * relativeX) + (yRotationClockwise[1] * relativeY);
                        }

                        Square offset1 = I_OFFSET_DATA[oldRotationIndex][testIndex];
                        Square offset2 = I_OFFSET_DATA[newRotationIndex][testIndex];
                        int offset1XCoord = offset1.getX();
                        int offset1YCoord = offset1.getY();

                        int offset2XCoord = offset2.getX();
                        int offset2YCoord = offset2.getY();

                        int endOffsetX = offset1XCoord - offset2XCoord;
                        int endOffsetY = offset1YCoord - offset2YCoord;


                        tempShape.squareMatrix[i][j].setX(newX + centerX + endOffsetX);
                        tempShape.squareMatrix[i][j].setY(newY + centerY + endOffsetY);
                    }
                }
            }
            if (!checkIfShapeHasNeighbour(tempShape, 0) && !checkIfShapeHasNeighbour(tempShape, 1) && !checkIfShapeHasNeighbour(tempShape, 2)) {
                for (int i = 0; i < currentShape.squareMatrix.length; i++) {
                    for (int j = 0; j < currentShape.squareMatrix[0].length; j++) {
                        if (currentShape.squareMatrix[i][j].getColor() != null) {
                            currentShape.squareMatrix[i][j].setX(tempShape.squareMatrix[i][j].getX());
                            currentShape.squareMatrix[i][j].setY(tempShape.squareMatrix[i][j].getY());
                        }
                    }
                }
                return;
            }
        }
    }

    public boolean checkIfShapeHasNeighbour(Shape shape, int direction) {
        for (Square[] squareArray : shape.squareMatrix) {
            for (Square square : squareArray) {
                if (square.getColor() != null) {
                    int x = square.getX();
                    int y = square.getY();
                    switch (direction) {
                        case 0: //check right
                            if (x > 9 || board[y][x + 1] != null) {
                                return true;
                            }
                            break;
                        case 1: //check left
                            if (x <= 0 || board[y][x - 1] != null) {
                                return true;
                            }
                            break;
                        case 2: //check bottom
                            if (y >= 19 || board[y + 1][x] != null) {
                                return true;
                            }
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid direction");
                    }
                }
            }
        }
        return false;
    }

    public void checkIfBoardHasFilledRow() {
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

    private void emptyFilledRow(int rowNumber) {
        for (int i = 0; i < 10; i++) {
            board[rowNumber][i] = null;
        }

        score += 100;

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

    public boolean boardLimitReached() {
        for (int i = 3; i < 7; i++) {
            if (board[0][i] != null) {
                return true;
            }
        }
        return false;
    }

    public void resetBoard() {
        for (Square[] squares : board) {
            Arrays.fill(squares, null);
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

    public void fillBoard() {
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
        if (e.getKeyCode() == KeyEvent.VK_Q && !pause) {
            rotate(1);
        }
        if (e.getKeyCode() == KeyEvent.VK_E && !pause) {
            rotate(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_A && !checkIfShapeHasNeighbour(currentShape, 1) && !pause) {
            currentShape.moveLeft();
        }
        if (e.getKeyCode() == KeyEvent.VK_D && !checkIfShapeHasNeighbour(currentShape, 0) && !pause) {
            currentShape.moveRight();
        }
        if (e.getKeyCode() == KeyEvent.VK_S && !checkIfShapeHasNeighbour(currentShape, 2) && !pause) {
            delayTimeForMovement = FAST;
        }
        if (e.getKeyCode() == KeyEvent.VK_P) {
            pause = !pause;
        }
        if (e.getKeyCode() == KeyEvent.VK_R && gameIsOver) {
            resetBoard();
            looper.start();
            NORMAL = 700;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A) {
            delayTimeForMovement = NORMAL;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            delayTimeForMovement = NORMAL;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            delayTimeForMovement = NORMAL;
        }
    }
}
