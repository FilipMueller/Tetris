import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class WindowGame {

    private static final int WIDTH = 445, HEIGHT = 630;

    private JFrame window;

    private Board board;

    private KeyListener keyListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {
        }
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                System.out.println("UP");
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                System.out.println("LEFT");
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                System.out.println("RIGHT");
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                System.out.println("DOWN");
            }
        }
        @Override
        public void keyReleased(KeyEvent e) {
        }
    };

    public WindowGame() {
        window = new JFrame("Tetris");
        window.setSize(WIDTH, HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        board = new Board();
        window.addKeyListener(keyListener);
        window.add(board);
        window.setVisible(true);

    }

     public static void main(String[] args) {
        new WindowGame();
    }
}
