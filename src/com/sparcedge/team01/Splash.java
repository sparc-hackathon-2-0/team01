package com.sparcedge.team01;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Splash extends Activity {

    public static int MSG_TYPE_HIDESPLASH = 2000;
    public static int REQUEST_SCREEN2 = 4000;
    public static String TAG = "Trippy";
    public static Boolean debug = false;
    public static Context mContext;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mContext = this;

        // logic: if not logged in with facebook, show facebook button and wait until they click it
        // if already logged in - show screen for 1 second and then move to screen 2 (new trip)

        Boolean fb_logged_in = false;  // temp this goes away when we have the real deal
        if(fb_logged_in) {
            hideSplashSoon(2000L);
        }

    }

    public static void LOG(String s) {
        LOG(false, s);
    }

    public static void LOG(Boolean always, String s) {
        if(always) {
            Log.v(TAG, s);
        }
        else if (debug) {
            Log.d(TAG, s);
        }
    }

    public void hideSplashSoon(Long delayMillis) {
        android.os.Message timeout = Message.obtain();
        timeout.what = MSG_TYPE_HIDESPLASH;
        if(handler != null) handler.sendMessageDelayed(timeout, delayMillis);
    }

    private Handler handler = new Handler() {
        @Override
        public synchronized void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                if (msg.what == MSG_TYPE_HIDESPLASH) {
                    Intent sndMsgIntent4 = new Intent(mContext, Screen2.class);
                    startActivityForResult(sndMsgIntent4, REQUEST_SCREEN2);
                }
            }
        }
    };

}
