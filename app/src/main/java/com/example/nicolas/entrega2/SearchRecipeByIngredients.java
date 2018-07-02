package com.example.nicolas.entrega2;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nicolas.entrega2.db.AppDatabase;
import com.example.nicolas.entrega2.db.Ingredient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchRecipeByIngredients extends AppCompatActivity {

    private ArrayList<String> namesList;
    private List<Integer> idsList;
    private List<Integer> searchIds;
    private List<String> searchIdsNames;

    private AutoCompleteTextView actv;
    private ListView lv;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe_by_ingredients);
        db = AppDatabase.getDatabase(getApplicationContext());
        
        // Inicio listas vacias.
        namesList = new ArrayList<>();
        idsList = new ArrayList<>();
        searchIds = new ArrayList<>();
        searchIdsNames = new ArrayList<>();

        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        lv = (ListView) findViewById(R.id.list_view);

        loadAutoCompleteTextView();
        onClickEvent();
    }
    private void loadAutoCompleteTextView() {
        //Thread para leer los ingredientes de la base de datos.
        new Thread(new Runnable() {
            @Override
            public void run() {

                //Consulta a la base de datos.
                final List<Ingredient> ingredients = db.ingredientDao().getAllIngredients();
                //Agrego todos los nombres e ids a 2 listas.
                for (int i = 0; i < ingredients.size(); i++) {
                    String aux = ingredients.get(i).getName();
                    Integer auxi = ingredients.get(i).getId();
                    namesList.add(aux);
                    idsList.add(auxi);
                }
                //lleno el autocompletetextview con los ingredientes.
                Handler mainHandler = new Handler(getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                (getBaseContext(), android.R.layout.select_dialog_item, namesList);
                        actv.setThreshold(1);
                        actv.setAdapter(adapter);
                    }
                });
            }
        }).start();
    }
    private void onClickEvent() {
        //Click para agregar ingredientes a la busqueda.
        findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = actv.getText().toString();
                s = s.toLowerCase();
                //Si el ingrediente existe, intenta agregarlo.
                if (namesList.contains(s)) {
                    Integer in = namesList.indexOf(s);
                    Integer id = idsList.get(in);
                    //Si el ingrediente no esta en la lista, lo agrega.
                    if (!searchIds.contains(id)) {
                        searchIds.add(id);
                        searchIdsNames.add(s);
                        //Se llena la lista con los elementos escogidos.
                        final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                (getBaseContext(), android.R.layout.select_dialog_item, searchIdsNames);
                        lv.setAdapter(adapter);
                        //Si se le hace Long Click a un elemento en la lista, este es removido.
                        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                                searchIds.remove(searchIds.get(i));
                                searchIdsNames.remove(searchIdsNames.get(i));
                                //Actualiza la lista de ingredientes.
                                adapter.notifyDataSetChanged();
                                return  false;
                            }
                        });
                    }
                    //Si el ingrediente ya esta en la lista, avisa.
                    else {
                        Toast.makeText(getApplicationContext(), "already added",
                                Toast.LENGTH_LONG).show();
                    }
                }
                //Si no existe, lo avisa.
                else{
                    Toast.makeText(getApplicationContext(), "Ingredient not found.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Debe haber por lo menos un ingrediente en la lista para buscar.
                if (searchIds.size()>0) {
                    Intent intent = new Intent(getApplicationContext(), RecipeIngredientActivity.class);
                    intent.putExtra("ids", (Serializable) searchIds);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Choose at least 1 ingredient to look for recipes.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
