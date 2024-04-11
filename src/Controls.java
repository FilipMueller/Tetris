import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controls {

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

    public void moveShape() {


    }


    public Controls() {


    }
}
