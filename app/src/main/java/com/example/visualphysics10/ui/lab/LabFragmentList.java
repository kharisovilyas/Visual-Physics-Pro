package com.example.visualphysics10.ui.lab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visualphysics10.MainActivity;
import com.example.visualphysics10.R;
import com.example.visualphysics10.adapter.RecyclerViewAdapter;
import com.example.visualphysics10.databinding.FragmentLabListBinding;
import com.example.visualphysics10.databinding.FragmentLecturesBinding;
import com.example.visualphysics10.ph_lab.PlaceHolderContent4;
import com.example.visualphysics10.ph_lectures.PlaceHolderContent3;
import com.example.visualphysics10.ui.lectures.FragmentInfo;

import java.util.Objects;

public class LabFragmentList extends Fragment implements RecyclerViewAdapter.OnLessonListener{
    private FragmentLabListBinding binding;
    private final int mColumnCount = 1;
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
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                .replace(R.id.container, new LabFragment(position))
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }
}
