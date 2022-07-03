package com.example.visualphysics10.ui;

import static androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE;
import static androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.visualphysics10.MainActivity;
import com.example.visualphysics10.R;
import com.example.visualphysics10.database.LessonData;
import com.example.visualphysics10.database.LessonViewModel;
import com.example.visualphysics10.databinding.EndEducationDialogBinding;
import com.example.visualphysics10.databinding.ExitDilogBinding;
import com.example.visualphysics10.ui.login.LoginFragment;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;
import java.util.Objects;

public class ExitDialog extends DialogFragment {
    private ExitDilogBinding binding;
    SharedPreferences enter;
    private final String FIRST_ENTER = "isFirstEnter";
    private boolean isFirstEnter;
    private LessonViewModel viewModel;
    private LessonData lessonDataList;
    SharedPreferences education;
    private String EDUCATION_PREFERENCES = "educationEnd";
    private boolean educationEnd;

    public static DialogFragment newInstance() {
        return new ExitDialog();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        assert dialog != null;
        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ExitDilogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addToolbar();
        enter = requireActivity().getSharedPreferences(FIRST_ENTER, Context.MODE_PRIVATE);
        if (enter.contains(FIRST_ENTER)) {
            isFirstEnter = enter.getBoolean(FIRST_ENTER, false);
        }
        education = requireActivity().getSharedPreferences(EDUCATION_PREFERENCES, Context.MODE_PRIVATE);
        if (education.contains(EDUCATION_PREFERENCES)) {
            educationEnd = education.getBoolean(EDUCATION_PREFERENCES, false);
        }
        binding.noExit.setOnClickListener(v->{
            dismiss();
        });
        binding.exit.setOnClickListener(v->{
            exit();
        });
    }

    private void exit() {
        SharedPreferences.Editor editor = enter.edit();
        SharedPreferences.Editor editor2 = education.edit();
        editor.putBoolean(FIRST_ENTER, false);
        editor2.putBoolean(EDUCATION_PREFERENCES, false);
        editor.apply();
        editor2.apply();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setTransition(TRANSIT_FRAGMENT_CLOSE)
                .replace(R.id.container, new LoginFragment())
                .commit();
        dismiss();
    }

    private void addToolbar() {
        MaterialToolbar toolbar = binding.toolbar;
        ((MainActivity) requireActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.close);
        toolbar.setNavigationIconTint(Color.WHITE);
        toolbar.setTitle("Выход");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(v -> {
            dismiss();
        });
    }
}
