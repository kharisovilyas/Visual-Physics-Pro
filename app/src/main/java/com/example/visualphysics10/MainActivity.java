package com.example.visualphysics10;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.visualphysics10.ui.login.LoginFragment;
import com.example.visualphysics10.ui.lesson.ItemFragmentList;
import com.example.visualphysics10.databinding.ActivityMainBinding;
import com.example.visualphysics10.ui.MainFlag;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    ItemFragmentList itemFragmentList = new ItemFragmentList();
    LoginFragment loginFragment = new LoginFragment();
    private ActivityMainBinding binding;
    private int count;
    SharedPreferences enter;
    private final String FIRST_ENTER = "isFirstEnter";
    private boolean isFirstEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        enter = getSharedPreferences(FIRST_ENTER, Context.MODE_PRIVATE);
        if (enter.contains(FIRST_ENTER)) {
            isFirstEnter = enter.getBoolean(FIRST_ENTER, false);
        }
        if (!isFirstEnter) {
            MainFlag.setEndReg(true);
            startLogin();
        }else{
            startApp();
        }
    }

    private void startApp() {
        if (itemFragmentList != null) {
            fragmentTransaction.add(R.id.container, itemFragmentList).commit();
        }
    }

    private void startLogin() {
        if (loginFragment != null) {
            fragmentTransaction.add(R.id.container, loginFragment).commit();
        }
    }

    //redefine the method...
    @Override
    public void onBackPressed() {

        count = fragmentManager.getBackStackEntryCount();

        if (count == 0 || MainFlag.isEndReg()) {
            exitApp();
        } else {
            exitFragment();
        }

    }

    private void exitFragment() {
        if (count > 1 || MainFlag.isNotLesson()) fragmentManager.popBackStack();
        else new MaterialAlertDialogBuilder(this,R.style.MaterialAlert)
                .setTitle(R.string.title_1)
                .setMessage(R.string.message_1)
                .setCancelable(false)
                .setPositiveButton("Завершить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        fragmentManager.popBackStack();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Отмена", null)
                .show().getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
    }

    private void exitApp() {
        new MaterialAlertDialogBuilder(this,R.style.MaterialAlert)
                .setTitle(R.string.title_1)
                .setMessage(R.string.message_2)
                .setCancelable(true)
                .setPositiveButton("Выйти", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("Назад", null)
                .show().getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}