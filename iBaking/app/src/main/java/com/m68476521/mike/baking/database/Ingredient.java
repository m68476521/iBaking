package com.m68476521.mike.baking.database;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Bean Ingredient used for recipes. It contains
 * name of the ingredient
 * quantity
 * measured
 */

public class Ingredient implements Parcelable {
    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel parcel) {
            return new Ingredient(parcel);
        }

        @Override
        public Ingredient[] newArray(int i) {
            return new Ingredient[i];
        }

    };

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    private String ingredient;
    private Double quantity;
    private String measure;

    public Ingredient() {
    }

    public Ingredient(String ingredient, Double quantity, String measure) {
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.measure = measure;
    }

    private Ingredient(Parcel in) {
        ingredient = in.readString();
        quantity = in.readDouble();
        measure = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ingredient);
        parcel.writeDouble(quantity);
        parcel.writeString(measure);
    }
}
