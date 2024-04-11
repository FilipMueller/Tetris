import java.awt.*;

public class Shape {

    Square[][] squareMatrix;

    private boolean shapeInitialSpawn = true;

    private static final int BLOCK_SIZE = 30;

    public Shape() {
        int random = (int) (Math.random() * 7);
        if (random == 0) {
            squareMatrix = shapeI;
        }
        else if (random == 1) {
            squareMatrix = shapeJ;
        }
        else if (random == 2) {
            squareMatrix = shapeL;
        }
        else if (random == 3) {
            squareMatrix = shapeO;
        }
        else if (random == 4) {
            squareMatrix = shapeS;
        }
        else if (random == 5) {
            squareMatrix = shapeT;
        }
        else {
            squareMatrix = shapeZ;
        }
    }

    public void draw(Graphics g) {
        for (int row = 0; row < squareMatrix.length; row++) {
            for (int col = 0; col < squareMatrix[0].length; col++) {
                if (squareMatrix[row][col].color != null) {
                    g.setColor(squareMatrix[row][col].color);
                    if (shapeInitialSpawn) {
                        squareMatrix[row][col].setX(row * BLOCK_SIZE + 120);
                        squareMatrix[row][col].setY(col * BLOCK_SIZE);
                        g.fillRect(row * BLOCK_SIZE + 120, col * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                    }
                    if (!shapeInitialSpawn) {
                        g.fillRect(squareMatrix[row][col].getX(), squareMatrix[row][col].getY(), BLOCK_SIZE, BLOCK_SIZE);
                    }
                }
            }
        }
        shapeInitialSpawn = false;
    }

    public void moveDown() {
        for (int i = 0; i < squareMatrix.length; i++) {
            for (int j = 0; j < squareMatrix[0].length; j++) {
                if (squareMatrix[i][j] != null) {
                    squareMatrix[i][j].setY(squareMatrix[i][j].getY() + BLOCK_SIZE);
                }
            }
        }
    }

    public void moveLeft() {
        for (int i = 0; i < squareMatrix.length; i++) {
            for (int j = 0; j < squareMatrix[0].length; j++) {
                if (squareMatrix[i][j] != null) {
                    squareMatrix[i][j].setX(squareMatrix[i][j].getX() - BLOCK_SIZE);
                }
            }
        }
    }

    public void moveRight() {
        for (int i = 0; i < squareMatrix.length; i++) {
            for (int j = 0; j < squareMatrix[0].length; j++) {
                if (squareMatrix[i][j] != null) {
                    squareMatrix[i][j].setX(squareMatrix[i][j].getX() + BLOCK_SIZE);
                }
            }
        }
    }

    private Square[][] shapeI = {
            {new Square(Color.CYAN), new Square(Color.CYAN), new Square(Color.CYAN), new Square(Color.CYAN)}
    };

    private Square[][] shapeJ = {
            {new Square(Color.BLUE), new Square(null), new Square(null)},
            {new Square(Color.BLUE), new Square(Color.BLUE), new Square(Color.BLUE)}
    };

    private Square[][] shapeL = {
            {new Square(null), new Square(null), new Square(Color.ORANGE)},
            {new Square(Color.ORANGE), new Square(Color.ORANGE), new Square(Color.ORANGE)}
    };

    private Square[][] shapeO = {
            {new Square(Color.YELLOW), new Square(Color.YELLOW)},
            {new Square(Color.YELLOW), new Square(Color.YELLOW)}
    };

    private Square[][] shapeS = {
            {new Square(null), new Square(Color.GREEN), new Square(Color.GREEN)},
            {new Square(Color.GREEN), new Square(Color.GREEN), new Square(null)}
    };

    private Square[][] shapeT = {
            {new Square(null), new Square(Color.PINK), new Square(null)},
            {new Square(Color.PINK), new Square(Color.PINK), new Square(Color.PINK)}
    };

    private Square[][] shapeZ = {
            {new Square(Color.RED), new Square(Color.RED), new Square(null)},
            {new Square(null), new Square(Color.RED), new Square(Color.RED)}
    };
}
