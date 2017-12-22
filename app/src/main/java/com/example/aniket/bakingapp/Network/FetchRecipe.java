package com.example.aniket.bakingapp.Network;

import com.example.aniket.bakingapp.data.CakeConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by aniket on 12/20/17.
 */

public class FetchRecipe {
    public static IFetchedRecipe retriveData() {
        Gson gson = new GsonBuilder().create();

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        return new Retrofit.Builder()
                .baseUrl(CakeConstants.RECIPE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .callFactory(httpClientBuilder.build())
                .build().create(IFetchedRecipe.class);

    }
}
