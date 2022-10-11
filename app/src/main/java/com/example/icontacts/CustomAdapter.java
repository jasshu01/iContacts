package com.example.icontacts;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
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

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            view.setOnClickListener(this);
            textView = (TextView) view.findViewById(R.id.contactName);

        }


        public TextView getTextView() {
            return textView;
        }


        @Override
        public void onClick(View view) {

            Log.d("contactIndex", String.valueOf(getAdapterPosition()));
            int position = getAdapterPosition();
            Intent intent = new Intent(context, ContactInfo.class);
//            intent.putParcelableArrayListExtra("Contacts", (ArrayList<? extends Parcelable>) localDataSet);
//            intent.putExtra("Contacts",ArrayList<Contact> localDataSet);

            Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
//            intent.putExtra("Contacts", localDataSet);
            intent.putExtra("position", position);
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
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        viewHolder.getTextView().setText(localDataSet.get(position).getFirstName());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}


