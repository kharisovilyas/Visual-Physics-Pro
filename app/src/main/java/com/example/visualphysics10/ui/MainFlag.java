package com.example.visualphysics10.ui;

public abstract class MainFlag {
    public static boolean notLesson;
    public static boolean endReg;

    public static boolean isNotLesson() {
        return notLesson;
    }

    public static void setNotLesson(boolean notLesson) {
        MainFlag.notLesson = notLesson;
    }

    public static boolean isEndReg() {
        return endReg;
    }

    public static void setEndReg(boolean endReg) {
        MainFlag.endReg = endReg;
    }
}
