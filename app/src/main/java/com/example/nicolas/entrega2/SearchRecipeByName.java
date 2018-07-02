package com.example.nicolas.entrega2;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nicolas.entrega2.db.AppDatabase;
import com.example.nicolas.entrega2.db.Ingredient;
import com.example.nicolas.entrega2.db.Recipe;

import java.util.ArrayList;
import java.util.List;

public class SearchRecipeByName extends AppCompatActivity {
    private AppDatabase db;
    private EditText etS;
    private ListView lv;

    private ArrayList<String> namesList;
    private List<Integer> idsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe_by_name);
        db = AppDatabase.getDatabase(getApplicationContext());

        etS = (EditText) findViewById(R.id.editTextSearch);
        lv = (ListView) findViewById(R.id.list_view);

        namesList = new ArrayList<>();
        idsList = new ArrayList<>();

        onClickEvent();
    }

    private void onClickEvent() {
        findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchTitle = etS.getText().toString();
                searchRecipes(searchTitle);
            }
        });
    }

    private void searchRecipes(String s) {
        final String titleSearch = s;
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Consulta a la base de datos.
                final List<Recipe> recipes = db.recipeDao().fetchRecipesbyTitle('%'+titleSearch+'%');

                if (recipes.size() > 0) {
                    namesList.clear();
                    idsList.clear();
                    for (int i = 0; i < recipes.size(); i++) {
                        String aux = recipes.get(i).getName();
                        Integer auxi = recipes.get(i).getId();
                        namesList.add(aux);
                        idsList.add(auxi);
                    }
                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                            (getBaseContext(), android.R.layout.select_dialog_item, namesList);

                    Handler mainHandler = new Handler(getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            lv.setAdapter(adapter);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(getApplicationContext(), DisplayRecipeRegular.class);
                                    intent.putExtra("recipeId", String.valueOf(idsList.get(i)));
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                }
                else {
                    Handler mainHandler = new Handler(getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Couldn't find any Recipe Titles that matched: " + titleSearch ,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }
}
