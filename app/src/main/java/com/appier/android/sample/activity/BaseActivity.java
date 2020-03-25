package com.appier.android.sample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.appier.android.sample.R;

public class BaseActivity extends Activity {
    public static final String EXTRA_SUB_TITLE = "SUB_TITLE";
    public static final String EXTRA_TITLE = "TITLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        TextView textTitle = findViewById(R.id.text_title);
        TextView textSubTitle = findViewById(R.id.text_sub_title);
        ImageButton buttonBack = findViewById(R.id.button_back);

        Bundle bundle = getIntent().getExtras();
        textTitle.setText(bundle.getString(EXTRA_TITLE));
        textSubTitle.setText(bundle.getString(EXTRA_SUB_TITLE));

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
