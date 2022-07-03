package com.example.visualphysics10.ui.lesson;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.visualphysics10.MainActivity;
import com.example.visualphysics10.R;
import com.example.visualphysics10.database.PhysicsData;
import com.example.visualphysics10.databinding.LessonFragmentBinding;
import com.example.visualphysics10.objects.PhysicsModel;
import com.example.visualphysics10.physics.MathPart;
import com.example.visualphysics10.physics.PhysicView;
import com.example.visualphysics10.ui.input.FullScreenDialog;
import com.example.visualphysics10.ui.input.FullScreenDialog5;
import com.example.visualphysics10.ui.lectures.FragmentInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;

//
//TODO: a fragment in which the main actions take place - communication with SurfaceView, output and input of data, saving them to the database
// there are 5 such fragments in total for for each lesson
// for example this fragment - Velocity (first in RecyclerView), this is where the logic of user interaction with the physics engine and the database takes place
// there is no point in writing comments for the remaining 4 fragments - they are identical
public class LessonFragment extends Fragment {
    public static boolean isMoving2 = false;
    public static boolean isMoving4 = false;
    public static boolean isMoving5 = false;
    public static boolean vectorsEnabled = false;
    private LessonFragmentBinding binding;
    private PhysicView gameView;
    public static boolean isMoving = false;
    private FloatingActionButton play;
    private int count = 0;
    private final int position;

    public LessonFragment(int position) {
        this.position = position;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = LessonFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addToolbar();
        count = 0;
        gameView = binding.physicsView;
        selectFlags(position);
        if (position == 1) binding.land.setVisibility(View.GONE);
        // in this method we wait for SurfaceView until she gets her size. And let's start!
        waitingForSV();
        play = binding.play;
        FloatingActionButton restart = binding.restart;
        FloatingActionButton startInput = binding.startInput;
        FloatingActionButton startGraph = binding.startGraph;

        //double click on this button calls another function - this way we save space in fragment
        getMessage();
        play.setOnClickListener(v -> {
            if (count % 2 == 0) {
                playClick();
                outputData();
            } else pauseClick();
            count++;
        });
        //allows the user to restart the visualization with the input data if if you want to change the data - restart and input
        restart.setOnClickListener(v -> {
            createDialog();
        });
        //create Dialog where input Data for visualization
        startInput.setOnClickListener(v -> {
            createdFullScreenDialog();
        });
        binding.vectors.setOnClickListener(v -> {
            vectorsEnabled = binding.vectors.isChecked();
        });
        //start testings
        if (position == 4) startGraph.setVisibility(View.GONE);
        startGraph.setOnClickListener(v -> {
            startGraph();
        });
    }

    private void selectFlags(int position) {
        switch (position) {
            case 0:
                PhysicsModel.L1 = true;
                break;
            case 1:
                PhysicsModel.L2 = true;
                PhysicsModel.L2start = true;
                PhysicsModel.firstDraw = true;
                break;
            case 2:
                PhysicsModel.L3 = true;
                break;
            case 3:
                PhysicsModel.L4 = true;
                break;
            case 4:
                PhysicsModel.L5 = true;
                break;

        }
    }

    private void waitingForSV() {
        final Handler handler = new Handler();
        //minimal latency for users
        handler.postDelayed(() -> {
            //call the engine constructor for first fragment to Velocity
            gameView.addModelGV(position);
        }, 100);
    }

    private void getMessage() {
        addToolbarNav();
        MaterialTextView outputMes = binding.output1;
        outputMes.setText(R.string.outputMes);
        binding.output2.setText("");
        binding.output3.setText("");
        binding.output4.setText("");
        binding.output5.setText("");
        binding.output6.setText("");
    }

    //Output Data
    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    public void outputData() {
        DrawerLayout drawerLayout = binding.drawerLayout;
        NavigationView navigation = binding.navigationView;
        addToolbarNav();
        MaterialTextView output1 = binding.output1;
        MaterialTextView output2 = binding.output2;
        MaterialTextView output3 = binding.output3;
        MaterialTextView output4 = binding.output4;
        MaterialTextView output5 = binding.output5;
        MaterialTextView output6 = binding.output6;
        String string = "";
        String string2 = "";
        String string3 = "";
        String string4 = "";
        String string5 = "";
        String string6 = "";
        MathPart.setFrequency((2 * Math.PI * PhysicsData.getRadius()) / PhysicsData.getSpeed());
        string = getString(R.string.outputSpeed) + "\n" + PhysicsData.getSpeed() + " [м/с]";
        switch (position) {
            case 0:
                string2 = getString(R.string.outputAcc) + "\n" + PhysicsData.getAcc() + " [м/с^2]";
                break;
            case 1:
                string2 = getString(R.string.outputRadius) + "\n" + PhysicsData.getRadius() + " [м]";
                string3 = getString(R.string.outputFreq) + MathPart.getFrequency() + "[c^-1]";
                break;
            case 2:
                string2 = getString(R.string.outputF) + PhysicsData.getForce() + " [Н]";
                string3 = getString(R.string.outputAccG) + PhysicsData.getAcc() + " [м/с^2]";
                break;
            case 3:
                 string2 = getString(R.string.outputAcc) + "\n" + PhysicsData.getAcc() + " [м/с^2]";
                 string3 = getString(R.string.outputAngle) + "\n" + PhysicsData.getAngle() + " [°]";
                 string4 = getString(R.string.outputHeight) + "\n" + PhysicsData.getY0() / 2 + " [м]";
                 string5 = getString(R.string.outputTime) + "\n" + MathPart.getTime(PhysicsData.getSpeed(), PhysicsData.getAngle()) + " [c]";
                 break;
            case 4:
                string = getString(R.string.outputSpeed1) + "\n" + PhysicsData.getSpeed() + " [м/с]";
                string2 = getString(R.string.outputSpeed2) + "\n" + PhysicsData.getSpeed2() + " [м/с]";
                string3 = getString(R.string.outputMass1) + "\n" + PhysicsData.getMass1() + " [кг]";
                string4 = getString(R.string.outputMass2) + "\n" + PhysicsData.getMass2() + " [кг]";
                string5 = getString(R.string.outputImp1) + "\n" + MathPart.getImp1(PhysicsData.getSpeed(), PhysicsData.getMass1()) + " [кг * м/с]";
                string6 = getString(R.string.outputImp2) + "\n" + MathPart.getImp2(PhysicsData.getSpeed2(), PhysicsData.getMass2()) + " [кг * м/с]";
        }
        output1.setText(string);
        output2.setText(string2);
        output3.setText(string3);
        output4.setText(string4);
        output5.setText(string5);
        output6.setText(string6);

        switch (position){
            case 1:
                output3.setTextColor(R.color.red);
                break;
            case 2:
                output4.setTextColor(R.color.red);
                break;
            case 3:
                output4.setTextColor(R.color.red);
                output5.setTextColor(R.color.red);
                break;
            case 4:
                output5.setTextColor(R.color.red);
                output6.setTextColor(R.color.red);
        }
    }

    private void addToolbarNav() {
        Toolbar toolbar = binding.toolbarNavView;
        ((MainActivity) requireActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.outputDataTitle);
    }

    private void pauseClick() {
        play.setImageResource(R.drawable.play_arrow);
        gameView.stopDraw(0);
        if (position == 4) gameView.stopDraw(1);

    }

    private void playClick() {
        play.setImageResource(R.drawable.pause_circle);
        startMoving(position);
        if (position == 3) PhysicsModel.beginning = true;
        gameView.updateMoving(PhysicsData.getSpeed(), 0, 0);
        if (position == 4) gameView.updateMoving(-PhysicsData.getSpeed2(), 0, 1);
    }

    private void startMoving(int position) {
        switch (position) {
            case 0:
            case 2:
                isMoving = true;
                break;
            case 1:
                isMoving2 = true;
                break;
            case 3:
                isMoving4 = true;
                break;
            case 4:
                isMoving5 = true;
                break;
        }
    }


    private void startGraph() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim)
                .replace(R.id.container, new GraphFragment(position))
                .addToBackStack(null)
                .commit();
    }

    private void createdFullScreenInfo() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim)
                .replace(R.id.container, new FragmentInfo(position))
                .addToBackStack(null)
                .commit();
    }

    private void createdFullScreenDialog() {
        DialogFragment fragment;
        if (position == 4) fragment = FullScreenDialog5.newInstance();
        else fragment = new FullScreenDialog(position);
        fragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "input");
    }

    @SuppressLint("ResourceType")
    private void createDialog() {
        play.setImageResource(R.drawable.play_arrow);
        count += count % 2;
        gameView.restartClick(0);
        if (position == 4) gameView.restartClick(1);
        getMessage();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.icon_toolbar, menu);
        setHasOptionsMenu(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("RestrictedApi")
    private void addToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((MainActivity) requireActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setTitle(selectTitle(position));
        toolbar.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        toolbar.inflateMenu(R.menu.icon_toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                createDrawer();
                return false;
            }
        });
    }

    //choose title for any lesson
    private int selectTitle(int position) {
        switch (position) {
            //title for lesson #1 - velocity and ect..
            case 0:
                return R.string.titleL1;
            case 1:
                return R.string.titleL2;
            case 2:
                return R.string.titleL3;
            case 3:
                return R.string.titleL4;
            case 4:
                return R.string.titleL5;
            default:
                return 0;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PhysicsModel.L1 = false;
        PhysicsModel.L2 = false;
        PhysicsModel.L3 = false;
        PhysicsModel.L4 = false;
        PhysicsModel.L5 = false;
        binding = null;
    }

    private void createDrawer() {
        DrawerLayout drawerLayout = binding.drawerLayout;
        drawerLayout.openDrawer(GravityCompat.END);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vectorsEnabled = false;
    }

}
