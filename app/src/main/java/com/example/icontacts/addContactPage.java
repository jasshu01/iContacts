package com.example.icontacts;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

public class addContactPage extends AppCompatActivity {
    Button cancelButton, saveButton;
    EditText firstName, lastName, contactPhone1, contactPhone2, contactEmail;
    ImageView contactImage;
    FloatingActionButton editContactImage;
    ActivityResultLauncher<Intent> activityResultLauncher_capture,activityResultLauncher_choose;

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

                Intent intent=new Intent(addContactPage.this,MainActivity.class);
                startActivity(intent);
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

                Bitmap photo = null;
                try {
                    photo = ((BitmapDrawable) contactImage.getDrawable()).getBitmap();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Contact contact = new Contact(contact_firstName, contact_lastName, contact_Phone1, contact_Phone2, contact_Email);


                dbHandler handler = new dbHandler(addContactPage.this, "Contacts", null, 1);

                if (handler.addContact(contact, addContactPage.this, photo)) {


                    Toast.makeText(addContactPage.this, "Contact Saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(addContactPage.this, MainActivity.class);
                    startActivity(intent);

                }


            }
        });

        activityResultLauncher_capture = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    Bitmap photo = (Bitmap) bundle.get("data");


                    contactImage.setImageBitmap(photo);
                }

            }
        });

         activityResultLauncher_choose = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        if (data != null && data.getData() != null) {
                            Uri selectedImageUri = data.getData();
                            Bitmap selectedImageBitmap;
                            try {
                                selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImageUri);
                                contactImage.setImageBitmap(selectedImageBitmap);
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });


        editContactImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                        PopupMenu popup = new PopupMenu(addContactPage.this, view);
                        MenuInflater inflater = popup.getMenuInflater();
                        inflater.inflate(R.menu.ask_camera_or_storage, popup.getMenu());
                        popup.show();


                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {

                                switch (menuItem.getItemId())
                                {
                                    case R.id.captureImage:
                                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        try {
                                            activityResultLauncher_capture.launch(takePictureIntent);
                                        } catch (ActivityNotFoundException e) {
                                            Toast.makeText(addContactPage.this, "Enable Camera Permission", Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                    case R.id.chooseFromStorage:
                                        Intent choosePictureIntent = new Intent();
                                        choosePictureIntent.setType("image/*");
                                        choosePictureIntent.setAction(Intent.ACTION_GET_CONTENT);
                                        try {
                                            activityResultLauncher_choose.launch(choosePictureIntent);
                                        } catch (ActivityNotFoundException e) {
                                            Toast.makeText(addContactPage.this, "Enable Storage Permission", Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                    default:
                                        return false;
                                }

                                return true;

                            }
                        });








            }

        });
    }


}