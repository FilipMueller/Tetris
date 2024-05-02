import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
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

    Shape currentShape = new Shape();

    private int paintBackground = 0;

    Timer looper;

    public Board() {
        looper = new Timer(delay, _ -> {
            if (System.currentTimeMillis() - beginTime > delayTimeForMovement) {
                if (!checkIfHasNeighbour(2) && !pause) currentShape.moveDown();
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

    public void writeHighscoreToCSV(int score) {
        try(FileWriter writer = new FileWriter("src/highscore.txt")) {
            writer.append(String.valueOf(score));
        } catch (IOException e) {
            throw new RuntimeException("WRITE FAILED");
        }
    }

    public int readHighscoreFromCSV() {
        int highscore = 0;
        try (InputStream inputStream = getClass().getResourceAsStream("/highscore.txt")) {
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = reader.readLine();
                if (line != null) {
                    highscore = Integer.parseInt(line.trim());
                }
            } else {
                throw new RuntimeException("File not found");
            }
        } catch (IOException e) {
            throw new RuntimeException("ERROR reading File");
        }
        return highscore;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Font fontScore = new Font("fontScore", Font.PLAIN, 20);
        Font fontp = new Font("fontp", Font.PLAIN, 16);
        Font fontPause = new Font("fontPause", Font.BOLD, 50);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (pause) {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, 300, 600);
        }

        if (checkIfHasNeighbour(2)) {
            fillArray();
            currentShape = new Shape();
            if (board[0][5] != null) {
                gameover(g);
                return;
            }
            NORMAL--;
        }

        if (readHighscoreFromCSV() < score) {
            writeHighscoreToCSV(score);
        }

        g.setColor(Color.YELLOW);
        g.setFont(fontScore);
        g.drawString("Highscore", 315, 150);
        g.drawString(readHighscoreFromCSV() + "", 315, 175);
        g.setColor(Color.WHITE);
        g.drawString("Score", 315, 300);
        g.drawString(score + "", 315, 325);
        g.setFont(fontp);
        g.drawString( "'w' = pause", 315, 480);

        checkIfRowIsFilled();
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
        g.setColor(Color.YELLOW);
        g.drawString(score + "", 200, 325);
        g.drawString( "'r' = restart", 100, 360);
    }

    public void resetBoard() {
        for (Square[] squares : board) {
            Arrays.fill(squares, null);
        }
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

        Square[][] tempMatrix = new Square[currentShape.squareMatrix.length][currentShape.squareMatrix[0].length];

        int counter = 0;

        for (int i = 0; i < currentShape.squareMatrix.length; i++) {
            for (int j = 0; j < currentShape.squareMatrix[0].length; j++) {
                if (currentShape.squareMatrix[i][j].getColor() != null) {
                    int x = currentShape.squareMatrix[i][j].getX() - centerX;
                    int y = currentShape.squareMatrix[i][j].getY() - centerY;
                    int newX;
                    int newY;

                    if (direction == 1) {
                        newX = (xRotationCounterClockwise[0] * x) + (xRotationCounterClockwise[1] * y);
                        newY = (yRotationCounterClockwise[0] * x) + (yRotationCounterClockwise[1] * y);
                    } else {
                        newX = (xRotationClockwise[0] * x) + (xRotationClockwise[1] * y);
                        newY = (yRotationClockwise[0] * x) + (yRotationClockwise[1] * y);
                    }


                    tempMatrix[i][j] = new Square(null);
                    tempMatrix[i][j].setX(newX + centerX);
                    tempMatrix[i][j].setY(newY + centerY);

                    if (tempMatrix[i][j].getX() > 9 || tempMatrix[i][j].getX() < 0 || tempMatrix[i][j].getY() < 0 || checkIfHasNeighbour(0) || checkIfHasNeighbour(1) || checkIfHasNeighbour(2)) {
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
                            if (y >= board.length - 1 || board[y + 1][x] != null) {
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
        if (e.getKeyCode() == KeyEvent.VK_Q && !pause) {
            rotate(1);
        }
        if (e.getKeyCode() == KeyEvent.VK_E && !pause) {
            rotate(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_A && (!checkIfHasNeighbour(1)) && !pause) {
            currentShape.moveLeft();
        }
        if (e.getKeyCode() == KeyEvent.VK_D && (!checkIfHasNeighbour(0)) && !pause) {
            currentShape.moveRight();
        }
        if (e.getKeyCode() == KeyEvent.VK_S && !checkIfHasNeighbour(2) && !pause) {
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
