import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Board  extends JPanel implements KeyListener {

    private static final int BOARD_WIDTH = 10;

    private static final int BOARD_HEIGHT = 20;

    private final Color[][] board = new Color[BOARD_WIDTH][BOARD_HEIGHT];

    private static final int BLOCK_SIZE = 30;

    private static int FPS = 60;

    private static int delay = 1000 / FPS;

    private int delayTimeForMovement = 600;

    private long beginTime;

    Shape currentShape = new Shape();

    private Timer looper;

    public Board() {
        looper = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (System.currentTimeMillis() - beginTime > delayTimeForMovement) {
                    System.out.println(System.currentTimeMillis());
                    currentShape.moveDown();
                    beginTime = System.currentTimeMillis();
                }
                repaint();
            }
        });
        looper.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        currentShape.draw(g);

        g.setColor(Color.WHITE);
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            g.drawLine(0, BLOCK_SIZE * row, BLOCK_SIZE * BOARD_WIDTH, BLOCK_SIZE * row);
        }
        g.setColor(Color.WHITE);
        for (int column = 0; column < BOARD_WIDTH + 1; column++) {
            g.drawLine(column * BLOCK_SIZE, 0, column * BLOCK_SIZE, BLOCK_SIZE * BOARD_HEIGHT);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            currentShape.moveLeft();
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            currentShape.moveRight();
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            delayTimeForMovement = 50;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            delayTimeForMovement = 600;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            delayTimeForMovement = 600;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            delayTimeForMovement = 600;
        }


    }
}
