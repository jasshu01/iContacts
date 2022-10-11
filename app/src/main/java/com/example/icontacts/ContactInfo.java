package com.example.icontacts;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ContactInfo extends AppCompatActivity {

    TextView[] textViewArray = new TextView[5];
    ImageView deleteContact, shareContact, editContact;
    String firstName, lastName, phone1, phone2, email;
    int sno;


    @SuppressLint({"ResourceType", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);
        Intent intent = getIntent();

        sno = intent.getIntExtra("sno", -1);


        if (sno == -1)
            return;

        firstName = intent.getStringExtra("firstName");
        lastName = intent.getStringExtra("lastName");
        phone1 = intent.getStringExtra("phone1");
        phone2 = intent.getStringExtra("phone2");
        email = intent.getStringExtra("email");

        textViewArray[0] = findViewById(R.id.infoTextview1);
        textViewArray[1] = findViewById(R.id.infoTextview2);
        textViewArray[2] = findViewById(R.id.infoTextview3);
        textViewArray[3] = findViewById(R.id.infoTextview4);
        textViewArray[4] = findViewById(R.id.infoTextview5);

        deleteContact = findViewById(R.id.deleteContact);
        editContact = findViewById(R.id.editContact);
        shareContact = findViewById(R.id.shareContact);



        deleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(ContactInfo.this, "Deleting", Toast.LENGTH_SHORT).show();
                dbHandler handler = new dbHandler(ContactInfo.this, "Contacts", null, 1);
                handler.deleteContact(sno);
                Intent intent1 = new Intent(ContactInfo.this, MainActivity.class);
                startActivity(intent1);
            }
        });

        editContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ContactInfo.this, EditContact.class);

                intent1.putExtra("sno", sno);
                intent1.putExtra("firstName", firstName);
                intent1.putExtra("lastName", lastName);
                intent1.putExtra("phone1", phone1);
                intent1.putExtra("phone2", phone2);
                intent1.putExtra("email", email);


                startActivity(intent1);
            }
        });


        textViewArray[0].setText(firstName);

        int index = 1;

        if (lastName.length() != 0) {
            textViewArray[index++].setText(lastName);
        }
        if (phone1.length() != 0) {
            textViewArray[index++].setText(phone1);
        }
        if (phone2.length() != 0) {
            textViewArray[index++].setText(phone2);
        }
        if (email.length() != 0) {
            textViewArray[index].setText(email);
        }


    }


}