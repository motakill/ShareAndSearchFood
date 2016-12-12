package com.shareandsearchfood.ParcelerObjects;


/**
 * Created by david_000 on 07/12/2016.
 */

public class Recipe {
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
    private String recipe_id;
    private String numPeople;



    private String prepareTime;
    private String confectionTime;
    private String category;

    //private List<Photo> photos;
    //private List<Video> videos;
    //private List<Favorite> favorites;

    public Recipe(String title, String ingredients,
                  String steps, String photoRecipe, String calories,
                  int status, String userId, String date, float rate,
                  boolean favorite, String recipe_id, String numPeople,
                  String prepareTime, String confectionTime, String category) {
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
        this.recipe_id = recipe_id;
        this.numPeople = numPeople;
        this.prepareTime = prepareTime;
        this.confectionTime = confectionTime;
        this.category = category;
    }

    public Recipe() {
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

    public String getRecipeId() {
        return this.recipe_id;
    }

    public void setRecipeId(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getNumPeople() {
        return numPeople;
    }

    public void setNumPeople(String numPeople) {
        this.numPeople = numPeople;
    }

    public String getPrepareTime() {
        return prepareTime;
    }

    public void setPrepareTime(String prepareTime) {
        this.prepareTime = prepareTime;
    }

    public String getConfectionTime() {
        return confectionTime;
    }

    public void setConfectionTime(String confectionTime) {
        this.confectionTime = confectionTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}