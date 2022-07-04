package com.example.visualphysics10.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.visualphysics10.MainActivity;
import com.example.visualphysics10.R;

import com.example.visualphysics10.databinding.AuthorsListBinding;
import com.google.android.material.appbar.MaterialToolbar;

public class AuthorsInfoDialog extends DialogFragment {
    private AuthorsListBinding binding;
    public static DialogFragment newInstance() {
        return new AuthorsInfoDialog();
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
        binding = AuthorsListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addToolbar();
        initText();
    }

    @SuppressLint("SetTextI18n")
    private void initText() {
        binding.text1.setText(getString(R.string.text1));
        binding.text2.setMovementMethod(LinkMovementMethod.getInstance());
        binding.text3.setMovementMethod(LinkMovementMethod.getInstance());
        binding.text4.setText(R.string.text2);
        binding.text5.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private void addToolbar() {
        MaterialToolbar toolbar = binding.toolbar;
        ((MainActivity) requireActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.close);
        toolbar.setTitle("Список заимствований");
        toolbar.setNavigationOnClickListener(v -> {
            dismiss();
        });
    }
}
