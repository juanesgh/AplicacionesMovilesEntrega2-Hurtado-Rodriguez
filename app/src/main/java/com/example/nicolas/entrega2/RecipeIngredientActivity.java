package com.example.nicolas.entrega2;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nicolas.entrega2.db.AppDatabase;
import com.example.nicolas.entrega2.db.Recipe;
import com.example.nicolas.entrega2.db.RecipeIngredient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecipeIngredientActivity extends AppCompatActivity {
    private AppDatabase db;
    private ListView lv;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_ingredient);
        db = AppDatabase.getDatabase(this);
        lv = (ListView) findViewById(R.id.List);
        final ArrayList<Integer> ids = (ArrayList<Integer>) getIntent().getSerializableExtra("ids");
        new Thread(new Runnable() {
            @Override
            public void run() {

                final List<Recipe> recipes = db.recipeDao().getAllRecipes();
                List<String> your_array_list = new ArrayList<String>();
                final List<Integer> RecipeSelect = new ArrayList<>();
                for (int i=0; i<recipes.size(); i++) {
                    Integer id = recipes.get(i).getId();
                    List<RecipeIngredient> recipeIngredients = db.recipeIngredientDao().fetchIngredientsByRecipeId(id);
                    ArrayList<Integer> recipeIds = new ArrayList<>();
                    for (int j=0; j<recipeIngredients.size(); j++) {
                        recipeIds.add(recipeIngredients.get(j).getIngredientId());
                    }
                    if (ids.containsAll(recipeIds)){
                        String aux = recipes.get(i).getName() + "\nReady to Cook!!";
                        your_array_list.add(aux);
                        RecipeSelect.add(id);
                    }
                    else {
                        for (int j=0; j<recipeIds.size(); j++) {
                            if (ids.contains(recipeIds.get(j))){
                                String aux = recipes.get(i).getName() + "\nMissing Ingredients!!";
                                your_array_list.add(aux);
                                RecipeSelect.add(id);
                                break;
                            }
                        }
                    }
                }
                arrayAdapter = new ArrayAdapter<String>(RecipeIngredientActivity.this,android.R.layout.simple_list_item_1,your_array_list );
                Handler mainHandler = new Handler(getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        lv.setAdapter(arrayAdapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(getApplicationContext(), DisplayRecipe.class);
                                intent.putExtra("ingredientIds", ids);
                                intent.putExtra("recipeId", String.valueOf(RecipeSelect.get(i)));
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        }) .start();
    }
}
