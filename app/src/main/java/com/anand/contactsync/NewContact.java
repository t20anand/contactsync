package com.anand.contactsync;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class NewContact extends AppCompatActivity {

    private static final String TAG = "NewContact => ";

    private static final int CAMERA_PHOTO = 111;

    private SQLiteDbHelper dbAdapter;
    private EditText editTextName, editTextMobileNo, editTextPhoneNo, editTextEmail, editTextAddress;
    private ConstraintLayout tempImageCont;
    private ImageView imageViewContact;
    private Uri imageToUploadUri;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
        context = this;

        //getting database instance
        dbAdapter = SQLiteDbHelper.getDbAdapter(this);
        findViews();

        tempImageCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent,100);
            }
        });
    }

    private void findViews(){
        editTextName = (EditText) findViewById(R.id.editTextPersonName);
        editTextMobileNo = (EditText) findViewById(R.id.editTextMobileNo);
        editTextPhoneNo = (EditText) findViewById(R.id.editTextPhoneNo);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        tempImageCont = (ConstraintLayout) findViewById(R.id.tempImageCont);
        imageViewContact = (ImageView) findViewById(R.id.imageViewContact);
    }

    private void selectImage(){
        final String[] options = {"Take Photo", "Choose from Gallery","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                if(options[item].equals("Take Photo")){

                    PackageManager pm = context.getPackageManager();
                    if(pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)){
                        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if(null != takePhotoIntent.resolveActivity(getPackageManager()))
                        startActivityForResult(takePhotoIntent,CAMERA_PHOTO);
                    }
                    else{
                        Toast.makeText(context,"No Camera Available",Toast.LENGTH_SHORT).show();
                    }


                }else if(options[item].equals("Choose from Gallery")){
                    Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                }else if(options[item].equals("Cancel")){
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==CAMERA_PHOTO){
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageViewContact.setImageBitmap(photo);
                tempImageCont.setVisibility(View.GONE);
                imageViewContact.setVisibility(View.VISIBLE);
            }
        }
        /*if(requestCode==100&&resultCode== Activity.RESULT_OK){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageViewContact.setImageBitmap(photo);
            tempImageCont.setVisibility(View.GONE);
            imageViewContact.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_save,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id==R.id.action_save){
            Boolean result = saveContact();
            if(result) {
                Toast.makeText(this, "Contact Saved Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        return true;
    }

    private boolean saveContact(){
        boolean isValid = true;

        String name = editTextName.getText().toString();
        String mobileNo = editTextMobileNo.getText().toString();
        String phoneNo = editTextPhoneNo.getText().toString();
        String email = editTextEmail.getText().toString();
        String address = editTextAddress.getText().toString();

        if(TextUtils.isEmpty(name)){
            editTextName.setError(getString(R.string.error_field_required));
            isValid = false;
        } else{
            editTextName.setError(null);
        }

        if(TextUtils.isEmpty(mobileNo)){
            editTextMobileNo.setError(getString(R.string.error_field_required));
            isValid = false;
        }else if(!Patterns.PHONE.matcher(mobileNo).matches()) {
            editTextMobileNo.setError(getString(R.string.error_field_invalidMobileNo));
            editTextMobileNo.requestFocus();
            isValid = false;
        }else {
            editTextMobileNo.setError(null);
        }

        if(TextUtils.isEmpty(phoneNo)){
            editTextPhoneNo.setError(getString(R.string.error_field_required));
            isValid = false;
        }else if(!Patterns.PHONE.matcher(phoneNo).matches()){
            editTextPhoneNo.setError(getString(R.string.error_field_invalidPhoneNo));
            editTextPhoneNo.requestFocus();
            isValid = false;
        } else {
            editTextPhoneNo.setError(null);
        }

        if (TextUtils.isEmpty(email)){
            editTextEmail.setError(getString(R.string.error_field_required));
            isValid = false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError(getString(R.string.error_field_invalidEMail));
            editTextEmail.requestFocus();
            isValid = false;
        }else {
            editTextEmail.setError(null);
        }

        if(TextUtils.isEmpty(address)){
            editTextAddress.setError(getString(R.string.error_field_required));
            isValid = false;
        } else {
            editTextAddress.setError(null);
        }

        try{
            if(isValid) {
                Cursor cursor = this.dbAdapter.getWritableDatabase().rawQuery("SELECT seq FROM sqlite_sequence WHERE name='CONTACTS'",null);
                ContentValues values = new ContentValues();
                values.put(SQLiteDbHelper.COL_NAME, name);
                values.put(SQLiteDbHelper.COL_MOBILE_NO, mobileNo);
                values.put(SQLiteDbHelper.COL_PHONE_NO, phoneNo);
                values.put(SQLiteDbHelper.COL_EMAIL, email);
                values.put(SQLiteDbHelper.COL_ADDRESS, address);

                //opening the database and inserting row
                long result = this.dbAdapter.getWritableDatabase().insert(SQLiteDbHelper.TABLE_NAME,null,values);

                if(-1 == result){
                    throw new Exception("Unable to insert data");
                }


                NetworkHelper networkHelper = new NetworkHelper(this);
                //checking for internet connection
                if (networkHelper.isNetworkAvailable()){
                    String id = String.valueOf(result);
                    Contact contact = new Contact(id, name, mobileNo, phoneNo, email, address);

                    //pushing data to firebase
                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
                    databaseRef.child("contact").child(contact.getId()).setValue(contact);

                    //updating row locally after data pushed to firebase
                    values.clear();
                    values.put(SQLiteDbHelper.COL_SYNC_STATUS, "Y");
                    this.dbAdapter.getWritableDatabase().update(SQLiteDbHelper.TABLE_NAME, values, "ID = ?", new String[]{id});
                }


                return true;
            }
        }catch (SQLException e){
            Log.d(TAG, e.getMessage());
        } catch (Exception e) {
            Log.d(TAG,e.getMessage());
        }finally {
            this.dbAdapter.close();
        }

        return false;
    }
}
