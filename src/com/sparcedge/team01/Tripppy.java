package com.sparcedge.team01;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public class Tripppy extends Activity {

    public static int MSG_TYPE_HIDESPLASH = 2000;
    public static int REQUEST_SCREEN2 = 4000;
    public static int REQUEST_SCREEN3 = 4001;
    public static int REQUEST_SCREEN5 = 4002;
    public static int REQUEST_SCREEN7 = 4003;

    public static String TAG = "Tripppy";
    public static Boolean debug = false;
    public static Context mContext;
    public static Facebook facebook = new Facebook("147306792075631");
    public static Activity currentActivity = new Activity();
    public static DBAdapter db = null;
    public static Weather wx = null;
    public static String current_trip_name = "default";
    public static String woeid = "29466";
    static String GSPREFS = "GS-prefs";
    static public Boolean first_time = true;
    static public String[] any = {"shorts","pants","ID","hat","sweater","rain coat","phone","chargers","shoes","flip flops","laptop",
            "water","dress clothes","underwear","batteries","fanny pack","sunglasses","watch","socks","beer","liquor"};
    static public String[] beach = {"swimsuit","beach chair","aloe vera gel","beach ball","beach umbrella","beach towels","cooler",
            "ice","fishing rod","beach games"};
    static public String[] cold = {"jacket","thermal underwear","snow pants","toboggan","skiis","gloves","snow shoes","boots","goggles",
            "earmuffs","lip balm"};


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = this;
        currentActivity = this;
        wx = new Weather();
        db = new DBAdapter(this);

        Button confirmButton = (Button)findViewById(R.id.logIn);
        confirmButton.setOnClickListener(loginListener);


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebook.authorizeCallback(requestCode, resultCode, data);
    }


    private View.OnClickListener loginListener = new View.OnClickListener() {
        public void onClick(View v) {
            /*
            facebook.authorize(currentActivity,new String[] {"publish_stream"},new Facebook.DialogListener() {
                @Override
                public void onComplete(Bundle values) {
                    loadPreferences();
                    if(first_time){
                        updateStatus(values.getString(Facebook.TOKEN));
                    }
                    Intent sndMsgIntent4 = new Intent(mContext, Screen2.class);
                    startActivityForResult(sndMsgIntent4, REQUEST_SCREEN2);
                    first_time = false;
                    savePreferences();
                }

                @Override
                public void onFacebookError(FacebookError e) {
                    //To change body of implemented methods use File | Settings | File Templates.
                    LOG("WTF 1: " + e.getMessage());

                }

                @Override
                public void onError(DialogError e) {
                    //To change body of implemented methods use File | Settings | File Templates.
                    LOG("WTF 2: " + e.getMessage());
                }

                @Override
                public void onCancel() {
                    //To change body of implemented methods use File | Settings | File Templates.
                }
            });  */
            Intent sndMsgIntent4 = new Intent(mContext, Screen2.class);
            startActivityForResult(sndMsgIntent4, REQUEST_SCREEN2);

        }
    };

    //updating Status
    public void updateStatus(String accessToken){
        Bundle bundle = new Bundle();
        bundle.putString("message", "is now gettin TRIPPPY!!!");
        bundle.putString(Facebook.TOKEN,accessToken);
        AsyncFacebookRunner mAsyncFbRunner = new AsyncFacebookRunner(facebook);
        mAsyncFbRunner.request("me/feed",bundle,"POST", new PostRequestListener(), null);
    }

    private final class PostRequestListener implements AsyncFacebookRunner.RequestListener {

        @Override
        public void onComplete(String response, Object state) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onIOException(IOException e, Object state) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onFileNotFoundException(FileNotFoundException e, Object state) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onMalformedURLException(MalformedURLException e, Object state) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onFacebookError(FacebookError e, Object state) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

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


    public void savePreferences() {
        SharedPreferences mySharedPrefs = getSharedPreferences(GSPREFS,
                Activity.MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = mySharedPrefs.edit();
        editor.putBoolean("first_time", first_time);
        editor.commit();
    }

    public void loadPreferences() {
        SharedPreferences mySharedPrefs = getSharedPreferences(GSPREFS,
                Activity.MODE_WORLD_WRITEABLE);
        first_time = mySharedPrefs.getBoolean("first_time", true);
    }


}
