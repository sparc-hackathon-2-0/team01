package com.sparcedge.team01;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created with IntelliJ IDEA.
 * User: stevewoodruff
 * Date: 8/25/12
 * Time: 11:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class Screen2 extends Activity {

    public static int REQUEST_SCREEN3 = Tripppy.REQUEST_SCREEN3;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Button btnNewTrip = (Button) findViewById(R.id.btnNewTrip);
        Button btnOldTrip = (Button) findViewById(R.id.btnOldTrip);

        if(btnNewTrip != null) {
            btnNewTrip.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent sndMsgIntent4 = new Intent(Tripppy.mContext, Screen3.class);
                    startActivityForResult(sndMsgIntent4, REQUEST_SCREEN3);
                }
            });
        }



    }
}