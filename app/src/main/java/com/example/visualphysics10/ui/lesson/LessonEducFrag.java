package com.example.visualphysics10.ui.lesson;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
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
import com.example.visualphysics10.database.LessonViewModel;
import com.example.visualphysics10.database.PhysicsData;
import com.example.visualphysics10.databinding.LessonFragmentBinding;
import com.example.visualphysics10.objects.PhysicsModel;
import com.example.visualphysics10.physics.PhysicView;
import com.example.visualphysics10.ui.EndEducationDialog;
import com.example.visualphysics10.ui.input.FullScreenDialog;
import com.example.visualphysics10.ui.test.FragmentTest;
import com.example.visualphysics10.ui.lectures.FragmentInfo;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;

public class LessonEducFrag extends Fragment {
    private LessonFragmentBinding binding;
    private PhysicView gameView;
    public static boolean isMoving = false;
    private MaterialCheckBox vectors;
    private FloatingActionButton play;
    private DrawerLayout drawerLayout;
    private NavigationView navigation;
    private LessonViewModel viewModel;
    private int count = 0;
    SharedPreferences education;
    private final String EDUCATION_PREFERENCES = "educationEnd";
    private boolean educationEnd;
    private int targetCount = 0;
    public static boolean vectorsEnabled = false;
    public static boolean repeatEdu;

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
        PhysicsModel.L1 = true;
        // in this method we wait for SurfaceView until she gets her size. And let's start!
        waitingForSV();
        play = binding.play;
        FloatingActionButton restart = binding.restart;
        FloatingActionButton startInput = binding.startInput;
        FloatingActionButton startGraph = binding.startGraph;
        vectors = binding.vectors;
        //double click on this button calls another function - this way we save space in fragment
        getMessage();
        play.setOnClickListener(v -> {
            if (count % 2 == 0) {
                playClick();
                outputData();
            }
            else pauseClick();
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

        //start testings
        startGraph.setOnClickListener(v -> {
            startGraph();
        });

        //when setVisible - we can click in info and watch YouTube Video
        vectors.setOnClickListener(v -> {
            vectorsEnabled = binding.vectors.isChecked();
        });

        //TODO: start education, but once...
        education = getContext().getSharedPreferences(EDUCATION_PREFERENCES, Context.MODE_PRIVATE);
        if (education.contains(EDUCATION_PREFERENCES)) {
            educationEnd = education.getBoolean(EDUCATION_PREFERENCES, false);
        }
        if (!educationEnd) {
            startEducation();
        }
    }

    private void waitingForSV() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //call the engine constructor for first fragment to Velocity
                gameView.addModelGV(0);
            }
            //minimal latency for users
        }, 100);
    }

    //logic TapTargetView - MaterialDesign
    private void startEducation() {
        new TapTargetSequence((Activity) getContext()).targets(
                TapTarget.forView(binding.startInput,
                        "Введите исходные данные здесь", "Не забудьте сохранить данные и закройте окно")
                        .outerCircleColor(R.color.primary)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(24)
                        .descriptionTextSize(18)
                        .titleTextColor(R.color.white)
                        .descriptionTextColor(R.color.black)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .targetRadius(100),
                TapTarget.forView(binding.play,
                        "Нажмите старт", "Чтобы начать визуализацию")
                        .outerCircleColor(R.color.primary)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(24)
                        .descriptionTextSize(18)
                        .titleTextColor(R.color.white)
                        .descriptionTextColor(R.color.black)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .targetRadius(100),
                TapTarget.forView(binding.outputHere, "Нажмите или свайпните", "Чтобы посмотреть какие данные можно посчитать, на основании ввведенных")
                        .outerCircleColor(R.color.primary)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(24)
                        .descriptionTextSize(18)
                        .titleTextColor(R.color.white)
                        .descriptionTextColor(R.color.black)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .targetRadius(100),
                TapTarget.forView(binding.vectors,
                        "Выбирите показать векторы", "Чтобы получить увидеть как меняется скорость в процессе движения")
                        .outerCircleColor(R.color.primary)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(24)
                        .descriptionTextSize(18)
                        .titleTextColor(R.color.white)
                        .descriptionTextColor(R.color.black)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .targetRadius(100),
                TapTarget.forView(binding.startGraph,
                        "Нажмите и пройдите тест", "Чтобы закрепить усвоенный материал")
                        .outerCircleColor(R.color.primary)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(24)
                        .descriptionTextSize(18)
                        .titleTextColor(R.color.white)
                        .descriptionTextColor(R.color.black)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .targetRadius(100),
                TapTarget.forView(binding.exitHere, "Выйдите из урока", "")
                        .outerCircleColor(R.color.primary)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(24)
                        .descriptionTextSize(18)
                        .titleTextColor(R.color.white)
                        .descriptionTextColor(R.color.black)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .targetRadius(100))
                        .listener(new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {

            }

            @Override
            public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                targetCount++;
                if (targetCount == 6) {
                    requireActivity().getSupportFragmentManager().popBackStack();
                    repeatEdu = true;
                    //createEndEducationDialog();
                    //educationEnd();
                }
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {

            }
        }).start();
    }

    private void getMessage() {
        addToolbarNav();
        MaterialTextView outputMes = binding.output1;
        MaterialTextView outputNull = binding.output2;
        outputMes.setText(R.string.outputMes);
        outputNull.setText("");
    }

    //Output Data
    @SuppressLint("SetTextI18n")
    public void outputData() {
        drawerLayout = binding.drawerLayout;
        navigation = binding.navigationView;
        addToolbarNav();
        MaterialTextView outputSpeed = binding.output1;
        MaterialTextView outputAcc = binding.output2;
        String string = getString(R.string.outputSpeed) + "\n" + PhysicsData.getSpeed() + " [м/с]";
        String string2 = getString(R.string.outputAcc) + "\n" + PhysicsData.getAcc() + " [м/с^2]";
        outputSpeed.setText(string);
        outputAcc.setText(string2);
    }

    private void addToolbarNav() {
        Toolbar toolbar = binding.toolbarNavView;
        ((MainActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.outputDataTitle);
    }

    private void pauseClick() {
        play.setImageResource(R.drawable.play_arrow);
        gameView.stopDraw(0);

    }

    private void playClick() {
        play.setImageResource(R.drawable.pause_circle);
        isMoving = true;
        vectors.setVisibility(View.VISIBLE);
        gameView.updateMoving(PhysicsData.getSpeed(), 0, 0);
    }


    private void startGraph() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim)
                .replace(R.id.container, new GraphFragment(0))
                .addToBackStack(null)
                .commit();
    }

    private void createdFullScreenInfo() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim)
                .replace(R.id.container, new FragmentInfo(0))
                .addToBackStack(null)
                .commit();
    }

    private void createdFullScreenDialog() {
        DialogFragment dialogFragment = new FullScreenDialog(0);
        dialogFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "input");
    }

    @SuppressLint("ResourceType")
    private void createDialog() {
        play.setImageResource(R.drawable.play_arrow);
        count += count % 2;
        gameView.restartClick(0);
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
        ((MainActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setTitle(R.string.titleL1);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        PhysicsModel.L1 = false;
        binding = null;
    }

    private void createDrawer() {
        DrawerLayout drawerLayout = binding.drawerLayout;
        drawerLayout.openDrawer(GravityCompat.END);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}

