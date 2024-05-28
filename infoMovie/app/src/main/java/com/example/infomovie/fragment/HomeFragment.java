package com.example.infomovie.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infomovie.Adapter.MovieAdapter;
import com.example.infomovie.Api.ApiConfig;
import com.example.infomovie.Api.ApiServices;
import com.example.infomovie.Model.MovieModels;
import com.example.infomovie.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private ApiServices apiServices;
    private MovieAdapter movieAdapter;
    private MovieAdapter searchAdapter;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewSearchResults;
    private SearchView searchView;
    private TextView tvNoData;
    private Context context;
    private ArrayList<MovieModels> movieModels = new ArrayList<>();
    private ArrayList<MovieModels> searchResults = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerViewSearchResults = view.findViewById(R.id.recyclerViewSearchResults);
        searchView = view.findViewById(R.id.search_movie);
        tvNoData = view.findViewById(R.id.tvNoData);
        context = getContext();

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewSearchResults.setLayoutManager(new LinearLayoutManager(context));

        apiServices = ApiConfig.getClient().create(ApiServices.class);

        movieAdapter = new MovieAdapter(movieModels, context);
        searchAdapter = new MovieAdapter(searchResults, context);
        recyclerView.setAdapter(movieAdapter);
        recyclerViewSearchResults.setAdapter(searchAdapter);

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    recyclerView.setVisibility(View.GONE);
                    recyclerViewSearchResults.setVisibility(View.VISIBLE);
                    searchResults.clear();
                    searchAdapter.notifyDataSetChanged();
                    tvNoData.setVisibility(View.GONE);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchMovies(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerViewSearchResults.setVisibility(View.GONE);
                    tvNoData.setVisibility(View.GONE);
                } else {
                    searchMovies(newText);
                }
                return false;
            }
        });

        fetchDataFromApi();

        return view;
    }

    private void fetchDataFromApi() {
        Call<List<MovieModels>> call = apiServices.getMovie();
        call.enqueue(new Callback<List<MovieModels>>() {
            @Override
            public void onResponse(Call<List<MovieModels>> call, Response<List<MovieModels>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movieModels.clear();
                    movieModels.addAll(response.body());
                    movieAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MovieModels>> call, Throwable t) {
                Toast.makeText(context, "Request failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchMovies(String query) {
        // Filter the existing list based on the query
        ArrayList<MovieModels> filteredList = new ArrayList<>();
        for (MovieModels movie : movieModels) {
            if (movie.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(movie);
            }
        }
        if (!filteredList.isEmpty()) {
            searchResults.clear();
            searchResults.addAll(filteredList);
            searchAdapter.notifyDataSetChanged();

            recyclerView.setVisibility(View.GONE);
            recyclerViewSearchResults.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.GONE);
        } else {
            searchResults.clear();
            searchAdapter.notifyDataSetChanged();
            recyclerViewSearchResults.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }
    }
}
