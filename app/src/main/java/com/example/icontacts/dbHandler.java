package com.example.icontacts;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class dbHandler extends SQLiteOpenHelper {


    public dbHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE CONTACTS (sno INTEGER PRIMARY KEY,FIRSTNAME TEXT,LASTNAME TEXT,PHONE1 TEXT,PHONE2 TEXT , EMAIL TEXT)";

        db.execSQL(create);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }




    public void addContact(Contact contact) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        String contact_firstName = contact.getFirstName();
        String contact_lastName = contact.getLastName();
        String contact_Phone1 = contact.getPhone1();
        String contact_Phone2 = contact.getPhone2();
        String contact_Email = contact.getEmail();


        if (contact_firstName != null) {
            contact.setFirstName(contact_firstName);
        } else {
            contact.setFirstName("");
        }

        if (contact_lastName != null) {
            contact.setLastName(contact_lastName);
        } else {
            contact.setLastName("");
        }

        if (contact_Phone1 != null) {
            contact.setPhone1(contact_Phone1);
        } else {
            contact.setPhone1("");
        }

        if (contact_Phone2 != null) {
            contact.setPhone2(contact_Phone2);
        } else {
            contact.setPhone2("");
        }

        if (contact_Email != null) {
            contact.setEmail(contact_Email);
        } else {
            contact.setEmail("");
        }


        contentValues.put("firstName", contact.getFirstName());
        contentValues.put("lastName", contact.getLastName());
        contentValues.put("phone1", contact.getPhone1());
        contentValues.put("phone2", contact.getPhone2());
        contentValues.put("email", contact.getEmail());

        long k = db.insert("Contacts", null, contentValues);

        Log.d("adding", "addContact: " + k);
    }

    public Contact accessContacts(int sno) {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query("Contacts", new String[]{"sno", "firstName", "lastName", "phone1", "phone2", "email"}, "sno=?", new String[]{String.valueOf(sno)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {

            Contact contact=new Contact(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));
            return contact;
//            Log.d("mytag", String.valueOf(sno));
//            Log.d("mytag", cursor.getString(1));
//            Log.d("mytag", cursor.getString(2));
//            Log.d("mytag", cursor.getString(3));
//            Log.d("mytag", cursor.getString(4));
//            Log.d("mytag", cursor.getString(5));
//            Log.d("mytag", String.valueOf(sno) + "---");

        } else {
            Log.d("mytag", "some error ");

        }

        return null;

    }

}
