package com.example.icontacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SearchView searchView;
    ImageView addContact;
    String[] arr={"rgs","Agr","rgs","Agr","rgs","Agr","rgs","Agr","rgs","Agr","rgs","Agr","rgs","Agr","rgs","Agr","rgs","Agr","rgs","Agr","rgs","Agr"};
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView= findViewById(R.id.recyclerView);
        searchView= findViewById(R.id.searchView);
        addContact= findViewById(R.id.addContact);

        CustomAdapter ca=new CustomAdapter(arr);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ca);

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,addContactPage.class);
                startActivity(intent);
            }
        });


    }
}