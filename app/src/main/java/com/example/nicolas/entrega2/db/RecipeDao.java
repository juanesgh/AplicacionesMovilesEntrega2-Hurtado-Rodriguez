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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Recipe... recipes);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Recipe recipe);

    @Query("DELETE FROM recipe")
    void deleteAll();
}
