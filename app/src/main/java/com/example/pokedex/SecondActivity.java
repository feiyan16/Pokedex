package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.cronet.CronetHttpStack;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SecondActivity extends AppCompatActivity {

    Pokemon pokemon = new Pokemon();
    TextView pokeName, pokeType1, pokeType2, pokeSpecies, pokeHeight, pokeWeight, pokeAbilities;
    ImageView pokeImage;
    ScrollView background;
    String poke = "bulbasaur";
    String url = "https://pokeapi.glitch.me/v1/pokemon/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        setAttributes();

        Intent intent = getIntent();
        if (intent != null) {
            String extra = intent.getStringExtra("Search");
            if (extra != "") {
                poke = extra;
            }
        }

        url += poke;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    parseJson(response);
                    setPokemon();
                    pokemon.printPokemon();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response", "Connection failed");
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    public void setPokemon() {
        pokeName.setText(pokemon.name);
        pokeHeight.setText(pokemon.height);
        pokeWeight.setText(pokemon.weight);
        pokeSpecies.setText(pokemon.species);
        String abilities = "";
        for (int i = 0; i < pokemon.abilities.size(); i++) {
            abilities += (pokemon.abilities.get(i) + (i == pokemon.abilities.size() - 1 ? "" : ", "));
        }
        pokeAbilities.setText(abilities);
        pokeType1.setText(pokemon.types.get(0));

        if (pokeType1.getText() == "Grass") {
            background.setBackground(ContextCompat.getDrawable(this, R.drawable.background_grass));
        } else if (pokemon.types.get(0).contains("Electric")) {
            background.setBackgroundResource(R.drawable.background_electric);
        } else if (pokemon.types.get(0).contains("Water")) {
            background.setBackground(ContextCompat.getDrawable(this, R.drawable.background_water));
        } else if (pokemon.types.get(0).contains("Fire")) {
            background.setBackground(ContextCompat.getDrawable(this, R.drawable.background_fire));
        }

        if (pokemon.types.size() > 1) {
            pokeType2.setVisibility(View.VISIBLE);
            pokeType2.setText(pokemon.types.get(1));
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        ImageRequest imageRequest = new ImageRequest(pokemon.imageURL, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                pokeImage.setImageBitmap(response);
            }
        }, 200, 200, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "Image request failed");
            }
        });
        requestQueue.add(imageRequest);
    }

    public void parseJson(JSONArray jsonArray) throws JSONException {
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        pokemon.name = jsonObject.getString("name");
        pokemon.height = jsonObject.getString("height");
        pokemon.weight = jsonObject.getString("weight");
        pokemon.species = jsonObject.getString("species");
        JSONArray types = jsonObject.getJSONArray("types");
        for(int i = 0; i < types.length(); i++) {
            pokemon.types.add(types.getString(i));
        }
        JSONObject abilities = jsonObject.getJSONObject("abilities");
        JSONArray normal = abilities.getJSONArray("normal");
        JSONArray hidden = abilities.getJSONArray("hidden");
        for(int i = 0; i < normal.length(); i++) {
            pokemon.abilities.add(normal.getString(i));
        }
        for(int i = 0; i < hidden.length(); i++) {
            pokemon.abilities.add(hidden.getString(i));
        }
        pokemon.imageURL = jsonObject.getString("sprite");
    }

    public void setAttributes() {
        background = findViewById(R.id.background);
        pokeName = findViewById(R.id.pokeName);
        pokeType1 = findViewById(R.id.pokeType1);
        pokeType2 = findViewById(R.id.pokeType2);
        pokeSpecies = findViewById(R.id.species);
        pokeHeight = findViewById(R.id.height);
        pokeWeight = findViewById(R.id.weight);
        pokeAbilities = findViewById(R.id.abilities);
        pokeImage = findViewById(R.id.pokeImg);
    }

    public void goBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}