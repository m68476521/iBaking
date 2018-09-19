package com.m68476521.mike.baking.utilities;

import android.net.Uri;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import com.m68476521.mike.baking.database.Ingredient;
import com.m68476521.mike.baking.database.RecipeItem;
import com.m68476521.mike.baking.database.Step;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Utility to pull information for JSON file
 */

public class NetworkUtils {

    private final static String RECIPES_BASE_URL =
            "https://d17h27t6h515a5.cloudfront.net/";
    private final static String PARAM_KEY = "api_key";
    private final static String KEY_PASS = "";
    private final static String RECIPES = "topher/2017/May/59121517_baking/baking.json";

    private final static String TAG = "NetworkUtils";

    public static URL buildUrl(String type, String id) {
        Uri builtUri = null;
        switch (type) {
            case "recipes":
                builtUri = Uri.parse(RECIPES_BASE_URL + RECIPES).buildUpon()
                        .build();
                break;
            case "something_else":
                builtUri = Uri.parse(RECIPES_BASE_URL + id + "/").buildUpon()
                        .appendQueryParameter(PARAM_KEY, KEY_PASS)
                        .build();
                break;
        }

        URL url = null;
        try {
            url = new URL(builtUri != null ? builtUri.toString() : null);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public ArrayList<RecipeItem> getRecipes() {

        ArrayList<RecipeItem> mResult = null;
        try {
            InputStream response = buildUrl("recipes", "").openStream();
            mResult = readJsonStream(response);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mResult;
    }

    private ArrayList<RecipeItem> readJsonStream(InputStream in) throws IOException {
        try (JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"))) {
            return readMessagesArray(reader);
        }
    }

    private ArrayList<RecipeItem> readMessagesArray(JsonReader reader) throws IOException {
        ArrayList<RecipeItem> messages = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            messages.add(readMessage(reader));
        }
        reader.endArray();
        return messages;
    }

    private RecipeItem readMessage(JsonReader reader) throws IOException {
        int id = -1;
        RecipeItem mRecipeItem = new RecipeItem();

        String ingredientName = null;
        String serving = "";
        String image = "";
        ArrayList<Ingredient> ingredients = null;
        ArrayList<Step> steps = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                id = reader.nextInt();
            } else if (name.equals("name")) {
                ingredientName = reader.nextString();
            } else if (name.equals("image")) {
                image = reader.nextString();
            } else if (name.equals("servings")) {
                serving = reader.nextString();
            } else if (name.equals("ingredients") && reader.peek() != JsonToken.NULL) {
                ingredients = readIngredients(reader);
            } else if (name.equals("steps")) {
                steps = readSteps(reader);
            } else {
                reader.skipValue();
            }
        }

        Log.d("MIKE", id + " | " + ingredientName + " | " + image + " | " + serving);
        reader.endObject();
        mRecipeItem.setId(id);
        mRecipeItem.setIngredientName(ingredientName);
        mRecipeItem.setImage(image);
        mRecipeItem.setServing(serving);
        mRecipeItem.setIngredients(ingredients);
        mRecipeItem.setSteps(steps);
        return mRecipeItem;
    }

    private ArrayList<Ingredient> readIngredients(JsonReader reader) throws IOException {

        ArrayList<Ingredient> mIngredients = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            Ingredient mIngredient =
                    readIngredientsItem(reader);
            mIngredients.add(mIngredient);
        }
        reader.endArray();
        return mIngredients;
    }

    private ArrayList<Step> readSteps(JsonReader reader) throws IOException {

        ArrayList<Step> steps = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            Step step =
                    readStepsItem(reader);
            steps.add(step);
        }
        reader.endArray();
        return steps;
    }

    private Ingredient readIngredientsItem(JsonReader reader) throws IOException {
        Ingredient mMyIngredient = new Ingredient();
        Double mQuantity = null;
        String mMeasure = "";
        String mIngredient = "";

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "quantity":
                    mQuantity = reader.nextDouble();
                    break;
                case "measure":
                    mMeasure = reader.nextString();
                    break;
                case "ingredient":
                    mIngredient = reader.nextString();
                    break;
                default:
                    reader.skipValue();
                    Log.d(TAG, " node item unknown " + reader.nextName());
            }
        }
        reader.endObject();
        mMyIngredient.setIngredient(mIngredient);
        mMyIngredient.setMeasure(mMeasure);
        mMyIngredient.setQuantity(mQuantity);

        return mMyIngredient;
    }

    private Step readStepsItem(JsonReader reader) throws IOException {
        Step mStep = new Step();
        int mId = 0;
        String mSortDescription = "";
        String mDescription = "";
        String mVideo = "";
        String mImage = "";

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();

            switch (name) {
                case "id":
                    mId = reader.nextInt();
                    break;
                case "shortDescription":
                    mSortDescription = reader.nextString();
                    break;
                case "description":
                    mDescription = reader.nextString();
                    break;
                case "videoURL":
                    mVideo = reader.nextString();
                    break;
                case "thumbnailURL":
                    mImage = reader.nextString();
                    break;
                default:
                    reader.skipValue();
                    Log.d(TAG, " node item unknown " + name);
            }
        }
        reader.endObject();
        mStep.setId(mId);
        mStep.setSortDescription(mSortDescription);
        mStep.setDescription(mDescription);
        mStep.setVideo(mVideo);
        mStep.setImage(mImage);

        return mStep;
    }
}

