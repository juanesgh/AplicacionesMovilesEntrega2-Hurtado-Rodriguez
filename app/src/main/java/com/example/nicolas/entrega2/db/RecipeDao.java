package com.example.nicolas.entrega2.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {
    @Query("SELECT * FROM recipe")
    List<Recipe> getAllRecipes();

    @Query("SELECT * FROM recipe WHERE id = :id")
    Recipe fetchOneRecipebyId (int id);

    @Query("SELECT * FROM recipe WHERE name LIKE :title")
    List<Recipe> fetchRecipesbyTitle (String title);

    @Query("SELECT * FROM recipe WHERE preparation_time BETWEEN :low AND :high ")
    List<Recipe> fetchRecipesbyTime (Integer low, Integer high);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Recipe... recipes);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Recipe recipe);

    @Query("DELETE FROM recipe")
    void deleteAll();
}
