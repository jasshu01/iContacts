package com.example.icontacts;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.internal.TextDrawableHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private static ArrayList<Contact> localDataSet;
    private static Context context;


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView textView;
        private static ImageView fav, displayPic;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            view.setOnClickListener(this);
            textView = (TextView) view.findViewById(R.id.contactName);

            fav = (ImageView) view.findViewById(R.id.fav);
            displayPic = (ImageView) view.findViewById(R.id.displayPic);


        }


        public TextView getTextView() {
            return textView;
        }



        public ImageView getFavImageView() {
            return fav;
        }

        public ImageView getDisplapPic() {
            return displayPic;
        }


        @Override
        public void onClick(View view) {

            Log.d("contactIndex", String.valueOf(getAdapterPosition()));
            int position = getAdapterPosition();
            Intent intent = new Intent(context, ContactInfo.class);

            Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();

            intent.putExtra("sno", localDataSet.get(position).getSno());
            intent.putExtra("firstName", localDataSet.get(position).getFirstName());
            intent.putExtra("lastName", localDataSet.get(position).getLastName());
            intent.putExtra("phone1", localDataSet.get(position).getPhone1());
            intent.putExtra("phone2", localDataSet.get(position).getPhone2());
            intent.putExtra("email", localDataSet.get(position).getEmail());
//            intent.putExtra("Contacts", localDataSet);
            context.startActivity(intent);


        }
    }


    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public CustomAdapter(Context context, ArrayList<Contact> dataSet) {
        this.context = context;
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.contactview, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {


        viewHolder.getTextView().setText(localDataSet.get(position).getFirstName());
        ImageView img = viewHolder.getFavImageView();

        ImageView displayPic = viewHolder.getDisplapPic();
        displayPic.setImageResource(R.drawable.person);
//        displayPic.setImageResource(R.drawable.letter_a);
//            displayPic.setImageBitmap( );
        String filename=localDataSet.get(position).getFirstName()+localDataSet.get(position).getLastName()+localDataSet.get(position).getPhone1();
        try {
            File f=new File("/data/user/0/com.example.icontacts/app_imageDir", filename+".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

            displayPic.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            Log.d("imagepick","error");
            e.printStackTrace();
        }


        if (localDataSet.get(position).isFav() == 1) {
            img.setImageResource(R.drawable.fav);
        } else {
            img.setImageResource(R.drawable.fav_borderd);
        }


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbHandler handler = new dbHandler(context, "Contacts", null, 1);

                if (localDataSet.get(position).isFav() == 0) {
                    img.setImageResource(R.drawable.fav);
                    localDataSet.get(position).setFav(1);
                    handler.updateContact(localDataSet.get(position));
                } else {
                    img.setImageResource(R.drawable.fav_borderd);
                    localDataSet.get(position).setFav(0);
                    handler.updateContact(localDataSet.get(position));
                }


//                Toast.makeText(context, "fav clicked " + localDataSet.get(position).getFirstName(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}


