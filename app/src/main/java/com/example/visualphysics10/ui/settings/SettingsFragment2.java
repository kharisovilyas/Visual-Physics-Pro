package com.example.visualphysics10.ui.settings;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.visualphysics10.R;
import com.example.visualphysics10.ui.AboutUs;
import com.example.visualphysics10.ui.AuthorsInfoDialog;

import java.util.Objects;

public class SettingsFragment2 extends PreferenceFragmentCompat {
    Preference authorsList;
    Preference aboutUs;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        authorsList = findPreference("authors");
        aboutUs = findPreference("about");
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authorsList.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                onSelectedAuthorsList();
                return false;
            }
        });
        aboutUs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                onSelectedAboutUS();
                return false;
            }
        });
    }

    private void onSelectedAboutUS() {
        DialogFragment dialogFragment = AboutUs.newInstance();
        dialogFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "about!");
    }

    private void onSelectedAuthorsList() {
        DialogFragment dialogFragment = AuthorsInfoDialog.newInstance();
        dialogFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "about!");
    }
}