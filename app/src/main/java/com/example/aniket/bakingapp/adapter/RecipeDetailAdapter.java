package com.example.aniket.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aniket.bakingapp.R;
import com.example.aniket.bakingapp.pojo.RecipeItem;
import com.example.aniket.bakingapp.pojo.Step;

import java.util.List;

/**
 * Created by aniket on 12/21/17.
 */

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.RecyclerViewHolder> {

    private List<Step> mSteps;
    private String recipeName;
    final private RecipeStepClickListener mRecipeStepListener;
    private String TAG = RecipeDetailAdapter.class.getSimpleName();

    public interface RecipeStepClickListener {
        void onRecipeStepClick(List<Step> stepOut, int clickedItemIndex, String recipeName);
    }

    public RecipeDetailAdapter(RecipeStepClickListener listener) {
        mRecipeStepListener = listener;
    }

    public void setRecipeStepData(List<RecipeItem> recipeItem, Context context) {
        mSteps = recipeItem.get(0).getSteps();
        recipeName = recipeItem.get(0).getName();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recipe_detail_cardview, parent, false);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.textRecyclerView.setText(
                new StringBuilder()
                        .append("Step ")
                        .append(mSteps.get(position).getId() + 1)
                        .append(": ")
                        .append(mSteps.get(position).getShortDescription())
                        .toString());
    }

    @Override
    public int getItemCount() {
        return mSteps != null ? mSteps.size() : 0;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public TextView textRecyclerView;


        public RecyclerViewHolder(View itemView) {
            super(itemView);
            textRecyclerView = (TextView) itemView.findViewById(R.id.short_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mRecipeStepListener.onRecipeStepClick(mSteps, getAdapterPosition(), recipeName);
        }
    }
}
