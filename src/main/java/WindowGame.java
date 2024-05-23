import Database.DatabaseInitializer;

import javax.swing.*;

public class WindowGame {

    private static final int WIDTH = 445, HEIGHT = 639;

    public WindowGame() {
        DatabaseInitializer x = new DatabaseInitializer();
        x.initialize();
        JFrame window = new JFrame("Tetris");
        window.setSize(WIDTH, HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setLocationRelativeTo(null);

        Board board = new Board();
        window.addKeyListener(board);
        window.add(board);
        window.setVisible(true);

    }

     public static void main(String[] args) {
        new WindowGame();
    }
}
