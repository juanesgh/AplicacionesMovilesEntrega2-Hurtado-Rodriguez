package com.example.nicolas.entrega2;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nicolas.entrega2.db.AppDatabase;
import com.example.nicolas.entrega2.db.Recipe;

import java.util.ArrayList;
import java.util.List;

public class SearchRecipeByNationality extends AppCompatActivity {
    private AppDatabase db;
    private Spinner sp;
    private ListView lv;

    private ArrayList<String> namesList;
    private List<Integer> idsList;

    private String searchNation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe_by_nationality);
        db = AppDatabase.getDatabase(getApplicationContext());

        sp = (Spinner) findViewById(R.id.spinnerNations);
        lv = (ListView) findViewById(R.id.list_view);

        namesList = new ArrayList<>();
        idsList = new ArrayList<>();

        fillSpinner();
        onChangeEvent();
    }

    private void fillSpinner() {
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Nationality");
        spinnerArray.add("Italian");
        spinnerArray.add("Chinese");
        spinnerArray.add("French");
        spinnerArray.add("Spanish");

        ArrayAdapter<String> spadapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        spadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(spadapter);
    }

    private void onChangeEvent() {
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                final String spSelected = sp.getSelectedItem().toString();
                if (!"Nationality".equals(spSelected)){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //Consulta a la base de datos.
                            final List<Recipe> recipes = db.recipeDao().fetchRecipesbyNationality(spSelected);

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
                                        Toast.makeText(getApplicationContext(), "Couldn't find any Recipes that matched the selected Nationality." ,
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }).start();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }
}
