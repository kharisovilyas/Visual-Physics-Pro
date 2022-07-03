package com.example.visualphysics10.ui.lesson;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.visualphysics10.MainActivity;
import com.example.visualphysics10.R;
import com.example.visualphysics10.database.LessonData;
import com.example.visualphysics10.database.LessonViewModel;
import com.example.visualphysics10.databinding.LabfileDialogBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class LabFileDialogFragment extends DialogFragment {
    private LabfileDialogBinding binding;
    public final Bitmap bmp1;
    public final Bitmap bmp2;
    private Bitmap imageGraph;
    private LessonViewModel viewModel;
    private LessonData lessonDataList = new LessonData();
    private LessonData lessonDataList2 = new LessonData();
    private byte[] image;


    public LabFileDialogFragment(Bitmap bmp1, Bitmap bmp2) {
        this.bmp1 = bmp1;
        this.bmp2 = bmp2;
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        assert dialog != null;
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setLayout(width, height);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = LabfileDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        waiting();
        setGraph();
        addToolbar();
    }

    private void waiting() {
        final Handler handler = new Handler();
        //minimal latency for users
        handler.postDelayed(() -> {
            imageGraph = loadBitmapFromView(binding.labFile, binding.labFile.getWidth(), binding.labFile.getHeight());
            try {
                saveBitmap();
                createdDialog();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 100);
    }

    private void createdDialog() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.eror_title)
                .setMessage(R.string.eror_body)
                .setPositiveButton("Понятно", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .show().getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
    }

    private void saveBitmap() throws IOException {
        lessonDataList.image = BitmapToBytes(imageGraph);
        viewModel = ViewModelProviders.of(requireActivity()).get(LessonViewModel.class);
        viewModel.getLessonLiveData().observe(getViewLifecycleOwner(), new Observer<List<LessonData>>() {
            @Override
            public void onChanged(List<LessonData> lessonData) {
                lessonData.add(1, lessonDataList);
            }
        });
        viewModel.update(lessonDataList);
        saveInGallery();
    }

    private void saveInGallery() throws IOException {
        Bitmap bitmap = takeBitmap();
        String mPath = Environment.getExternalStorageDirectory() + File.separator + "VisualPhysics" + System.currentTimeMillis() + ".jpeg";
        File imageFile = new File(mPath);
        OutputStream fout = null;
        try {
            fout = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
            fout.flush();
        } catch (FileNotFoundException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        } finally {
            fout.close();
        }

    }
    private Bitmap takeBitmap(){
        viewModel.getLessonLiveData().observe(getViewLifecycleOwner(), new Observer<List<LessonData>>() {
            @Override
            public void onChanged(List<LessonData> lessonData) {
                image = lessonData.get(1).image;
            }
        });
        return BytesToBimap(image);
    }

    private void addToolbar() {
        MaterialToolbar toolbar = binding.toolbar;
        ((MainActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setTitle(R.string.saveGraph);
        toolbar.setNavigationOnClickListener(v -> {
            dismiss();
        });
    }

    public void setGraph() {
        binding.firstGraph.setImageBitmap(bmp1);
        binding.secondGraph.setImageBitmap(bmp2);
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }

    public static byte[] BitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream bArr = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, bArr);
        return bArr.toByteArray();
    }

    public static Bitmap BytesToBimap(byte[] b) {
        if (b.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }
}
