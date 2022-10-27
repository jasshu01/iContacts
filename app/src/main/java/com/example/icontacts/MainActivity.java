package com.example.icontacts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SearchView searchView;
    ImageView addContact, showOptions;

    ArrayList<Contact> contactsArr = new ArrayList<>();
    dbHandler handler;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        addContact = findViewById(R.id.addContact);
        showOptions = findViewById(R.id.showOptions);


        Dexter.withContext(this)
                .withPermissions(

                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();


        showOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(MainActivity.this, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.actions, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.importContacts:
                                Toast.makeText(MainActivity.this, "Importing Contacts", Toast.LENGTH_SHORT).show();
                                try {

                                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                                    intent.setType("text/plain");

                                    try {
                                        activityResultLauncher_pickFile.launch(intent);
                                    } catch (ActivityNotFoundException e) {

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return true;
                            case R.id.exportContacts:
                                try {
                                    exportContacts();

                                    Toast.makeText(MainActivity.this, "Exporting Contacts", Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Log.d("exportingContacts", "onMenuItemClick: " + e.toString());
                                }
                                return true;
                        }


                        return false;
                    }
                });

            }
        });


        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, addContactPage.class);
                startActivity(intent);
            }
        });

        handler = new dbHandler(MainActivity.this, "Contacts", null, 1);


        contactsArr = handler.allContacts();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            contactsArr.sort(Contact.contactsComparator);
        }


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String filter) {
                ArrayList<Contact> filteredlist = new ArrayList<Contact>();


                for (Contact item : contactsArr) {
                    if (item.getFirstName().toLowerCase().contains(filter.toLowerCase()) ||
                            item.getLastName().toLowerCase().contains(filter.toLowerCase()) ||
                            item.getPhone1().toLowerCase().contains(filter.toLowerCase()) ||
                            item.getEmail().toLowerCase().contains(filter.toLowerCase()) ||
                            item.getPhone2().toLowerCase().contains(filter.toLowerCase())) {
                        filteredlist.add(item);
                    }
                }
                CustomAdapter ca = new CustomAdapter(MainActivity.this, filteredlist);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(ca);
                if (filteredlist.isEmpty()) {

                    Toast.makeText(MainActivity.this, "No Data Found..", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });


        CustomAdapter ca = new CustomAdapter(MainActivity.this, contactsArr);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ca);


    }


    ActivityResultLauncher<Intent> activityResultLauncher_pickFile = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();

            if (data != null && data.getData() != null) {

                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    BufferedReader r = new BufferedReader(new InputStreamReader(in));
                    importContacts(r);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    });


    public void importContacts(BufferedReader br) throws Exception {

        String line;

        String fname = "", lname = "", p1 = "", p2 = "", email = "";
        while ((line = br.readLine()) != null) {
            String[] splitted = line.split(",");
            fname = "";
            lname = "";
            p1 = "";
            p2 = "";
            email = "";


            for (int i = 0; i < splitted.length; i++) {


                if (splitted[i].contains("First")) {
                    fname = String.valueOf(splitted[i].split(": ")[1]);

                } else if (splitted[i].contains("Last")) {
                    lname = String.valueOf(splitted[i].split(": ")[1]);

                } else if (splitted[i].contains("Phone1")) {
                    p1 = String.valueOf(splitted[i].split(": ")[1]);

                } else if (splitted[i].contains("Phone2")) {
                    p2 = String.valueOf(splitted[i].split(": ")[1]);
                } else if (splitted[i].contains("Email")) {
                    email = String.valueOf(splitted[i].split(": ")[1]);

                }
            }

//            Log.d("importContacts", "importContacts: "+fname+lname+p1+p2+email);
            if (handler.addContact(new Contact(fname, lname, p1, p2, email), this, null)) {
                Log.d("importingContacts", "importContacts: " + 1);
            } else {
                Log.d("importingContacts", "importContacts: " + 0);
            }


        }


        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);


    }


    public void exportContacts() throws IOException {


        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);


        File file = new File(folder, "ExportedContacts.txt");
        String mydata = "";
        writeTextData(file, "");
        ArrayList<Contact> mycontacts = handler.allContacts();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mycontacts.sort(Contact.contactsComparator);
        }

        for (int i = 0; i < mycontacts.size(); i++) {
            String contact = "";

            if (mycontacts.get(i).getFirstName().length() != 0)
                contact += "First Name: " + mycontacts.get(i).getFirstName() + ", ";
            if (mycontacts.get(i).getLastName().length() != 0)
                contact += "Last Name: " + mycontacts.get(i).getLastName() + ", ";
            if (mycontacts.get(i).getPhone1().length() != 0)
                contact += "Phone1: " + mycontacts.get(i).getPhone1() + ", ";
            if (mycontacts.get(i).getPhone2().length() != 0)
                contact += "Phone2: " + mycontacts.get(i).getPhone2() + ", ";
            if (mycontacts.get(i).getEmail().length() != 0)
                contact += "Email: " + mycontacts.get(i).getEmail() + ", ";


            mydata += contact + "\n";
        }
        Log.d("exportingContacts", "exportContacts: " + mydata);
        writeTextData(file, mydata);

    }

    private void writeTextData(File file, String data) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data.getBytes());
            Toast.makeText(this, "Done" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

