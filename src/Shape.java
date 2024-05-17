import java.awt.*;

public class Shape {

    final Square[][] squareMatrix;

    private boolean initialSpawn = true;

    private static final int BLOCK_SIZE = 30;

    private static final Square[][] I_OFFSET_DATA = new Square[4][5];

    private static final Square[][] O_OFFSET_DATA = new Square[4][1];

    private static final Square[][] JLSTZ_OFFSET_DATA = new Square[4][5];

    private final ShapeType shapeType;

    private String centerPoint;

    private int rotationIndex;

    public Shape(ShapeType shapeType, int currentRotation) {
        this.shapeType = shapeType;
        this.rotationIndex = currentRotation;
        squareMatrix = getSquareMatrix(shapeType);
    }

    public Shape() {
        this.shapeType = ShapeType.values() [(int) (Math.random() * ShapeType.values().length)];
        this.rotationIndex = 0;
        squareMatrix = getSquareMatrix(shapeType);
    }

    private Square[][] getSquareMatrix(ShapeType shapeType) {
        final Square[][] squareMatrix;
        switch (shapeType) {
            case I_Shape:
                squareMatrix = new Square[][]{
                        {new Square(Color.CYAN), new Square(Color.CYAN), new Square(Color.CYAN), new Square(Color.CYAN)}
                };
                this.centerPoint = "01";
                break;
            case J_Shape:
                squareMatrix = new Square[][]{
                        {new Square(Color.BLUE), new Square(null), new Square(null)},
                        {new Square(Color.BLUE), new Square(Color.BLUE), new Square(Color.BLUE)}
                };
                this.centerPoint = "11";
                break;
            case L_Shape:
                squareMatrix = new Square[][]{
                        {new Square(null), new Square(null), new Square(Color.ORANGE)},
                        {new Square(Color.ORANGE), new Square(Color.ORANGE), new Square(Color.ORANGE)}
                };
                this.centerPoint = "11";
                break;
            case O_Shape:
                squareMatrix = new Square[][]{
                        {new Square(Color.YELLOW), new Square(Color.YELLOW)},
                        {new Square(Color.YELLOW), new Square(Color.YELLOW)}
                };
                this.centerPoint = "10";
                break;
            case S_Shape:
                squareMatrix = new Square[][]{
                        {new Square(null), new Square(Color.GREEN), new Square(Color.GREEN)},
                        {new Square(Color.GREEN), new Square(Color.GREEN), new Square(null)}
                };
                this.centerPoint = "11";
                break;
            case T_Shape:
                squareMatrix = new Square[][]{
                        {new Square(null), new Square(Color.PINK), new Square(null)},
                        {new Square(Color.PINK), new Square(Color.PINK), new Square(Color.PINK)}
                };
                this.centerPoint = "11";
                break;
            case Z_Shape:
            default:
                squareMatrix = new Square[][]{
                        {new Square(Color.RED), new Square(Color.RED), new Square(null)},
                        {new Square(null), new Square(Color.RED), new Square(Color.RED)}
                };
                this.centerPoint = "11";
                break;
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

    public Square[][] getSquareMatrix() {
        return squareMatrix;
    }

    public ShapeType getShapeType() {
        return shapeType;
    }

    public String getCenterPoint() {
        return centerPoint;
    }

    public void setCenterPoint(String centerPoint) {
        this.centerPoint = centerPoint;
    }

    public int getRotationIndex() {
        return rotationIndex;
    }

    public void setRotationIndex(int rotationIndex) {
        this.rotationIndex = rotationIndex;
    }

    public static Square[][] getOOffset() {
        O_OFFSET_DATA[0][0] = new Square(null);
        O_OFFSET_DATA[0][0].setX(0);
        O_OFFSET_DATA[0][0].setY(0);
        O_OFFSET_DATA[1][0] = new Square(null);
        O_OFFSET_DATA[1][0].setX(0);
        O_OFFSET_DATA[1][0].setY(1);
        O_OFFSET_DATA[2][0] = new Square(null);
        O_OFFSET_DATA[2][0].setX(-1);
        O_OFFSET_DATA[2][0].setY(1);
        O_OFFSET_DATA[3][0] = new Square(null);
        O_OFFSET_DATA[3][0].setX(-1);
        O_OFFSET_DATA[3][0].setY(0);

        return O_OFFSET_DATA;
    }

    public static Square[][] getJLSTZOffset() {
        JLSTZ_OFFSET_DATA[0][0] = new Square(null);
        JLSTZ_OFFSET_DATA[0][0].setX(0);
        JLSTZ_OFFSET_DATA[0][0].setY(0);
        JLSTZ_OFFSET_DATA[0][1] = new Square(null);
        JLSTZ_OFFSET_DATA[0][1].setX(0);
        JLSTZ_OFFSET_DATA[0][1].setY(0);
        JLSTZ_OFFSET_DATA[0][2] = new Square(null);
        JLSTZ_OFFSET_DATA[0][2].setX(0);
        JLSTZ_OFFSET_DATA[0][2].setY(0);
        JLSTZ_OFFSET_DATA[0][3] = new Square(null);
        JLSTZ_OFFSET_DATA[0][3].setX(0);
        JLSTZ_OFFSET_DATA[0][3].setY(0);
        JLSTZ_OFFSET_DATA[0][4] = new Square(null);
        JLSTZ_OFFSET_DATA[0][4].setX(0);
        JLSTZ_OFFSET_DATA[0][4].setY(0);

        JLSTZ_OFFSET_DATA[1][0] = new Square(null);
        JLSTZ_OFFSET_DATA[1][0].setX(0);
        JLSTZ_OFFSET_DATA[1][0].setY(0);
        JLSTZ_OFFSET_DATA[1][1] = new Square(null);
        JLSTZ_OFFSET_DATA[1][1].setX(1);
        JLSTZ_OFFSET_DATA[1][1].setY(0);
        JLSTZ_OFFSET_DATA[1][2] = new Square(null);
        JLSTZ_OFFSET_DATA[1][2].setX(1);
        JLSTZ_OFFSET_DATA[1][2].setY(1);
        JLSTZ_OFFSET_DATA[1][3] = new Square(null);
        JLSTZ_OFFSET_DATA[1][3].setX(0);
        JLSTZ_OFFSET_DATA[1][3].setY(-2);
        JLSTZ_OFFSET_DATA[1][4] = new Square(null);
        JLSTZ_OFFSET_DATA[1][4].setX(1);
        JLSTZ_OFFSET_DATA[1][4].setY(-2);

        JLSTZ_OFFSET_DATA[2][0] = new Square(null);
        JLSTZ_OFFSET_DATA[2][0].setX(0);
        JLSTZ_OFFSET_DATA[2][0].setY(0);
        JLSTZ_OFFSET_DATA[2][1] = new Square(null);
        JLSTZ_OFFSET_DATA[2][1].setX(0);
        JLSTZ_OFFSET_DATA[2][1].setY(0);
        JLSTZ_OFFSET_DATA[2][2] = new Square(null);
        JLSTZ_OFFSET_DATA[2][2].setX(0);
        JLSTZ_OFFSET_DATA[2][2].setY(0);
        JLSTZ_OFFSET_DATA[2][3] = new Square(null);
        JLSTZ_OFFSET_DATA[2][3].setX(0);
        JLSTZ_OFFSET_DATA[2][3].setY(0);
        JLSTZ_OFFSET_DATA[2][4] = new Square(null);
        JLSTZ_OFFSET_DATA[2][4].setX(0);
        JLSTZ_OFFSET_DATA[2][4].setY(0);

        JLSTZ_OFFSET_DATA[3][0] = new Square(null);
        JLSTZ_OFFSET_DATA[3][0].setX(0);
        JLSTZ_OFFSET_DATA[3][0].setY(0);
        JLSTZ_OFFSET_DATA[3][1] = new Square(null);
        JLSTZ_OFFSET_DATA[3][1].setX(-1);
        JLSTZ_OFFSET_DATA[3][1].setY(0);
        JLSTZ_OFFSET_DATA[3][2] = new Square(null);
        JLSTZ_OFFSET_DATA[3][2].setX(-1);
        JLSTZ_OFFSET_DATA[3][2].setY(1);
        JLSTZ_OFFSET_DATA[3][3] = new Square(null);
        JLSTZ_OFFSET_DATA[3][3].setX(0);
        JLSTZ_OFFSET_DATA[3][3].setY(2);
        JLSTZ_OFFSET_DATA[3][4] = new Square(null);
        JLSTZ_OFFSET_DATA[3][4].setX(-1);
        JLSTZ_OFFSET_DATA[3][4].setY(-2);

        return JLSTZ_OFFSET_DATA;
    }

    public static Square[][] getIOffset() {
        I_OFFSET_DATA[0][0] = new Square(null);
        I_OFFSET_DATA[0][0].setX(0);
        I_OFFSET_DATA[0][0].setY(0);
        I_OFFSET_DATA[0][1] = new Square(null);
        I_OFFSET_DATA[0][1].setX(-1);
        I_OFFSET_DATA[0][1].setY(0);
        I_OFFSET_DATA[0][2] = new Square(null);
        I_OFFSET_DATA[0][2].setX(2);
        I_OFFSET_DATA[0][2].setY(0);
        I_OFFSET_DATA[0][3] = new Square(null);
        I_OFFSET_DATA[0][3].setX(-1);
        I_OFFSET_DATA[0][3].setY(0);
        I_OFFSET_DATA[0][4] = new Square(null);
        I_OFFSET_DATA[0][4].setX(2);
        I_OFFSET_DATA[0][4].setY(0);

        I_OFFSET_DATA[1][0] = new Square(null);
        I_OFFSET_DATA[1][0].setX(-1);
        I_OFFSET_DATA[1][0].setY(0);
        I_OFFSET_DATA[1][1] = new Square(null);
        I_OFFSET_DATA[1][1].setX(0);
        I_OFFSET_DATA[1][1].setY(0);
        I_OFFSET_DATA[1][2] = new Square(null);
        I_OFFSET_DATA[1][2].setX(0);
        I_OFFSET_DATA[1][2].setY(0);
        I_OFFSET_DATA[1][3] = new Square(null);
        I_OFFSET_DATA[1][3].setX(0);
        I_OFFSET_DATA[1][3].setY(-1);
        I_OFFSET_DATA[1][4] = new Square(null);
        I_OFFSET_DATA[1][4].setX(0);
        I_OFFSET_DATA[1][4].setY(2);

        I_OFFSET_DATA[2][0] = new Square(null);
        I_OFFSET_DATA[2][0].setX(-1);
        I_OFFSET_DATA[2][0].setY(-1);
        I_OFFSET_DATA[2][1] = new Square(null);
        I_OFFSET_DATA[2][1].setX(1);
        I_OFFSET_DATA[2][1].setY(-1);
        I_OFFSET_DATA[2][2] = new Square(null);
        I_OFFSET_DATA[2][2].setX(-2);
        I_OFFSET_DATA[2][2].setY(-1);
        I_OFFSET_DATA[2][3] = new Square(null);
        I_OFFSET_DATA[2][3].setX(1);
        I_OFFSET_DATA[2][3].setY(0);
        I_OFFSET_DATA[2][4] = new Square(null);
        I_OFFSET_DATA[2][4].setX(-2);
        I_OFFSET_DATA[2][4].setY(0);

        I_OFFSET_DATA[3][0] = new Square(null);
        I_OFFSET_DATA[3][0].setX(0);
        I_OFFSET_DATA[3][0].setY(-1);
        I_OFFSET_DATA[3][1] = new Square(null);
        I_OFFSET_DATA[3][1].setX(0);
        I_OFFSET_DATA[3][1].setY(-1);
        I_OFFSET_DATA[3][2] = new Square(null);
        I_OFFSET_DATA[3][2].setX(0);
        I_OFFSET_DATA[3][2].setY(-1);
        I_OFFSET_DATA[3][3] = new Square(null);
        I_OFFSET_DATA[3][3].setX(0);
        I_OFFSET_DATA[3][3].setY(1);
        I_OFFSET_DATA[3][4] = new Square(null);
        I_OFFSET_DATA[3][4].setX(0);
        I_OFFSET_DATA[3][4].setY(-2);

        return I_OFFSET_DATA;
    }
}
