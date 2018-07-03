package com.example.nicolas.entrega2.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    @NonNull
    private String name;

    @ColumnInfo(name = "description")
    @NonNull
    private String description;

    @ColumnInfo(name = "nationality")
    private String nationality;

    @ColumnInfo(name = "servings")
    @NonNull
    private Integer servings;

    @ColumnInfo(name = "preparation_time")
    @NonNull
    private Integer preparation_time;

    @ColumnInfo(name = "favorite")
    @NonNull
    private Boolean favorite;

    @ColumnInfo(name = "image_path")
    private String image_path;

    public Recipe(String name, String description, Integer servings, Integer preparation_time) {
        this.name = name;
        this.description = description;
        this.servings = servings;
        this.preparation_time = preparation_time;
        this.favorite = false;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getNationality() {
        return nationality;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public Integer getServings() {
        return servings;
    }

    public void setPreparation_time(Integer preparation_time) { this.preparation_time = preparation_time; }

    public Integer getPreparation_time() {
        return preparation_time;
    }

    public void setFavorite(Boolean favorite) { this.favorite = favorite; }
    
    public Boolean getFavorite() { return favorite; }

    public void setImage_path(String image_path) { this.image_path = image_path; }

    public String getImage_path() { return image_path; }
}
