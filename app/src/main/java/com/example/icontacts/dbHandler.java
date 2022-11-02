package com.example.icontacts;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class dbHandler extends SQLiteOpenHelper {

    public long sNo = 1;
    Context mainActivityContext;
    boolean cancelSaving = false;
    boolean addinExisting = false;
    boolean sameName = false;
    Dialog dialog, dialog1, dialog2;


    public dbHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mainActivityContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE CONTACTS (sno INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,FIRSTNAME TEXT,LASTNAME TEXT,PHONE1 TEXT,PHONE2 TEXT , EMAIL TEXT,fav INTEGER)";
        String create2 = "CREATE TABLE CONTACTS_GROUP (sno INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,GROUP_NAME TEXT,GROUP_MEMBERS TEXT)";

        db.execSQL(create);
        db.execSQL(create2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    @SuppressLint("SuspiciousIndentation")
    public boolean addContact(Contact contact, Context context, Bitmap photo) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        String contact_firstName = contact.getFirstName();
        String contact_lastName = contact.getLastName();
        String contact_Phone1 = contact.getPhone1();
        String contact_Phone2 = contact.getPhone2();
        String contact_Email = contact.getEmail();


        if (contact_Phone1.length() == 0 && contact_Phone2.length() == 0) {
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


        ArrayList<Contact> allContact = allContacts();

        for (Contact item : allContact) {


            if (contact.getPhone1().length() != 0) {
                if (item.getPhone1().equals(contact.getPhone1()) ||
                        item.getPhone2().equals(contact.getPhone1())) {

//                    Log.d("importingContacts", "addContact: "+item.getPhone1()+","+item.getPhone2()+","+contact.getPhone1());
                    if (context != mainActivityContext)
                        Toast.makeText(context, "Same Phone1 already exists", Toast.LENGTH_SHORT).show();
                    return false;
                }

            }
            if (contact.getPhone2().length() != 0) {
                if (item.getPhone2().equals(contact.getPhone2()) ||
                        item.getPhone1().equals(contact.getPhone2())) {
                    if (context != mainActivityContext)
                        Toast.makeText(context, "Same Phone2 already exists", Toast.LENGTH_SHORT).show();
                    return false;
                }

            }

            if ((item.getFirstName().toLowerCase() + item.getLastName().toLowerCase()).equals(contact.getFirstName().toLowerCase() + contact.getLastName().toLowerCase())) {
                sameName = true;

                dialog = new Dialog(context);
                dialog1 = new Dialog(context);
                dialog2 = new Dialog(context);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


                dialog.setCancelable(true);
                dialog1.setCancelable(true);
                dialog2.setCancelable(true);

                dialog.setContentView(R.layout.add_in_existing_contact_dailog_box);
                Button addInExistingButton = dialog.findViewById(R.id.addInExistingButton);
                Button cancelSavingButton = dialog.findViewById(R.id.cancelSavingButton);

                addInExistingButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        Log.d("updating", item.getPhone1() + " " + item.getPhone2());

                        dialog1.setContentView(R.layout.choose_replaceble_numbers);
                        Button saveButton = dialog1.findViewById(R.id.saveChanges);
                        Button p1 = dialog1.findViewById(R.id.replacablePhone);
                        Button p2 = dialog1.findViewById(R.id.replacablePhone1);
                        TextView textview = dialog1.findViewById(R.id.replacingPhone);


                        saveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Toast.makeText(context, "Phone added in existing contact", Toast.LENGTH_SHORT).show();
                                dialog1.dismiss();

                                if (contact_Phone2.length() != 0)
                                    callSecondDialogBox(dialog2, item, contact, context);

                            }
                        });


                        if (contact_Phone1.length() != 0) {

                            textview.setText("Replace Phone:" + contact_Phone1 + " with ");
                            p1.setText(item.getPhone1());
                            p2.setText(item.getPhone2());

                            p1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    item.setPhone1(contact_Phone1);
                                    updateContact(item);

                                    p1.setBackgroundColor(Color.argb(200, 122, 122, 122));
                                    p2.setBackgroundColor(Color.argb(255, 0, 0, 125));
                                }
                            });

                            p2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    item.setPhone2(contact_Phone1);
                                    updateContact(item);
                                    p1.setBackgroundColor(Color.argb(255, 0, 0, 125));
                                    p2.setBackgroundColor(Color.argb(200, 122, 122, 122));
                                }
                            });
                        } else {
                            textview.setVisibility(View.GONE);
                            p1.setVisibility(View.GONE);
                            p2.setVisibility(View.GONE);
                        }


//                        dialog.show();

                        if (contact_Phone1.length() != 0)
                            dialog1.show();
                        else if (contact_Phone2.length() != 0)
                            callSecondDialogBox(dialog2, item, contact, context);


                    }
                });

                cancelSavingButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();


                    }

                });


                dialog.show();
                return false;


            }


        }


        contentValues.put("firstName", contact.getFirstName());
        contentValues.put("lastName", contact.getLastName());
        contentValues.put("phone1", contact.getPhone1());
        contentValues.put("phone2", contact.getPhone2());
        contentValues.put("email", contact.getEmail());
        contentValues.put("fav", 0);

        long k = db.insert("Contacts", null, contentValues);


        if (photo != null) {
            saveToInternalStorage(photo, String.valueOf(k), context);
        }


        db.close();
        Log.d("adding", "addContact: " + k);


        return true;
    }

    private void callSecondDialogBox(Dialog dialog2, Contact item, Contact contact, Context context) {

        dialog2.setContentView(R.layout.choose_replaceble_numbers2);
        Button saveButton2 = dialog2.findViewById(R.id.saveChanges);
        Button p3 = dialog2.findViewById(R.id.replacablePhone);
        Button p4 = dialog2.findViewById(R.id.replacablePhone1);
        TextView textview2 = dialog2.findViewById(R.id.replacingPhone);

        saveButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "Phone added in existing contact", Toast.LENGTH_SHORT).show();
                dialog2.dismiss();
                Toast.makeText(context, "Phone added in existing contact", Toast.LENGTH_SHORT).show();
            }
        });


        if (contact.getPhone2().length() != 0) {

            textview2.setText("Replace Phone:" + contact.getPhone2() + " with ");
            p3.setText(item.getPhone1());
            p4.setText(item.getPhone2());

            p3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item.setPhone1(contact.getPhone2());
                    updateContact(item);
                    p4.setBackgroundColor(Color.argb(255, 0, 0, 125));
                    p3.setBackgroundColor(Color.argb(200, 122, 122, 122));
                }
            });

            p4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item.setPhone2(contact.getPhone2());
                    updateContact(item);
                    p3.setBackgroundColor(Color.argb(255, 0, 0, 125));
                    p4.setBackgroundColor(Color.argb(200, 122, 122, 122));
                }
            });
        } else {
            textview2.setVisibility(View.GONE);
            p3.setVisibility(View.GONE);
            p4.setVisibility(View.GONE);
        }

        dialog2.show();

    }

    private void createDailogBox(Context context) {

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.add_in_existing_contact_dailog_box);


        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        Button addInExistingButton = dialog.findViewById(R.id.addInExistingButton);
        Button cancelSavingButton = dialog.findViewById(R.id.cancelSavingButton);

        addInExistingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                addinExisting = true;
                Toast.makeText(context, "save", Toast.LENGTH_SHORT).show();
                Log.d("checking", "existing : checking if saving " + addinExisting);
                Log.d("checking", ": checking if cancel " + cancelSaving);

            }
        });

        cancelSavingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                cancelSaving = true;
                Toast.makeText(context, "CAncel", Toast.LENGTH_SHORT).show();
                Log.d("checking", "cancel : checking if saving " + addinExisting);
                Log.d("checking", ": checking if cancel " + cancelSaving);

            }

        });


        dialog.show();

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
                Log.d("fetching", "allContacts: " + cursor.getString(0));
            } while (cursor.moveToNext());
        }

        return contactsArr;
    }

    public Contact fetchContact(int Sno) {
        Contact contact = new Contact();
        SQLiteDatabase db = getReadableDatabase();
        String query = "Select * from Contacts where sno=" + Sno + ";";
        Cursor cursor = db.rawQuery(query, null);

        Log.d("fetchingContact", "fetchContact: " + query);

        if (cursor != null) {
            cursor.moveToFirst();

            contact.setSno(Integer.parseInt(cursor.getString(0)));
            contact.setFirstName(cursor.getString(1));
            contact.setLastName(cursor.getString(2));
            contact.setPhone1(cursor.getString(3));
            contact.setPhone2(cursor.getString(4));
            contact.setEmail(cursor.getString(5));
            contact.setFav(cursor.getInt(6));
        }
        else{
            return null;
        }



        return contact;
    }

    public ContactGroup fetchGroup(int Sno) {
        ContactGroup contactGroup = new ContactGroup();
        SQLiteDatabase db = getReadableDatabase();
        String query = "Select * from CONTACTS_GROUP where sno=" + Sno + ";";
        Cursor cursor = db.rawQuery(query, null);

        Log.d("ContactGroup", "ContactGroup: " + query);

        if (cursor != null) {
            cursor.moveToFirst();

            contactGroup.setSno(Integer.parseInt(cursor.getString(0)));
            contactGroup.setGroupName(cursor.getString(1));
            contactGroup.setGroupMembers(cursor.getString(2));
        }
        else{
            return null;
        }

        Log.d("fetchinggroup", "fetchGroup: "+contactGroup);
        return contactGroup;
    }

    public void deleteContact(int sno) {
        SQLiteDatabase db = getWritableDatabase();
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


    private String saveToInternalStorage(Bitmap bitmapImage, String filename, Context context) {
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, filename + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.d("address", directory.getAbsolutePath());
        return directory.getAbsolutePath();
    }


    public Boolean addGroup(ContactGroup contactGroup, Context context) {


        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put("Group_Name", contactGroup.getGroupName());
        contentValues.put("Group_Members", contactGroup.getGroupMembers());


        long k = db.insert("CONTACTS_GROUP", null, contentValues);
        db.close();
        Log.d("adding", "addContact: " + k);

        return true;
    }

    public void updateGroup(ContactGroup contactGroup) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("Group_Name", contactGroup.getGroupName());
        contentValues.put("Group_Members", contactGroup.getGroupMembers());


        db.update("CONTACTS_GROUP", contentValues, "sno=?", new String[]{String.valueOf(contactGroup.getSno())});


    }

    public void deleteGroup(int sno) {
        SQLiteDatabase db = getWritableDatabase();

        Log.d("Delete", "deleteContact: " + String.valueOf(sno));
        db.delete("CONTACTS_GROUP", "sno=?", new String[]{String.valueOf(sno)});
    }

    public ArrayList<ContactGroup> fetchGroups() {
        ArrayList<ContactGroup> groupsArr = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "Select *  from CONTACTS_GROUP";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor!=null && cursor.moveToFirst()) {
            do {
                ContactGroup contactGroup = new ContactGroup();
                Log.d("allContacts", "groupname: " + cursor.getString(1));
                contactGroup.setSno(Integer.parseInt(cursor.getString(0)));
                contactGroup.setGroupName(cursor.getString(1));
                contactGroup.setGroupMembers(cursor.getString(2));

                groupsArr.add(contactGroup);
                Log.d("fetching", "groups: " + cursor.getString(0));
            } while (cursor.moveToNext());
        }

        return groupsArr;
    }

    public void deleteFromGroup(int groupSno,int contactSno)
    {
        ContactGroup contactGroup=fetchGroup(groupSno);


//        String newMembers=contactGroup.getGroupMembers().replace(String.valueOf(contactSno),"");
        String[] members=contactGroup.getGroupMembers().split(",");

        String newMembers="";
        for(int i=0;i<members.length;i++)
        {
            if(members[i].equals(String.valueOf(contactSno)))
                continue;
            else
                newMembers+=members[i]+",";
        }

        contactGroup.setGroupMembers(newMembers);

        Log.d("fetchinggroup", "fetchGroup: "+contactGroup);
        updateGroup(contactGroup);

    }


    public void fetchDataFromAPI(Context context,String apiURL)
    {


        //        API Testing

//        [{"fname":"test6","lname":"","p1":"6","p2":"66","email":"test6@gmail.com"},{"fname":"test7","lname":"","p1":"7","p2":"77","email":"test7@gmail.com"}]


        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, apiURL, null, new Response.Listener<JSONArray>() {


            public void onResponse(JSONArray response) {
                try {


                    for (int i = 0; i < response.length(); i++) {
                        Contact contact = new Contact();

                        JSONObject obj = response.getJSONObject(i);
                        contact.setFirstName(obj.getString("fname"));
                        contact.setLastName(obj.getString("lname"));
                        contact.setPhone1(obj.getString("p1"));
                        contact.setPhone2(obj.getString("p2"));
                        contact.setEmail(obj.getString("email"));
//                        Log.d("fetching API", obj.getString("fname"));
//                        Log.d("fetching API", obj.getString("lname"));
//                        Log.d("fetching API", obj.getString("p1"));
//                        Log.d("fetching API", obj.getString("p2"));
//                        Log.d("fetching API", obj.getString("email"));


                        String src = obj.getString("imgSource");
                        String bitmap = null;
                        Log.d("fetching", "before: " + bitmap);
                        new GetImageFromUrl(contact).execute(src);
                        Log.d("fetching", "after: " + bitmap);
//                        handler.addContact(contact, MainActivity.this, bitmap);
                    }


//                    Log.d("fetching API", response.get(0).toString());
//                    Log.d("fetching API", response.get(1).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("fetching API", String.valueOf(error));
                    }
                });

        requestQueue.add(jsonArrayRequest);


//        ----------------------------


    }

    public class GetImageFromUrl extends AsyncTask<String, Void, Bitmap> {

        Contact contact = new Contact();

        public GetImageFromUrl(Contact contact) {
            this.contact = contact;
        }

        protected Bitmap doInBackground(String... url) {
            String stringUrl = url[0];
            Bitmap bitmap = null;
            InputStream inputStream;
            try {
                inputStream = new java.net.URL(stringUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("fetching", "doInBackground: " + bitmap);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmapPhoto) {
            super.onPostExecute(bitmapPhoto);
            Bitmap bitmap = bitmapPhoto;

            addContact(contact, mainActivityContext, bitmap);
            Log.d("fetchBitmap", "onPostExecute: " + bitmap);
        }
    }

}
