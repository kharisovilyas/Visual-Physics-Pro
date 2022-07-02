package com.example.visualphysics10.ui.lesson;

import android.graphics.Bitmap;
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
import com.example.visualphysics10.objects.PhysicsModel;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Objects;

public class GraphFragment extends Fragment {
    FragmentGraphBinding binding;
    private final int position;
    public static ArrayList<DataPoint> vectorsX = new ArrayList<DataPoint>() {
    };
    public static ArrayList<DataPoint> vectorsY = new ArrayList<DataPoint>() {
    };


    public GraphFragment(int position) {
        this.position = position;
    }

    private void addToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((MainActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setTitle(R.string.title_graph);
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
        addToolbar();
        //first series
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
        });
        GraphView graph = binding.velocityGraph;
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(100);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(100);
        graph.getGridLabelRenderer().setGridColor(R.color.black);
        graph.addSeries(series);
        graph.getGridLabelRenderer().setVerticalLabelsColor(R.color.black);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(R.color.black);
        series.setThickness(5);
        //second series
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>(new DataPoint[]{
        });
        GraphView graph2 = binding.velocityGraph2;
        graph2.getViewport().setScrollableY(true);
        graph2.getViewport().setScrollable(true);
        graph2.getViewport().setMinX(0);
        graph2.getViewport().setMaxX(100);
        graph2.getViewport().setMinY(0);
        graph2.getViewport().setMaxY(100);
        graph2.getGridLabelRenderer().setGridColor(R.color.black);
        graph2.addSeries(series2);
        graph2.getGridLabelRenderer().setVerticalLabelsColor(R.color.black);
        graph2.getGridLabelRenderer().setHorizontalLabelsColor(R.color.black);
        series2.setThickness(5);


        //first graph
        binding.buildGraph.setOnClickListener(v -> {
            for (int i = 0; i < vectorsX.size(); i++) {
                series.appendData((DataPoint) vectorsX.toArray()[i], false, vectorsX.size());
                Log.d("TAG", String.valueOf(vectorsX.size()));
            }
            vectorsX.clear();
            vectorsX.remove(series);
            PhysicsModel.time = 0;
        });
        //second graph
        binding.buildGraph2.setOnClickListener(v -> {
            for (int i = 0; i < vectorsY.size(); i++) {
                Log.d("TAG", String.valueOf(vectorsY.size()));
                series2.appendData((DataPoint) vectorsY.toArray()[i], false, vectorsY.size());
                Log.d("TAG", String.valueOf(vectorsY.size()));
            }
            vectorsY.clear();
            vectorsY.remove(series2);
            PhysicsModel.time = 0;
        });

    }

    public static void addVectorX(int x, int y, int time) {
        DataPoint dataPoint = new DataPoint(x, y);
        vectorsX.add(dataPoint);
    }

    public static void addVectorY(int x, int y, int time) {
        DataPoint dataPoint = new DataPoint(x, y);
        vectorsY.add(dataPoint);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        vectorsX.clear();
        vectorsY.clear();
    }
}
