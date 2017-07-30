package com.anand.contactsync;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by RiderRebrn on 7/29/2017.
 */

public class Contact implements Parcelable {

    private String id,name, mobileNo, phoneNo, email, address;

    public Contact(String id,String name, String mobileNo, String phoneNo, String email, String address) {
        this.id = id;
        this.name = name;
        this.mobileNo = mobileNo;
        this.phoneNo = phoneNo;
        this.email = email;
        this.address = address;
    }

    protected Contact(Parcel in) {
        id = in.readString();
        name = in.readString();
        mobileNo = in.readString();
        phoneNo = in.readString();
        email = in.readString();
        address = in.readString();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public String getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }

    public String getMobileNo() {
        return this.mobileNo;
    }

    public String getPhoneNo() {
        return this.phoneNo;
    }

    public String getEmail() {
        return this.email;
    }

    public String getAddress() {
        return this.address;
    }

    @Override
    public String toString(){
        return "Contact{"+
                "Id="+this.id+
                "Name=" + this.name+
                ", Mobile=" + this.mobileNo +
                ", Phone=" + this.phoneNo +
                ", Email=" + this.phoneNo +
                ", Address=" + this.address +
                "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(mobileNo);
        parcel.writeString(phoneNo);
        parcel.writeString(email);
        parcel.writeString(address);
    }
}
