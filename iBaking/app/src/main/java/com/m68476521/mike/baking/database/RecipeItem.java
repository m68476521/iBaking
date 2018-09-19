package com.m68476521.mike.baking.database;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Main bean it is the recipe itself contained
 * name of recipe
 */

public class RecipeItem implements Parcelable {

    public static final Parcelable.Creator<RecipeItem> CREATOR = new Parcelable.Creator<RecipeItem>() {
        @Override
        public RecipeItem createFromParcel(Parcel parcel) {
            return new RecipeItem(parcel);
        }

        @Override
        public RecipeItem[] newArray(int i) {
            return new RecipeItem[i];
        }
    };

    private UUID myId;
    private int mId;
    private String ingredientName;
    private String serving;
    private String image;

    private ArrayList<Ingredient> ingredients;

    private ArrayList<Step> steps;

    private RecipeItem(Parcel in) {
        mId = in.readInt();
        ingredientName = in.readString();
        serving = in.readString();
        image = in.readString();
    }

    public RecipeItem() {
        this(UUID.randomUUID());
    }

    private RecipeItem(UUID id) {
        myId = id;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getServing() {
        return serving;
    }

    public void setServing(String serving) {
        this.serving = serving;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(ingredientName);
        parcel.writeString(image);
        parcel.writeString(serving);
    }
}

