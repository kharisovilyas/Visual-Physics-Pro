package com.example.visualphysics10.ui.lab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visualphysics10.MainActivity;
import com.example.visualphysics10.R;
import com.example.visualphysics10.adapter.RecyclerViewAdapter;
import com.example.visualphysics10.database.LessonData;
import com.example.visualphysics10.database.LessonViewModel;
import com.example.visualphysics10.databinding.FragmentLabListBinding;
import com.example.visualphysics10.ph_lab.PlaceHolderContent4;

import java.util.List;
import java.util.Objects;

public class LabFragmentList extends Fragment implements RecyclerViewAdapter.OnLessonListener {
    private FragmentLabListBinding binding;
    private final int mColumnCount = 1;
    private String emailTeacher;
    private String name;
    private String youClass;
    private LessonViewModel viewModel;
    private byte[] image;
    private boolean im;
    private int lenIm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLabListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addRecycler(view);
        addToolbar();
    }


    @SuppressLint("ResourceAsColor")
    private void addToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((MainActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setTitle(R.string.lecture);
        toolbar.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });
    }

    private void addRecycler(View view) {
        Context context = view.getContext();
        RecyclerView recyclerView = binding.listLab;
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(new RecyclerViewAdapter(PlaceHolderContent4.ITEMS, this, "FOR LAB FRAGMENT", false, 0));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //perform a fragment transaction on a specific-Lesson click
    @Override
    public void onLessonClick(int position) {
        Toast.makeText(requireContext(), getString(R.string.forSave), Toast.LENGTH_LONG).show();
        switch (position) {
            case 0:
                sendLesson(String.valueOf(R.string.sbj1));
                break;
            case 1:
                sendLesson(String.valueOf(R.string.sbj2));
                break;
            case 2:
                sendLesson(String.valueOf(R.string.sbj3));
                break;
            case 3:
                sendLesson(String.valueOf(R.string.sbj4));
                break;
            case 4:
                sendLesson(String.valueOf(R.string.sbj5));
                break;
        }
    }

    @SuppressLint("IntentReset")
    public void sendLesson(String body) {
        initData();
        if (image != null) lenIm = image.length;
        String[] recipients = new String[]{emailTeacher};
        String subject = body + " " + name + getString(R.string.sbj) + " " + youClass;
        String content = getString(R.string.prof) + " " + im;
        im = (lenIm > 74000);
        Intent intentEmail = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
        intentEmail.putExtra(Intent.EXTRA_EMAIL, recipients);
        intentEmail.putExtra(Intent.EXTRA_SUBJECT, subject);
        intentEmail.putExtra(Intent.EXTRA_TEXT, content);
        intentEmail.setType("text/plain");
        startActivity(Intent.createChooser(intentEmail, getString(R.string.forGmail)));
    }

    private void initData() {
        viewModel = ViewModelProviders.of(requireActivity()).get(LessonViewModel.class);
        viewModel.getLessonLiveData().observe(this, new Observer<List<LessonData>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(List<LessonData> lessonData) {
                //
                // we take the last recorded value from the database, while the database is not empty
                // and paste into textview
                //
                if (lessonData.size() != 0) {
                    name = lessonData.get(0).name;
                    youClass = lessonData.get(0).myClass;
                    emailTeacher = lessonData.get(0).emailTeacher;
                    if (lessonData.size() > 1) image = lessonData.get(1).image;
                }
            }
        });
    }
}
