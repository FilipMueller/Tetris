import java.awt.*;

public class BoardGraphics {

    private final Font bold = new Font("font", Font.BOLD, 50);

    private final Font italic = new Font("font2", Font.ITALIC, 45);

    private final Font score = new Font("fontScore", Font.PLAIN, 20);

    private final Font pauseKey = new Font("fontPauseKey", Font.PLAIN, 16);

    private final Font pause = new Font("fontPause", Font.BOLD, 50);

    public BoardGraphics() {

    }

    public void drawGameField(Graphics g) {
        g.setColor(Color.WHITE);
        for (int row = 0; row < Board.Constants.BOARD_HEIGHT; row++) {
            g.drawLine(0, Board.Constants.BLOCK_SIZE * row, Board.Constants.BLOCK_SIZE * Board.Constants.BOARD_WIDTH, Board.Constants.BLOCK_SIZE * row);
        }
        for (int column = 0; column < Board.Constants.BOARD_WIDTH + 1; column++) {
            g.drawLine(column * Board.Constants.BLOCK_SIZE, 0, column * Board.Constants.BLOCK_SIZE, Board.Constants.BLOCK_SIZE * Board.Constants.BOARD_HEIGHT);
        }
    }

    public void drawBoard(Graphics g, Square[][] board) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                if (board[row][col] != null) {
                    g.setColor(board[row][col].getColor());
                    g.fillRect(board[row][col].getX() * Board.Constants.BLOCK_SIZE, board[row][col].getY() * Board.Constants.BLOCK_SIZE, Board.Constants.BLOCK_SIZE, Board.Constants.BLOCK_SIZE);
                }
            }
        }
    }

    public void drawBackGround(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Board.Constants.BOARD_WIDTH * 43, Board.Constants.BOARD_HEIGHT * 30);
    }
    public void drawSideInfo(Graphics g, int highscore, int score) {
        g.setColor(Color.YELLOW);
        g.setFont(this.score);
        g.drawString("Highscore", 315, 150);
        g.drawString(highscore + "", 315, 175);
        g.setColor(Color.WHITE);
        g.drawString("Score", 315, 300);
        g.drawString(score + "", 315, 325);
        g.setFont(pauseKey);
        g.drawString( "'P' = pause", 315, 480);
        g.drawString("next", 350, 25);
    }

    public void drawPauseScreen(Graphics g, Square[][] board, Shape currentShape) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, 300, 600);
        drawBoard(g, board);
        currentShape.draw(g);
        drawGameField(g);
        g.setColor(Color.RED);
        g.setFont(pause);
        g.drawString("PAUSED", 48, 280);
    }

    public void drawGameOverScreen(Graphics g, int score) {
        g.setColor(Color.BLACK);
        g.setFont(bold);
        g.drawString("GAME OVER", 50, 279);
        g.drawString("GAME OVER", 49, 280);
        g.drawString("GAME OVER", 50, 281);
        g.drawString("GAME OVER", 51, 280);
        g.setColor(Color.RED);
        g.drawString("GAME OVER", 50, 280);
        g.setFont(italic);
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
}
