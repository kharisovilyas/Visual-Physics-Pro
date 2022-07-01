package com.example.visualphysics10.ph_lab;

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
        switch (position){
            case 1:
                return "Вы запускали визуализацию 2 раза" + "\n" + "Вы построили 2 графика, нажмите чтобы выбрать" + "\n" + "Вы посмотрели видео по данной теме" + "\n" + "Вы решелии 2/2 задач";
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

