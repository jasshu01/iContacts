package com.example.icontacts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditContact extends AppCompatActivity {
    EditText editFirstName, editLastName, editPhone1, editPhone2, editEmail;
    Button editCancelbtn, editUpdatebtn;
    int sno,fav;

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

//        Toast.makeText(this, intent.getStringExtra("phone1"), Toast.LENGTH_SHORT).show();
        sno = intent.getIntExtra("sno",-1);
        fav = intent.getIntExtra("fav",-1);
        editFirstName.setText(intent.getStringExtra("firstName"));
        editLastName.setText(intent.getStringExtra("lastName"));
        editPhone1.setText(intent.getStringExtra("phone1"));
        editPhone2.setText(intent.getStringExtra("phone2"));
        editEmail.setText(intent.getStringExtra("email"));



        editCancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editUpdatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact contact = new Contact();
                contact.setSno(sno);
                contact.setFirstName(String.valueOf(editFirstName.getText()));
                contact.setLastName(String.valueOf(editLastName.getText()));
                contact.setPhone1(String.valueOf(editPhone1.getText()));
                contact.setPhone2(String.valueOf(editPhone2.getText()));
                contact.setEmail(String.valueOf(editEmail.getText()));
                contact.setFav(fav);

                dbHandler handler= new dbHandler(EditContact.this,"Contacts",null,1);
                handler.updateContact(contact);

                Intent intent1=new Intent(EditContact.this,MainActivity.class);
                startActivity(intent1);
            }
        });

    }
}