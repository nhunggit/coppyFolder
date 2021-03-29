package com.example.coppyfolder;

import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.appcompat.widget.Toolbar;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class inAppActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.in_app);
//
//        Log.d("nhungltk", "onCreate inAppActivity: ");
//
//        Toolbar topToolBar = (Toolbar) findViewById(R.id.tool_bar);
//        setSupportActionBar(topToolBar);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
