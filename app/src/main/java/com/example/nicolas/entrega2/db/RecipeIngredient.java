package com.example.nicolas.entrega2.db;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {@ForeignKey(entity = Recipe.class,
        parentColumns = "id",
        childColumns = "recipeId",
        onDelete = CASCADE),@ForeignKey(entity = Ingredient.class,
        parentColumns = "id",
        childColumns = "ingredientId",
        onDelete = CASCADE)})

public class RecipeIngredient {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "recipeId")
    @NonNull
    private int recipeId;

    @ColumnInfo(name = "ingredientId")
    @NonNull
    private int ingredientId;

    public RecipeIngredient(int recipeId, int ingredientId) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public int getIngredientId() {
        return ingredientId;
    }
}
