package com.sparcedge.team01;

import android.app.Activity;
import android.os.Bundle;

public class Splash extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // logic: if not logged in with facebook, show facebook button and wait until they click it
        // if already logged in - show screen for 1 second and then move to screen 2 (new trip)


    }
}
