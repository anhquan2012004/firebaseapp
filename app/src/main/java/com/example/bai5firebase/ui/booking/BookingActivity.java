package com.example.bai5firebase.ui.booking;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bai5firebase.databinding.ActivityBookingBinding;
import com.example.bai5firebase.models.Showtime;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BookingActivity extends AppCompatActivity {

    private ActivityBookingBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String showtimeId;
    private String movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        showtimeId = getIntent().getStringExtra("showtimeId");
        movieId = getIntent().getStringExtra("movieId");

        if (showtimeId != null) {
            fetchShowtimeDetails();
        }

        binding.btnConfirmBooking.setOnClickListener(v -> confirmBooking());
    }

    private void fetchShowtimeDetails() {
        db.collection("showtimes").document(showtimeId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Showtime showtime = documentSnapshot.toObject(Showtime.class);
                        if (showtime != null) {
                            binding.tvShowtimeInfo.setText("Suất chiếu: " + showtime.getStartTime());
                            binding.tvPriceInfo.setText(String.format("Giá vé: %,.0fđ", showtime.getPrice()));
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e("BookingActivity", "Error fetching showtime", e));
    }

    private void confirmBooking() {
        String seatNumber = binding.etSeatNumber.getText().toString().trim();

        if (TextUtils.isEmpty(seatNumber)) {
            binding.etSeatNumber.setError("Vui lòng nhập số ghế");
            return;
        }

        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Vui lòng đăng nhập để đặt vé", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.btnConfirmBooking.setEnabled(false);
        String uid = mAuth.getCurrentUser().getUid();
        String ticketId = UUID.randomUUID().toString();

        // Tạo dữ liệu vé
        Map<String, Object> ticket = new HashMap<>();
        ticket.put("ticketId", ticketId);
        ticket.put("uid", uid);
        ticket.put("showtimeId", showtimeId);
        ticket.put("movieId", movieId);
        ticket.put("seatNumber", seatNumber);
        ticket.put("bookingTime", FieldValue.serverTimestamp());
        ticket.put("status", "booked");

        // Lưu vào Firestore
        db.collection("tickets").document(ticketId)
                .set(ticket)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(BookingActivity.this, "Đặt vé thành công!", Toast.LENGTH_LONG).show();
                    finish(); // Quay lại màn hình trước đó
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(BookingActivity.this, "Lỗi đặt vé: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.btnConfirmBooking.setEnabled(true);
                });
    }
}
