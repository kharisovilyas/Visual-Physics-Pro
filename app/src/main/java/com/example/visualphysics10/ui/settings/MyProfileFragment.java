package com.example.visualphysics10.ui.settings;

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

import java.util.List;

public class MyProfileFragment extends Fragment {
    private FragmentMyProfileBinding binding;
    public static String string = "VisPhysUser";
    private LessonViewModel viewModel;
    public LessonData lessonDataList = new LessonData();


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
        binding.change.setOnClickListener(v -> {
            setData();
        });
        binding.save.setOnClickListener(v->{
            saveData();
            init();
        });
        init();
    }

    private void saveData() {
        lessonDataList.name = String.valueOf(binding.inputName.getText());
        lessonDataList.myClass = String.valueOf(binding.inputYouClass.getText());
        lessonDataList.emailTeacher = String.valueOf(binding.inputEmailTeacher.getText());
        viewModel = ViewModelProviders.of(requireActivity()).get(LessonViewModel.class);
        viewModel.getLessonLiveData().observe(this, new Observer<List<LessonData>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(List<LessonData> lessonData) {
                //
                // we take the last recorded value from the database, while the database is not empty
                // and paste into textview
                //
                lessonData.add(0, lessonDataList);
            }
        });
        viewModel.deleteAllData();
        viewModel.insert(lessonDataList);
    }

    private void setData() {
        binding.inputName.setEnabled(true);
        binding.inputName.setText(dataSelect(0));
        binding.inputYouClass.setEnabled(true);
        binding.inputYouClass.setText(dataSelect(1));
        binding.inputEmailTeacher.setEnabled(true);
        binding.inputEmailTeacher.setText(dataSelect(2));
        binding.save.setEnabled(true);
    }

    private void init() {
        TextView name = binding.name;
        TextView myClass = binding.youClass;
        TextView emailTeacher = binding.emailTeacher;
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
                    name.setText(getString(R.string.prof1) + " " + lessonData.get(0).name);
                    myClass.setText(getString(R.string.prof2) + " " + lessonData.get(0).myClass);
                    emailTeacher.setText(getString(R.string.prof3) + " "+ lessonData.get(0).emailTeacher);
                }
            }
        });
    }

    private String dataSelect(int i) {
        final String[] string = new String[1];
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
                    switch (i) {
                        case 0:
                            string[0] = String.valueOf(lessonData.get(0).name);
                            break;
                        case 1:
                            string[0] = String.valueOf(lessonData.get(0).myClass);
                            break;
                        case 2:
                            string[0] = String.valueOf(lessonData.get(0).emailTeacher);
                            break;

                    }
                }
            }
        });
        return string[0];
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void addToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((MainActivity) requireActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
    }
}
