package com.example.aniket.bakingapp.Network;

import com.example.aniket.bakingapp.pojo.RecipeItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by aniket on 12/20/17.
 */

public interface IFetchedRecipe {
    @GET("baking.json")
    Call<ArrayList<RecipeItem>> getRecipe();
}
