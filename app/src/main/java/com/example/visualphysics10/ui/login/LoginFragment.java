package com.example.visualphysics10.ui.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.visualphysics10.MainActivity;
import com.example.visualphysics10.R;
import com.example.visualphysics10.databinding.FragmentLoginBinding;
import com.example.visualphysics10.ui.ContactDialog;
import com.example.visualphysics10.ui.MainFlag;
import com.example.visualphysics10.ui.lesson.ItemFragmentList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private String USER_KEY = "User";
    SharedPreferences enter;
    private final String FIRST_ENTER = "isFirstEnter";
    private boolean isFirstEnter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference(USER_KEY);
        enter = getContext().getSharedPreferences(FIRST_ENTER, Context.MODE_PRIVATE);
        addToolbar();
        if (enter.contains(FIRST_ENTER)) {
            isFirstEnter = enter.getBoolean(FIRST_ENTER, false);
        }
        binding.save.setOnClickListener(v -> {
            saveClick();
        });
        binding.registration.setOnClickListener(v -> {
            registrationClick();
        });
    }

    public void saveClick() {
        if (binding.inputEmail.getText().toString().isEmpty() || binding.inputPassword.getText().toString().isEmpty()) {
            Snackbar.make(binding.container, getString(R.string.hintSnack), Snackbar.LENGTH_LONG).show();
        } else {
            auth.signInWithEmailAndPassword(binding.inputEmail.getText().toString(), binding.inputPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                requireActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                                        .replace(R.id.container, new ItemFragmentList())
                                        .commit();
                                regEnd();
                            } else {
                                Snackbar.make(binding.container, getString(R.string.hintSnack2), Snackbar.LENGTH_LONG)
                                        .setAction(R.string.regSnack3, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                DialogFragment dialogFragment = ContactDialog.newInstance();
                                                dialogFragment.show(requireActivity().getSupportFragmentManager(), "please message me!");
                                            }
                                        }).show();
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

    public void registrationClick() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                .replace(R.id.container, new RegistrationFragment())
                .commit();
    }
    private void addToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((MainActivity) requireActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.login_title);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}