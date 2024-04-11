import java.awt.*;

public class Square {

    int x;
    int y;
    static final int BLOCK_SIZE = 30;
    Color color;

    public Square(Color color) {
        this.color = color;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
