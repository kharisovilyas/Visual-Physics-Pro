package com.example.visualphysics10.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visualphysics10.MainActivity;
import com.example.visualphysics10.R;
import com.example.visualphysics10.database.LessonData;
import com.example.visualphysics10.database.LessonViewModel;
import com.example.visualphysics10.databinding.FragmentItemListBinding;
import com.example.visualphysics10.ui.fragments_dl.SettingsFragment1;
import com.example.visualphysics10.ui.fragments_dl.TaskListFragment;
import com.example.visualphysics10.lessonsFragment.L1Fragment;
import com.example.visualphysics10.lessonsFragment.L2Fragment;
import com.example.visualphysics10.lessonsFragment.L3Fragment;
import com.example.visualphysics10.lessonsFragment.L4Fragment;
import com.example.visualphysics10.lessonsFragment.L5Fragment;
import com.example.visualphysics10.placeholder.PlaceholderContent;
import com.example.visualphysics10.ui.MainFlag;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Objects;

public class ItemFragment extends Fragment implements RecyclerViewAdapter.OnLessonListener {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    View header;
    private TextView headerName;
    private FragmentItemListBinding binding;
    private DrawerLayout drawerLayout;
    private NavigationView navigation;
    private LessonViewModel viewModel;
    SettingsFragment1 settingsFragment1 = new SettingsFragment1();
    SharedPreferences education;
    private String EDUCATION_PREFERENCES = "educationEnd";
    private boolean educationEnd;

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
        if(education.contains(EDUCATION_PREFERENCES)){
            educationEnd = education.getBoolean(EDUCATION_PREFERENCES, false);
        }
        if (!educationEnd) {
            startEducation();
        }
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
                        onLessonClick(0);
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
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                        .replace(R.id.container, selectDrawerItem(menu))
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .commit();
                MainFlag.setNotLesson(true);
                return true;
            }
        });
    }

    //get data from database
    private void editProfile() throws IndexOutOfBoundsException {
        header = navigation.getHeaderView(0);
        headerName = (TextView) header.findViewById(R.id.textView2);
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
                    String username = lessonData.get(lessonData.size() - 1).name;
                    headerName.setText(username);
                    Log.d("this flag is...", " " +lessonData.get(lessonData.size()-1).education);
                    settingsFragment1.setStr(username);
                }
            }
        });
    }

    //opening fragments from NavigationView
    @SuppressLint("NonConstantResourceId")
    private Fragment selectDrawerItem(MenuItem menu) {
        Fragment fragment;
        switch (menu.getItemId()) {
            case R.id.task:
                fragment = new TaskListFragment();
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
                .replace(R.id.container, Objects.requireNonNull(selectFragment(position)))
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }


    private Fragment selectFragment(int position) {
        switch (position) {
            case 0:
                return new L1Fragment();
            case 1:
                return new L2Fragment();
            case 2:
                return new L3Fragment();
            case 3:
                return new L4Fragment();
            case 4:
                return new L5Fragment();
            default:
                return new Fragment();
        }
    }

}