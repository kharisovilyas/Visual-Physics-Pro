package com.example.visualphysics10.ui.lesson;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visualphysics10.MainActivity;
import com.example.visualphysics10.R;
import com.example.visualphysics10.adapter.RecyclerViewAdapter;
import com.example.visualphysics10.database.LessonData;
import com.example.visualphysics10.database.LessonViewModel;
import com.example.visualphysics10.databinding.FragmentItemListBinding;
import com.example.visualphysics10.ph_lesson.PlaceholderContent;
import com.example.visualphysics10.ui.EndEducationDialog;
import com.example.visualphysics10.ui.ExitDialog;
import com.example.visualphysics10.ui.MainFlag;
import com.example.visualphysics10.ui.lab.LabFragmentList;
import com.example.visualphysics10.ui.lectures.LecturesFragList;
import com.example.visualphysics10.ui.settings.MyProfileFragment;
import com.example.visualphysics10.ui.settings.SettingsFragment1;
import com.example.visualphysics10.ui.test.TaskListFragment;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Objects;

public class ItemFragmentList extends Fragment implements RecyclerViewAdapter.OnLessonListener {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    View header;
    private TextView headerName;
    private TextView headerClass;
    private FragmentItemListBinding binding;
    ImageView imageView;
    private DrawerLayout drawerLayout;
    private NavigationView navigation;
    private LessonViewModel viewModel;
    SettingsFragment1 settingsFragment1 = new SettingsFragment1();
    SharedPreferences education;
    private String EDUCATION_PREFERENCES = "educationEnd";
    private boolean educationEnd;
    private int targetCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentItemListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = view.getContext();
        RecyclerView recyclerView = binding.list;
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(new RecyclerViewAdapter(PlaceholderContent.ITEMS, this));
        addToolbar();
        listenerNav();
        editProfile();
        education = context.getSharedPreferences(EDUCATION_PREFERENCES, Context.MODE_PRIVATE);
        if (education.contains(EDUCATION_PREFERENCES)) {
            educationEnd = education.getBoolean(EDUCATION_PREFERENCES, false);
        }
        if (!educationEnd) {
            if (!LessonEducFrag.repeatEdu) startEducation();
            else repeatEducation();
        }
    }

    private void repeatEducation() {
        new TapTargetSequence((Activity) getContext()).targets(
                TapTarget.forView(binding.menuHere,
                        "Здесь вы найдете много полезной информации", "Вкладка Леции позволит закрпепить материал" + "\n" +
                                "Вкладка Задачи поможет усвоить материал" + "\n" +
                                "Вкладка Лабораторные позволит отправить учителю ваш прогресс для дальнейшей оценки")
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
                        createDrawer();
                        createEndEducationDialog();
                        educationEnd();
                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {

                    }
                }).start();
    }

    private void createEndEducationDialog() {
        DialogFragment dialogFragment = EndEducationDialog.newInstance();
        dialogFragment.show(requireActivity().getSupportFragmentManager(), "congratulations!");
    }

    //using SharedPreferences we end education
    @SuppressLint("CommitPrefEdits")
    private void educationEnd() {
        SharedPreferences.Editor editor = education.edit();
        editor.putBoolean(EDUCATION_PREFERENCES, true);
        editor.apply();
    }


    private void startEducation() {
        TapTargetView.showFor((Activity) requireContext(),
                TapTarget.forView(
                        binding.forEducation,
                        "Выберете урок и нажмите сюда,", "Чтобы запустить урок")
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
                new TapTargetView.Listener() {
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);
                        startEducFrag();
                    }
                }
        );

    }


    //created drawerLayout and NavigationView in him
    private void listenerNav() {
        drawerLayout = binding.drawerLayout;
        navigation = binding.navigationView;
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menu) {
                if (menu.getItemId() == R.id.exit) {
                    exitProfile();
                    return true;
                }
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                        .replace(R.id.container, selectDrawerItem(menu))
                        .addToBackStack(null)
                        .commit();
                MainFlag.setNotLesson(true);
                return true;
            }
        });
    }

    //get data from database
    @SuppressLint("SetTextI18n")
    private void editProfile() throws IndexOutOfBoundsException {
        header = navigation.getHeaderView(0);
        headerName = (TextView) header.findViewById(R.id.textView1);
        headerClass = (TextView) header.findViewById(R.id.textView2);
        imageView = header.findViewById(R.id.imageView);
        //
        //using ViewModel for subscribe to a database update using the observe method
        //
        viewModel = ViewModelProviders.of(requireActivity()).get(LessonViewModel.class);
        viewModel.getLessonLiveData().observe(this, new Observer<List<LessonData>>() {
            @Override
            public void onChanged(List<LessonData> lessonData) {
                //
                // we take the last recorded value from the database, while the database is not empty
                // and paste into textview
                //
                if (lessonData.size() != 0) {
                    String username = lessonData.get(0).name;
                    headerName.setText(username);
                    String string = String.valueOf(lessonData.get(0).myClass);
                    headerClass.setText(getString(R.string.child) + " " + string);

                }
            }
        });
    }

    //opening fragments from NavigationView
    @SuppressLint("NonConstantResourceId")
    private Fragment selectDrawerItem(MenuItem menu) {
        Fragment fragment;
        switch (menu.getItemId()) {
            case R.id.lecture:
                fragment = new LecturesFragList();
                break;
            case R.id.task:
                fragment = new TaskListFragment();
                break;
            case R.id.lab:
                fragment = new LabFragmentList();
                break;
            case R.id.progress:
                fragment = new MyProfileFragment();
                break;
            case R.id.settings:
                fragment = new SettingsFragment1();
                break;
            default:
                fragment = null;
        }
        menu.setChecked(true);
        drawerLayout.closeDrawers();
        return fragment;
    }

    private void exitProfile() {
        DialogFragment dialogFragment = ExitDialog.newInstance();
        dialogFragment.show(requireActivity().getSupportFragmentManager(), "exit!");
    }

    private void addToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((MainActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.menu);
        toolbar.setTitle(R.string.app_name);
        toolbar.setNavigationOnClickListener(v -> {
            createDrawer();
        });
        NavigationView navigationView = binding.navigationView;
        navigationView.setItemIconTintList(null);
    }

    private void createDrawer() {
        DrawerLayout drawerLayout = binding.drawerLayout;
        drawerLayout.openDrawer(GravityCompat.START);
    }


    //perform a fragment transaction on a specific-Lesson click
    @Override
    public void onLessonClick(int position) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                .replace(R.id.container, new LessonFragment(position))
                .addToBackStack(null)
                .commit();
        MainFlag.setNotLesson(false);
    }

    private void startEducFrag() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                .replace(R.id.container, new LessonEducFrag())
                .addToBackStack(null)
                .commit();
    }

    public static Bitmap BytesToBimap(byte[] b) {
        if (b.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }
}