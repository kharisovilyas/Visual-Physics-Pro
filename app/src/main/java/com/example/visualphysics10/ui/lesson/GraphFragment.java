package com.example.visualphysics10.ui.lesson;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.visualphysics10.MainActivity;
import com.example.visualphysics10.R;
import com.example.visualphysics10.databinding.FragmentGraphBinding;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Objects;

public class GraphFragment extends Fragment {
    FragmentGraphBinding binding;
    private final int position;
    public static ArrayList<DataPoint> dataPoints = new ArrayList<DataPoint>() {
    };

    public static LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{

    });
    public static boolean deleteDataPoints=false;

    public GraphFragment(int position) {
        this.position = position;
    }

    private void addToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((MainActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGraphBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GraphView graph = binding.velocityGraph;
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(10);
        //graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(10);
        //graph.getViewport().setYAxisBoundsManual(true);
        graph.addSeries(series);
    }

    public static void addPoint(int x, int y, int time) {
        DataPoint dataPoint = new DataPoint(x, y);
        series.appendData(dataPoint, true, time);
        dataPoints.add(dataPoint);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        deleteDataPoints=true;
        DataPoint[] dp = dataPoints.toArray(new DataPoint[dataPoints.size()]);
        series.resetData(dp);
        dataPoints.remove(dataPoints);
        dataPoints.clear();
    }
}
