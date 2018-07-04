package com.example.nicolas.entrega2;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nicolas.entrega2.db.AppDatabase;
import com.example.nicolas.entrega2.db.Ingredient;
import com.example.nicolas.entrega2.db.Recipe;
import com.example.nicolas.entrega2.db.RecipeIngredient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AddNewRecipe extends AppCompatActivity {
    private ArrayList<String> ingredientNameList;
    private List<Integer> ingredientIdList;
    private List<Integer> addIngredientIdsList;
    private List<String> addIngredientNamesList;
    private String imgPath;

    private Spinner sp;
    private AutoCompleteTextView actv;
    private ListView lv;

    private EditText etT;
    private EditText etI;
    private EditText etPT;
    private EditText etS;

    private AppDatabase db;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_recipe);
        db = AppDatabase.getDatabase(getApplicationContext());

        // Inicio listas vacias.
        ingredientNameList = new ArrayList<>();
        ingredientIdList = new ArrayList<>();
        addIngredientIdsList = new ArrayList<>();
        addIngredientNamesList = new ArrayList<>();

        //Castear elementos a llenar.
        sp = (Spinner) findViewById(R.id.spinnerNations);
        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        lv = (ListView) findViewById(R.id.IngredientList);

        //Castear elementos de los cuales obtener valores.
        etT = (EditText) findViewById(R.id.etTitle);
        etI = (EditText) findViewById(R.id.etInstructions);
        etPT = (EditText) findViewById(R.id.etPreparationTime);
        etS = (EditText) findViewById(R.id.etServings);

        imV = (ImageView) findViewById(R.id.image_view);

        imgPath = "";

        fillSpinner();
        loadAutoCompleteTextView();
        onClickEvent();
    }

    //Funcion para llenar spinner con paises.
    private void fillSpinner(){
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("None");
        spinnerArray.add("Italian");
        spinnerArray.add("Chinese");
        spinnerArray.add("French");
        spinnerArray.add("Spanish");

        ArrayAdapter<String> spadapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        spadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(spadapter);
    }

    //Funcion para cargar ingredientes en AutoCompleteTextView.
    private void loadAutoCompleteTextView() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                //Consulta a la base de datos.
                List<Ingredient> ingredients = db.ingredientDao().getAllIngredients();
                //Agrego todos los nombres e ids a 2 listas.
                for (int i = 0; i < ingredients.size(); i++) {
                    String aux = ingredients.get(i).getName();
                    Integer auxi = ingredients.get(i).getId();
                    ingredientNameList.add(aux);
                    ingredientIdList.add(auxi);
                }
                Handler mainHandler = new Handler(getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        populateAutoCompleteTextView();
                    }
                });
            }
        }).start();
    }

    //Parte que llena el ACTV con los ingredientes.
    private void populateAutoCompleteTextView(){
        ArrayAdapter<String> actvadapter = new ArrayAdapter<String>
                (getBaseContext(), android.R.layout.select_dialog_item, ingredientNameList);
        actv.setThreshold(1);
        actv.setAdapter(actvadapter);
    }

    private void onClickEvent() {
        //Click para agregar ingredientes a la receta..
        findViewById(R.id.add_ingredient_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = actv.getText().toString();
                s = s.toLowerCase();
                //Si el ingrediente existe, intenta agregarlo.
                if (ingredientNameList.contains(s)) {
                    Integer in = ingredientNameList.indexOf(s);
                    Integer id = ingredientIdList.get(in);
                    //Si el ingrediente no esta en la lista, lo agrega.
                    if (!addIngredientIdsList.contains(id)) {
                        addIngredientIdsList.add(id);
                        addIngredientNamesList.add(s);
                        populateIngredientList();
                    }
                    //Si el ingrediente ya esta en la lista, avisa.
                    else {
                        Toast.makeText(getApplicationContext(), "already added",
                                Toast.LENGTH_LONG).show();
                    }
                }
                //Si no existe, pregunta si se quiere agregar a la base de datos.
                else{
                    newIngredientEvent(s);
                }
            }
        });
        //Click para crear nueva receta.
        findViewById(R.id.add_recipe_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (addIngredientIdsList.size() > 0){
                    if (etT.getText().length() != 0 && etI.getText().length() != 0 && etPT.getText().length() != 0 && etS.getText().length() != 0){
                        String title = etT.getText().toString();
                        String desc = etI.getText().toString();
                        Integer prepTime = Integer.valueOf(etPT.getText().toString());
                        Integer servings = Integer.valueOf(etS.getText().toString());
                        final Recipe newRecipe = new Recipe(title,desc,servings,prepTime);
                        if (!"None".equals(sp.getSelectedItem().toString())){
                            newRecipe.setNationality(sp.getSelectedItem().toString());
                        }
                        if (!"".equals(imgPath)){
                            newRecipe.setImage_path(imgPath);
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //Se agrega receta a la base de datos.
                                long idlong = db.recipeDao().insert(newRecipe);
                                int idInt = new BigDecimal(idlong).intValueExact();
                                ArrayList<RecipeIngredient> riList = new ArrayList<>();
                                for (int i = 0; i < addIngredientIdsList.size(); i++) {
                                    RecipeIngredient ri = new RecipeIngredient(idInt,addIngredientIdsList.get(i));
                                    riList.add(ri);
                                }
                                db.recipeIngredientDao().insertAll(riList);

                                //Se vuelve a poblar la lista de ingredientes y el ACTV.
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
                    else {
                        Toast.makeText(getApplicationContext(), "You need to give the Recipe a Title, Instructions, Preparation Time and Servings.",
                                Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "The Recipe must have at least one Ingredient.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.add_photo_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pide permiso para tomar fotos.
                ActivityCompat.requestPermissions(AddNewRecipe.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Si acepta o ya tiene permiso para utilizar camara, se inicia la camara.
                    dispatchTakePictureIntent();
                } else {
                    Toast.makeText(AddNewRecipe.this, "Permission denied to write on your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    //Inicia camara.
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    //Carga la foto tomada en el ImageView.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imV.setImageBitmap(imageBitmap);

            Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);
            String finalPath = getRealPathFromURI(tempUri);

            imgPath = finalPath;
        }
    }

    //Funciones para conseguir path de la foto tomada.
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    //Funciones para conseguir path de la foto tomada.
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    //Funcion que carga los ingredientes en el listview de ingredientes.
    private void populateIngredientList(){
        //Se llena la lista con los elementos escogidos.
        final ArrayAdapter<String> lvadapter = new ArrayAdapter<String>
                (getBaseContext(), android.R.layout.select_dialog_item, addIngredientNamesList);
        lv.setAdapter(lvadapter);
        //Si se le hace Long Click a un elemento en la lista, este es removido.
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                addIngredientIdsList.remove(addIngredientIdsList.get(i));
                addIngredientNamesList.remove(addIngredientNamesList.get(i));
                //Actualiza la lista de ingredientes.
                lvadapter.notifyDataSetChanged();
                return  false;
            }
        });
    }

    //Funcion para agregar ingredientes a la base de datos.
    public void newIngredientEvent(String ingredient_to_add){
        if (ingredient_to_add.length() > 0){
            AlertDialog.Builder altdial = new AlertDialog.Builder(AddNewRecipe.this);
            final String newIngredient = ingredient_to_add;
            altdial.setMessage("'"+ingredient_to_add+"' was not found on the Database, do you want to add it?").setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //Se agrega ingrediente a la base de datos.
                                    Ingredient newIn = new Ingredient(newIngredient);
                                    long idlong = db.ingredientDao().insert(newIn);
                                    int idInt = new BigDecimal(idlong).intValueExact();

                                    //Se agrega el ingrediente nuevo a la lista de ingredientes de la receta.
                                    addIngredientIdsList.add(idInt);
                                    addIngredientNamesList.add(newIngredient);

                                    //Se agrega el ingrediente a la lista de ingredientes del ACTV.
                                    ingredientIdList.add(idInt);
                                    ingredientNameList.add(newIngredient);

                                    //Se vuelve a poblar la lista de ingredientes y el ACTV.
                                    Handler mainHandler = new Handler(getMainLooper());
                                    mainHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            populateIngredientList();
                                            populateAutoCompleteTextView();
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
            alert.setTitle("Add Ingredient?");
            alert.show();
        }
    }
}
