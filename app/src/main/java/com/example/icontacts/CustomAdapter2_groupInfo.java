package com.example.icontacts;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

public class CustomAdapter2_groupInfo extends RecyclerView.Adapter<CustomAdapter2_groupInfo.ViewHolder> {

    private static ArrayList<Contact> localDataSet;
    private static int groupSno;

    private static Context context;


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static TextView contactName;
        private static TextView contactPhone;
        private static ImageView deleteFromGroup;
        private static TextView displayText;
        private static ImageView displayPic;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            view.setOnClickListener(this);
            contactName = (TextView) view.findViewById(R.id.contactName);
            contactPhone = (TextView) view.findViewById(R.id.contactPhone);
            deleteFromGroup = (ImageView) view.findViewById(R.id.deleteFromGroup);
displayPic=(ImageView) view.findViewById(R.id.displayPic);
            displayText=(TextView) view.findViewById(R.id.displayText);

        }

        public TextView getDisplayText() {
            return displayText;
        }

        public ImageView getDisplayPic() {
            return displayPic;
        }

        public TextView getContactName() {
            return contactName;
        }

        public ImageView getDeleteFromGroup() {
            return deleteFromGroup;
        }

        public TextView getContactPhone() {
            return contactPhone;
        }


        @Override
        public void onClick(View view) {

//
//            int position = getAdapterPosition();
//            Intent intent = new Intent(context, GroupInfo.class);
//
//            intent.putExtra("Sno", localDataSet.get(position).getSno());
//
//            context.startActivity(intent);


        }


    }


    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     * @param sno
     */
    public CustomAdapter2_groupInfo(Context context, ArrayList<Contact> dataSet, int sno) {
        this.context = context;
        localDataSet = dataSet;

        groupSno = sno;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.groupinfo_contact, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {


        viewHolder.getContactName().setText(localDataSet.get(position).getFirstName() + " " + localDataSet.get(position).getLastName());

        viewHolder.getContactPhone().setText(localDataSet.get(position).getPhone1());

        viewHolder.getDeleteFromGroup().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "deleting", Toast.LENGTH_SHORT).show();
                dbHandler handler = new dbHandler(context, "Contacts", null, 1);
                handler.deleteFromGroup(groupSno, localDataSet.get(position).getSno());
                Intent intent = new Intent(context, GroupInfo.class);
                context.startActivity(intent);

            }
        });


        ImageView displayPic = viewHolder.getDisplayPic();
        TextView displayText = viewHolder.getDisplayText();

        displayPic.setImageResource(R.drawable.person);
        String filename = String.valueOf(localDataSet.get(position).getSno());
        try {
            File f = new File("/data/user/0/com.example.icontacts/app_imageDir", filename + ".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            displayPic.setImageBitmap(b);
            displayText.setVisibility(View.GONE);
        } catch (FileNotFoundException e)
        {

            displayPic.setVisibility(View.GONE);
            String firstLetter = (String) localDataSet.get(position).getFirstName().subSequence(0, 1);
            firstLetter = firstLetter.toUpperCase();
            displayText.setText(firstLetter);
            Random randomBackgroundColor = new Random();
            int color = Color.argb(randomBackgroundColor.nextInt(256), randomBackgroundColor.nextInt(256), randomBackgroundColor.nextInt(256), randomBackgroundColor.nextInt(256));
            displayText.setBackgroundColor(color);


            e.printStackTrace();
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}


