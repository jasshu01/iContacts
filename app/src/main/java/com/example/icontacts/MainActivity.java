package com.example.icontacts;

import androidx.annotation.LongDef;
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
import android.widget.Toast;

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

        for (int i = 0; i < contactsArr.size(); i++) {
            Log.d("GetId",String.valueOf(contactsArr.get(i).getSno()));
            Log.d("GetId",contactsArr.get(i).getFirstName());
            Log.d("GetId",String.valueOf(contactsArr.get(i).isFav()));
        }

//        handler.deleteContact(3);
//        handler.deleteContact(4);
//        handler.deleteContact(5);
//

//        if(contactsArr.size()!=0)
//        {
        CustomAdapter ca = new CustomAdapter(MainActivity.this, contactsArr);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ca);
//        }

    }


}