import java.awt.*;

public class Shape {

    final Square[][] squareMatrix;

    private boolean initialSpawn = true;

    private static final int BLOCK_SIZE = 30;

    public Shape() {
        int random = (int) (Math.random() * 7);
        if (random == 0) {
            squareMatrix = new Square[][]{
                    {new Square(Color.CYAN), new Square(Color.CYAN), new Square(Color.CYAN), new Square(Color.CYAN)}
            };
        }
        else if (random == 1) {
            squareMatrix = new Square[][]{
                    {new Square(Color.BLUE), new Square(null), new Square(null)},
                    {new Square(Color.BLUE), new Square(Color.BLUE), new Square(Color.BLUE)}
            };
        }
        else if (random == 2) {
            squareMatrix = new Square[][]{
                    {new Square(null), new Square(null), new Square(Color.ORANGE)},
                    {new Square(Color.ORANGE), new Square(Color.ORANGE), new Square(Color.ORANGE)}
            };
        }
        else if (random == 3) {
            squareMatrix = new Square[][]{
                    {new Square(Color.YELLOW), new Square(Color.YELLOW)},
                    {new Square(Color.YELLOW), new Square(Color.YELLOW)}
            };
        }
        else if (random == 4) {
            squareMatrix = new Square[][]{
                    {new Square(null), new Square(Color.GREEN), new Square(Color.GREEN)},
                    {new Square(Color.GREEN), new Square(Color.GREEN), new Square(null)}
            };
        }
        else if (random == 5) {
            squareMatrix = new Square[][]{
                    {new Square(null), new Square(Color.PINK), new Square(null)},
                    {new Square(Color.PINK), new Square(Color.PINK), new Square(Color.PINK)}
            };
        }
        else {
            squareMatrix = new Square[][]{
                    {new Square(Color.RED), new Square(Color.RED), new Square(null)},
                    {new Square(null), new Square(Color.RED), new Square(Color.RED)}
            };
        }
    }

    public void draw(Graphics g) {
        for (int row = 0; row < squareMatrix.length; row++) {
            for (int col = 0; col < squareMatrix[0].length; col++) {
                if (squareMatrix[row][col].getColor() != null) {
                    g.setColor(squareMatrix[row][col].getColor());
                    if (initialSpawn) {
                        squareMatrix[row][col].setX(row + 4);
                        squareMatrix[row][col].setY(col);
                    }
                    if (!initialSpawn) {
                        g.fillRect(squareMatrix[row][col].getX() * BLOCK_SIZE, squareMatrix[row][col].getY() * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                    }
                }
            }
        }
        initialSpawn = false;
    }

    public void spin() {

    }

    public void moveDown() {
        for (int i = 0; i < squareMatrix.length; i++) {
            for (int j = 0; j < squareMatrix[0].length; j++) {
                if (squareMatrix[i][j] != null) {
                    squareMatrix[i][j].setY(squareMatrix[i][j].getY() + 1);
                }
            }
        }
    }

    public void moveLeft() {
        for (int i = 0; i < squareMatrix.length; i++) {
            for (int j = 0; j < squareMatrix[0].length; j++) {
                if (squareMatrix[i][j] != null) {
                    squareMatrix[i][j].setX(squareMatrix[i][j].getX() - 1);
                }
            }
        }
    }

    public void moveRight() {
        for (int i = 0; i < squareMatrix.length; i++) {
            for (int j = 0; j < squareMatrix[0].length; j++) {
                if (squareMatrix[i][j] != null) {
                    squareMatrix[i][j].setX(squareMatrix[i][j].getX() + 1);
                }
            }
        }
    }
}
