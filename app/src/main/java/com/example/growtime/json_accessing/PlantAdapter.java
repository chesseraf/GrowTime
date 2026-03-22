package com.example.growtime.json_accessing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.growtime.R;  // ✅ your actual package

import java.util.List;
public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {

    private List<Plant> plantList;
    private Context context;

    public PlantAdapter(Context context, List<Plant> plantList) {
        this.context = context;
        this.plantList = plantList;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_plant, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        Plant plant = plantList.get(position);

        holder.name.setText(plant.getCommon_name());
        holder.watering.setText(plant.getWaterness());

        // If using Glide (recommended)
        Glide.with(context)
                .load(plant.getImage_url())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }

    public static class PlantViewHolder extends RecyclerView.ViewHolder {
        TextView name, watering;
        ImageView image;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.plant_name);
            watering = itemView.findViewById(R.id.plant_watering);
            image = itemView.findViewById(R.id.plant_image);
        }
    }
}
