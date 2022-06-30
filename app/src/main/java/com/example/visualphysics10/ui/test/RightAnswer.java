package com.example.visualphysics10.ui.test;

abstract class RightAnswer {

    public static boolean selectFromPosition(int position, double ans) {
        switch (position) {
            case 0:
                return task1FromL1(ans);
            case 1:
                return task1FromL2(ans);
            case 2:
                return task1FromL3(ans);
            case 3:
                return task1FromL4(ans);
            case 4:
                return task1FromL5(ans);
        }
        return false;
    }
    public static boolean selectFromPosition2(int position, double ans) {
        switch (position) {
            case 0:
                return task2FromL1(ans);
            case 1:
                return task2FromL2(ans);
            case 2:
                return task2FromL3(ans);
            case 3:
                return task2FromL4(ans);
            case 4:
                return task2FromL5(ans);
        }
        return false;
    }

    public static boolean task1FromL1(double ans) {
        boolean answer;
        answer = (ans == 50.00);
        return answer;
    }

    public static boolean task2FromL1(double ans) {
        boolean answer;
        answer = (ans == 25.00);
        return answer;
    }

    public static boolean task1FromL2(double ans) {
        boolean answer;
        answer = (ans == 1.90);
        return answer;
    }

    public static boolean task2FromL2(double ans) {
        boolean answer;
        answer = (ans == 6.00);
        return answer;
    }

    public static boolean task1FromL3(double ans) {
        boolean answer;
        answer = (ans == 60.00);
        return answer;
    }

    public static boolean task2FromL3(double ans) {
        boolean answer;
        answer = (ans == 120.00);
        return answer;
    }

    public static boolean task1FromL4(double ans) {
        boolean answer;
        answer = (ans == 2.50);
        return answer;
    }

    public static boolean task2FromL4(double ans) {
        boolean answer;
        answer = (ans == 35);
        return answer;
    }

    public static boolean task1FromL5(double ans) {
        boolean answer;
        answer = (ans == 30);
        return answer;
    }

    public static boolean task2FromL5(double ans) {
        boolean answer;
        answer = (ans == 3);
        return answer;
    }

}
