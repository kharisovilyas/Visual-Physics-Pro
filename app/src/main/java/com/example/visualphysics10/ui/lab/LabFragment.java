package com.example.visualphysics10.ui.lab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.visualphysics10.MainActivity;
import com.example.visualphysics10.R;
import com.example.visualphysics10.databinding.FragmentInfoBinding;
import com.example.visualphysics10.databinding.FragmentLabBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.Objects;

public class LabFragment extends Fragment {
    private FragmentLabBinding binding;
    private MaterialTextView textView;
    private final int position;
    YouTubePlayerView player;

    public LabFragment(int position) {
        this.position = position;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLabBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //methods initialize data depending on the lesson (fragment)
        addToolbar();
    }


    //select desired text (lecture of lesson)
    private int selectDescription() {
        switch (position) {
            case 0:
                return R.string.lesson1_inform;
            case 1:
                return R.string.lesson2_inform;
            case 2:
                return R.string.lesson3_inform;
            case 3:
                return R.string.lesson4_inform;
            case 4:
                return R.string.lesson5_inform;
            default:
                return 0;
        }
    }



    private void addToolbar() {
        MaterialToolbar toolbar = binding.toolbar;
        ((MainActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.close);
        toolbar.setTitle(R.string.title_lab);
        toolbar.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
