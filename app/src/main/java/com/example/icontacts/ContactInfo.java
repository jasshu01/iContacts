package com.example.icontacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactInfo extends AppCompatActivity {

    TextView[] textViewArray = new TextView[5];
    ImageView deleteContact, shareContact, editContact,call1,call2,message1,message2;
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
        call1=findViewById(R.id.call1);
        message1=findViewById(R.id.message1);
        call2=findViewById(R.id.call2);
        message2=findViewById(R.id.message2);


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


        call1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
//                Toast.makeText(ContactInfo.this, "Calling "+intent.getStringExtra("phone1"), Toast.LENGTH_SHORT).show();
                callIntent.setData(Uri.parse("tel:"+intent.getStringExtra("phone1")));
                if(ActivityCompat.checkSelfPermission(ContactInfo.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
                {
                    return;
                }
                startActivity(callIntent);
            }
        });

        call2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
//                Toast.makeText(ContactInfo.this, "Calling "+intent.getStringExtra("phone2"), Toast.LENGTH_SHORT).show();
                callIntent.setData(Uri.parse("tel:"+intent.getStringExtra("phone2")));
                if(ActivityCompat.checkSelfPermission(ContactInfo.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
                {
                    return;
                }
                startActivity(callIntent);
            }
        });


        textViewArray[0].setText(firstName);

        if (phone1.length()==0)
        {
            call1.setVisibility(View.GONE);
            message1.setVisibility(View.GONE);
        }
         if (phone2.length()==0)
        {
            call2.setVisibility(View.GONE);
            message2.setVisibility(View.GONE);
        }


        int index = 1;
        textViewArray[index++].setText(lastName);
        textViewArray[index++].setText(phone1);
        textViewArray[index++].setText(phone2);
        textViewArray[index].setText(email);




    }


}