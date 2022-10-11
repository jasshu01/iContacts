package com.example.icontacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ContactInfo extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);
        Intent intent = getIntent();
//        int position=Integer.parseInt(intent.getStringExtra("position"));
        int position=intent.getIntExtra("position",0);

        textView = findViewById(R.id.textView3);
        textView.setText(String.valueOf(position));

    }
}