package com.shareandsearchfood.ParcelerObjects;


/**
 * Created by david_000 on 07/12/2016.
 */

public class RecipeFirebase {
    private String title;
    private String ingredients;
    private String steps;
    private String photoRecipe;
    private String calories;
    private int status;
    private String userId;
    private String date;
    private float rate;
    private boolean favorite;

    //private List<Photo> photos;
    //private List<Video> videos;
    //private List<Favorite> favorites;

    public RecipeFirebase(String title, String ingredients,
                  String steps, String photoRecipe, String calories,
                  int status, String userId, String date, float rate,
                  boolean favorite) {
        this.title = title;
        this.ingredients = ingredients;
        this.steps = steps;
        this.photoRecipe = photoRecipe;
        this.calories = calories;
        this.status = status;
        this.userId = userId;
        this.date = date;
        this.rate = rate;
        this.favorite = favorite;
    }

    public RecipeFirebase() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getPhotoRecipe() {
        return photoRecipe;
    }

    public void setPhotoRecipe(String photoReceipt) {
        this.photoRecipe = photoReceipt;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public float getRate() {
        return this.rate;
    }
}