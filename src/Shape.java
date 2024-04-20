import java.awt.*;

public class Shape {

    final Square[][] squareMatrix;

    private boolean initialSpawn = true;

    private static final int BLOCK_SIZE = 30;

    private String centerPoint;

    private final int[] xRotationCounterClockwise = {0, 1};

    private final int[] yRotationCounterClockwise = {-1, 0};

    public Shape() {
        int random = (int) (Math.random() * 7);
        if (random == 0) {
            squareMatrix = new Square[][]{
                    {new Square(Color.CYAN), new Square(Color.CYAN), new Square(Color.CYAN), new Square(Color.CYAN)}
            };
            this.centerPoint = "01";
        }
        else if (random == 1) {
            squareMatrix = new Square[][]{
                    {new Square(Color.BLUE), new Square(null), new Square(null)},
                    {new Square(Color.BLUE), new Square(Color.BLUE), new Square(Color.BLUE)}
            };
            this.centerPoint = "11";
        }
        else if (random == 2) {
            squareMatrix = new Square[][]{
                    {new Square(null), new Square(null), new Square(Color.ORANGE)},
                    {new Square(Color.ORANGE), new Square(Color.ORANGE), new Square(Color.ORANGE)}
            };
            this.centerPoint = "11";
        }
        else if (random == 3) {
            squareMatrix = new Square[][]{
                    {new Square(Color.YELLOW), new Square(Color.YELLOW)},
                    {new Square(Color.YELLOW), new Square(Color.YELLOW)}
            };
            this.centerPoint = "10";
        }
        else if (random == 4) {
            squareMatrix = new Square[][]{
                    {new Square(null), new Square(Color.GREEN), new Square(Color.GREEN)},
                    {new Square(Color.GREEN), new Square(Color.GREEN), new Square(null)}
            };
            this.centerPoint = "11";
        }
        else if (random == 5) {
            squareMatrix = new Square[][]{
                    {new Square(null), new Square(Color.PINK), new Square(null)},
                    {new Square(Color.PINK), new Square(Color.PINK), new Square(Color.PINK)}
            };
            this.centerPoint = "11";
        }
        else {
            squareMatrix = new Square[][]{
                    {new Square(Color.RED), new Square(Color.RED), new Square(null)},
                    {new Square(null), new Square(Color.RED), new Square(Color.RED)}
            };
            this.centerPoint = "11";
        }
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

    public void rotate() {
        int row = Integer.parseInt(centerPoint.substring(0, 1));
        int col = Integer.parseInt(centerPoint.substring(1));
        int centerX = squareMatrix[row][col].getX();
        int centerY = squareMatrix[row][col].getY();

        Square[][] tempMatrix = new Square[squareMatrix.length][squareMatrix[0].length];

        int counter = 0;

        for (int i = 0; i < squareMatrix.length; i++) {
            for (int j = 0; j < squareMatrix[0].length; j++) {
                if (squareMatrix[i][j] != null) {
                    int x = squareMatrix[i][j].getX() - centerX;
                    int y = squareMatrix[i][j].getY() - centerY;

                    int newX = (xRotationCounterClockwise[0] * x) + (xRotationCounterClockwise[1] * y);
                    int newY = (yRotationCounterClockwise[0] * x) + (yRotationCounterClockwise[1] * y);

                    int controlX = newX + centerX;

                    //2 schleifen oder 2 arrays

                    if (controlX <= 9 && controlX >= 0) {
                        tempMatrix[i][j] = new Square(null);
                        tempMatrix[i][j].setX(newX + centerX);
                        tempMatrix[i][j].setY(newY + centerY);
                    }
                    if (tempMatrix[i][j].getX() + 1 > 9 || tempMatrix[i][j].getX() - 1 < 0) {
                        counter++;
                    }
                }
            }
        }
        if (counter == 0) {
            for (int i = 0; i < squareMatrix.length; i++) {
                for (int j = 0; j < squareMatrix[0].length; j++) {
                    if (squareMatrix[i][j] != null) {
                        squareMatrix[i][j].setX(tempMatrix[i][j].getX());
                        squareMatrix[i][j].setY(tempMatrix[i][j].getY());
                    }
                }
            }
        }
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
