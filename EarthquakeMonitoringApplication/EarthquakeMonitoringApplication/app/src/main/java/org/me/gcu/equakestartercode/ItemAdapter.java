//Author: Okemdi Udeh
//Student ID: S1903344

package org.me.gcu.equakestartercode;

//Import the necessary libraries
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//Definition of ItemAdapter class
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.Holder> {
    //Declare variables
    Context context;
    List<Item> itemList;

    public ItemAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        //Set the displayed text using values from the getter methods of the Item class
        holder.title.setText(itemList.get(position).getLocation());
        holder.title.setTypeface(holder.title.getTypeface(), Typeface.BOLD);
        holder.magnitude.setText("Magnitude: " + itemList.get(position).getMagnitude());

        //Set color indicator for magnitude according to its value
        double magnitude = itemList.get(position).getMagnitude();
        if (magnitude <= 0.9 && magnitude > 0.0) {
            holder.magColor.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red_200));
        } else if (magnitude <= 1.9 && magnitude >= 1.0) {
            holder.magColor.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red_400));
        } else if (magnitude <= 2.9 && magnitude >= 2.0) {
            holder.magColor.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red_600));
        } else if (magnitude >= 3.0) {
            holder.magColor.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red_900));
        } else {
            holder.magColor.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red_200));
        }

        //add onclick listener for each earthquake item
        holder.itemView.setOnClickListener(view -> {
            //Call the detailActivity
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("item", itemList.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    //Define Holder class
    public class Holder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title, magnitude;
        CardView magColor;

        public Holder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            title = itemView.findViewById(R.id.title);
            magnitude = itemView.findViewById(R.id.magnitude);
            magColor = itemView.findViewById(R.id.magColor);
        }
    }


}
