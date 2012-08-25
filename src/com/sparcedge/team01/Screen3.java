package com.sparcedge.team01;

import android.app.Activity;
import android.content.Intent;
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen3);

        Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
        if(btnSubmit != null) {
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent sndMsgIntent4 = new Intent(Tripppy.mContext, Screen5.class);
                    startActivityForResult(sndMsgIntent4, REQUEST_SCREEN5);
                }
            });
        }
    }
}