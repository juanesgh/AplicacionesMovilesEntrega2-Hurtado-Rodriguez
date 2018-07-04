package com.example.nicolas.entrega2.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
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
                                            Ingredient i1 =new Ingredient("egg");
                                            Ingredient i2 =new Ingredient("grinded meat");
                                            Ingredient i3 =new Ingredient("bread");
                                            Ingredient i4 =new Ingredient("sausage");
                                            Ingredient i5 =new Ingredient("onion");
                                            Ingredient i6 =new Ingredient("flour");
                                            Ingredient i7 =new Ingredient("milk");
                                            Ingredient i8 =new Ingredient("butter");
                                            Ingredient i9 =new Ingredient("meat");
                                            Recipe r1 = new Recipe( "Hard Boiled Egg","Boil a pot of water over high heat. Reduce the heat to low, add eggs. Cook for 5 minutes. Drain, cool in water, peel.",2,10);
                                            Recipe r2 = new Recipe( "Grinded Meat Hamburguer","Make patties from the grilled meat. Cook the patties. Place the patties on the bread. Add condiments.",2,10);
                                            Recipe r3 = new Recipe( "Hotdog","Boil a pot of water. Add the sausages. Wait 5 minutes. Drain, place the sausages on the bread. Add condiments.",2,10);
                                            Recipe r4 = new Recipe( "Scrambled Eggs on Bread","Break eggs and put on frying pan. Cook until no further liquid parts visible. Serve on the bread.",2,10);
                                            Recipe r5 = new Recipe("Empanada de Pino", "Cook and slice 1 Kilo of the meat of your preference (soft meat is better) add salt, pepper or any other seasoning you like. Add 3 chopped onions to the cooked meat and let it cook for 30 minutes at low temperature. Now we prepare the dough, mix one cup of milk with one cup of warm water and salt, stir until all the salt is disolved. Add to a bowl 4 egg yolks, 1 Kilo of flour and butter, mix until the mix is uniform and the dough is soft to the touch and elastic. Pre heat the oven to 180 Celcius. Separate the dough in 20 pieces and make circumferences with everyone of them , place the mix of the meat and the onions on the center, close and take them to the oven for 40 minutes. Now Your Empanadas are ready to Eat", 20, 130);
                                            Recipe r6 = new Recipe("Onion Rings", "Slice in Juliene 2 onions and whip up 2 eggs on a bowl, then in a separated bowl toast and crush 4 slices of bread until it is bread powder, add seasoning of your preferences. \nDip the Onions into the whipped up eggs, then dip them into the bread powder, and finally fry or bake them until a gold color is achieved.", 4, 30);
                                            RecipeIngredient ri1 = new RecipeIngredient(1,1);
                                            RecipeIngredient ri2 = new RecipeIngredient(2,2);
                                            RecipeIngredient ri3 = new RecipeIngredient(2,3);
                                            RecipeIngredient ri4 = new RecipeIngredient(3,3);
                                            RecipeIngredient ri5 = new RecipeIngredient(3,4);
                                            RecipeIngredient ri6 = new RecipeIngredient(4,1);
                                            RecipeIngredient ri7 = new RecipeIngredient(4,3);
                                            RecipeIngredient ri8 = new RecipeIngredient(5, 1);
                                            RecipeIngredient ri9 = new RecipeIngredient(5, 5);
                                            RecipeIngredient ri10 = new RecipeIngredient(5, 6);
                                            RecipeIngredient ri11 = new RecipeIngredient(5, 7);
                                            RecipeIngredient ri12 = new RecipeIngredient(5, 8);
                                            RecipeIngredient ri13 = new RecipeIngredient(5, 9);
                                            RecipeIngredient ri14 = new RecipeIngredient(6, 1);
                                            RecipeIngredient ri15 = new RecipeIngredient(6, 3);
                                            RecipeIngredient ri16 = new RecipeIngredient(6, 5);

                                            AppDatabase db = AppDatabase.getDatabase(context);
                                            db.ingredientDao().insertAll(i1,i2,i3,i4,i5,i6,i7,i8,i9);
                                            db.recipeDao().insertAll(r1,r2,r3,r4,r5,r6);
                                            ArrayList<RecipeIngredient> riList = new ArrayList<>();
                                            riList.add(ri1);
                                            riList.add(ri2);
                                            riList.add(ri3);
                                            riList.add(ri4);
                                            riList.add(ri5);
                                            riList.add(ri6);
                                            riList.add(ri7);
                                            riList.add(ri8);
                                            riList.add(ri9);
                                            riList.add(ri10);
                                            riList.add(ri11);
                                            riList.add(ri12);
                                            riList.add(ri13);
                                            riList.add(ri14);
                                            riList.add(ri15);
                                            riList.add(ri16);
                                            db.recipeIngredientDao().insertAll(riList);
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
