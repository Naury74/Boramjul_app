package com.naury.boramjul;

import android.graphics.Bitmap;

public class PlaceListItem {

    private String place_id;
    private String place_name;
    private String place_address;
    private String Place_sub_address;


    public PlaceListItem(String place_id, String place_name, String place_address, String Place_sub_address) {
        this.place_id = place_id;
        this.place_name = place_name;
        this.place_address = place_address;
        this.Place_sub_address = Place_sub_address;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getPlace_address() {
        return place_address;
    }

    public void setPlace_address(String place_address) {
        this.place_address = place_address;
    }

    public String getPlace_sub_address() {
        return Place_sub_address;
    }

    public void setPlace_sub_address(String place_sub_address) {
        Place_sub_address = place_sub_address;
    }
}
