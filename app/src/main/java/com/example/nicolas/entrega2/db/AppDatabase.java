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
                                            Ingredient i10 =new Ingredient("tomato sauce");
                                            Ingredient i11 =new Ingredient("pepperoni");
                                            Ingredient i12 =new Ingredient("cheese");

                                            Recipe r1 = new Recipe( "Hard Boiled Egg","Boil a pot of water over high heat. Reduce the heat to low, add eggs. Cook for 5 minutes. Drain, cool in water, peel.",6,8);
                                            Recipe r2 = new Recipe( "Grinded Meat Hamburguer","Make patties from the grinded meat. Cook the patties and add cheese. Place the patties on the bread. Add condiments.",4,35);
                                            Recipe r3 = new Recipe( "Hotdog","Boil a pot of water. Add the sausages. Wait 5 minutes. Drain, place the sausages on the bread. Add condiments.",3,20);
                                            Recipe r4 = new Recipe( "Scrambled Eggs on Bread","Break eggs and put on frying pan. Cook until no further liquid parts visible. Serve on the bread.",4,35);
                                            Recipe r5 = new Recipe("Empanada de Pino", "Cook and slice 1 Kilo of the meat of your preference (soft meat is better) add salt, pepper or any other seasoning you like. Add 3 chopped onions to the cooked meat and let it cook for 30 minutes at low temperature. Now we prepare the dough, mix one cup of milk with one cup of warm water and salt, stir until all the salt is disolved. Add to a bowl 4 egg yolks, 1 Kilo of flour and butter, mix until the mix is uniform and the dough is soft to the touch and elastic. Pre heat the oven to 180 Celcius. Separate the dough in 20 pieces and make circumferences with everyone of them , place the mix of the meat and the onions on the center, close and take them to the oven for 40 minutes. Now Your Empanadas are ready to Eat", 20, 130);
                                            Recipe r6 = new Recipe("Onion Rings", "Slice in Juliene 2 onions and whip up 2 eggs on a bowl, then in a separated bowl toast and crush 4 slices of bread until it is bread powder, add seasoning of your preferences. \nDip the Onions into the whipped up eggs, then dip them into the bread powder, and finally fry or bake them until a gold color is achieved.", 4, 30);
                                            Recipe r7 = new Recipe( "Pepperoni Pizza","Mix flour, yeast and water to make dough. Place the dough on the over for 15 minutes. Put tomato sauce on the pizza base and then add cheese and pepperoni. Finally, put the pizza on the oven for 20 minutes.",8,70);

                                            r1.setImage_path("hard_boiled_eggs");
                                            r2.setImage_path("burguer");
                                            r3.setImage_path("hot_dog");
                                            r4.setImage_path("scrambled_eggs");
                                            r5.setImage_path("empanadas");
                                            r6.setImage_path("onion_rings");
                                            r7.setImage_path("pepperoni_pizza");
                                            r7.setNationality("Italian");
                                            r5.setNationality("Spanish");


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
                                            RecipeIngredient ri17 = new RecipeIngredient(2,12);
                                            RecipeIngredient ri18 = new RecipeIngredient(7,6);
                                            RecipeIngredient ri19 = new RecipeIngredient(7,10);
                                            RecipeIngredient ri20 = new RecipeIngredient(7,12);
                                            RecipeIngredient ri21 = new RecipeIngredient(7,11);

                                            AppDatabase db = AppDatabase.getDatabase(context);
                                            db.ingredientDao().insertAll(i1,i2,i3,i4,i5,i6,i7,i8,i9,i10,i11,i12);
                                            db.recipeDao().insertAll(r1,r2,r3,r4,r5,r6,r7);
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
                                            riList.add(ri17);
                                            riList.add(ri18);
                                            riList.add(ri19);
                                            riList.add(ri20);
                                            riList.add(ri21);
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
