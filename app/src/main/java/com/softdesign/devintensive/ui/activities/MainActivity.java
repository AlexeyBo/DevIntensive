package com.softdesign.devintensive.ui.activities;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.RoundedAvatarDrawable;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";

    private ImageView callImage;
    private CoordinatorLayout coordinatorLayout;
    private Toolbar toolbar;
    private DrawerLayout navigationDrawer;
    private FloatingActionButton fab;
    private EditText userPhone, userMail, userVk, userGit, userBio;
    private DataManager dataManager;

    private List<EditText> userInfoViews;
    private NavigationView navigationView;
    private boolean inEditMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataManager = DataManager.getInstance();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        navigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        callImage = (ImageView) findViewById(R.id.call);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        userPhone = (EditText) findViewById(R.id.phone_et);
        userMail = (EditText) findViewById(R.id.email_et);
        userVk = (EditText) findViewById(R.id.vk_et);
        userGit = (EditText) findViewById(R.id.github_et);
        userBio = (EditText) findViewById(R.id.bio_et);

        userInfoViews = new ArrayList<>();
        userInfoViews.add(userPhone);
        userInfoViews.add(userMail);
        userInfoViews.add(userVk);
        userInfoViews.add(userGit);
        userInfoViews.add(userBio);

        fab.setOnClickListener(this);
        callImage.setOnClickListener(this);

        setupToolbar();
        setupDrawer();
        setupAvatar();

        Log.d(TAG, "onCreate");

        if (savedInstanceState != null) {
            inEditMode = savedInstanceState.getBoolean(ConstantManager.EDIT_MODE_KEY, false);
            loadUserInfoValue();
        }

        changeEditMode(inEditMode);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            navigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveUserInfoValue();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.call:
                showProgress();
                break;
            case R.id.fab:
                showSnackBar("click");
                inEditMode = !inEditMode;
                changeEditMode(inEditMode);

                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ConstantManager.EDIT_MODE_KEY, inEditMode);
    }

    protected void showSnackBar(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawer() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                showSnackBar(item.getTitle().toString());
                item.setCheckable(true);
                navigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    private void setupAvatar() {
        BitmapDrawable bImage = (BitmapDrawable) getResources().getDrawable(R.drawable.user_avatar);
        RoundedAvatarDrawable roundedAvatar = new RoundedAvatarDrawable(bImage.getBitmap());

        ImageView avatarImage = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.avatar_image);
        avatarImage.setImageDrawable(roundedAvatar);
    }

    private void changeEditMode(boolean editMode) {
        if (editMode) {
            fab.setImageResource(R.drawable.ic_done_black_24dp);
            for (View view : userInfoViews) {
                view.setEnabled(true);
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
            }
        } else {
            fab.setImageResource(R.drawable.ic_create_black_24dp);
            for (View view : userInfoViews) {
                view.setEnabled(false);
                view.setFocusable(false);
                view.setFocusableInTouchMode(false);
            }
            saveUserInfoValue();
        }
    }

    private void loadUserInfoValue() {
        List<String> userData = dataManager.getPreferencesManager().loadUserProfileData();
        for (int i = 0; i < userData.size(); i++) {
            userInfoViews.get(i).setText(userData.get(i));
        }
    }

    private void saveUserInfoValue() {
        List<String> userData = new ArrayList<>();
        for (int i = 0; i < userData.size(); i++) {
            userData.add(userInfoViews.get(i).getText().toString());
        }
        dataManager.getPreferencesManager().saveUserProfileData(userData);
    }

    @Override
    public void onBackPressed() {
        if (navigationDrawer.isDrawerOpen(GravityCompat.START)) {
            navigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
