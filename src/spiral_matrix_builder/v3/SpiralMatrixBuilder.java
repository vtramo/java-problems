package spiral_matrix_builder.v3;

public class SpiralMatrixBuilder {

    int[][] buildMatrixOfSize(int size) {
        int count = 1;
        int[][] result = new int[size][size];
        int lo = 0, hi = size;

        while (lo < hi) {
            int x = lo, y = lo;

            while (x < hi - 1)
                result[y][x++] = count++;
            while (y < hi - 1)
                result[y++][x] = count++;
            while (x > lo)
                result[y][x--] = count++;
            while (y > lo)
                result[y--][x] = count++;

            ++lo;
            --hi;
        }

        if (size % 2 == 1) result[size / 2][size / 2] = count;

        return result;
    }
}