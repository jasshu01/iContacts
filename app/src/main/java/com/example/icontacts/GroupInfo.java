package com.example.icontacts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.nio.file.attribute.GroupPrincipal;
import java.util.ArrayList;

public class GroupInfo extends AppCompatActivity {


    String groupName, groupMembers;
    int sno;
    RecyclerView recyclerView;


    @SuppressLint({"ResourceType", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        Intent intent = getIntent();

        sno = intent.getIntExtra("Sno", -1);
        groupName = intent.getStringExtra("GroupName");
        groupMembers = intent.getStringExtra("GroupMembers");
        ArrayList<Contact> list = new ArrayList<>();
        dbHandler handler = new dbHandler(GroupInfo.this, "Contacts", null, 1);

        Log.d("groupinfo", "onCreate: in groupinfo->"+groupMembers);
        recyclerView = findViewById(R.id.groupInfo_displayContacts);

        for (String s : groupMembers.split(",")) {
            Log.d("groupinfo", "onCreate: in groupinfo=>"+s);

            list.add(handler.fetchContact(Integer.parseInt(s)));
        }


        TextView textView=findViewById(R.id.groupInfo_groupName);
        Button deleteGroupButton=findViewById(R.id.groupInfo_deleteGroup);
        Button sendMessage=findViewById(R.id.groupInfo_sendMessage);

        textView.setText(groupName);
        CustomAdapter2_groupInfo ca = new CustomAdapter2_groupInfo(GroupInfo.this, list,sno);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ca);



        deleteGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHandler handler=new dbHandler(GroupInfo.this,"Contacts",null,1);
                handler.deleteGroup(sno);
                Intent intent1=new Intent(GroupInfo.this,MainActivity2.class);
                startActivity(intent1);
            }
        });



    }
}