package com.example.icontacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class addContactPage extends AppCompatActivity {
    Button cancelButton, saveButton;
    EditText firstName, lastName, contactPhone1, contactPhone2, contactEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact_page);

        cancelButton = findViewById(R.id.cancelButton);
        saveButton = findViewById(R.id.saveButton);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        contactPhone1 = findViewById(R.id.contactPhone1);
        contactPhone2 = findViewById(R.id.contactPhone2);
        contactEmail = findViewById(R.id.contactEmail);


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(addContactPage.this, MainActivity.class);
//                startActivity(intent);

                finish();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String contact_firstName, contact_lastName, contact_Phone1, contact_Phone2, contact_Email;
                contact_firstName = String.valueOf(firstName.getText());
                contact_lastName = String.valueOf(lastName.getText());
                contact_Phone1 = String.valueOf(contactPhone1.getText());
                contact_Phone2 = String.valueOf(contactPhone2.getText());
                contact_Email = String.valueOf(contactEmail.getText());


                Contact contact = new Contact(contact_firstName,contact_lastName,contact_Phone1,contact_Phone2,contact_Email);



                dbHandler handler = new dbHandler(addContactPage.this, "Contacts", null, 1);

                if(handler.addContact(contact))
                {
                    Toast.makeText(addContactPage.this, "Contact Saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(addContactPage.this, MainActivity.class);
                    startActivity(intent);

                }

            }
        });


    }
}