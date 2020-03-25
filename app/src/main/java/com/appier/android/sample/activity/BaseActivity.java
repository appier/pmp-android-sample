package com.appier.android.sample.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.appier.android.sample.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }
}
