package com.example.visualphysics10.ui.lesson;

import android.graphics.Bitmap;

import androidx.fragment.app.DialogFragment;

public class SaveDialogFragment extends DialogFragment {
    final Bitmap bitmap;

    public SaveDialogFragment(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
