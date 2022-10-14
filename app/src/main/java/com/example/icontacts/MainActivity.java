package com.example.icontacts;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.security.Permission;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SearchView searchView;
    ImageView addContact;

    ArrayList<Contact> contactsArr = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        addContact = findViewById(R.id.addContact);


        Dexter.withContext(this)
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                    }
                })
                .check();






        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, addContactPage.class);
                startActivity(intent);
            }
        });

        dbHandler handler = new dbHandler(MainActivity.this, "Contacts", null, 1);
//        handler.addContact(new Contact("jasshu", "8195850098"));
//        handler.addContact(new Contact("shivam", "8195850098"));
//        handler.addContact(new Contact("abhay", "8195850098"));
//        handler.addContact(new Contact("raghav", "8195850098"));
//        handler.addContact(new Contact("tanishq", "8195850098"));


        contactsArr = handler.allContacts();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            contactsArr.sort(Contact.contactsComparator);
        }


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String filter) {
                ArrayList<Contact> filteredlist = new ArrayList<Contact>();

                // running a for loop to compare elements.
                for (Contact item : contactsArr) {
                    if (item.getFirstName().toLowerCase().contains(filter.toLowerCase())) {
                        filteredlist.add(item);
                    }
                }
                CustomAdapter ca = new CustomAdapter(MainActivity.this, filteredlist);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(ca);
                if (filteredlist.isEmpty()) {

                    Toast.makeText(MainActivity.this, "No Data Found..", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });


        CustomAdapter ca = new CustomAdapter(MainActivity.this, contactsArr);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ca);


    }


}