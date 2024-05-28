package com.example.infomovie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.infomovie.Api.ApiConfig;
import com.example.infomovie.Api.ApiServices;
import com.example.infomovie.Model.DetailModel;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailActivity extends AppCompatActivity {

    private String movieId;
    private ImageView iv_photo;
    private ImageView iv_back;
    private ImageView iv_fav;
    private TextView tv_ratingdet;
    private TextView tv_yeardet;
    private TextView tv_titledet;
    private TextView tv_desc;
    private ProgressBar progressBar;
    private TextView textdesc;
    private ImageView imgstar;
    private Handler handler = new Handler(Looper.getMainLooper());

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        iv_photo = findViewById(R.id.iv_photo);
        iv_back = findViewById(R.id.iv_back);
        iv_fav = findViewById(R.id.iv_fav);
        tv_ratingdet = findViewById(R.id.tv_ratingdet);
        tv_yeardet = findViewById(R.id.tv_yeardet);
        tv_titledet = findViewById(R.id.tv_titledet);
        tv_desc = findViewById(R.id.tv_desc);
        progressBar = findViewById(R.id.progresbarDetail);
        textdesc = findViewById(R.id.textDesc);
        imgstar = findViewById(R.id.imgstar);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("movieId")) {
            movieId = intent.getStringExtra("movieId");
            if (movieId != null) {
                getMovieDetails(movieId);
            } else {
                Toast.makeText(this, "Invalid Movie ID", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Movie ID not provided", Toast.LENGTH_SHORT).show();
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getMovieDetails(String movieId) {
        Retrofit retrofit = ApiConfig.getClient();
        ApiServices apiServices = retrofit.create(ApiServices.class);
        progressBar.setVisibility(View.VISIBLE);

        // Sembunyikan elemen lain saat ProgressBar muncul
        iv_photo.setVisibility(View.GONE);
        iv_back.setVisibility(View.GONE);
        iv_fav.setVisibility(View.GONE);
        tv_ratingdet.setVisibility(View.GONE);
        tv_yeardet.setVisibility(View.GONE);
        tv_titledet.setVisibility(View.GONE);
        tv_desc.setVisibility(View.GONE);
        textdesc.setVisibility(View.GONE);
        imgstar.setVisibility(View.GONE);

        Call<DetailModel> call = apiServices.getMovieById(movieId);
        call.enqueue(new Callback<DetailModel>() {
            @Override
            public void onResponse(Call<DetailModel> call, Response<DetailModel> response) {
                handler.post(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        DetailModel detailModel = response.body();

                        tv_titledet.setText(detailModel.getTitle());
                        tv_yeardet.setText(String.valueOf(detailModel.getYear()));
                        tv_ratingdet.setText(detailModel.getRating());
                        tv_desc.setText(detailModel.getDescription());

                        // Muat gambar menggunakan Picasso
                        Picasso.get().load(detailModel.getBig_image()).into(iv_photo);

                        // Tampilkan elemen lain saat ProgressBar disembunyikan
                        iv_photo.setVisibility(View.VISIBLE);
                        iv_back.setVisibility(View.VISIBLE);
                        iv_fav.setVisibility(View.VISIBLE);
                        tv_ratingdet.setVisibility(View.VISIBLE);
                        tv_yeardet.setVisibility(View.VISIBLE);
                        tv_titledet.setVisibility(View.VISIBLE);
                        tv_desc.setVisibility(View.VISIBLE);
                        textdesc.setVisibility(View.VISIBLE);
                        imgstar.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(DetailActivity.this, "Failed to retrieve movie details", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                });
            }

            @Override
            public void onFailure(Call<DetailModel> call, Throwable t) {
                handler.post(() -> {
                    Toast.makeText(DetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                    // Pastikan elemen lain tampil kembali meskipun panggilan API gagal
                    iv_photo.setVisibility(View.VISIBLE);
                    iv_back.setVisibility(View.VISIBLE);
                    iv_fav.setVisibility(View.VISIBLE);
                    tv_ratingdet.setVisibility(View.VISIBLE);
                    tv_yeardet.setVisibility(View.VISIBLE);
                    tv_titledet.setVisibility(View.VISIBLE);
                    tv_desc.setVisibility(View.VISIBLE);
                    textdesc.setVisibility(View.VISIBLE);
                    imgstar.setVisibility(View.VISIBLE);
                });
            }
        });
    }
}
