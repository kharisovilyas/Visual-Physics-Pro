package com.example.visualphysics10.ui.item;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.visualphysics10.MainActivity;
import com.example.visualphysics10.R;
import com.example.visualphysics10.database.LessonData;
import com.example.visualphysics10.database.LessonViewModel;
import com.example.visualphysics10.databinding.FragmentMyProfileBinding;
import com.example.visualphysics10.databinding.FragmentSettingsBinding;

import java.util.List;
import java.util.Objects;

public class MyProfileFragment extends Fragment {
    private FragmentMyProfileBinding binding;
    public static String string="VisPhysUser";
    private static String nameHint;
    public static boolean isFABClicked = false;
    private LessonViewModel viewModel;
    public LessonData lessonDataList = new LessonData();

    public void setStr(String hint) {
        nameHint = hint;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addToolbar();
        init();
    }

    private void init() {
        TextView name = binding.name;
        TextView myClass = binding.youClass;
        TextView emailTeacher= binding.emailTeacher;
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
                    name.setText(getString(R.string.prof1)+ lessonData.get(0).name);
                    myClass.setText(getString(R.string.prof2) + lessonData.get(0).myClass);
                    emailTeacher.setText(getString(R.string.prof3) +lessonData.get(0).emailTeacher);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void addToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((MainActivity)requireActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
    }
}
