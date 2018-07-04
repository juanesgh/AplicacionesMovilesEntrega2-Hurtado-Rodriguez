package com.example.nicolas.entrega2;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicolas.entrega2.db.AppDatabase;
import com.example.nicolas.entrega2.db.Ingredient;
import com.example.nicolas.entrega2.db.Recipe;
import com.example.nicolas.entrega2.db.RecipeIngredient;

import java.io.File;
import java.math.BigDecimal;
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
    private Button fB;
    private Button dB;
    private ImageView imV;
    private Boolean favoriteAtt;
    private Recipe recipe;
    private String title;


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
        fB = (Button) findViewById(R.id.favoriteButton);
        dB = (Button) findViewById(R.id.deleteButton);
        imV = (ImageView) findViewById(R.id.image_view);

        final String recipeId = (String) getIntent().getStringExtra("recipeId");
        final String manage = (String) getIntent().getStringExtra("manage");
        if (!"1".equals(manage)){
            dB.setVisibility(View.GONE);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                recipe = db.recipeDao().fetchOneRecipebyId(Integer.valueOf(recipeId));
                title = recipe.getName();
                final String description = recipe.getDescription();
                Integer servings = recipe.getServings();
                final String servingsText = "Servings: " + String.valueOf(servings);
                Integer preparationTime = recipe.getPreparation_time();
                final String preparationText = "Preparation Time: " + String.valueOf(preparationTime) + " minutes.";
                final String nationality = "Nationality: " + recipe.getNationality();
                final String imgPath = recipe.getImage_path();
                favoriteAtt = recipe.getFavorite();
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

                        if (imgPath != null) {
                            File imgFile = new File(imgPath);

                            if (imgFile.exists()) {
                                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                imV.setImageBitmap(myBitmap);
                            }
                            else{
                                Context context = imV.getContext();
                                int id = context.getResources().getIdentifier(imgPath, "drawable", context.getPackageName());
                                if (id != 0) {
                                    imV.setImageResource(id);
                                }
                                else {
                                    imV.setVisibility(View.GONE);
                                }
                            }
                        }
                        else {
                            imV.setVisibility(View.GONE);
                        }

                        if (favoriteAtt){
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
                        if (favoriteAtt){
                            db.recipeDao().update(0,Integer.valueOf(recipeId));
                            favoriteAtt = false;
                        }
                        else {
                            db.recipeDao().update(1,Integer.valueOf(recipeId));
                            favoriteAtt = true;
                        }

                        Handler mainHandler = new Handler(getMainLooper());
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (favoriteAtt){
                                    fB.setText("Remove from Favorites");
                                    Toast.makeText(getApplicationContext(), "Recipe added to Favorites." ,
                                            Toast.LENGTH_LONG).show();
                                }
                                else {
                                    fB.setText("Add to Favorites");
                                    Toast.makeText(getApplicationContext(), "Recipe removed from Favorites." ,
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }) .start();
            }
        });

        dB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder altdial = new AlertDialog.Builder(DisplayRecipeRegular.this);
                altdial.setMessage("Are you sure you want to delete the recipe: '"+title+"'?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        db.recipeDao().deleteRecipe(recipe);
                                        Handler mainHandler = new Handler(getMainLooper());
                                        mainHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                            }
                                        });
                                    }
                                }).start();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog alert = altdial.create();
                alert.setTitle("Delete Recipe?");
                alert.show();
            }
        });
    }
}
