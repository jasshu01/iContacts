package com.example.icontacts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Cell;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
//                                importContacts();
                                return true;
                            case R.id.exportContacts:
                                try {
                                    exportContacts();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(MainActivity.this, "Exporting Contacts", Toast.LENGTH_SHORT).show();
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

    public void exportContacts() throws IOException {


        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);


        File file = new File(folder, "ExportedContacts.txt");
        String mydata = "";

        ArrayList<Contact> mycontacts = handler.allContacts();

        for(int i=0;i< mycontacts.size();i++)
        {
            String contact="";

            if(mycontacts.get(i).getFirstName().length()!=0) contact+="First Name: "+mycontacts.get(i).getFirstName()+", ";
            if(mycontacts.get(i).getLastName().length()!=0) contact+="Last Name: "+mycontacts.get(i).getLastName()+", ";
            if(mycontacts.get(i).getPhone1().length()!=0)contact+="Phone1: "+mycontacts.get(i).getPhone1()+", ";
            if(mycontacts.get(i).getPhone2().length()!=0)contact+="Phone2: "+mycontacts.get(i).getPhone2()+", ";
            if(mycontacts.get(i).getEmail().length()!=0)contact+="Email: "+mycontacts.get(i).getEmail()+", ";


            mydata+=contact+"\n";
        }

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

