package com.example.infomovie.fragment;


import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.infomovie.IntroActivity;
import com.example.infomovie.LoginActivity;
import com.example.infomovie.R;
import com.example.infomovie.Sqlite.DatabaseHelper;

public class ProfileFragment extends Fragment {

    Button btn_logout;
    DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseHelper = new DatabaseHelper(getContext());

        btn_logout = view.findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(v -> {
            logoutUser();
        });
    }

    private void logoutUser() {
        try (SQLiteDatabase db = databaseHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_IS_LOGGED_IN, 0);
            db.update(DatabaseHelper.TABLE_NAME, values, DatabaseHelper.COLUMN_IS_LOGGED_IN + " = ?", new String[]{"1"});
        }

        startActivity(new Intent(getActivity(), IntroActivity.class));
        getActivity().finish();
    }
}