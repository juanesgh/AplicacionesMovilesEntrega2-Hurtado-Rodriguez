package com.example.nicolas.entrega2.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface IngredientDao {
    @Query("SELECT * FROM ingredient")
    List<Ingredient> getAllIngredients();

    @Query("SELECT * FROM ingredient WHERE id = :id")
    Ingredient fetchOneIngredientbyId (int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Ingredient... ingredients);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Ingredient ingredient);

    @Query("DELETE FROM ingredient")
    void deleteAll();
}
