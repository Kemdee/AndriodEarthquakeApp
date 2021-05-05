//Author: Okemdi Udeh
//Student ID: S1903344

package org.me.gcu.equakestartercode;

//Import the required libraries
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

//Definition of filterAdapter class
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.Holder> {
    Context context;
    List<Item> itemList;
    int highestIndex;
    int lowestIndex;
    int highestLatitudeId, lowestLatitudeId, highestLongitudeId, lowestLongitudeId;


    public FilterAdapter(Context context, List<Item> itemList, int highestIndex, int lowestIndex, int highestLatitudeId, int lowestLatitudeId, int highestLongitudeId, int lowestLongitudeId) {
        this.context = context;
        this.itemList = itemList;
        this.highestIndex = highestIndex;
        this.lowestIndex = lowestIndex;
        this.highestLatitudeId = highestLatitudeId;
        this.lowestLatitudeId = lowestLatitudeId;
        this.highestLongitudeId = highestLongitudeId;
        this.lowestLongitudeId = lowestLongitudeId;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_filter, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        //Set text values for the different textViews
        holder.magnitude.setText("Magnitude: " + itemList.get(position).getMagnitude());
        holder.location.setText(itemList.get(position).getLocation());
        holder.location.setTypeface(holder.location.getTypeface(), Typeface.BOLD);

        //Change the color of the location according to the magnitude of the quake
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

        //Set the depth value if it is the shallowest or deepest
        if (position == lowestIndex) {
            holder.label.setVisibility(View.VISIBLE);
            holder.label.setText("Depth: "+itemList.get(position).getDepth()+" km (Shallowest)");
        } else if (position == highestIndex) {
            holder.label.setVisibility(View.VISIBLE);
            holder.label.setText("Depth: "+itemList.get(position).getDepth()+" km (Deepest)");
        } else {
            holder.label.setVisibility(View.GONE);
        }

        //Set the values for northerly, easterly, westerly, southerly
        if (itemList.get(position).getId() == highestLatitudeId) {
            holder.label1.setVisibility(View.VISIBLE);
            holder.label1.setText("Position: Most Northerly");
        } else if (itemList.get(position).getId() == lowestLatitudeId) {
            holder.label1.setVisibility(View.VISIBLE);
            holder.label1.setText("Position: Most Southerly");
        } else if (itemList.get(position).getId() == highestLongitudeId) {
            holder.label1.setVisibility(View.VISIBLE);
            holder.label1.setText("Position: Most Easterly");
        } else if (itemList.get(position).getId() == lowestLongitudeId) {
            holder.label1.setVisibility(View.VISIBLE);
            holder.label1.setText("Position: Most Westerly");
        } else {
            holder.label1.setVisibility(View.GONE);
        }

        //Add an onClickListener to each item
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("item", itemList.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        CardView cardView;
        CardView magColor;
        TextView magnitude, label, label1, location;

        public Holder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            magnitude = itemView.findViewById(R.id.magnitude);
            label = itemView.findViewById(R.id.label);
            label1 = itemView.findViewById(R.id.label1);
            location = itemView.findViewById(R.id.location);
            magColor = itemView.findViewById(R.id.magColor);
        }
    }

    public void reloadData(int highestIndex, int lowestIndex, int highestLatitudeId, int lowestLatitudeId, int highestLongitudeId, int lowestLongitudeId) {
        this.highestIndex = highestIndex;
        this.lowestIndex = lowestIndex;
        this.highestLatitudeId = highestLatitudeId;
        this.lowestLatitudeId = lowestLatitudeId;
        this.highestLongitudeId = highestLongitudeId;
        this.lowestLongitudeId = lowestLongitudeId;
        notifyDataSetChanged();
    }
}
