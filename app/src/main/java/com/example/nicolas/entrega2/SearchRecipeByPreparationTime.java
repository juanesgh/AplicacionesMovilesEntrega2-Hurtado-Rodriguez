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

public class SearchRecipeByPreparationTime extends AppCompatActivity {
    private AppDatabase db;
    private Spinner sp;
    private ListView lv;

    private ArrayList<String> namesList;
    private List<Integer> idsList;

    private Integer low;
    private Integer high;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe_by_preparation_time);
        db = AppDatabase.getDatabase(getApplicationContext());

        sp = (Spinner) findViewById(R.id.spinnerTime);
        lv = (ListView) findViewById(R.id.list_view);

        namesList = new ArrayList<>();
        idsList = new ArrayList<>();

        fillSpinner();
        onChangeEvent();
    }

    private void fillSpinner() {
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Length");
        spinnerArray.add("<30 mins");
        spinnerArray.add("30-60 mins");
        spinnerArray.add("1-2 hrs");
        spinnerArray.add(">2 hrs");

        ArrayAdapter<String> spadapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        spadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(spadapter);
    }

    private void onChangeEvent() {
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String spSelected = sp.getSelectedItem().toString();
                if (!"Length".equals(spSelected)){
                    if ("<30 mins".equals(spSelected)){
                        low = 0;
                        high = 30;
                    }
                    else if ("30-60 mins".equals(spSelected)){
                        low = 30;
                        high = 60;
                    }
                    else if ("1-2 hrs".equals(spSelected)){
                        low = 60;
                        high = 120;
                    }
                    else if (">2 hrs".equals(spSelected)){
                        low = 120;
                        high = 4320;
                    }

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //Consulta a la base de datos.
                            final List<Recipe> recipes = db.recipeDao().fetchRecipesbyTime(low,high);

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
                                        Toast.makeText(getApplicationContext(), "Couldn't find any Recipes that matched the time frame." ,
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
