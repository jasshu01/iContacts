package com.example.icontacts;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

public class CustomAdapter2 extends RecyclerView.Adapter<CustomAdapter2.ViewHolder> {

    private static ArrayList<ContactGroup> localDataSet;
    private static Context context;


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static TextView groupName;



        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            view.setOnClickListener(this);
            groupName = (TextView) view.findViewById(R.id.groupName);


        }


        public TextView getGroupName() {
            return groupName;
        }



        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            Toast.makeText(context,localDataSet.get(position).getGroupName() , Toast.LENGTH_SHORT).show();
//
            Intent intent = new Intent(context, GroupInfo.class);
//
            intent.putExtra("Sno", localDataSet.get(position).getSno());
            intent.putExtra("GroupName", localDataSet.get(position).getGroupName());
            intent.putExtra("GroupMembers", localDataSet.get(position).getGroupMembers());
            Log.d("groupmembers", "onClick: "+localDataSet.get(position).getGroupMembers());
            context.startActivity(intent);


        }



    }


    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet            String[] containing the data to populate views to be used
     *                           by RecyclerView.

     */
    public CustomAdapter2(Context context, ArrayList<ContactGroup> dataSet) {
        this.context = context;
        localDataSet = dataSet;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.groupview, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {


        viewHolder.getGroupName().setText(localDataSet.get(position).getGroupName());


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}


