package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    EditText searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBox = findViewById(R.id.pokeSearch);
    }

    public void searchBtn(View view) {

        String searchString = searchBox.getText().toString();

        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("Search", searchString);
        startActivity(intent);
    }
}