package com.example.bai5firebase.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.bai5firebase.adapters.ShowtimeAdapter;
import com.example.bai5firebase.databinding.ActivityMovieDetailBinding;
import com.example.bai5firebase.models.Movie;
import com.example.bai5firebase.models.Showtime;
import com.example.bai5firebase.ui.booking.BookingActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {

    private ActivityMovieDetailBinding binding;
    private FirebaseFirestore db;
    private ShowtimeAdapter adapter;
    private List<Showtime> showtimeList;
    private String movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        showtimeList = new ArrayList<>();
        movieId = getIntent().getStringExtra("movieId");

        if (movieId != null) {
            fetchMovieDetails();
            setupShowtimeRecyclerView();
            fetchShowtimes();
        } else {
            Toast.makeText(this, "Không tìm thấy ID phim", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void fetchMovieDetails() {
        db.collection("movies").document(movieId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Movie movie = documentSnapshot.toObject(Movie.class);
                        if (movie != null) {
                            displayMovieDetails(movie);
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e("MovieDetail", "Error fetching movie", e));
    }

    private void displayMovieDetails(Movie movie) {
        binding.tvMovieTitle.setText(movie.getTitle());
        binding.tvMovieDescription.setText(movie.getDescription());
        binding.tvMovieDuration.setText("Thời lượng: " + movie.getDuration() + " phút");

        Glide.with(this)
                .load(movie.getPosterUrl())
                .into(binding.ivMoviePoster);
    }

    private void setupShowtimeRecyclerView() {
        binding.rvShowtimes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new ShowtimeAdapter(showtimeList, showtime -> {
            // Chuyển sang BookingActivity (F4)
            Intent intent = new Intent(MovieDetailActivity.this, BookingActivity.class);
            intent.putExtra("movieId", movieId);
            intent.putExtra("showtimeId", showtime.getShowtimeId());
            startActivity(intent);
        });
        binding.rvShowtimes.setAdapter(adapter);
    }

    private void fetchShowtimes() {
        db.collection("showtimes")
                .whereEqualTo("movieId", movieId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showtimeList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Showtime showtime = document.toObject(Showtime.class);
                            showtime.setShowtimeId(document.getId());
                            showtimeList.add(showtime);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("MovieDetail", "Error fetching showtimes", task.getException());
                    }
                });
    }
}
