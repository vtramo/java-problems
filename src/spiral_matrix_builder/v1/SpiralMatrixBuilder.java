package spiral_matrix_builder.v1;
class SpiralMatrixBuilder {
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
    private int x = 0, y = 0;

    int[][] buildMatrixOfSize(int size) {
        if (size < 1) throw new IllegalArgumentException();
        init();
        final int[][] matrix = new int[size][size];
        int maxDirectionChange = 3;
        int counterChangedDirections = 0;
        int counter = 1;
        int maxSteps = size;
        int counterSteps = 0;
        while (--maxSteps > 0) {
            while (counterChangedDirections < maxDirectionChange) {
                while (counterSteps < maxSteps) {
                    matrix[x][y] = counter++;
                    goNext();
                    counterSteps++;
                }
                changeDirection();
                counterChangedDirections++;
                counterSteps = 0;
            }
            if (maxSteps + 1 == size) --maxDirectionChange;
            counterChangedDirections = 0;
        }
        matrix[x][y] = counter;
        return matrix;
    }

    private void init() {
        actualDirection = Direction.RIGHT;
        x = 0;
        y = 0;
    }

    private void goNext() {
        x = x + actualDirection.xOffset;
        y = y + actualDirection.yOffset;
    }

    private void changeDirection() {
        this.actualDirection = actualDirection.getNextDirection();
    }
}

