package com.example.aniket.bakingapp.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aniket.bakingapp.Network.FetchRecipe;
import com.example.aniket.bakingapp.Network.IFetchedRecipe;
import com.example.aniket.bakingapp.R;
import com.example.aniket.bakingapp.adapter.RecipeAdapter;
import com.example.aniket.bakingapp.data.CakeConstants;
import com.example.aniket.bakingapp.idlingtestresource.IdlingTestResource;
import com.example.aniket.bakingapp.pojo.RecipeItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aniket on 12/20/17.
 */

public class RecipeListingFragment extends Fragment {
    private static final String TAG = RecipeListingFragment.class.getSimpleName();

    public RecipeListingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        RecyclerView recyclerView;

        View rootView = inflater.inflate(R.layout.recipe_fragment, viewGroup, false);

        recyclerView = rootView.findViewById(R.id.recipe_recycler);
        final RecipeAdapter recipeAdapter = new RecipeAdapter((RecipeListingActivity) getActivity());
        recyclerView.setAdapter(recipeAdapter);
        if (rootView.getTag() != null && rootView.getTag().equals("phone-land")) {
            GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
            recyclerView.setLayoutManager(mLayoutManager);
        } else {
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
        }

        IFetchedRecipe fetchedRecipe = FetchRecipe.retriveData();
        final Call<ArrayList<RecipeItem>> recipe = fetchedRecipe.getRecipe();

        final IdlingTestResource idlingResource = ((RecipeListingActivity)getActivity()).getIdlingResource();

        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        recipe.enqueue(new Callback<ArrayList<RecipeItem>>() {
            @Override
            public void onResponse(Call<ArrayList<RecipeItem>> call, Response<ArrayList<RecipeItem>> response) {
                Integer statusCode = response.code();
                Log.i(TAG, "Aniket1, Status Code: " + statusCode.toString());
                ArrayList<RecipeItem> recipes = response.body();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(CakeConstants.FETCHED_RECIPES, recipes);
                recipeAdapter.setRecipeData(recipes, getContext());
                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RecipeItem>> call, Throwable t) {
                Log.e(TAG, "Failed to fetch Cake recipe: " + t.getMessage());
            }
        });

        return rootView;

    }
}
