package com.example.visualphysics10.ui.input;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.visualphysics10.MainActivity;
import com.example.visualphysics10.R;
import com.example.visualphysics10.database.LessonData;
import com.example.visualphysics10.database.LessonViewModel;
import com.example.visualphysics10.database.PhysicsData;
import com.example.visualphysics10.databinding.L5FullscreenDialogBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class FullScreenDialog5 extends DialogFragment {
    private L5FullscreenDialogBinding binding;
    private TextInputEditText input_speed1;
    private TextInputEditText input_mass1;
    private TextInputEditText input_speed2;
    private TextInputEditText input_mass2;
    private SwitchMaterial typeImpulse;
    public static LessonData lessonDataList = new LessonData();
    private LessonViewModel viewModel;
    private boolean type = false;


    public static FullScreenDialog5 newInstance() {
        return new FullScreenDialog5();
    }

    //TODO: in order not to overload the FullScreenDialog, we call it only for lesson 5. functionality and methods are identical..

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
        binding = L5FullscreenDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addToolbar();
        input();
        typeImpulse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PhysicsData.setElasticImpulse(isChecked);
            }
        });
    }

    private void input() {
        input_speed1 = binding.inputSpeed1;
        input_mass1 = binding.inputMass1;
        input_speed2 = binding.inputSpeed2;
        input_mass2 = binding.inputMass2;
        typeImpulse = binding.typeImpulse;
        FloatingActionButton saveInput = binding.save;
        saveInput.setOnClickListener(v -> {
            try {
                saveData();
            } catch (Exception e) {
                e.printStackTrace();
            }
            dismiss();
        });
    }

    private void saveData() {
        PhysicsData.setSpeed(toDouble(input_speed1));
        PhysicsData.setMass1(toDouble(input_mass1));
        PhysicsData.setSpeed2(toDouble(input_speed2));
        PhysicsData.setMass2(toDouble(input_mass2));

    }


    private double toDouble(TextInputEditText input) {
        return Double.parseDouble(String.valueOf(input.getText()));
    }

    private void addToolbar() {
        MaterialToolbar toolbar = binding.toolbar;
        ((MainActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.close);
        toolbar.setTitle(R.string.title_input);
        toolbar.setNavigationOnClickListener(v -> {
            dismiss();
        });
    }

}
