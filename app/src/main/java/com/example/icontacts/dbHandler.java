package com.example.icontacts;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class dbHandler extends SQLiteOpenHelper {

    public long sNo = 1;

    public dbHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE CONTACTS (sno INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,FIRSTNAME TEXT,LASTNAME TEXT,PHONE1 TEXT,PHONE2 TEXT , EMAIL TEXT,fav INTEGER)";

        db.execSQL(create);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public boolean addContact(Contact contact,Context context) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        String contact_firstName = contact.getFirstName();
        String contact_lastName = contact.getLastName();
        String contact_Phone1 = contact.getPhone1();
        String contact_Phone2 = contact.getPhone2();
        String contact_Email = contact.getEmail();


        if (contact_Phone1.length() == 0) {
            Toast.makeText(context, "Add valid Phone Number", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (contact_firstName != null) {
            if (contact_firstName.length() == 0) {
                contact.setFirstName(contact_Phone1);

            } else
                contact.setFirstName(contact_firstName);
        } else {
            contact.setFirstName(contact_Phone1);
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


        ArrayList<Contact> allContact=allContacts();

        for (Contact item : allContact) {

            if (item.getFirstName().toLowerCase().equals(contact.getFirstName().toLowerCase()))
            {
                Toast.makeText(context, "Same Name already exists", Toast.LENGTH_SHORT).show();
                return false;
            }

            if(contact.getPhone1().length()!=0)
            {
                if (item.getPhone1().equals(contact.getPhone1()) ||
                        item.getPhone2().equals(contact.getPhone1()))
                {
                    Toast.makeText(context, "Same Phone already exists", Toast.LENGTH_SHORT).show();
                    return false;
                }

            }
            if(contact.getPhone2().length()!=0)
            {
                if (item.getPhone2().equals(contact.getPhone2()) ||
                        item.getPhone1().equals(contact.getPhone2()))
                {
                    Toast.makeText(context, "Same Phone already exists", Toast.LENGTH_SHORT).show();
                    return false;
                }

            }

        }


        contentValues.put("firstName", contact.getFirstName());
        contentValues.put("lastName", contact.getLastName());
        contentValues.put("phone1", contact.getPhone1());
        contentValues.put("phone2", contact.getPhone2());
        contentValues.put("email", contact.getEmail());
        contentValues.put("fav", 0);

        long k = db.insert("Contacts", null, contentValues);

        db.close();
        Log.d("adding", "addContact: " + k);
        return true;
    }

    public ArrayList<Contact> allContacts() {
        ArrayList<Contact> contactsArr = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "Select *  from Contacts";
        Cursor cursor = db.rawQuery(query, null);
        int sno = 1;
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                Log.d("allContacts", "allContacts: " + cursor.getString(1));
                contact.setSno(Integer.parseInt(cursor.getString(0)));
                contact.setFirstName(cursor.getString(1));
                contact.setLastName(cursor.getString(2));
                contact.setPhone1(cursor.getString(3));
                contact.setPhone2(cursor.getString(4));
                contact.setEmail(cursor.getString(5));
                contact.setFav(cursor.getInt(6));
                contactsArr.add(contact);
            } while (cursor.moveToNext());
        }

        return contactsArr;
    }

    public void deleteContact(int sno) {
        SQLiteDatabase db = getWritableDatabase();
//        Contact c = accessContacts(sno +1);
//        Log.d("Delete", "deleteContact: " + c.getFirstName());
        Log.d("Delete", "deleteContact: " + String.valueOf(sno));
        db.delete("Contacts", "sno=?", new String[]{String.valueOf(sno)});

    }


    public void updateContact(Contact contact) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("firstName", contact.getFirstName());
        contentValues.put("lastName", contact.getLastName());
        contentValues.put("phone1", contact.getPhone1());
        contentValues.put("phone2", contact.getPhone2());
        contentValues.put("email", contact.getEmail());
        contentValues.put("fav", contact.isFav());
        Log.d("updating", "updateContact: " + contact.getFirstName() + " " + contact.isFav() + " " + contact.getSno());

        db.update("Contacts", contentValues, "sno=?", new String[]{String.valueOf(contact.getSno())});


    }


}
