package com.example.icontacts;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.Manifest;
import android.app.Activity;
import android.app.VoiceInteractor;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class addContactPage extends AppCompatActivity {
    Button cancelButton, saveButton;
    EditText firstName, lastName, contactPhone1, contactPhone2, contactEmail;
    ImageView contactImage;
    FloatingActionButton editContactImage;
    ActivityResultLauncher<Intent> activityResultLauncher;

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
        contactImage = findViewById(R.id.contactImage);
        editContactImage = findViewById(R.id.editContactImage);


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                try {
                    Bitmap photo = ((BitmapDrawable) contactImage.getDrawable()).getBitmap();
                    String location = saveToInternalStorage(photo, contact_firstName + contact_lastName + contact_Phone1);
                    Log.d("bitmapdata", "onActivityResult: " + location +contact_firstName + contact_lastName + contact_Phone1 );
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


                Contact contact = new Contact(contact_firstName, contact_lastName, contact_Phone1, contact_Phone2, contact_Email);


                dbHandler handler = new dbHandler(addContactPage.this, "Contacts", null, 1);

                if (handler.addContact(contact,addContactPage.this)) {
                    Toast.makeText(addContactPage.this, "Contact Saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(addContactPage.this, MainActivity.class);
                    startActivity(intent);

                }

            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    Bitmap photo = (Bitmap) bundle.get("data");


                    contactImage.setImageBitmap(photo);
                }


            }
        });


        editContactImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    activityResultLauncher.launch(intent);
                } else {
                    Toast.makeText(addContactPage.this, "service not available", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String filename) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, filename + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.d("address", directory.getAbsolutePath());
        return directory.getAbsolutePath();
    }


}