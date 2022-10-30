package com.example.icontacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    RecyclerView groupRecyclerView;
    ArrayList<ContactGroup> contactGroups=new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        groupRecyclerView=findViewById(R.id.groupRecyclerView);

        dbHandler handler=new dbHandler(MainActivity2.this,"Contacts",null,1);
        contactGroups=handler.fetchGroups();
        Log.d("fetchGroups", "onCreate: "+contactGroups);

        CustomAdapter2 ca = new CustomAdapter2(MainActivity2.this, contactGroups);
        groupRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        groupRecyclerView.setAdapter(ca);

    }
}