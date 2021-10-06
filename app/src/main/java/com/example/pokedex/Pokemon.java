package com.example.pokedex;

import android.util.Log;

import java.util.ArrayList;

public class Pokemon {
    String name;
    ArrayList<String> types = new ArrayList<>();
    String species;
    String height;
    String weight;
    ArrayList<String> abilities = new ArrayList<>();
    String imageURL;

    void printPokemon() {
        Log.d("Pokemon", name + "\n" + types + "\n" + species + "\n" + height + "\n" + weight + "\n" + abilities + "\n" + imageURL);

    }
}
