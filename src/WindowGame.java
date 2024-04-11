import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class WindowGame {

    private static final int WIDTH = 445, HEIGHT = 630;

    private JFrame window;

    final private Board board;

    private Controls controls;


    public WindowGame() {
        window = new JFrame("Tetris");
        window.setSize(WIDTH, HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        board = new Board();
        window.addKeyListener(board);
        window.add(board);
        window.setVisible(true);

    }

     public static void main(String[] args) {
        new WindowGame();
    }
}
