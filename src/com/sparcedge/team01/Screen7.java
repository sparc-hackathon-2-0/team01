package com.sparcedge.team01;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: stevewoodruff
 * Date: 8/25/12
 * Time: 2:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class Screen7 extends ListActivity {
    private LayoutInflater mInflater;
    private Vector<String> data;
    public static int REQUEST_SCREEN2 = Tripppy.REQUEST_SCREEN2;
    Menu menu = null;

    String rd;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.screen7);

        try {
            refreshList();
        }catch (Exception e) {}


    }



    public void refreshList() {

        mInflater = (LayoutInflater) getSystemService(
                Activity.LAYOUT_INFLATER_SERVICE);
        data = new Vector<String>();
        ArrayList<String> d = new ArrayList<String>();

        List<String> trips = getTrips();;

        for (String s : trips) {
            Tripppy.LOG("Adding trip: " + s);
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
        String info = ((TextView)((LinearLayout) v).getChildAt(1)).getText().toString().trim();
        Tripppy.LOG("trip name is: " + info);
//        List<ItemInfo> itemInfos = Tripppy.db.getItem(info);
//        ArrayList<ItemInfo> trip_stuff = Tripppy.db.getItem();
//        Tripppy.db.close();
        Tripppy.current_trip_name = info;
        Intent sndMsgIntent4 = new Intent(Tripppy.mContext, Screen5.class);
        startActivityForResult(sndMsgIntent4, Tripppy.REQUEST_SCREEN5);
    }



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
            istat.setImageResource(R.drawable.button_arrow);
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

    private List<String> getTrips(){
        Tripppy.db.open();
        List<ItemInfo> itemInfos = Tripppy.db.getItem();
        Tripppy.db.close();
        Set<String> set = new HashSet<String>();
        for(ItemInfo itemInfo : itemInfos){
            set.add(itemInfo.getItemName());
        }
        List<String> results = new ArrayList<String>();
        results.addAll(set);
        return results;
    }
}