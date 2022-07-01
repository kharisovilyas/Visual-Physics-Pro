package com.example.visualphysics10.ph_lectures;

import androidx.annotation.NonNull;

import com.example.visualphysics10.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceHolderContent3 {
    public static final List<PlaceHolderContent3.PlaceHolderItem3> ITEMS = new ArrayList<PlaceHolderContent3.PlaceHolderItem3>();

    public static final Map<String, PlaceHolderContent3.PlaceHolderItem3> ITEM_MAP = new HashMap<String, PlaceHolderContent3.PlaceHolderItem3>();

    private static final int COUNT = 5;

    static {
        for (int i = 1; i <= COUNT; i++) {
            addItem(createPlaceHolderItem3(i));
        }
    }

    private static void addItem(PlaceHolderContent3.PlaceHolderItem3 item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static PlaceHolderContent3.PlaceHolderItem3 createPlaceHolderItem3(int position) {
        return new PlaceHolderContent3.PlaceHolderItem3(String.valueOf(position), switchLesson(position), switchImageView(position));
    }


    private static String switchLesson(int position) {
        switch (position){
            case 1:
                return "Ускорение";
            case 2:
                return "Движение по Окружности";
            case 3:
                return "II Закон Ньютона";
            case 4:
                return "Движение под углом";
            case 5:
                return "Закон Сохранения Импульса";
            default: return "";
        }
    }


    static int switchImageView(int position) {
        switch (position){
            case 1: return R.drawable.lesson_1;
            case 2: return R.drawable.lesson_2;
            case 3: return R.drawable.lesson_3;
            case 4: return R.drawable.lesson_4;
            case 5: return R.drawable.lesson_5;
            default: return 0;
        }
    }

    public static class PlaceHolderItem3 {
        public final String id;
        public final String title;
        public final int imageView;


        public PlaceHolderItem3(String id, String content, int imageView) {
            this.id = id;
            this.title = content;
            this.imageView = imageView;
        }

        @NonNull
        @Override
        public String toString() {
            return title;
        }
    }

}

