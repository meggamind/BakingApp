package com.example.aniket.bakingapp.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.aniket.bakingapp.R;
import com.example.aniket.bakingapp.adapter.RecipeDetailAdapter;
import com.example.aniket.bakingapp.data.CakeConstants;
import com.example.aniket.bakingapp.pojo.RecipeItem;
import com.example.aniket.bakingapp.pojo.Step;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity
        implements RecipeDetailAdapter.RecipeStepClickListener,
        RecipeStepInfoFragment.RequestedStepClickListener {
    String recipeName;
    private String TAG = RecipeDetailActivity.class.getSimpleName();
    private ArrayList<RecipeItem> recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (savedInstanceState == null) {
            Bundle selectedRecipeBundle = getIntent().getExtras();
            recipe = new ArrayList<>();
            recipe = selectedRecipeBundle.getParcelableArrayList(CakeConstants.SELECTED_RECIPES);
            recipeName = recipe.get(0).getName();

            final RecipeDetailInfoFragment fragment = new RecipeDetailInfoFragment();
            fragment.setArguments(selectedRecipeBundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(CakeConstants.STACK_RECIPE_DETAIL)
                    .commit();

            if (findViewById(R.id.recipe_linear_layout).getTag() != null
                    && findViewById(R.id.recipe_linear_layout).getTag().equals("tablet-land")) {
                final RecipeStepInfoFragment infoFragment = new RecipeStepInfoFragment();
                infoFragment.setArguments(selectedRecipeBundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container2, infoFragment)
                        .addToBackStack(CakeConstants.STACK_RECIPE_STEP_DETAIL)
                        .commit();
            }


        } else {
            recipeName = savedInstanceState.getString("recipeTitle");
        }
    }


    @Override
    public void onRecipeStepClick(List<Step> stepOut, int clickedItemIndex, String recipeName) {
        final RecipeStepInfoFragment fragment = new RecipeStepInfoFragment();

//        getSupportActionBar().setTitle(recipeName);

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(CakeConstants.SELECTED_STEPS, (ArrayList<Step>) stepOut);
        bundle.putInt(CakeConstants.SELECTED_INDEX, clickedItemIndex);
        bundle.putString(CakeConstants.SELECTED_STEP_RECIPE_TITLE, recipeName);

        fragment.setArguments(bundle);


        if (findViewById(R.id.recipe_linear_layout).getTag() != null
                && findViewById(R.id.recipe_linear_layout).getTag().equals("tablet-land")) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container2, fragment)
                    .addToBackStack(CakeConstants.STACK_RECIPE_STEP_DETAIL)
                    .commit();

        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(CakeConstants.STACK_RECIPE_STEP_DETAIL)
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CakeConstants.SELECTED_STEP_RECIPE_TITLE, recipeName);
    }


}
