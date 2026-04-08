package com.example.bai5firebase.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bai5firebase.R;
import com.example.bai5firebase.models.Showtime;

import java.util.List;

public class ShowtimeAdapter extends RecyclerView.Adapter<ShowtimeAdapter.ShowtimeViewHolder> {

    private List<Showtime> showtimeList;
    private OnShowtimeClickListener listener;

    public interface OnShowtimeClickListener {
        void onShowtimeClick(Showtime showtime);
    }

    public ShowtimeAdapter(List<Showtime> showtimeList, OnShowtimeClickListener listener) {
        this.showtimeList = showtimeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShowtimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_showtime, parent, false);
        return new ShowtimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowtimeViewHolder holder, int position) {
        Showtime showtime = showtimeList.get(position);
        holder.tvStartTime.setText(showtime.getStartTime());
        holder.tvPrice.setText(String.format("%,.0fđ", showtime.getPrice()));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onShowtimeClick(showtime);
            }
        });
    }

    @Override
    public int getItemCount() {
        return showtimeList.size();
    }

    static class ShowtimeViewHolder extends RecyclerView.ViewHolder {
        TextView tvStartTime, tvPrice;

        public ShowtimeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStartTime = itemView.findViewById(R.id.tvStartTime);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
