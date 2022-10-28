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

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private static ArrayList<Contact> localDataSet;
    private static Context context;
    private static CardView cardView;


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static TextView contactName;
        private static TextView contactPhone;
        private static ImageView fav, displayPic;
        private static TextView displayText;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            view.setOnClickListener(this);
            contactName = (TextView) view.findViewById(R.id.contactName);
            contactPhone = (TextView) view.findViewById(R.id.contactPhone);

            fav = (ImageView) view.findViewById(R.id.fav);
            displayPic = (ImageView) view.findViewById(R.id.displayPic);
            displayText = (TextView) view.findViewById(R.id.displayText);
            cardView = (CardView) view.findViewById(R.id.cardView);


        }


        public TextView getContactName() {
            return contactName;
        }

        public TextView getContactPhone() {
            return contactPhone;
        }

        public CardView getCardView() {
            return cardView;
        }


        public ImageView getFavImageView() {
            return fav;
        }

        public ImageView getDisplapPic() {
            return displayPic;
        }

        public TextView getDisplapText() {
            return displayText;
        }


        @Override
        public void onClick(View view) {


            int position = getAdapterPosition();
            Intent intent = new Intent(context, ContactInfo.class);

//            Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();

            intent.putExtra("sno", localDataSet.get(position).getSno());
            intent.putExtra("firstName", localDataSet.get(position).getFirstName());
            intent.putExtra("lastName", localDataSet.get(position).getLastName());
            intent.putExtra("phone1", localDataSet.get(position).getPhone1());
            intent.putExtra("phone2", localDataSet.get(position).getPhone2());
            intent.putExtra("email", localDataSet.get(position).getEmail());
            intent.putExtra("fav", localDataSet.get(position).isFav());
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


        viewHolder.getContactName().setText(localDataSet.get(position).getFirstName() + " " + localDataSet.get(position).getLastName());
        if (localDataSet.get(position).getPhone1().length() != 0)
            viewHolder.getContactPhone().setText(localDataSet.get(position).getPhone1());
        else
            viewHolder.getContactPhone().setText(localDataSet.get(position).getPhone2());

        ImageView img = viewHolder.getFavImageView();

        ImageView displayPic = viewHolder.getDisplapPic();
        TextView displayText = viewHolder.getDisplapText();

        displayPic.setImageResource(R.drawable.person);
        String filename = String.valueOf(localDataSet.get(position).getSno());
        try {
            File f = new File("/data/user/0/com.example.icontacts/app_imageDir", filename + ".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            displayPic.setImageBitmap(b);
            displayText.setVisibility(View.GONE);
        } catch (FileNotFoundException e) {

            displayPic.setVisibility(View.GONE);
            String firstLetter = (String) localDataSet.get(position).getFirstName().subSequence(0, 1);
            firstLetter = firstLetter.toUpperCase();
            displayText.setText(firstLetter);
            Random randomBackgroundColor = new Random();
            int color = Color.argb(randomBackgroundColor.nextInt(256), randomBackgroundColor.nextInt(256), randomBackgroundColor.nextInt(256), randomBackgroundColor.nextInt(256));
            displayText.setBackgroundColor(color);

//            Log.d("imagepick", "error");
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


