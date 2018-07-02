package com.example.nicolas.entrega2.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RecipeIngredientDao {
    @Query("SELECT * FROM recipeingredient")
    List<RecipeIngredient> getAllRecipeIngredients();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(ArrayList<RecipeIngredient> recipes);

    @Query("SELECT * FROM recipeingredient WHERE recipeId = :recipeId")
    List<RecipeIngredient> fetchIngredientsByRecipeId (int recipeId);

    @Query("DELETE FROM recipeingredient")
    void deleteAll();
}
