package com.example.visualphysics10.ph_lab;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.example.visualphysics10.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceHolderContent4 {
    public static final List<PlaceHolderContent4.PlaceHolderItem4> ITEMS = new ArrayList<PlaceHolderContent4.PlaceHolderItem4>();

    public static final Map<String, PlaceHolderContent4.PlaceHolderItem4> ITEM_MAP = new HashMap<String, PlaceHolderContent4.PlaceHolderItem4>();

    private static final int COUNT = 5;
    private static int countL1, countL2, countL3, countL4, countL5;
    private static int countGraphL1, countGraphL2, countGraphL3, countGraphL4, countGraphL5;
    private static boolean seeVideoL1, seeVideoL2, seeVideoL3, seeVideoL4, seeVideoL5;
    private static int taskL1, taskL2, taskL3, taskL4, taskL5;
    private static String lesson1, lesson2, lesson3, lesson4, lesson5;
    SharedPreferences lessons;
    private String EDUCATION_PREFERENCES = "educationEnd";


    static {
        for (int i = 1; i <= COUNT; i++) {
            addItem(createPlaceHolderItem4(i));
        }
    }

    private static void addItem(PlaceHolderContent4.PlaceHolderItem4 item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static PlaceHolderContent4.PlaceHolderItem4 createPlaceHolderItem4(int position) {
        return new PlaceHolderContent4.PlaceHolderItem4(String.valueOf(position), switchLesson(position), selectBody(position));
    }

    private static String selectBody(int position) {
        String part1 = "Вы запускали визуализацию ";
        String part2 = "Вы построили ";
        String part3 = "графика, учителю отправиться последний по данной теме";
        String part4 = "Вы посмотрели видео по данной теме ";
        String part5 = "Вы решелии ";
        String part6 = "/2 задач";
        switch (position){
            case 1:
                lesson1 =  part1 + countL1 + "\n" + part2 + countGraphL1 + part3  + "\n" + part4 + seeVideoL1 + "\n" + part5 + taskL1 + part6;
                return lesson1;
            case 2:
                lesson2 = part1 + countL2 + "\n" + part2 + countGraphL2 + part3  + "\n" + part4 + seeVideoL2 + "\n" + part5 + taskL2 + part6;
                return lesson2;
            case 3:
                lesson3 =  part1 + countL3 + "\n" + part2 + countGraphL3 + part3  + "\n" + part4 + seeVideoL3 + "\n" + part5 + taskL3 + part6;
                return lesson3;
            case 4:
                lesson4 =  part1 + countL4 + "\n" + part2 + countGraphL4 + part3  + "\n" + part4 + seeVideoL4 + "\n" + part5 + taskL4 + part6;
                return lesson4;
            case 5:
                lesson5 =  part1 + countL5 + "\n" + part2 + countGraphL5 + part3  + "\n" + part4 + seeVideoL5 + "\n" + part5 + taskL5 + part6;
                return lesson5;
            default: return "";
        }
    }


    private static String switchLesson(int position) {
        switch (position){
            case 1:
                return "Лабораторная работа по теме: Ускорение";
            case 2:
                return "Лабораторная работа по теме: Движение по Окружности";
            case 3:
                return "Лабораторная работа по теме: II Закон Ньютона";
            case 4:
                return "Лабораторная работа по теме: Движение под углом";
            case 5:
                return "Лабораторная работа по теме: Закон Сохранения Импульса";
            default: return "";
        }
    }


    public static class PlaceHolderItem4 {
        public final String id;
        public final String title;
        public final String body;

        public PlaceHolderItem4(String id, String content, String body) {
            this.id = id;
            this.title = content;
            this.body = body;
        }

        @NonNull
        @Override
        public String toString() {
            return title;
        }
    }

}

