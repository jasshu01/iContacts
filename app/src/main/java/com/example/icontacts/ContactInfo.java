package com.example.icontacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ContactInfo extends AppCompatActivity {
    TextView textview;
    TextView[] textViewArray = new TextView[5];
    ArrayList<Contact> myContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int position = intent.getIntExtra("position", 2);
        String firstName, lastName, phone1, phone2, email;


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


        textViewArray[0].setText(firstName);

        int sno = 1;

        if (lastName.length() != 0) {
            textViewArray[sno++].setText(lastName);
        }
        if (phone1.length() != 0) {
            textViewArray[sno++].setText(phone1);
        }
        if (phone2.length() != 0) {
            textViewArray[sno++].setText(phone2);
        }
        if (email.length() != 0) {
            textViewArray[sno].setText(email);
        }

    }
}