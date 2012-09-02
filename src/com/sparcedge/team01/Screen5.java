package com.sparcedge.team01;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: stevewoodruff
 * Date: 8/25/12
 * Time: 2:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class Screen5 extends ListActivity {
    private LayoutInflater mInflater;
    private Vector<String> data;
    public static int REQUEST_SCREEN2 = Tripppy.REQUEST_SCREEN2;
    public static Context mContext;
    public static Dialog dlg = null;

    String rd;
    static final String[] items =
            new String[] { "underwear", "dress clothes" , "DVDs", "water", "money" };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.screen5);
        mContext = this;
        refreshList();

        Button btnSaveTrip = (Button) findViewById(R.id.btnSaveTrip);
        btnSaveTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sndMsgIntent4 = new Intent(Tripppy.mContext, Screen2.class);
                startActivityForResult(sndMsgIntent4, REQUEST_SCREEN2);
            }
        });


    }

    public void refreshList() {

        mInflater = (LayoutInflater) getSystemService(
                Activity.LAYOUT_INFLATER_SERVICE);
        data = new Vector<String>();
        ArrayList<String> d = new ArrayList<String>();

        Tripppy.db.open();
        List<ItemInfo> itemInfos = Tripppy.db.getItem(Tripppy.current_trip_name);

        List<String> items = new ArrayList<String>();
        for (ItemInfo itemInfo: itemInfos){
            items.add(itemInfo.getItemName());
        }


        for (String s : items) {
            Tripppy.LOG("Adding item: " + s);
            data.add(s);
            d.add(s);
        }
        CustomAdapter adapter = new CustomAdapter(this, R.layout.device_screen_list,   d);
        setListAdapter(adapter);
        getListView().setTextFilterEnabled(true);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Tripppy.db.open();
        List<ItemInfo> itemInfos = Tripppy.db.getItem(Tripppy.current_trip_name);
        Tripppy.db.removeItemFromTrip(itemInfos.get(position));
        Tripppy.db.close();
        refreshList();
        Tripppy.LOG("Adding item: ");
    }


    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            Tripppy.LOG( "Touched item item");
        }
    };

    private class CustomAdapter extends ArrayAdapter<String> {

        public CustomAdapter(Context context, int resource,
                             List<String> objects) {

            super(context, resource, objects);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            TextView name = null;
            ImageView istat = null;
            String rowData= getItem(position);
            if(null == convertView){
                convertView = mInflater.inflate(R.layout.device_screen_list, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            name = holder.getName();
            name.setText(rowData);
            istat = holder.getIstat();
            istat.setImageResource(R.drawable.button_minus);
            return convertView;
        }
        private class ViewHolder {
            private View mRow;
            private TextView name = null;
            private ImageView istat = null;

            public ViewHolder(View row) {
                mRow = row;
            }

            public TextView getName() {
                if(null == name) {
                    name = (TextView) mRow.findViewById(R.id.name);
                }
                return name;
            }
            public ImageView getIstat() {
                if(null == istat){
                    istat = (ImageView) mRow.findViewById(R.id.istat);
                }
                return istat;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.menu, menu);
        return true;
    }

    /**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        dlg = new Dialog(mContext);
        dlg.setContentView(R.layout.dlg_item_name);
        dlg.setTitle("Enter Item Name");
        dlg.setCancelable(false);
        Button b1 = (Button) dlg.findViewById(R.id.btnSave);
        b1.setOnClickListener(listener);
        dlg.show();
        return true;

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        public void onClick(View v) {
            EditText e = (EditText)dlg.findViewById(R.id.etItemName);
            if(e != null && !e.getText().toString().trim().equals("")) {
                String name = e.getText().toString().trim();
                Tripppy.db.open();
                ItemInfo itemInfo = new ItemInfo(Tripppy.current_trip_name,name);
                Tripppy.db.addItemToTrip(itemInfo);
                Tripppy.db.close();
                dlg.dismiss();
            }
            refreshList();
        }
    };



}