package com.example.bai5firebase.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bai5firebase.R;
import com.example.bai5firebase.models.Seat;

import java.util.List;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {

    private List<Seat> seatList;
    private OnSeatClickListener listener;

    public interface OnSeatClickListener {
        void onSeatClick(Seat seat);
    }

    public SeatAdapter(List<Seat> seatList, OnSeatClickListener listener) {
        this.seatList = seatList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seat, parent, false);
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        Seat seat = seatList.get(position);
        holder.tvSeatName.setText(seat.getName());

        if (seat.isBooked()) {
            holder.cardSeat.setCardBackgroundColor(Color.GRAY);
            holder.itemView.setEnabled(false);
        } else if (seat.isSelected()) {
            holder.cardSeat.setCardBackgroundColor(Color.GREEN);
        } else {
            holder.cardSeat.setCardBackgroundColor(Color.LTGRAY);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSeatClick(seat);
            }
        });
    }

    @Override
    public int getItemCount() {
        return seatList.size();
    }

    static class SeatViewHolder extends RecyclerView.ViewHolder {
        CardView cardSeat;
        TextView tvSeatName;

        public SeatViewHolder(@NonNull View itemView) {
            super(itemView);
            cardSeat = itemView.findViewById(R.id.cardSeat);
            tvSeatName = itemView.findViewById(R.id.tvSeatName);
        }
    }
}
