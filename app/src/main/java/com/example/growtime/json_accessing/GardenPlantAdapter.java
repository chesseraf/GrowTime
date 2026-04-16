package com.example.growtime.json_accessing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.growtime.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GardenPlantAdapter extends RecyclerView.Adapter<GardenPlantAdapter.ViewHolder> {

    public interface OnEditListener {
        void onEdit(StoredPlant storedPlant);
    }

    public interface OnWaterListener {
        void onWater(StoredPlant storedPlant);
    }

    private static final SimpleDateFormat DATE_FMT =
            new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());

    private final List<StoredPlant> plants;
    private final Context context;
    private final OnEditListener editListener;
    private final OnWaterListener waterListener;

    public GardenPlantAdapter(Context context, List<StoredPlant> plants, OnEditListener editListener, OnWaterListener waterListener) {
        this.context = context;
        this.plants = plants;
        this.editListener = editListener;
        this.waterListener = waterListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_plant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StoredPlant sp = plants.get(position);
        Plant plant = sp.plant;
        holder.name.setText(plant.getCommon_name());
        holder.watering.setText("Every " + sp.water_days + " days");

        holder.indoor.setVisibility(View.VISIBLE);
        holder.indoor.setText(sp.indoor ? "Indoor" : "Outdoor");

        holder.reminders.setVisibility(View.VISIBLE);
        holder.reminders.setText(sp.getReminders ? "Reminders: on" : "Reminders: off");

        holder.lastWatered.setVisibility(View.VISIBLE);
        holder.lastWatered.setText("Last watered: " + DATE_FMT.format(new Date(sp.lastWateredMillis)));

        if (sp.skipNextWateringDueToRain) {
            holder.rainDefer.setVisibility(View.VISIBLE);
            holder.rainDefer.setText(R.string.rain_skip_next_cycle_note);
        } else {
            holder.rainDefer.setVisibility(View.GONE);
        }

        Glide.with(context).load(plant.getImage_url()).into(holder.image);
        holder.addToGarden.setVisibility(View.GONE);
        holder.editPlant.setVisibility(View.VISIBLE);
        holder.editPlant.setOnClickListener(v -> editListener.onEdit(sp));
        holder.waterPlant.setVisibility(View.VISIBLE);
        holder.waterPlant.setOnClickListener(v -> waterListener.onWater(sp));
    }

    @Override
    public int getItemCount() {
        return plants == null ? 0 : plants.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, watering, indoor, reminders, lastWatered, rainDefer;
        ImageView image;
        Button addToGarden;
        ImageButton editPlant, waterPlant;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.plant_name);
            watering = itemView.findViewById(R.id.plant_watering);
            indoor = itemView.findViewById(R.id.plant_indoor);
            reminders = itemView.findViewById(R.id.plant_reminders);
            lastWatered = itemView.findViewById(R.id.plant_last_watered);
            rainDefer = itemView.findViewById(R.id.plant_rain_defer);
            image = itemView.findViewById(R.id.plant_image);
            addToGarden = itemView.findViewById(R.id.btn_add_to_garden);
            editPlant = itemView.findViewById(R.id.btn_edit_plant);
            waterPlant = itemView.findViewById(R.id.btn_water_plant);
        }
    }
}
