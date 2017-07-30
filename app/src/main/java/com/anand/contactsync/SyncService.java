package com.anand.contactsync;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by RiderRebrn on 7/30/2017.
 */

public class SyncService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NetworkHelper networkHelper = new NetworkHelper(getApplicationContext());
                    //checking for internet connection
                    if (networkHelper.isNetworkAvailable()) {
                        SQLiteDbHelper dbAdapter = SQLiteDbHelper.getDbAdapter(getApplicationContext());

                        //getting rows which are not synced to firebase
                        Cursor cursor = dbAdapter.getWritableDatabase().rawQuery("SELECT * FROM " + SQLiteDbHelper.TABLE_NAME + " WHERE " + SQLiteDbHelper.COL_SYNC_STATUS + "='N'", null);
                        if (cursor.getCount() > 0) {
                            DatabaseReference firebaseDbRef = FirebaseDatabase.getInstance().getReference();
                            ContentValues values = new ContentValues();
                            while (cursor.moveToNext()) {
                                String id = cursor.getString(cursor.getColumnIndex(SQLiteDbHelper.COL_ID));
                                String name = cursor.getString(cursor.getColumnIndex(SQLiteDbHelper.COL_NAME));
                                String mobile = cursor.getString(cursor.getColumnIndex(SQLiteDbHelper.COL_MOBILE_NO));
                                String phone = cursor.getString(cursor.getColumnIndex(SQLiteDbHelper.COL_PHONE_NO));
                                String email = cursor.getString(cursor.getColumnIndex(SQLiteDbHelper.COL_EMAIL));
                                String address = cursor.getString(cursor.getColumnIndex(SQLiteDbHelper.COL_ADDRESS));

                                firebaseDbRef.child("contact").child(id).setValue(new Contact(id, name, mobile, phone, email, address));
                                values.clear();
                                values.put(SQLiteDbHelper.COL_SYNC_STATUS, "Y");
                                dbAdapter.getWritableDatabase().update(SQLiteDbHelper.TABLE_NAME, values, "ID = ?", new String[]{id});
                            }
                        }
                    }
                }
            }).start();
        return START_STICKY;
    }
}
