package com.example.nicolas.entrega2.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.concurrent.Executors;

@Database(entities = {Ingredient.class,Recipe.class,RecipeIngredient.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract IngredientDao ingredientDao();
    public abstract RecipeDao recipeDao();
    public abstract RecipeIngredientDao recipeIngredientDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            Ingredient i1 =new Ingredient("Egg");
                                            Ingredient i2 =new Ingredient("Grinded Meat");
                                            Ingredient i3 =new Ingredient("Bread");
                                            Ingredient i4 =new Ingredient("Sausage");
                                            Recipe r1 = new Recipe( "Hard Boiled Egg","Boil a pot of water over high heat. Reduce the heat to low, add eggs. Cook for 5 minutes. Drain, cool in water, peel.",2,10);
                                            Recipe r2 = new Recipe( "Grinded Meat Hamburguer","Make patties from the grilled meat. Cook the patties. Place the patties on the bread. Add condiments.",2,10);
                                            Recipe r3 = new Recipe( "Hotdog","Boil a pot of water. Add the sausages. Wait 5 minutes. Drain, place the sausages on the bread. Add condiments.",2,10);
                                            Recipe r4 = new Recipe( "Scrambled Eggs on Bread","Break eggs and put on frying pan. Cook until no further liquid parts visible. Serve on the bread.",2,10);
                                            RecipeIngredient ri1 = new RecipeIngredient(1,1);
                                            RecipeIngredient ri2 = new RecipeIngredient(2,2);
                                            RecipeIngredient ri3 = new RecipeIngredient(2,3);
                                            RecipeIngredient ri4 = new RecipeIngredient(3,3);
                                            RecipeIngredient ri5 = new RecipeIngredient(3,4);
                                            RecipeIngredient ri6 = new RecipeIngredient(4,1);
                                            RecipeIngredient ri7 = new RecipeIngredient(4,3);
                                            AppDatabase db = AppDatabase.getDatabase(context);
                                            db.ingredientDao().insertAll(i1,i2,i3,i4);
                                            db.recipeDao().insertAll(r1,r2,r3,r4);
                                            db.recipeIngredientDao().insertAll(ri1,ri2,ri3,ri4,ri5,ri6,ri7);
                                        }
                                    });
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
