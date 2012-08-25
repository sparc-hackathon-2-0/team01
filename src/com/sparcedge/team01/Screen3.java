package com.sparcedge.team01;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created with IntelliJ IDEA.
 * User: stevewoodruff
 * Date: 8/25/12
 * Time: 2:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class Screen3 extends Activity {
    public static int REQUEST_SCREEN5 = Tripppy.REQUEST_SCREEN5;
    ProgressDialog progressDialog = null;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen3);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
        if(btnSubmit != null) {
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    progressDialog = ProgressDialog.show(Screen3.this, "", "Getting Weather...", true);
                    // you can put spaces in city name but NOT after the comma.  spaces should be %20 / urlencoded
                    new WxWOEIDTask().execute("Folly%20Beach,SC");

                }
            });
        }
    }

    class WxWOEIDTask extends AsyncTask<String, Void, Boolean> {
        private Exception exception;

        protected Boolean doInBackground(String... args) {
            try {
                Tripppy.LOG("Get woeid for: " + args[0]);
                Tripppy.woeid = Tripppy.wx.getWOEID(args[0]);
                String hmm = Tripppy.wx.getForecast(Tripppy.woeid);
                Tripppy.LOG("Got forecast: " + hmm);
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
                Tripppy.LOG("An error occurred: " + exception.getMessage());
            }
            else if(result) {
                // we got it
                Tripppy.LOG("WOEID: " + Tripppy.woeid);
                Tripppy.LOG("Temp: " + Tripppy.wx.getTemp());
                Intent sndMsgIntent4 = new Intent(Tripppy.mContext, Screen5.class);
                startActivityForResult(sndMsgIntent4, REQUEST_SCREEN5);
            }
            else {

                Tripppy.LOG("Failed to fetch weather data: Try again.");
            }
        }
    }

}