package com.lwj.lwjtest_coustomview;

public class Test {

    static int[][] result = null;
    public static void main(String[] args) {
        init(5);
    }

    public static void init(int rows) {
        result = new int[rows][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j <= i; j++) {
                System.out.print(getValue(i, j) + "\t");
            }
            System.out.println();
        }
    }


    public static int getValue(int rows, int cols) {

        if (rows == 0 || rows == 1) {
            return 1;
        } else if(cols == 0 || rows == cols){
            return 1;
        }else{
            result[rows][cols] = getValue(rows - 1, cols) + getValue(rows - 1, cols - 1);
            return result[rows][cols];
        }

    }
}
