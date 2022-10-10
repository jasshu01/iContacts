package com.example.icontacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SearchView searchView;
    ImageView addContact;
    String[] arr = {"rgs", "Agr", "rgs", "Agr", "rgs", "Agr"};
    ArrayList<Contact> contactsArr = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        addContact = findViewById(R.id.addContact);


        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, addContactPage.class);
                startActivity(intent);
            }
        });

        dbHandler handler = new dbHandler(MainActivity.this, "Contacts", null, 1);

        int sno = 1;

        while (true) {
            Contact contact = handler.accessContacts(sno);

            if (contact == null)
                break;
            Log.d("mytag", contact.getFirstName());
            contactsArr.add(contact);
            sno++;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            contactsArr.sort(Contact.contactsComparator);
        }

        CustomAdapter ca = new CustomAdapter(contactsArr);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ca);


    }


}