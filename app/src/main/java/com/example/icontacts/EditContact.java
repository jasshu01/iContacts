package com.example.icontacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditContact extends AppCompatActivity {
    EditText editFirstName, editLastName, editPhone1, editPhone2, editEmail;
    Button editCancelbtn, editUpdatebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editPhone1 = findViewById(R.id.editPhone1);
        editPhone2 = findViewById(R.id.editPhone2);
        editEmail = findViewById(R.id.editEmail);
        editCancelbtn = findViewById(R.id.editCancelbtn);
        editUpdatebtn = findViewById(R.id.editUpdatebtn);

        Intent intent = getIntent();

        Toast.makeText(this, intent.getStringExtra("phone1"), Toast.LENGTH_SHORT).show();

        editFirstName.setText(intent.getStringExtra("firstName"));
        editLastName.setText(intent.getStringExtra("lastName"));
        editPhone1.setText(intent.getStringExtra("phone1"));
        editPhone2.setText(intent.getStringExtra("phone2"));
        editEmail.setText(intent.getStringExtra("email"));



    }
}