package com.sparcedge.team01;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Trippy extends Activity {

    public static int MSG_TYPE_HIDESPLASH = 2000;
    public static int REQUEST_SCREEN2 = 4000;
    public static String TAG = "Trippy";
    public static Boolean debug = false;
    public static Context mContext;
    Weather wx = null;
    ProgressDialog progressDialog = null;
    String woeid = "29466";


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mContext = this;
        wx = new Weather();

        // logic: if not logged in with facebook, show facebook button and wait until they click it
        // if already logged in - show screen for 1 second and then move to screen 2 (new trip)

        Boolean fb_logged_in = false;  // temp this goes away when we have the real deal
        if(fb_logged_in) {
            hideSplashSoon(2000L);
        }

        progressDialog = ProgressDialog.show(Trippy.this, "", "Getting Weather...", true);
        new WxWOEIDTask().execute("29466");
    }

    public static void LOG(String s) {
        LOG(true, s);
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

    void showOKAlertMsg(String title, String msg, final Boolean xfinish) {
        LOG(title + ": " + msg);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setNeutralButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(xfinish) { finish(); }
                    }
                });

        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(msg);
        try {
            dialogBuilder.show();
        } catch (Exception e) {
            LOG("OOPS: I can't show a showOKAlertMsg dialog: " + e.getMessage());
        }
    }

    class WxWOEIDTask extends AsyncTask<String, Void, Boolean> {
        private Exception exception;

        protected Boolean doInBackground(String... args) {
            try {
                woeid = wx.getWOEID(args[0]);
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return true;
        }

        protected void onPostExecute(Boolean result) {
            try {
                progressDialog.dismiss();
            } catch (Exception e) {}
            if(result == null) {
                LOG("An error occurred: " + exception.getMessage());
            }
            else if(result) {
                // we got it
                LOG("WOEID: " + woeid);
            }
            else {

                showOKAlertMsg("Whoops!", "Failed to fetch weather data: Try again.", false);
            }
        }
    }

}
