package com.example.infomovie;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import com.example.infomovie.Sqlite.DatabaseHelper;

public class RegistrasiActivity extends AppCompatActivity {

    EditText et_nim;
    EditText et_password;
    Button btn_register;
    DatabaseHelper databaseHelper;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        databaseHelper = new DatabaseHelper(this);

        et_nim = findViewById(R.id.et_username2);
        et_password = findViewById(R.id.et_password2);
        btn_register = findViewById(R.id.btn_register);

        btn_register.setOnClickListener(v -> {
            String nim = et_nim.getText().toString().trim();
            String password = et_password.getText().toString().trim();

            if (!nim.isEmpty() && !password.isEmpty()) {
                databaseHelper.insertData(nim, password);

                Toast.makeText(RegistrasiActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(RegistrasiActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}