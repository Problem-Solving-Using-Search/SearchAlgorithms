package General;

import java.util.Arrays;

public class Utils {
    public static int[][] deepCopy(int[][] original) {
        if (original == null) {
            return null;
        }

        int[][] result = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
            // For Java versions prior to Java 6 use the next:
            // System.arraycopy(original[i], 0, result[i], 0, original[i].length);
        }
        return result;
    }

    public static char[][] ConvertStringToMatrix(String str) throws Exception
    {
        /* check that it's nxn mat */
        int n_rows = str.split("\n").length ;
        int n_cols = (str.length() - n_rows + 1) / n_rows; // subtract slash-ns
        if(n_rows != n_cols)
            throw new Exception("Cannot parse string with not matching n_rows to n_cols");
        /* check legal syntax of string*/
        char[][] mat = new char[n_rows][n_cols];
        int row = 0;
        int col = 0;
        for (int i = 0; i < str.length(); i++, col = (col+1) % n_cols) {
            if(str.charAt(i) == '\n' && col != 0)
                throw new Exception("Cannot parse string, slash n found before expected iteration: " + i);
            if(i == n_cols && str.charAt(i) != '\n')
                throw new Exception("Cannot parse string, out of bounds --> slash n expected iteration " + i);
            if(str.charAt(i) == '\n') {
                row++;
                col-=1;
            }
            else
                mat[row][col] = str.charAt(i);

        }
        return mat;
    }



}
