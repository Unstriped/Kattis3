import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

// WIP needs optimizing

/*
    SUDOKU VERIFICATOR

    Input consists of up to 100 Sudoku boards.
    Each board is given as a 9-by-9 matrix using digits 0 through 9, one matrix row per line.
    If a board square contains 0, that means it’s been erased.
    There is a blank line between pairs of boards. Input ends at end of file.

    For each board, if there is a unique solution,
    print the solution using the same format as the input.
    If there are multiple solutions, print “Non-unique”.
    If the designer has made a terrible mistake and there is no solution, print “Find another job”.
    Separate the outputs for adjacent cases with a blank line.
*/
public class SudokuVerificator  {
    static final int emptyField = 0;
    static final int boardSize = 9;

    static final int minVal = 1;
    static final int maxVal = 9;

    public static void main(String[] args) {
        Integer[][] board = new Integer[9][9];
        Integer[][] boardCopy = new Integer[9][9];
        FastReader s = new FastReader();
        bigLoop:
        while (true) {
            for (int i = 0; i < 9; i++) {
                String inputRow = s.nextLine();
                if(inputRow == null || inputRow.isEmpty()){
                    inputRow = s.nextLine();
                }
                if(inputRow == null || inputRow.isEmpty()){
                    // instead of hasNext()
                    break bigLoop;
                }
                for (int j = 0; j < 9; j++) {
                    int newNum = Integer.parseInt(inputRow.split(" ")[j]);
                    board[i][j] = newNum;
                    boardCopy[i][j] = newNum;
                }
            }

            if (isValidSudoku(board)) {
                solveSudoku(board, false);
                solveSudoku(boardCopy, true);
                boolean unique=true;
                outerLoop:
                for (int i = 0; i < boardSize; i++) {
                    for (int j = 0; j < boardSize; j++) {
                        if(board[i][j]!=boardCopy[i][j]){
                            System.out.println("Non-unique\n");
                            unique=false;
                            break outerLoop;
                        }
                    }
                }
                if(unique){
                    printSudoku(board);
                }
            } else {
                System.out.println("Find another job\n");
            }
        }


    }

    private static void printSudoku(Integer[][] board) {
        String sudoku = "";
        for (int i = 0; i < boardSize; i++) {
            String row = "";
            for (int j = 0; j < boardSize; j++) {
                row += board[i][j] + " ";
            }
            sudoku += row+"\n";
        }
        System.out.println(sudoku);
    }

    // Backtracking Algorithm - testing every solution until it works
    // When checking whether the board is unique, a new board is created backwards.
    private static boolean solveSudoku(Integer[][] board, boolean testForUnique) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == emptyField) {
                    if (testForUnique) {
                        for (int k = maxVal; k >= minVal; k--) {
                            board[i][j] = k;
                            if (isValidSudoku(board) && solveSudoku(board, testForUnique)) {
                                return true;
                            }
                            board[i][j] = emptyField;
                        }
                        return false;
                    } else {
                        for (int k = minVal; k <= maxVal; k++) {
                            board[i][j] = k;
                            if (isValidSudoku(board) && solveSudoku(board, testForUnique)) {
                                return true;
                            }
                            board[i][j] = emptyField;
                        }
                        return false;
                    }
                }

            }
        }
        return true;
    }



    private static boolean isValidSudoku(Integer[][] board) {

        for (int i = 0; i < boardSize; i++) {
            int[] row = new int[9];
            int[] square = new int[9];
            int[] column = new int[9];


            for (int j = 0; j < boardSize; j++) {
                row[j] = board[i][j];
                column[j] = board[j][i];
                square[j] = board[(i / 3) * 3 + j / 3][i * 3 % 9 + j % 3];
            }

            if (!isValidPart(square)) return false;
            if (!isValidPart(row)) return false;
            if (!isValidPart(column)) return false;

        }

        return true;
    }
    // if a row, column of square contains more than one of the same number its no valid. Excluding zeroes.
    private static boolean isValidPart(int[] part) {

        int[] occurrences = new int[10];

        for (int i : part) {
            if (i != emptyField) {
                occurrences[i]++;
                if (occurrences[i] > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    // from https://www.geeksforgeeks.org/fast-io-in-java-in-competitive-programming/
    // tried to implement custom reader to save time
    static class FastReader{
        BufferedReader br;
        StringTokenizer st;

        public FastReader()
        {
            br = new BufferedReader(new
                    InputStreamReader(System.in));
        }

        String next()
        {
            while (st == null || !st.hasMoreElements())
            {
                try
                {
                    st = new StringTokenizer(br.readLine());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt()
        {
            return Integer.parseInt(next());
        }

        long nextLong()
        {
            return Long.parseLong(next());
        }

        double nextDouble()
        {
            return Double.parseDouble(next());
        }

        String nextLine()
        {
            String str = "";
            try
            {
                str = br.readLine();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return str;
        }
    }

}

