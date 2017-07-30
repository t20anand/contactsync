package com.anand.contactsync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity=> ";

    private ListView listViewContactList;
    private List<Contact> contactList = new ArrayList<>();
    private SQLiteDbHelper dbAdapter;
    private ArrayAdapter contactItemAdapter;
    private String[] colorArray = {"#FF4081","#207068","#feb217","#cd8500","#cd919e"};
    private Random random = new Random();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        this.dbAdapter = SQLiteDbHelper.getDbAdapter(this);
        //Starting synchronizing service
        startService(new Intent(this, SyncService.class));

        listViewContactList = (ListView) findViewById(R.id.listViewContactList);

        FloatingActionButton addNewContact = (FloatingActionButton) findViewById(R.id.fab);
        addNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addContactIntent = new Intent(context,NewContact.class);
                if(null != addContactIntent.resolveActivity(getPackageManager()))
                startActivity(new Intent(view.getContext(), NewContact.class));
            }
        });

        //getting data from local and populating list view
        this.getData();
        contactItemAdapter = new ArrayAdapter<Contact>(this,R.layout.contact_row,contactList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                if(null == convertView){
                    convertView = getLayoutInflater().inflate(R.layout.contact_row,parent,false);
                }
                TextView textViewName =  convertView.findViewById(R.id.textViewFullName);
                TextView textViewInitial =  convertView.findViewById(R.id.textViewInitial);


                Contact contact = contactList.get(position);
                String name = contact.getName().toString();
                textViewName.setText(name);
                GradientDrawable initialBackgroundShape = (GradientDrawable) textViewInitial.getBackground();
                String color = colorArray[random.nextInt(colorArray.length)];
                initialBackgroundShape.setColor(Color.parseColor(color));

                if(name.length()>0) {
                    textViewInitial.setText(name.substring(0,1).toUpperCase());
                }
                else{
                    textViewInitial.setText("");
                }
                return convertView;
            }
        };
        listViewContactList.setAdapter(contactItemAdapter);

    }

    @Override
    protected void onResume(){
        super.onResume();
        this.getData();
        contactItemAdapter.notifyDataSetChanged();
    }

    public void getData(){
        contactList.clear();
        Cursor cursor = this.dbAdapter.getWritableDatabase().rawQuery("SELECT * FROM "+SQLiteDbHelper.TABLE_NAME+" ORDER BY UPPER("+SQLiteDbHelper.COL_NAME+") ASC", null);
        if(0 < cursor.getCount()){
            while (cursor.moveToNext()){
                String id = cursor.getString(cursor.getColumnIndex(SQLiteDbHelper.COL_ID));
                String name = cursor.getString(cursor.getColumnIndex(SQLiteDbHelper.COL_NAME));
                String mobile = cursor.getString(cursor.getColumnIndex(SQLiteDbHelper.COL_MOBILE_NO));
                String phone = cursor.getString(cursor.getColumnIndex(SQLiteDbHelper.COL_PHONE_NO));
                String email = cursor.getString(cursor.getColumnIndex(SQLiteDbHelper.COL_EMAIL));
                String address = cursor.getString(cursor.getColumnIndex(SQLiteDbHelper.COL_ADDRESS));
                contactList.add(new Contact(id, name, mobile, phone, email, address));
            }
        }
        cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
