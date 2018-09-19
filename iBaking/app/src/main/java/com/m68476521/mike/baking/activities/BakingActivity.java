package com.m68476521.mike.baking.activities;

import android.support.v4.app.Fragment;

import com.m68476521.mike.baking.fragments.BakingFragment;
import com.m68476521.mike.baking.fragments.SingleFragmentActivity;

//This is the main activity that has a responsibility to launch the app
public class BakingActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new BakingFragment();
    }
}