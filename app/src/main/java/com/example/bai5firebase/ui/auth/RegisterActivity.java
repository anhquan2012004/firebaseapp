package com.example.bai5firebase.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bai5firebase.MainActivity;
import com.example.bai5firebase.databinding.ActivityRegisterBinding;
import com.example.bai5firebase.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        binding.btnRegister.setOnClickListener(v -> registerUser());
        binding.tvLogin.setOnClickListener(v -> finish());
    }

    private void registerUser() {
        String name = binding.etName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String phone = binding.etPhone.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            binding.etName.setError("Tên không được để trống");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            binding.etEmail.setError("Email không được để trống");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            binding.etPhone.setError("Số điện thoại không được để trống");
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            binding.etPassword.setError("Mật khẩu phải ít nhất 6 ký tự");
            return;
        }

        // Vô hiệu hóa nút để tránh bấm nhiều lần
        binding.btnRegister.setEnabled(false);
        Toast.makeText(this, "Đang xử lý đăng ký...", Toast.LENGTH_SHORT).show();

        // 1. Tạo tài khoản trong Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        Log.d(TAG, "Auth thành công, UID: " + uid);
                        saveUserToFirestore(uid, name, email, phone);
                    } else {
                        binding.btnRegister.setEnabled(true);
                        String errorMsg = "Đăng ký thất bại";
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            errorMsg = "Email này đã được sử dụng!";
                        } else if (task.getException() != null) {
                            errorMsg = task.getException().getMessage();
                        }
                        Log.e(TAG, "Lỗi Auth: " + errorMsg);
                        Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveUserToFirestore(String uid, String name, String email, String phone) {
        User user = new User(uid, name, email, phone, "customer");

        // 2. Lưu thông tin User vào Firestore
        db.collection("users").document(uid)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Lưu Firestore thành công");
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    binding.btnRegister.setEnabled(true);
                    Log.e(TAG, "Lỗi Firestore: " + e.getMessage());
                    Toast.makeText(RegisterActivity.this, "Lỗi lưu dữ liệu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}
