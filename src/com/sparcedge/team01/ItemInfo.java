package com.sparcedge.team01;

public class ItemInfo {

    String item_name = null;
    String trip_name = null;

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setTrip_name(String trip_name) {
        this.trip_name = trip_name;
    }

    ItemInfo() {
    }

    ItemInfo(String trip_name,String item_name) {
        this.trip_name = trip_name;
        this.item_name = item_name;
    }

    public String getTripName() { return trip_name; }
    public String getItemName() { return item_name; }

}
