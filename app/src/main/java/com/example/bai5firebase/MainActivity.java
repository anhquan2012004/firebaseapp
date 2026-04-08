package com.example.bai5firebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.bai5firebase.adapters.MovieAdapter;
import com.example.bai5firebase.databinding.ActivityMainBinding;
import com.example.bai5firebase.models.Movie;
import com.example.bai5firebase.models.Showtime;
import com.example.bai5firebase.ui.auth.LoginActivity;
import com.example.bai5firebase.ui.detail.MovieDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseFirestore db;
    private MovieAdapter adapter;
    private List<Movie> movieList;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mAuth = FirebaseAuth.getInstance();
        
        // 1. KIỂM TRA ĐĂNG NHẬP: Nếu chưa đăng nhập thì chuyển về Login ngay
        if (mAuth.getCurrentUser() == null) {
            goToLogin();
            return;
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        movieList = new ArrayList<>();

        setupRecyclerView();
        fetchMovies();
        
        // 2. KÍCH HOẠT NÚT ĐĂNG XUẤT
        setupLogout();
    }

    private void setupLogout() {
        binding.btnLogout.setOnClickListener(v -> {
            // Đăng xuất khỏi Firebase
            mAuth.signOut();
            Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
            // Chuyển về màn hình Login
            goToLogin();
        });
    }

    private void goToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        // Flag này giúp xóa sạch các Activity cũ, người dùng không thể nhấn "Back" để quay lại danh sách phim
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setupRecyclerView() {
        binding.rvMovies.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new MovieAdapter(movieList, movie -> {
            Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
            intent.putExtra("movieId", movie.getMovieId());
            startActivity(intent);
        });
        binding.rvMovies.setAdapter(adapter);
    }

    private void fetchMovies() {
        db.collection("movies").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                movieList.clear();
                if (task.getResult().isEmpty()) {
                    seedData();
                } else {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Movie movie = document.toObject(Movie.class);
                        movie.setMovieId(document.getId());
                        movieList.add(movie);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void seedData() {
        Movie movie = new Movie("", "Avengers: Endgame", 
                "Sau cú búng tay của Thanos, vũ trụ tan hoang. Các Avengers phải tập hợp lại để đảo ngược tình thế.", 
                "https://m.media-amazon.com/images/I/91EmWoPK3+L._AC_UF894,1000_QL80_.jpg", "", 181, "2019-04-26");

        db.collection("movies").add(movie).addOnSuccessListener(doc -> {
            String mid = doc.getId();
            db.collection("showtimes").add(new Showtime("", mid, "Rạp 1", "09:00", "12:00", 75000));
            db.collection("showtimes").add(new Showtime("", mid, "Rạp 1", "14:30", "17:30", 90000));
            db.collection("showtimes").add(new Showtime("", mid, "Rạp 2", "20:00", "23:00", 110000));
            
            Toast.makeText(this, "Đã khởi tạo dữ liệu mẫu!", Toast.LENGTH_SHORT).show();
            fetchMovies();
        });
    }
}
