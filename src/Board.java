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

    public Board() {
        looper = new Timer(delay, _ -> {
            if (System.currentTimeMillis() - beginTime > delayTimeForMovement) {
                if (!checkIfShapeHasNeighbour(currentShape, 2) && !pause) currentShape.moveDown();
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Font fontScore = new Font("fontScore", Font.PLAIN, 20);
        Font fontPauseKey = new Font("fontPauseKey", Font.PLAIN, 16);
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
        g.setFont(fontPauseKey);
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

    private void gameover(Graphics g) {
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

    private void drawGameField(Graphics g) {
        g.setColor(Color.WHITE);
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            g.drawLine(0, BLOCK_SIZE * row, BLOCK_SIZE * BOARD_WIDTH, BLOCK_SIZE * row);
        }
        for (int column = 0; column < BOARD_WIDTH + 1; column++) {
            g.drawLine(column * BLOCK_SIZE, 0, column * BLOCK_SIZE, BLOCK_SIZE * BOARD_HEIGHT);
        }
    }

    private void rotate(boolean direction) {
        final int[] centerCoords = getCenterCoordinates(currentShape);
        final int globalX = centerCoords[0];
        final int globalY = centerCoords[1];

        final int oldRotationIndex = currentShape.getRotationIndex();
        final int newRotationIndex = getNewRotationIndex(direction, oldRotationIndex);
        currentShape.setRotationIndex(newRotationIndex);

        final Square[][] currentOffsetData = getCurrentOffsetData(currentShape.getShapeType());
        Shape tempShape = createTemporaryShape(currentShape);

        for (int testIndex = 0; testIndex < currentOffsetData[0].length; testIndex++) {
            for (int i = 0; i < currentShape.squareMatrix.length; i++) {
                for (int j = 0; j < currentShape.squareMatrix[0].length; j++) {
                    if (currentShape.squareMatrix[i][j].getColor() != null) {
                        final int relativeX = currentShape.squareMatrix[i][j].getX() - globalX;
                        final int relativeY = currentShape.squareMatrix[i][j].getY() - globalY;
                        final int newX;
                        final int newY;

                        if (direction) {
                            newX = (xRotationClockwise[0] * relativeX) + (xRotationClockwise[1] * relativeY);
                            newY = (yRotationClockwise[0] * relativeX) + (yRotationClockwise[1] * relativeY);
                        } else {
                            newX = (xRotationCounterClockwise[0] * relativeX) + (xRotationCounterClockwise[1] * relativeY);
                            newY = (yRotationCounterClockwise[0] * relativeX) + (yRotationCounterClockwise[1] * relativeY);
                        }

                        Square offset1 = currentOffsetData[oldRotationIndex][testIndex];
                        Square offset2 = currentOffsetData[newRotationIndex][testIndex];

                        final int offset1X = offset1.getX();
                        final int offset1Y = offset1.getY();

                        final int offset2X = offset2.getX();
                        final int offset2Y = offset2.getY();

                        final int endOffsetX = offset1X - offset2X;
                        final int endOffsetY = offset1Y - offset2Y;


                        tempShape.squareMatrix[i][j].setX(newX + globalX + endOffsetX);
                        tempShape.squareMatrix[i][j].setY(newY + globalY + endOffsetY);
                    }
                }
            }
            if (isRotationValid(tempShape)) {
                updateCurrentShapeCoordinates(tempShape);
                return;
            }
        }
    }

    private int[] getCenterCoordinates(Shape shape) {
        final int row = Integer.parseInt(shape.getCenterPoint().substring(0, 1));
        final int col = Integer.parseInt(shape.getCenterPoint().substring(1));
        final int globalX = shape.squareMatrix[row][col].getX();
        final int globalY = shape.squareMatrix[row][col].getY();
        return new int[]{globalX, globalY};
    }

    private int getNewRotationIndex(boolean direction, int oldRotationIndex) {
        if (direction) {
            return (oldRotationIndex == 3) ? 0 : oldRotationIndex + 1;
        } else {
            return (oldRotationIndex == 0) ? 3 : oldRotationIndex - 1;
        }
    }

    private Square[][] getCurrentOffsetData(ShapeType shapeType) {
        return switch (shapeType) {
            case O_Shape -> Shape.getOOffsetData();
            case I_Shape -> Shape.getIOffsetData();
            default -> Shape.getJLSTZOffsetData();
        };
    }

    private Shape createTemporaryShape(Shape currentShape) {
        Shape tempShape = new Shape(currentShape.getShapeType(), currentShape.getRotationIndex());
        tempShape.setCenterPoint(currentShape.getCenterPoint());
        return tempShape;
    }

    private boolean isRotationValid(Shape tempShape) {
        return !checkIfShapeHasNeighbour(tempShape, 3) &&
        !checkIfShapeHasNeighbour(tempShape, 4) &&
        !checkIfShapeHasNeighbour(tempShape, 2);
    }

    private void updateCurrentShapeCoordinates(Shape tempShape) {
        for (int i = 0; i < currentShape.squareMatrix.length; i++) {
            for (int j = 0; j < currentShape.squareMatrix[0].length; j++) {
                if (currentShape.squareMatrix[i][j].getColor() != null) {
                    currentShape.squareMatrix[i][j].setX(tempShape.squareMatrix[i][j].getX());
                    currentShape.squareMatrix[i][j].setY(tempShape.squareMatrix[i][j].getY());
                }
            }
        }
    }

    private boolean checkIfShapeHasNeighbour(Shape shape, int direction) {
        for (Square[] squareArray : shape.getSquareMatrix()) {
            for (Square square : squareArray) {
                if (square.getColor() != null) {
                    int x = square.getX();
                    int y = square.getY();
                    switch (direction) {
                        case 0: //check right
                            if (x >= 9 || board[y][x + 1] != null) {
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
                        case 3:
                            if (x > 9) {
                                return true;
                            }
                            break;
                        case 4:
                            if (x < 0) {
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

    private void checkIfBoardHasFilledRow() {
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

    private boolean boardLimitReached() {
        for (int i = 3; i < 7; i++) {
            if (board[0][i] != null) {
                return true;
            }
        }
        return false;
    }

    private void resetBoard() {
        for (Square[] squares : board) {
            Arrays.fill(squares, null);
        }
    }

    private void drawBoard(Graphics g) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                if (board[row][col] != null) {
                    g.setColor(board[row][col].getColor());
                    g.fillRect(board[row][col].getX() * BLOCK_SIZE, board[row][col].getY() * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
    }

    private void fillBoard() {
        for (Square[] squareArray : currentShape.getSquareMatrix()) {
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
            rotate(false);
        }
        if (e.getKeyCode() == KeyEvent.VK_E && !pause) {
            rotate(true);
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
