package com.example.icontacts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SearchView searchView;
    ImageView addContact, showOptions;
    Boolean isSelectMode = false;
    TextView viewGroups;
    ArrayList<Integer> selectedContactSno = null;
    ArrayList<Contact> contactsArr = new ArrayList<>();
    dbHandler handler;
    String members = "";
    String groupName = "";
    Bitmap bitmap;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        addContact = findViewById(R.id.addContact);
        showOptions = findViewById(R.id.showOptions);
        viewGroups = findViewById(R.id.GroupCount);


        handler = new dbHandler(MainActivity.this, "Contacts", null, 1);


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

                Menu items = popup.getMenu();
                if (CustomAdapter.isSelectMode) {

                    MenuItem item = items.findItem(R.id.importContacts);
                    item.setEnabled(false);
                    item = items.findItem(R.id.exportContacts);
                    item.setEnabled(false);

                    item = items.findItem(R.id.deleteMultiple);
                    item.setEnabled(true);

                    item = items.findItem(R.id.createGroup);
                    if (CustomAdapter.selectedContacts.size() > 1) {
                        item.setEnabled(true);
                    } else {
                        item.setEnabled(false);
                    }
                } else {

                    MenuItem item = items.findItem(R.id.importContacts);
                    item.setEnabled(true);
                    item = items.findItem(R.id.exportContacts);
                    item.setEnabled(true);
                    item = items.findItem(R.id.deleteMultiple);
                    item.setEnabled(false);

                    item = items.findItem(R.id.createGroup);
                    item.setEnabled(false);
                }


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
                            case R.id.deleteMultiple: {

                                ArrayList<Integer> arr = CustomAdapter.selectedContacts;
                                dbHandler handler = new dbHandler(MainActivity.this, "Contacts", null, 1);
                                Log.d("delete", "onMenuItemClick: " + arr);
                                for (int i = 0; i < arr.size(); i++) {
                                    handler.deleteContact(arr.get(i));
                                }
                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                startActivity(intent);
                                return true;
                            }
                            case R.id.createGroup: {

                                ArrayList<Integer> arr = CustomAdapter.selectedContacts;
                                dbHandler handler = new dbHandler(MainActivity.this, "Contacts", null, 1);


                                for (int i = 0; i < arr.size(); i++) {
                                    members += arr.get(i) + ",";
                                }

                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Set Group Name");

                                final EditText input = new EditText(MainActivity.this);

                                input.setInputType(InputType.TYPE_CLASS_TEXT);
                                builder.setView(input);

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        groupName = input.getText().toString();
                                        handler.addGroup(new ContactGroup(groupName, members), MainActivity.this);
                                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();

                                    }
                                });

                                builder.show();

//                                Dialog dialog = new Dialog(MainActivity.this);
//                                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                                dialog.setContentView(R.layout.ask_group_name);
//
//                                Button saveGroupName=dialog.findViewById(R.id.saveGroupName);
//
//                                saveGroupName.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//
//                                        EditText askingGroupName=dialog.findViewById(R.id.askingGroupName);
//
//                                        handler.addGroup(new ContactGroup(askingGroupName.getText(),members),MainActivity.this);
//                                    }
//                                });

//                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                                startActivity(intent);
//                                return true;
                            }

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


        viewGroups.setText("See Groups(" + handler.fetchGroups().size() + ")");


        viewGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "opening Groups", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }

        });

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


        //        API Testing

//        [{"fname":"test6","lname":"","p1":"6","p2":"66","email":"test6@gmail.com"},{"fname":"test7","lname":"","p1":"7","p2":"77","email":"test7@gmail.com"}]


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String apiURL = "https://mocki.io/v1/3534e930-8cce-4949-b269-a461c4e6b617";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, apiURL, null, new Response.Listener<JSONArray>() {


            public void onResponse(JSONArray response) {
                try {


                    for (int i = 0; i < response.length(); i++) {
                        Contact contact = new Contact();

                        JSONObject obj = response.getJSONObject(i);
                        contact.setFirstName(obj.getString("fname"));
                        contact.setLastName(obj.getString("lname"));
                        contact.setPhone1(obj.getString("p1"));
                        contact.setPhone2(obj.getString("p2"));
                        contact.setEmail(obj.getString("email"));
//                        Log.d("fetching API", obj.getString("fname"));
//                        Log.d("fetching API", obj.getString("lname"));
//                        Log.d("fetching API", obj.getString("p1"));
//                        Log.d("fetching API", obj.getString("p2"));
//                        Log.d("fetching API", obj.getString("email"));


                        String src = obj.getString("imgSource");
                        Log.d("fetching", "before: " + bitmap);
                        new GetImageFromUrl(contact).execute(src);
                        Log.d("fetching", "after: " + bitmap);
//                        handler.addContact(contact, MainActivity.this, bitmap);
                    }


//                    Log.d("fetching API", response.get(0).toString());
//                    Log.d("fetching API", response.get(1).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("fetching API", String.valueOf(error));
                    }
                });

        requestQueue.add(jsonArrayRequest);


//        ----------------------------


    }


    public class GetImageFromUrl extends AsyncTask<String, Void, Bitmap> {

        Contact contact = new Contact();

        public GetImageFromUrl(Contact contact) {
            this.contact = contact;
        }

        protected Bitmap doInBackground(String... url) {
            String stringUrl = url[0];
            Bitmap bitmap = null;
            InputStream inputStream;
            try {
                inputStream = new java.net.URL(stringUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("fetching", "doInBackground: " + bitmap);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmapPhoto) {
            super.onPostExecute(bitmapPhoto);
            bitmap = bitmapPhoto;

            handler.addContact(contact, MainActivity.this, bitmap);
            Log.d("fetchBitmap", "onPostExecute: " + bitmap);
        }
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

            Log.d("importContacts", "importContacts:" + fname + "," + lname + "," + p1 + "," + p2 + "," + email);
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

        if (file.exists()) {
            file.delete();
        }

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

