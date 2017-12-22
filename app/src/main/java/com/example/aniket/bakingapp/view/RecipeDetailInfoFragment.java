package com.example.aniket.bakingapp.view;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aniket.bakingapp.R;
import com.example.aniket.bakingapp.adapter.RecipeDetailAdapter;
import com.example.aniket.bakingapp.data.CakeConstants;
import com.example.aniket.bakingapp.pojo.Ingredient;
import com.example.aniket.bakingapp.pojo.RecipeItem;

import java.util.ArrayList;
import java.util.List;


public class RecipeDetailInfoFragment extends Fragment {

    private ArrayList<RecipeItem> recipes;
    String recipeName;

    public RecipeDetailInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RecyclerView recyclerView;
        TextView textView;

        recipes = new ArrayList<>();

        if (savedInstanceState != null) {
            recipes = savedInstanceState.getParcelableArrayList(CakeConstants.SELECTED_RECIPES);
        } else {
            recipes = getArguments().getParcelableArrayList(CakeConstants.SELECTED_RECIPES);
        }

        List<Ingredient> ingredients = recipes.get(0).getIngredients();
        recipeName = recipes.get(0).getName();

        View rootView = inflater.inflate(R.layout.recipe_selected_detail_fragment, container, false);
        textView = rootView.findViewById(R.id.recipe_detail_text);

        ArrayList<String> recipeIngredients = new ArrayList<>();

        for (Ingredient ingredient : ingredients) {
            String ingredientReceived = ingredient.getIngredient();
            Double quantityReceived = ingredient.getQuantity();
            String measureReceived = ingredient.getMeasure();

            textView.append(ingredientReceived + "\t\t\t\t\t\t");
            textView.append(quantityReceived + " ");
            textView.append(measureReceived + "\n");
            recipeIngredients.add(ingredientReceived + "\n" +
                    "Quantity: " + quantityReceived + "\n" +
                    "Measure: " + measureReceived + "\n");
        }

        recyclerView = rootView.findViewById(R.id.recipe_detail_recycler);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        RecipeDetailAdapter recipeDetailAdapter = new RecipeDetailAdapter((RecipeDetailActivity) getActivity());
        recyclerView.setAdapter(recipeDetailAdapter);
        recipeDetailAdapter.setRecipeStepData(recipes, getContext());


        return rootView;

    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList(CakeConstants.SELECTED_RECIPES, recipes);
        currentState.putString("Title", recipeName);
    }


}
