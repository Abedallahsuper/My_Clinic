package com.example.myclinic.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myclinic.R;
import com.example.myclinic.db.entities.Doctor;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.Holder> {

    private List<Doctor> doctors = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener { void onItemClick(Doctor doctor); }
    public void setOnItemClickListener(OnItemClickListener l){ this.listener = l; }

    // Constructor يقبل قائمة عند الإنشاء
    public DoctorAdapter(List<Doctor> list){
        this.doctors = list != null ? list : new ArrayList<>();
    }

    public void setDoctors(List<Doctor> list){
        this.doctors = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Doctor d = doctors.get(position);

        holder.name.setText(d.getName());
        holder.category.setText(d.getCategory());

        if (d.getImageUri() != null && !d.getImageUri().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(d.getImageUri())
                    .placeholder(R.drawable.doctor)
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.doctor);
        }

        holder.itemView.setOnClickListener(v -> {
            if(listener != null) listener.onItemClick(d);
        });
    }

    @Override
    public int getItemCount() { return doctors.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        TextView name, category;
        ImageView image;
        Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvDoctorName);
            category = itemView.findViewById(R.id.tvDoctorSpecialty);
            image = itemView.findViewById(R.id.imgDoctor);
        }
    }

}

