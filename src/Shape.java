import java.awt.*;

public class Shape {

    final Square[][] squareMatrix;

    private boolean initialSpawn = true;

    private static final int BLOCK_SIZE = 30;

    String centerPoint;

    int random;

    public Shape(int random) {
        this.random = random;
        squareMatrix = getSquareMatrix(random);
    }


    public Shape() {
        random = (int) (Math.random() * 7);
        squareMatrix = getSquareMatrix(random);
    }

    private Square[][] getSquareMatrix(int random) {
        final Square[][] squareMatrix;
        if (random == 0) {
            squareMatrix = new Square[][]{
                    {new Square(Color.CYAN), new Square(Color.CYAN), new Square(Color.CYAN), new Square(Color.CYAN)}
            };
            this.random = 0;
            this.centerPoint = "01";
        }
        else if (random == 1) {
            squareMatrix = new Square[][]{
                    {new Square(Color.BLUE), new Square(null), new Square(null)},
                    {new Square(Color.BLUE), new Square(Color.BLUE), new Square(Color.BLUE)}
            };
            this.random = 1;
            this.centerPoint = "11";
        }
        else if (random == 2) {
            squareMatrix = new Square[][]{
                    {new Square(null), new Square(null), new Square(Color.ORANGE)},
                    {new Square(Color.ORANGE), new Square(Color.ORANGE), new Square(Color.ORANGE)}
            };
            this.random = 2;
            this.centerPoint = "11";
        }
        else if (random == 3) {
            squareMatrix = new Square[][]{
                    {new Square(Color.YELLOW), new Square(Color.YELLOW)},
                    {new Square(Color.YELLOW), new Square(Color.YELLOW)}
            };
            this.random = 3;
            this.centerPoint = "10";
        }
        else if (random == 4) {
            squareMatrix = new Square[][]{
                    {new Square(null), new Square(Color.GREEN), new Square(Color.GREEN)},
                    {new Square(Color.GREEN), new Square(Color.GREEN), new Square(null)}
            };
            this.random = 4;
            this.centerPoint = "11";
        }
        else if (random == 5) {
            squareMatrix = new Square[][]{
                    {new Square(null), new Square(Color.PINK), new Square(null)},
                    {new Square(Color.PINK), new Square(Color.PINK), new Square(Color.PINK)}
            };
            this.random = 5;
            this.centerPoint = "11";
        }
        else {
            squareMatrix = new Square[][]{
                    {new Square(Color.RED), new Square(Color.RED), new Square(null)},
                    {new Square(null), new Square(Color.RED), new Square(Color.RED)}
            };
            this.random = 6;
            this.centerPoint = "11";
        }
        return squareMatrix;
    }

    public void draw(Graphics g) {
        for (int row = 0; row < squareMatrix.length; row++) {
            for (int col = 0; col < squareMatrix[0].length; col++) {
                if (squareMatrix[row][col].getColor() != null) {
                    g.setColor(squareMatrix[row][col].getColor());
                    if (initialSpawn) {
                        squareMatrix[row][col].setX(col + 4);
                        squareMatrix[row][col].setY(row);
                    }
                    if (!initialSpawn) {
                        g.fillRect(squareMatrix[row][col].getX() * BLOCK_SIZE, squareMatrix[row][col].getY() * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                    }
                }
            }
        }
        initialSpawn = false;
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
