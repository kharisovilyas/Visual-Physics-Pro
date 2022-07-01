package com.example.visualphysics10.ui.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.visualphysics10.MainActivity;
import com.example.visualphysics10.R;
import com.example.visualphysics10.database.LessonData;
import com.example.visualphysics10.database.LessonViewModel;
import com.example.visualphysics10.databinding.FragmentRegistrationBinding;
import com.example.visualphysics10.ui.lesson.ItemFragmentList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Objects;

public class RegistrationFragment extends Fragment {
    private FragmentRegistrationBinding binding;
    private DatabaseReference mDatabase;
    private String USER_KEY = "User";
    private FirebaseAuth auth;
    SharedPreferences enter;
    private final String FIRST_ENTER = "isFirstEnter";
    private boolean isFirstEnter;
    private LessonViewModel viewModel;
    private LessonData lessonDataList = new LessonData();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference(USER_KEY);
        auth = FirebaseAuth.getInstance();
        enter = getContext().getSharedPreferences(FIRST_ENTER, Context.MODE_PRIVATE);
        addToolbar();
        if (enter.contains(FIRST_ENTER)) {
            isFirstEnter = enter.getBoolean(FIRST_ENTER, false);
        }
        binding.saveReg.setOnClickListener(v -> {
            saveClick();
        });
    }


    public void saveClick() {
        if (String.valueOf(binding.inputEmail.getText()).isEmpty() || String.valueOf(binding.inputPassword.getText()).isEmpty()
                || String.valueOf(binding.inputName.getText()).isEmpty() || String.valueOf(binding.inputEmailTeacher.getText()).isEmpty()) {
            Snackbar.make(binding.container, getString(R.string.hintSnack), Snackbar.LENGTH_LONG).show();
        } else {
            auth.createUserWithEmailAndPassword(String.valueOf(binding.inputEmail.getText()), String.valueOf(binding.inputPassword.getText()))
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                saveData();
                                requireActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                                        .replace(R.id.container, new ItemFragmentList())
                                        .commit();
                                regEnd();
                            } else {
                                Snackbar.make(binding.container, getString(R.string.hintSnack4), Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private void regEnd() {
        SharedPreferences.Editor editor = enter.edit();
        editor.putBoolean(FIRST_ENTER, true);
        editor.apply();
    }
    //save Data in Firebase and in RoomDatabase
    private void saveData() {
        String id = mDatabase.getKey();
        String name = String.valueOf(binding.inputName.getText());
        String email = String.valueOf(binding.inputEmail.getText());
        String emailTeacher = String.valueOf(binding.inputEmailTeacher.getText());
        String myClass = String.valueOf(binding.inputYouClass.getText());
        User newUser = new User(id, name, email, emailTeacher);
        //не работает
        mDatabase.push().setValue(newUser);
        lessonDataList.name = name;
        lessonDataList.emailTeacher = emailTeacher;
        lessonDataList.myClass = myClass;
        viewModel = ViewModelProviders.of(requireActivity()).get(LessonViewModel.class);
        viewModel.getLessonLiveData().observe(this, new Observer<List<LessonData>>() {
            @Override
            public void onChanged(List<LessonData> lessonData) {
                lessonData.add(lessonDataList);
            }
        });
        viewModel.insert(lessonDataList);
    }
    private void addToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((MainActivity)requireActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.reg_title);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}