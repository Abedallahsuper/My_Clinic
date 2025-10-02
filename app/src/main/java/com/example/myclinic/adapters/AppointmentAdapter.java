package com.example.myclinic.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myclinic.R;
import com.example.myclinic.db.entities.Appointment;
import com.example.myclinic.db.entities.Doctor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.Holder> {

    private List<Appointment> list = new ArrayList<>();
    private List<Doctor> doctors = new ArrayList<>();

    // واجهة للتعامل مع زر الإلغاء فقط
    public interface OnItemClickListener {
        void onCancelClick(Appointment appointment, int position);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener l) {
        this.listener = l;
    }

    public AppointmentAdapter(List<Appointment> items, List<Doctor> doctorsList){
        this.list = items != null ? items : new ArrayList<>();
        this.doctors = doctorsList != null ? doctorsList : new ArrayList<>();
    }

    public void setAppointments(List<Appointment> items){
        this.list = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setDoctors(List<Doctor> doctorsList){
        this.doctors = doctorsList != null ? doctorsList : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void updateAppointmentAt(int position, Appointment appointment) {
        if(position >= 0 && position < list.size()) {
            list.set(position, appointment);
            notifyItemChanged(position);
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Appointment a = list.get(position);

        // الاسم الافتراضي للطبيب والتخصص
        String doctorName = "طبيب غير محدد";
        String specialty = "-";
        for (Doctor d : doctors){
            if (d.getId() == a.doctorId){
                doctorName = d.getName();
                specialty = d.getCategory();
                break;
            }
        }

        holder.tvDoctorName.setText(doctorName);
        holder.tvSpecialty.setText(specialty);

        // تحويل timestamp إلى تاريخ ووقت
        Date date = a.timestamp != null ? a.timestamp : new Date();
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", Locale.getDefault());

        holder.tvDate.setText(sdfDate.format(date));
        holder.tvTime.setText(" | " + sdfTime.format(date));

        // الحالة
        holder.tvStatus.setText(a.status != null ? a.status : "قيد الانتظار");

        // زر الإلغاء فقط
        holder.btnCancel.setOnClickListener(v -> {
            if(listener != null) listener.onCancelClick(a, position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView tvDoctorName, tvSpecialty, tvDate, tvTime, tvStatus;
        Button btnCancel;

        Holder(@NonNull View v){
            super(v);
            tvDoctorName = v.findViewById(R.id.tvDoctorName);
            tvSpecialty = v.findViewById(R.id.tvSpecialty);
            tvDate = v.findViewById(R.id.tvDate);
            tvTime = v.findViewById(R.id.tvTime);
            tvStatus = v.findViewById(R.id.tvStatus);
            btnCancel = v.findViewById(R.id.btnCancel);
        }
    }
}
