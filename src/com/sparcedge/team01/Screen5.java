package com.sparcedge.team01;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

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
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    ListView newDevicesListView = null;
    private LayoutInflater mInflater;
    private Vector<String> data;
    String rd;
    static final String[] items =
            new String[] { "underwear", "dress clothes" , "DVDs", "water", "money" };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.screen5);
        refreshList();

    }

    public void refreshList() {

        mInflater = (LayoutInflater) getSystemService(
                Activity.LAYOUT_INFLATER_SERVICE);
        data = new Vector<String>();
        ArrayList<String> d = new ArrayList<String>();


        for (String s : items) {
            Tripppy.LOG("Adding item: " + s);
            data.add(s);
            d.add(s);
        }
        CustomAdapter adapter = new CustomAdapter(this, R.layout.device_screen_list,   d);
        setListAdapter(adapter);
        getListView().setTextFilterEnabled(true);

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
            istat.setImageResource(R.drawable.trash);
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

}