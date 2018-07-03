package com.example.nicolas.entrega2;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicolas.entrega2.db.AppDatabase;
import com.example.nicolas.entrega2.db.Ingredient;
import com.example.nicolas.entrega2.db.Recipe;
import com.example.nicolas.entrega2.db.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

public class DisplayRecipe extends AppCompatActivity {
    private AppDatabase db;
    private TextView tvT;
    private TextView tvD;
    private TextView tvA;
    private TextView tvM;
    private TextView tvS;
    private TextView tvPT;
    private TextView tvN;
    private Button fB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe);
        db = AppDatabase.getDatabase(this);
        tvT = (TextView) findViewById(R.id.tvTitle);
        tvD = (TextView) findViewById(R.id.tvDescription);
        tvA = (TextView) findViewById(R.id.tvAvailableIngredients);
        tvM = (TextView) findViewById(R.id.tvMissingIngredients);
        tvS = (TextView) findViewById(R.id.tvServings);
        tvPT = (TextView) findViewById(R.id.tvPreparationTime);
        tvN = (TextView) findViewById(R.id.tvNationality);
        fB = (Button) findViewById(R.id.favoriteButton);

        final ArrayList<Integer> ids = (ArrayList<Integer>) getIntent().getSerializableExtra("ingredientIds");
        final String recipeId = (String) getIntent().getStringExtra("recipeId");
        new Thread(new Runnable() {
            @Override
            public void run() {

                final Recipe recipe = db.recipeDao().fetchOneRecipebyId(Integer.valueOf(recipeId));
                final String title = recipe.getName();
                final String description = recipe.getDescription();
                Integer servings = recipe.getServings();
                final String servingsText = "Servings: " + String.valueOf(servings);
                Integer preparationTime = recipe.getPreparation_time();
                final String preparationText = "Preparation Time: " + String.valueOf(preparationTime) + " minutes.";
                final String nationality = "Nationality: " + recipe.getNationality();

                List<RecipeIngredient> recipeIngredients = db.recipeIngredientDao().fetchIngredientsByRecipeId(Integer.valueOf(recipeId));
                final StringBuilder available = new StringBuilder();
                final StringBuilder missing = new StringBuilder();
                for (int i=0; i<recipeIngredients.size(); i++) {
                    if (ids.contains(recipeIngredients.get(i).getIngredientId())){
                        Ingredient auxi = db.ingredientDao().fetchOneIngredientbyId(recipeIngredients.get(i).getIngredientId());
                        available.append("-"+auxi.getName() + "\n");
                    }
                    else {
                        Ingredient auxi = db.ingredientDao().fetchOneIngredientbyId(recipeIngredients.get(i).getIngredientId());
                        missing.append("-"+auxi.getName() + "\n");
                    }
                }

                final Boolean fav = recipe.getFavorite();

                Handler mainHandler = new Handler(getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        tvT.setText(title);
                        tvD.setText(description);
                        tvA.setText(available);
                        tvM.setText(missing);
                        tvS.setText(servingsText);
                        tvPT.setText(preparationText);
                        if (recipe.getNationality() == null){
                            tvN.setVisibility(View.GONE);
                        }
                        else {
                            tvN.setText(nationality);
                        }

                        if (fav){
                            fB.setText("Remove from Favorites");
                        }
                    }
                });
            }
        }) .start();

        fB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        final Recipe recipe = db.recipeDao().fetchOneRecipebyId(Integer.valueOf(recipeId));
                        final Boolean favAttribute = recipe.getFavorite();
                        if (favAttribute){
                            db.recipeDao().update(0,Integer.valueOf(recipeId));
                        }
                        else {
                            db.recipeDao().update(1,Integer.valueOf(recipeId));
                        }

                        Handler mainHandler = new Handler(getMainLooper());
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (favAttribute){
                                    fB.setText("Add to Favorites");
                                    Toast.makeText(getApplicationContext(), "Recipe removed from Favorites." ,
                                            Toast.LENGTH_LONG).show();
                                }
                                else {
                                    fB.setText("Remove from Favorites");
                                    Toast.makeText(getApplicationContext(), "Recipe added to Favorites." ,
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }) .start();
            }
        });
    }
}
