package com.br.ufc.bluetooth_android_arduino;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handle = new Handler();

        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                showSplash();
            }
        }, 3000);

    }

    private void showSplash() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);

        startActivity(intent);

        finish();
    }
}
