package com.example.growtime.json_accessing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.growtime.R;  // ✅ your actual package

import java.util.List;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {

    public interface OnAddToGardenListener {
        void onAddToGarden(Plant plant);
    }

    public interface OnEditPlantListener {
        void onEditPlant(Plant plant);
    }

    private final List<Plant> plantList;
    private final Context context;
    private final OnAddToGardenListener addToGardenListener;
    private final OnEditPlantListener editPlantListener;

    public PlantAdapter(Context context, List<Plant> plantList) {
        this(context, plantList, null, null);
    }

    public PlantAdapter(Context context, List<Plant> plantList, OnAddToGardenListener addToGardenListener) {
        this(context, plantList, addToGardenListener, null);
    }

    public PlantAdapter(Context context, List<Plant> plantList, OnAddToGardenListener addToGardenListener, OnEditPlantListener editPlantListener) {
        this.context = context;
        this.plantList = plantList;
        this.addToGardenListener = addToGardenListener;
        this.editPlantListener = editPlantListener;
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

        if (addToGardenListener != null) {
            holder.addToGarden.setVisibility(View.VISIBLE);
            holder.addToGarden.setOnClickListener(v -> addToGardenListener.onAddToGarden(plant));
        } else {
            holder.addToGarden.setVisibility(View.GONE);
            holder.addToGarden.setOnClickListener(null);
        }

        if (editPlantListener != null) {
            holder.editPlant.setVisibility(View.VISIBLE);
            holder.editPlant.setOnClickListener(v -> editPlantListener.onEditPlant(plant));
        } else {
            holder.editPlant.setVisibility(View.GONE);
            holder.editPlant.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return (plantList == null) ? 0 : plantList.size();
    }

    public static class PlantViewHolder extends RecyclerView.ViewHolder {
        TextView name, watering;
        ImageView image;
        Button addToGarden;
        Button editPlant;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.plant_name);
            watering = itemView.findViewById(R.id.plant_watering);
            image = itemView.findViewById(R.id.plant_image);
            addToGarden = itemView.findViewById(R.id.btn_add_to_garden);
            editPlant = itemView.findViewById(R.id.btn_edit_plant);
        }
    }
}
