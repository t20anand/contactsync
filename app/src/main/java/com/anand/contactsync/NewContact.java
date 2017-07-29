package com.anand.contactsync;

import android.content.ContentValues;
import android.database.SQLException;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class NewContact extends AppCompatActivity {

    private static final String TAG = "NewContact => ";

    private SQLiteDbHelper dbAdapter;
    private EditText editTextName, editTextMobileNo, editTextPhoneNo, editTextEmail, editTextAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        //getting database instance
        dbAdapter = SQLiteDbHelper.getDbAdapter(this);
        findViews();
    }

    private void findViews(){
        editTextName = (EditText) findViewById(R.id.editTextPersonName);
        editTextMobileNo = (EditText) findViewById(R.id.editTextMobileNo);
        editTextPhoneNo = (EditText) findViewById(R.id.editTextPhoneNo);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
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
