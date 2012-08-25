package com.sparcedge.team01;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created with IntelliJ IDEA.
 * User: stevewoodruff
 * Date: 8/25/12
 * Time: 11:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class Screen2 extends Activity {

    public static int REQUEST_SCREEN3 = Tripppy.REQUEST_SCREEN3;
    Context mContext = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = this;

        Button btnNewTrip = (Button) findViewById(R.id.btnNewTrip);
        Button btnOldTrip = (Button) findViewById(R.id.btnOldTrip);

        if(btnNewTrip != null) {
            btnNewTrip.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Tripppy.LOG("Creating dialog for trip name...");
                    final Dialog dlg = new Dialog(mContext);
                    dlg.setContentView(R.layout.dlg_trip_name);
                    dlg.setTitle("Enter Trip Name");
                    dlg.setCancelable(false);
                    Button b1 = (Button) dlg.findViewById(R.id.btnSave);
                    b1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText etName = (EditText) dlg.findViewById(R.id.etTripName);
                            if(etName != null) {
                                Tripppy.current_trip_name = etName.getText().toString().trim();
                                dlg.dismiss();
                                Intent sndMsgIntent4 = new Intent(Tripppy.mContext, Screen3.class);
                                startActivityForResult(sndMsgIntent4, REQUEST_SCREEN3);
                            }
                            else { // lol
                            }
                        }
                    });
                    dlg.show();
                }
            });
        }



    }
}