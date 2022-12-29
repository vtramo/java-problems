package spiral.matrix.builder.v2;

public class SpiralMatrixBuilder {
    private enum Direction {
        RIGHT(0, 1, "DOWN"),
        DOWN(1, 0, "LEFT"),
        LEFT(0, -1, "UP"),
        UP(-1, 0, "RIGHT");

        final int xOffset, yOffset;
        final String next;
        Direction(int xOffset, int yOffset, String next) {
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.next = next;
        }

        Direction getNextDirection() {
            return valueOf(next);
        }
    }

    private Direction actualDirection;
    private int maxDirectionChange = 3;
    private int counterDirectionChanged;
    private int counterSteps;
    private int maxSteps;
    private int squareMatrixSize;
    private int x = 0, y = 0;

    private void init(final int squareMatrixSize) {
        this.squareMatrixSize = squareMatrixSize;
        actualDirection = Direction.RIGHT;
        counterDirectionChanged = 0;
        counterSteps = 0;
        maxSteps = squareMatrixSize - 1;
        x = 0;
        y = 0;
    }

    public int[][] buildMatrixOfSize(final int size) {
        if (size < 1) throw new IllegalArgumentException();
        init(size);
        final int[][] matrix = new int[size][size];
        for (int i = 1; i <= size * size; i++) {
            matrix[x][y] = i;
            goNext();
        }
        return matrix;
    }

    private void goNext() {
        x = x + actualDirection.xOffset;
        y = y + actualDirection.yOffset;
        if (++counterSteps >= maxSteps) {
            changeDirection();
        }
    }

    private void changeDirection() {
        this.actualDirection = actualDirection.getNextDirection();
        counterDirectionChanged++;
        counterSteps = 0;
        if (counterDirectionChanged >= maxDirectionChange) {
            if (maxSteps + 1 == squareMatrixSize) maxDirectionChange--;
            maxSteps--;
            counterDirectionChanged = 0;
        }
    }
}

