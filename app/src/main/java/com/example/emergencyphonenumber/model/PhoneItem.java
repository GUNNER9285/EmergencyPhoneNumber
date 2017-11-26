package com.example.emergencyphonenumber.model;

/**
 * Created by GUNNER on 26/11/2560.
 */

public class PhoneItem {
    public final int _id;
    public final String title;
    public final String number;
    public final String picture;

    public PhoneItem(int _id, String title, String number, String picture) {
        this._id = _id;
        this.title = title;
        this.number = number;
        this.picture = picture;
    }

    @Override
    public String toString() {
        return title;
    }
}
