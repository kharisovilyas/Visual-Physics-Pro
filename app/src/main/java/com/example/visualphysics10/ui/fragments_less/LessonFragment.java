package com.example.visualphysics10.ui.fragments_less;

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
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.visualphysics10.MainActivity;
import com.example.visualphysics10.R;
import com.example.visualphysics10.database.LessonData;
import com.example.visualphysics10.database.LessonViewModel;
import com.example.visualphysics10.database.PhysicsData;
import com.example.visualphysics10.databinding.FragmentLessonBinding;

import com.example.visualphysics10.objects.PhysicsModel;
import com.example.visualphysics10.physics.MathPart;
import com.example.visualphysics10.physics.PhysicView;
import com.example.visualphysics10.ui.EndEducationDialog;
import com.example.visualphysics10.ui.MainFlag;
import com.example.visualphysics10.ui.fragments_info.input.FullScreenDialog;
import com.example.visualphysics10.ui.fragments_info.test.FragmentTest;
import com.example.visualphysics10.ui.fragments_info.youtube.FragmentInfo;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;
import java.util.Objects;

public class LessonFragment extends Fragment {
    private FragmentLessonBinding binding;
    private int position;
    private PhysicView gameView;
    public static boolean isMoving = false;
    private FloatingActionButton info;
    private FloatingActionButton play;
    private DrawerLayout drawerLayout;
    private NavigationView navigation;
    private LessonViewModel viewModel;
    private int count = 0;
    SharedPreferences education;
    private final String EDUCATION_PREFERENCES = "educationEnd";
    private boolean educationEnd;
    private int targetCount = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLessonBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public LessonFragment(int position) {
        this.position = position;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addToolbar();
        count = 0;
        gameView = binding.physicsView;
        MainFlag.setThreadStop(false);
        // in this method we wait for SurfaceView until she gets her size. And let's start!
        waitingForSV();
        //sound
        addMediaPlayer();
        if (position == 1) binding.land.setVisibility(View.GONE);
        //
        play = binding.play;
        FloatingActionButton restart = binding.restart;
        FloatingActionButton startInput = binding.startInput;
        FloatingActionButton startTest = binding.startTest;
        info = binding.info;
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

        //start testings
        startTest.setOnClickListener(v -> {
            startTesting();
        });

        //when setVisible - we can click in info and watch YouTube Video
        info.setOnClickListener(v -> {
            gameView.stopThread();
            createdFullScreenInfo();
        });

        //TODO: start education, but once...
        education = Objects.requireNonNull(getContext()).getSharedPreferences(EDUCATION_PREFERENCES, Context.MODE_PRIVATE);
        if (education.contains(EDUCATION_PREFERENCES)) {
            educationEnd = education.getBoolean(EDUCATION_PREFERENCES, false);
        }
        if (!educationEnd) {
            startEducation();
        }
    }

    private void addMediaPlayer() {
        MediaPlayer end = MediaPlayer.create(getContext(), R.raw.end);
        MediaPlayer rotation = MediaPlayer.create(getContext(), R.raw.rotation);
        MediaPlayer landing = MediaPlayer.create(getContext(), R.raw.landling);
        MediaPlayer collision = MediaPlayer.create(getContext(), R.raw.collision);
        //PhysicsModel.addSound(end, rotation, landing, collision);
    }

    private void waitingForSV() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //call the engine constructor for first fragment to Velocity
                //gameView.addModelGV(position);
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
                TapTarget.forView(binding.info,
                        "Нажмите инфо", "Чтобы получить больше информации, прослушать лекцию")
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
                TapTarget.forView(binding.outputHere,
                        "Нажмите на иконку или свапните", "Чтобы посмотреть введенные и найденные данные")
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
                TapTarget.forView(binding.startTest,
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
                        .targetRadius(100)).listener(new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {

            }

            @Override
            public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                targetCount++;
                if (targetCount == 5) {
                    createEndEducationDialog();
                    educationEnd();
                }
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {

            }
        }).start();
    }

    private void createEndEducationDialog() {
        DialogFragment dialogFragment = EndEducationDialog.newInstance();
        dialogFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "congratulations!");
    }

    //using SharedPreferences we end education
    @SuppressLint("CommitPrefEdits")
    private void educationEnd() {
        SharedPreferences.Editor editor = education.edit();
        editor.putBoolean(EDUCATION_PREFERENCES, true);
        editor.apply();
    }

    private void getMessage() {
        addToolbarNav();
        MaterialTextView outputMes = binding.output1;
        MaterialTextView outputNull = binding.output2;
        MaterialTextView outputNull1 = binding.output3;
        MaterialTextView outputNull2 = binding.output4;
        MaterialTextView outputNull3 = binding.output5;
        outputMes.setText(R.string.outputMes);
        outputNull.setText("");
        outputNull1.setText("");
        outputNull2.setText("");
        outputNull3.setText("");
    }

    //Output Data
    @SuppressLint("SetTextI18n")
    public void outputData() {
        drawerLayout = binding.drawerLayout;
        navigation = binding.navigationView;
        addToolbarNav();
        switch (position) {
            case 0:
                outputData1();
                break;
            case 1:
                outputData2();
                break;
            case 2:
                outputData3();
                break;
            case 3:
                outputData4();
                break;
        }

    }

    private void outputData4() {
        MaterialTextView outputSpeed = binding.output1;
        MaterialTextView outputAcc = binding.output2;
        MaterialTextView outputAngle = binding.output3;
        MaterialTextView outputHeight = binding.output4;
        MaterialTextView outputTime = binding.output5;
        String string = getString(R.string.outputSpeed) + "\n" + PhysicsData.getSpeed() + " [м/с]";
        String string2 = getString(R.string.outputAcc) + "\n" + PhysicsData.getAcc() + " [м/с^2]";
        String string3 = getString(R.string.outputAngle) + "\n" + PhysicsData.getAngle() + " [°]";
        String string4 = getString(R.string.outputHeight) + "\n" + PhysicsData.getY0() / 2 + " [м]";
        String string5 = getString(R.string.outputTime) + "\n" + MathPart.getTime(PhysicsData.getSpeed(), PhysicsData.getAngle()) + " [c]";
        outputSpeed.setText(string);
        outputAcc.setText(string2);
        outputAngle.setText(string3);
        outputHeight.setText(string4);
        outputTime.setText(string5);
    }

    private void outputData3() {
        MaterialTextView outputSpeed = binding.output1;
        MaterialTextView outputMass = binding.output2;
        MaterialTextView outputForce = binding.output3;
        MaterialTextView outputAcc = binding.output4;
        String string = getString(R.string.outputSpeed) + "\n" + PhysicsData.getSpeed() + " [м/с]";
        String string2 = getString(R.string.outputAcc) + "\n" + PhysicsData.getMass1() + " [кг]";
        String string3 = getString(R.string.outputForce) + "\n" + PhysicsData.getForce() + " [Н]";
        String string4 = getString(R.string.outputAcc2) + "\n" + PhysicsData.getAcc() + " [м/с^2]";
        outputSpeed.setText(string);
        outputMass.setText(string2);
        outputForce.setText(string3);
        outputAcc.setText(string4);
    }

    private void outputData2() {
        MaterialTextView outputSpeed = binding.output1;
        MaterialTextView outputAcc = binding.output2;
        MaterialTextView outputSpeedEnd = binding.output3;
        String string = getString(R.string.outputSpeed) + "\n" + PhysicsData.getSpeed() + " [м/с]";
        String string2 = getString(R.string.outputAcc) + "\n" + PhysicsData.getRadius() + " [м/с^2]";
        String string3 = getString(R.string.outputFrequency) + "\n" + MathPart.getFrequency() + " [c^-1]";
        outputSpeed.setText(string);
        outputAcc.setText(string2);
        outputSpeedEnd.setText(string3);
    }

    private void outputData1() {
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
        toolbar.setTitle("Введенные данные");
    }

    private void pauseClick() {
        play.setImageResource(R.drawable.play_arrow);
        gameView.stopDraw(0);

    }

    private void playClick() {
        play.setImageResource(R.drawable.pause_circle);
        isMoving = true;
        info.setVisibility(View.VISIBLE);
        viewModel = ViewModelProviders.of(requireActivity()).get(LessonViewModel.class);
        viewModel.getLessonLiveData().observe(this, new Observer<List<LessonData>>() {
            @Override
            public void onChanged(List<LessonData> lessonData) {
                PhysicsData.setSpeed(lessonData.get(0).speed);
                PhysicsData.setAcc(lessonData.get(0).acc);
            }
        });
        gameView.updateMoving(PhysicsData.getSpeed(), 0, 0);
    }


    private void startTesting() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim)
                .replace(R.id.container, new FragmentTest())
                .addToBackStack(null)
                .commit();
    }

    private void createdFullScreenInfo() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim)
                .replace(R.id.container, new FragmentInfo())
                .addToBackStack(null)
                .commit();
    }

    private void createdFullScreenDialog() {
        //DialogFragment dialogFragment = new FullScreenDialog(position);
        //dialogFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "input");
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

    private void createDrawer() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
