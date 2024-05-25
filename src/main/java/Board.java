import Database.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;
import java.util.Arrays;

public class Board  extends JPanel implements KeyListener {

    private final Constants constants = new Constants();

    public Board() {
        constants.looper = new Timer(Constants.delay, _ -> {
            if (System.currentTimeMillis() - constants.beginTime > constants.delayTimeForMovement) {
                if (!checkIfShapeHasNeighbour(constants.currentShape, 2) && !constants.pause)
                    constants.currentShape.moveDown();
                constants.beginTime = System.currentTimeMillis();
            }
            if (!constants.pause) {
                constants.paintBackground = 0;
                repaint();
            }

            if (constants.pause && constants.paintBackground == 0) {
                repaint();
                constants.paintBackground = 1;
            }

        });
        constants.looper.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Font fontScore = new Font("fontScore", Font.PLAIN, 20);
        Font fontPauseKey = new Font("fontPauseKey", Font.PLAIN, 16);
        Font fontPause = new Font("fontPause", Font.BOLD, 50);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (constants.pause) {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, 300, 600);
        }

        if (checkIfShapeHasNeighbour(constants.currentShape, 2)) {
            fillBoardWithCurrentShape();
            constants.currentShape = new Shape(constants.nextShape.getShapeType(), constants.nextShape.getRotationIndex());
            constants.nextShape = new Shape();
            if (boardLimitReached()) {
                gameover(g);
                return;
            }
            Constants.NORMAL--;
        }

        Constants.helper.readScores();
        int highscore = Constants.helper.getCurrentScore();

        if (highscore < constants.score) {
            Constants.helper.writeScore(constants.score);
        }

        g.setColor(Color.YELLOW);
        g.setFont(fontScore);
        g.drawString("Highscore", 315, 150);
        g.drawString(highscore + "", 315, 175);
        g.setColor(Color.WHITE);
        g.drawString("Score", 315, 300);
        g.drawString(constants.score + "", 315, 325);
        g.setFont(fontPauseKey);
        g.drawString( "'P' = pause", 315, 480);
        g.drawString("next", 350, 25);

        checkIfBoardHasFilledRow();
        drawBoard(g);
        constants.currentShape.draw(g);
        constants.nextShape.drawNext(g);
        createLandingLocationShape(g);
        drawGameField(g);

        if (constants.pause) {
            g.setColor(Color.RED);
            g.setFont(fontPause);
            g.drawString("PAUSED", 48, 280);
        }
    }

    private void gameover(Graphics g) {
        constants.gameIsOver = true;
        constants.score = 0;
        constants.looper.stop();
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
        g.drawString(constants.score + "", 160, 324);
        g.drawString(constants.score + "", 159, 325);
        g.drawString(constants.score + "", 160, 326);
        g.drawString(constants.score + "", 161, 325);
        g.setColor(Color.YELLOW);
        g.drawString(constants.score + "", 160, 325);
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
        for (int row = 0; row < Constants.BOARD_HEIGHT; row++) {
            g.drawLine(0, Constants.BLOCK_SIZE * row, Constants.BLOCK_SIZE * Constants.BOARD_WIDTH, Constants.BLOCK_SIZE * row);
        }
        for (int column = 0; column < Constants.BOARD_WIDTH + 1; column++) {
            g.drawLine(column * Constants.BLOCK_SIZE, 0, column * Constants.BLOCK_SIZE, Constants.BLOCK_SIZE * Constants.BOARD_HEIGHT);
        }
    }

    private void rotate(boolean direction) {
        final int[] centerCoords = getCenterCoordinates(constants.currentShape);
        final int globalX = centerCoords[0];
        final int globalY = centerCoords[1];

        final int oldRotationIndex = constants.currentShape.getRotationIndex();
        final int newRotationIndex = getNewRotationIndex(direction, oldRotationIndex);
        constants.currentShape.setRotationIndex(newRotationIndex);

        final Square[][] currentOffsetData = getCurrentOffsetData(constants.currentShape.getShapeType());
        Shape tempShape = createTemporaryShape(constants.currentShape);

        for (int testIndex = 0; testIndex < currentOffsetData[0].length; testIndex++) {
            for (int i = 0; i < constants.currentShape.squareMatrix.length; i++) {
                for (int j = 0; j < constants.currentShape.squareMatrix[0].length; j++) {
                    if (constants.currentShape.squareMatrix[i][j].getColor() != null) {
                        final int relativeX = constants.currentShape.squareMatrix[i][j].getX() - globalX;
                        final int relativeY = constants.currentShape.squareMatrix[i][j].getY() - globalY;
                        final int newX;
                        final int newY;

                        if (direction) {
                            newX = (constants.xRotationClockwise[0] * relativeX) + (constants.xRotationClockwise[1] * relativeY);
                            newY = (constants.yRotationClockwise[0] * relativeX) + (constants.yRotationClockwise[1] * relativeY);
                        } else {
                            newX = (constants.xRotationCounterClockwise[0] * relativeX) + (constants.xRotationCounterClockwise[1] * relativeY);
                            newY = (constants.yRotationCounterClockwise[0] * relativeX) + (constants.yRotationCounterClockwise[1] * relativeY);
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
                updateCurrentShape(tempShape);
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

    private boolean isRotationValid(Shape shape) {
        return !checkIfShapeHasNeighbour(shape, 3) &&
        !checkIfShapeHasNeighbour(shape, 4) &&
        !checkIfShapeHasNeighbour(shape, 2);
    }

    private void updateCurrentShape(Shape shape) {
        for (int i = 0; i < constants.currentShape.squareMatrix.length; i++) {
            for (int j = 0; j < constants.currentShape.squareMatrix[0].length; j++) {
                if (constants.currentShape.squareMatrix[i][j].getColor() != null) {
                    constants.currentShape.squareMatrix[i][j].setX(shape.squareMatrix[i][j].getX());
                    constants.currentShape.squareMatrix[i][j].setY(shape.squareMatrix[i][j].getY());
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
                            if (x >= 9 || constants.board[y][x + 1] != null) {
                                return true;
                            }
                            break;
                        case 1: //check left
                            if (x <= 0 || constants.board[y][x - 1] != null) {
                                return true;
                            }
                            break;
                        case 2: //check bottom
                            if (y >= 19 || constants.board[y + 1][x] != null) {
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
        for (Square[] squareArray : constants.board) {
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
            constants.board[rowNumber][i] = null;
        }

        constants.score = constants.score + 100;

        for (int i = rowNumber - 1; i >= 0; i--) {
            for (int j = 0; j < 10; j++) {
                if (constants.board[i][j] != null) {
                    constants.board[i + 1][j] = new Square(constants.board[i][j].getColor());
                    constants.board[i + 1][j].setX(constants.board[i][j].getX());
                    constants.board[i + 1][j].setY(constants.board[i][j].getY() + 1);
                    constants.board[i][j] = null;
                }
            }
        }
    }

    private boolean boardLimitReached() {
        for (int i = 3; i < 7; i++) {
            if (constants.board[0][i] != null) {
                return true;
            }
        }
        return false;
    }

    private void resetBoard() {
        for (Square[] squares : constants.board) {
            Arrays.fill(squares, null);
        }
    }

    private void drawBoard(Graphics g) {
        for (int row = 0; row < constants.board.length; row++) {
            for (int col = 0; col < constants.board[0].length; col++) {
                if (constants.board[row][col] != null) {
                    g.setColor(constants.board[row][col].getColor());
                    g.fillRect(constants.board[row][col].getX() * Constants.BLOCK_SIZE, constants.board[row][col].getY() * Constants.BLOCK_SIZE, Constants.BLOCK_SIZE, Constants.BLOCK_SIZE);
                }
            }
        }
    }

    private void createLandingLocationShape(Graphics g) {
        Shape tempShape = createTemporaryShape(constants.currentShape);
        for (int y = 1; y < constants.board.length; y++) {
            for (int i = 0; i < constants.currentShape.squareMatrix.length; i++) {
                for (int j = 0; j < constants.currentShape.squareMatrix[0].length; j++) {
                    if (tempShape.squareMatrix[i][j].getColor() != null) {
                        tempShape.squareMatrix[i][j].setX(constants.currentShape.squareMatrix[i][j].getX());
                        tempShape.squareMatrix[i][j].setY(constants.currentShape.squareMatrix[i][j].getY() + y);
                    }
                }
            }
            if (checkIfShapeHasNeighbour(tempShape, 2)) {
                tempShape.drawLandingLocation(g);
                return;
            }
        }
    }

    private void fillBoardWithCurrentShape() {
        for (Square[] squareArray : constants.currentShape.getSquareMatrix()) {
            for (Square square : squareArray) {
                int x = square.getX();
                int y = square.getY();
                if (square.getColor() != null) {
                    constants.board[y][x] = new Square(square.getColor());
                    constants.board[y][x].setX(x);
                    constants.board[y][x].setY(y);
                }
            }
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_Q && !constants.pause) {
            rotate(false);
        }
        if (e.getKeyCode() == KeyEvent.VK_E && !constants.pause) {
            rotate(true);
        }
        if (e.getKeyCode() == KeyEvent.VK_A && !checkIfShapeHasNeighbour(constants.currentShape, 1) && !constants.pause) {
            constants.currentShape.moveLeft();
        }
        if (e.getKeyCode() == KeyEvent.VK_D && !checkIfShapeHasNeighbour(constants.currentShape, 0) && !constants.pause) {
            constants.currentShape.moveRight();
        }
        if (e.getKeyCode() == KeyEvent.VK_S && !checkIfShapeHasNeighbour(constants.currentShape, 2) && !constants.pause) {
            constants.delayTimeForMovement = Constants.FAST;
        }
        if (e.getKeyCode() == KeyEvent.VK_P) {
            constants.pause = !constants.pause;
        }
        if (e.getKeyCode() == KeyEvent.VK_R && constants.gameIsOver) {
            resetBoard();
            constants.looper.start();
            Constants.NORMAL = 700;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A) {
            constants.delayTimeForMovement = Constants.NORMAL;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            constants.delayTimeForMovement = Constants.NORMAL;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            constants.delayTimeForMovement = Constants.NORMAL;
        }
    }

    public class Constants {
        static final int BOARD_WIDTH = 10;
        static final int BOARD_HEIGHT = 20;
        static final int BLOCK_SIZE = 30;
        static final int FPS = 60;
        static final int delay = 1000 / FPS;
        static int NORMAL = 700;
        static final int FAST = 40;
        final int[] xRotationCounterClockwise = {0, 1};
        final int[] yRotationCounterClockwise = {-1, 0};
        final int[] xRotationClockwise = {0, -1};
        final int[] yRotationClockwise = {1, 0};
        int delayTimeForMovement = NORMAL;
        long beginTime;
        int score = 0;
        boolean pause = false;
        boolean gameIsOver = false;
        final Square[][] board = new Square[BOARD_HEIGHT][BOARD_WIDTH];
        Shape currentShape = new Shape();
        Shape nextShape = new Shape();
        int paintBackground = 0;
        Timer looper;
        static final DatabaseManager helper = new DatabaseManager();

        public Constants() {
        }
    }
}
