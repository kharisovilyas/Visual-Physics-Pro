package com.example.visualphysics10.ph_lab;


import androidx.annotation.NonNull;

import com.example.visualphysics10.R;
import com.example.visualphysics10.ui.lesson.LessonEducFrag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceHolderContent4 {
    public static final List<PlaceHolderContent4.PlaceHolderItem4> ITEMS = new ArrayList<PlaceHolderContent4.PlaceHolderItem4>();

    public static final Map<String, PlaceHolderContent4.PlaceHolderItem4> ITEM_MAP = new HashMap<String, PlaceHolderContent4.PlaceHolderItem4>();
    public static final int COUNT = 5;

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
        String lesson1 = "Вы прошли тему Ускорение! Посмотрели видео-разбор темы, несколько раз запускали визуализацию и даже построили графики движения, решили все задачи. Отправить Вашему учителю последний построенный график движения по теме Ускорение ?";
        String lesson2 = "Вы прошли тему Движение по Окружности! Посмотрели видео-разбор темы, несколько раз запускали визуализацию и даже построили графики движения, решили все задачи. Отправить Вашему учителю последний построенный график движения по теме Движение по Окружности ?";
        String lesson3 = "Вы прошли тему II Закон Ньютона! Посмотрели видео-разбор темы, несколько раз запускали визуализацию и даже построили графики движения, решили все задачи. Отправить Вашему учителю последний построенный график движения по теме II Закон Ньютона ?";
        String lesson4 = "Вы прошли тему Движение под углом к горизонту! Посмотрели видео-разбор темы, несколько раз запускали визуализацию и даже построили графики движения, решили все задачи. Отправить Вашему учителю последний построенный график движения по теме Движение под углом к горизонту ?";
        String lesson5 = "Вы прошли тему Закон Сохранения импульса! Посмотрели видео-разбор темы, несколько раз запускали визуализацию и даже построили графики движения, решили все задачи. Отправить Вашему учителю последний построенный график движения по теме Движение по Окружности Закон Сохранения импульса ?";
        switch (position){
            case 1:
                return lesson1;
            case 2:
                return lesson2;
            case 3:
                return lesson3;
            case 4:
                return lesson4;
            case 5:
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

