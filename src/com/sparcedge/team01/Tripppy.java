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
import android.view.View;
import android.widget.Button;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

public class Tripppy extends Activity {

    public static int MSG_TYPE_HIDESPLASH = 2000;
    public static int REQUEST_SCREEN2 = 4000;
    public static int REQUEST_SCREEN3 = 4001;
    public static String TAG = "Tripppy";
    public static Boolean debug = false;
    public static Context mContext;
    public static Facebook facebook = new Facebook("147306792075631");
    public static Activity currentActivity = new Activity();
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
        currentActivity = this;
        wx = new Weather();

        Button confirmButton = (Button)findViewById(R.id.logIn);
        confirmButton.setOnClickListener(loginListener);

        progressDialog = ProgressDialog.show(Tripppy.this, "", "Getting Weather...", true);
        // you can put spaces in city name but NOT after the comma.  spaces should be %20 / urlencoded
        new WxWOEIDTask().execute("Mt%20Pleasant,SC");
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
            facebook.authorize(currentActivity,new String[] {"publish_stream"},new Facebook.DialogListener() {
                @Override
                public void onComplete(Bundle values) {
                    updateStatus(values.getString(Facebook.TOKEN));
                    Intent sndMsgIntent4 = new Intent(mContext, Screen2.class);
                    startActivityForResult(sndMsgIntent4, REQUEST_SCREEN2);
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
            });

        }
    };

    //updating Status
    public void updateStatus(String accessToken){
        Bundle bundle = new Bundle();
        bundle.putString("message", "is now Tripping!!!");
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

    class WxWOEIDTask extends AsyncTask<String, Void, Boolean> {
        private Exception exception;

        protected Boolean doInBackground(String... args) {
            try {
                LOG("Get woeid for: " + args[0]);
                woeid = wx.getWOEID(args[0]);
                String hmm = wx.getForecast(woeid);
                LOG("Got forecast: " + hmm);
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
