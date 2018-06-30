package com.example.nicolas.entrega2;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.nicolas.entrega2.db.AppDatabase;
import com.example.nicolas.entrega2.db.Ingredient;
import com.example.nicolas.entrega2.db.Recipe;
import com.example.nicolas.entrega2.db.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

public class DisplayRecipeRegular extends AppCompatActivity {
    private AppDatabase db;
    private TextView tvT;
    private TextView tvD;
    private TextView tvI;
    private TextView tvS;
    private TextView tvPT;
    private TextView tvN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe_regular);
        db = AppDatabase.getDatabase(this);
        tvT = (TextView) findViewById(R.id.tvTitle);
        tvD = (TextView) findViewById(R.id.tvDescription);
        tvI = (TextView) findViewById(R.id.tvIngredients);
        tvS = (TextView) findViewById(R.id.tvServings);
        tvPT = (TextView) findViewById(R.id.tvPreparationTime);
        tvN = (TextView) findViewById(R.id.tvNationality);

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
                final StringBuilder ingredients = new StringBuilder();
                for (int i=0; i<recipeIngredients.size(); i++) {
                    Ingredient auxi = db.ingredientDao().fetchOneIngredientbyId(recipeIngredients.get(i).getIngredientId());
                    ingredients.append("-"+auxi.getName() + "\n");
                }

                Handler mainHandler = new Handler(getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        tvT.setText(title);
                        tvD.setText(description);
                        tvI.setText(ingredients);
                        tvS.setText(servingsText);
                        tvPT.setText(preparationText);
                        if (recipe.getNationality() == null){
                            tvN.setVisibility(View.GONE);
                        }
                        else {
                            tvN.setText(nationality);
                        }
                    }
                });
            }
        }) .start();
    }
}