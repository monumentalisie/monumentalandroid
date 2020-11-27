package com.hgosot.skopje;

import android.os.Bundle;

import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_content);
        ButterKnife.bind(this);

    }
}
