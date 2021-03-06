package com.example.aniket.bakingapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;

import com.example.aniket.bakingapp.R;
import com.example.aniket.bakingapp.adapter.RecipeAdapter;
import com.example.aniket.bakingapp.data.CakeConstants;
import com.example.aniket.bakingapp.idlingtestresource.IdlingTestResource;
import com.example.aniket.bakingapp.pojo.RecipeItem;

import java.util.ArrayList;


/**
 * Created by aniket on 12/20/17.
 */

public class RecipeListingActivity extends AppCompatActivity
        implements RecipeAdapter.RecipeItemClickListener {

    @Nullable
    private IdlingTestResource mIdlingResource;


    @VisibleForTesting
    @NonNull
    public IdlingTestResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new IdlingTestResource();
        }
        return mIdlingResource;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_listing);
        getIdlingResource();
    }

    @Override
    public void onRecipeItemClick(RecipeItem clickedRecipeItem) {
        Bundle bundle = new Bundle();
        ArrayList<RecipeItem> selectedRecipes = new ArrayList<>();
        selectedRecipes.add(clickedRecipeItem);
        bundle.putParcelableArrayList(CakeConstants.SELECTED_RECIPES, selectedRecipes);

        final Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
