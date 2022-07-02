package com.example.visualphysics10.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.visualphysics10.MainActivity;
import com.example.visualphysics10.R;
import com.example.visualphysics10.databinding.FragmentSettingsBinding;

public class SettingsFragment1 extends Fragment {
    private FragmentSettingsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addToolbar();
    }

    private void addToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((MainActivity)requireActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.settings_0);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(v->{
            requireActivity().onBackPressed();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
