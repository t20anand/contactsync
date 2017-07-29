package com.anand.contactsync;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by RiderRebrn on 7/29/2017.
 */

public class SQLiteDbHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLiteDbHelper => ";

    private static SQLiteDbHelper databaseAdapter = null;
    private static final String DATABASE_NAME = "contactsync.db";
    private static final int DATABASE_VER = 1;

    public static final String TABLE_NAME = "CONTACTS";
    public static final String COL_ID = "ID";
    public static final String COL_NAME = "NAME";
    public static final String COL_MOBILE_NO = "MOBILE_NO";
    public static final String COL_PHONE_NO = "PHONE_NO";
    public static final String COL_EMAIL = "EMAIL";
    public static final String COL_ADDRESS = "ADDRESS";


    private SQLiteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
    }

    public static SQLiteDbHelper getDbAdapter(Context context){
        if(databaseAdapter==null){
            databaseAdapter = new SQLiteDbHelper(context);
        }
        return databaseAdapter;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String tableScript = "CREATE TABLE "+TABLE_NAME+"("+
                COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT"+
                ", "+COL_NAME+" VARCHAR NOT NULL"+
                ", "+COL_MOBILE_NO+" VARCHAR NOT NULL"+
                ", "+COL_PHONE_NO+" VARCHAR NOT NULL"+
                ", "+COL_EMAIL+" VARCHAR NOT NULL"+
                ", "+COL_ADDRESS+" VARCHAR NOT NULL )";

        try {
            sqLiteDatabase.execSQL(tableScript);
        }catch (SQLException e){
            Log.d(TAG, e.getMessage());
        }catch (Exception e){
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
