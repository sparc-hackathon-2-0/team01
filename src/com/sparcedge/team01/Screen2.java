package com.sparcedge.team01;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;

import java.util.ArrayList;
import java.util.List;

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

        btnOldTrip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent sndMsgIntent4 = new Intent(mContext, Screen7.class);
                startActivityForResult(sndMsgIntent4, Tripppy.REQUEST_SCREEN7);
            }
        });

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
                                Tripppy.db.open();
                                Tripppy.db.addTrip(Tripppy.current_trip_name);
                                List<ItemInfo> itemInfos = buildAnyItems(Tripppy.current_trip_name);
                                addItemsToDb(itemInfos);
                                Tripppy.db.close();

                                addItemsToDb(itemInfos);
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

    private List<ItemInfo> buildAnyItems(String trip){
        List<ItemInfo> itemInfos = new ArrayList<ItemInfo>();
        for (String item : Tripppy.any){
            ItemInfo itemInfo = new ItemInfo();
            itemInfo.setItem_name(item);
            itemInfo.setTrip_name(trip);
            itemInfos.add(itemInfo);
        }
        return itemInfos;
    }

    private void addItemsToDb(List<ItemInfo> itemInfos){
        for(ItemInfo itemInfo: itemInfos){
            Tripppy.db.open();
            Tripppy.db.addItemToTrip(itemInfo);
        }
    }

}