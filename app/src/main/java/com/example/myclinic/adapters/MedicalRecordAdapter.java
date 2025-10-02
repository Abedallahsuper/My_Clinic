package com.example.myclinic.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myclinic.R;
import com.example.myclinic.db.entities.MedicalRecord;

import java.util.ArrayList;
import java.util.List;

public class MedicalRecordAdapter extends RecyclerView.Adapter<MedicalRecordAdapter.Holder> {

    private List<MedicalRecord> list = new ArrayList<>();

    // Constructor يقبل قائمة بيانات
    public MedicalRecordAdapter(List<MedicalRecord> items) {
        this.list = items != null ? items : new ArrayList<>();
    }

    // دالة لتحديث البيانات لاحقًا
    public void setRecords(List<MedicalRecord> items) {
        this.list = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medical_record, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        MedicalRecord r = list.get(position);
        holder.title.setText(r.diagnosis != null ? r.diagnosis : "تشخيص");
        holder.date.setText(String.valueOf(r.visitDate));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView title, date;

        Holder(@NonNull View v) {
            super(v);
            title = v.findViewById(R.id.txtRecordTitle);
            date = v.findViewById(R.id.txtRecordDate);
        }
    }
}
