package com.example.icontacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

public class ContactInfo extends AppCompatActivity {

    TextView[] textViewArray = new TextView[5];
    ImageView deleteContact, shareContact, editContact, call1, call2, message1, message2,contactInfoPic;
    String firstName, lastName, phone1, phone2, email;
    TextView contactInfoDisplayText;
    int sno,fav;


    @SuppressLint({"ResourceType", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);
        Intent intent = getIntent();

        sno = intent.getIntExtra("sno", -1);
        fav = intent.getIntExtra("fav", -1);


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
        call1 = findViewById(R.id.call1);
        message1 = findViewById(R.id.message1);
        call2 = findViewById(R.id.call2);
        message2 = findViewById(R.id.message2);
        contactInfoPic = findViewById(R.id.contactInfoPic);
        contactInfoDisplayText = findViewById(R.id.contactInfoDisplayText);

        ImageView displayPic=contactInfoPic;
        TextView displayText=contactInfoDisplayText;

        displayPic.setImageResource(R.drawable.person);
        String filename = firstName+lastName+phone1;
        try {
            File f = new File("/data/user/0/com.example.icontacts/app_imageDir", filename + ".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            displayPic.setImageBitmap(b);
            displayText.setVisibility(View.GONE);
        } catch (FileNotFoundException e) {

            displayPic.setVisibility(View.GONE);
            String firstLetter = (String) firstName.subSequence(0, 1);
            firstLetter=firstLetter.toUpperCase();
            displayText.setText(firstLetter);
            Random randomBackgroundColor = new Random();
            int color = Color.argb(randomBackgroundColor.nextInt(256), randomBackgroundColor.nextInt(256), randomBackgroundColor.nextInt(256), randomBackgroundColor.nextInt(256));
            displayText.setBackgroundColor(color);

            Log.d("imagepick", "error");
            e.printStackTrace();
        }


        deleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(ContactInfo.this, "Deleting", Toast.LENGTH_SHORT).show();
                dbHandler handler = new dbHandler(ContactInfo.this, "Contacts", null, 1);
                handler.deleteContact(sno);
                Intent intent1 = new Intent(ContactInfo.this, MainActivity.class);
                startActivity(intent1);
            }
        });
        shareContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);

                String myMessage = "";
                if (firstName != phone1)
                    myMessage = firstName ;
                if (lastName.length() != 0)
                    myMessage += " " + lastName ;

                myMessage+="\n";
                if (phone1.length() != 0)
                    myMessage += phone1 + "\n";
                if (phone2.length() != 0)
                    myMessage += phone2 + "\n";
                if (email.length() != 0)
                    myMessage += email + "\n";


                sendIntent.putExtra(Intent.EXTRA_TEXT, myMessage);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
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
                intent1.putExtra("fav", fav);


                startActivity(intent1);
            }
        });


        call1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
//                Toast.makeText(ContactInfo.this, "Calling "+intent.getStringExtra("phone1"), Toast.LENGTH_SHORT).show();
                callIntent.setData(Uri.parse("tel:" + intent.getStringExtra("phone1")));
                if (ActivityCompat.checkSelfPermission(ContactInfo.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
                callIntent.setData(Uri.parse("tel:" + intent.getStringExtra("phone2")));
                if (ActivityCompat.checkSelfPermission(ContactInfo.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            }
        });
        message1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("smsto:" + intent.getStringExtra("phone1"));
                Intent messageIntent = new Intent(Intent.ACTION_SENDTO, uri);
                startActivity(messageIntent);

            }
        });
        message2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("smsto:" + intent.getStringExtra("phone2"));
                Intent messageIntent = new Intent(Intent.ACTION_SENDTO, uri);
                startActivity(messageIntent);

            }
        });


        textViewArray[0].setText(firstName);

        if (phone1.length() == 0) {
            call1.setVisibility(View.GONE);
            message1.setVisibility(View.GONE);
        }
        if (phone2.length() == 0) {
            call2.setVisibility(View.GONE);
            message2.setVisibility(View.GONE);
        }

//        Toast.makeText(this, lastName, Toast.LENGTH_SHORT).show();

        textViewArray[1].setText(lastName);
        textViewArray[2].setText(phone1);
        textViewArray[3].setText(phone2);
        textViewArray[4].setText(email);


    }


}