package com.skin.minicraft.pe.skinmeloforminecraft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class OpenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intentMain = new Intent(OpenActivity.this,ShowButtomSheetMainActivity.class);
                startActivity(intentMain);
                OpenActivity.this.finishAfterTransition();
            }
        },2000);
    }
}