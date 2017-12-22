package com.example.aniket.bakingapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aniket.bakingapp.R;
import com.example.aniket.bakingapp.data.CakeConstants;
import com.example.aniket.bakingapp.pojo.RecipeItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by aniket on 12/20/17.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecyclerViewHolder> {

    ArrayList<RecipeItem> mRecipeItems;
    Context mContext;
    final private RecipeItemClickListener mRecipeItemClickListener;
    private String TAG = RecipeAdapter.class.getSimpleName();

    public interface RecipeItemClickListener {
        void onRecipeItemClick(RecipeItem clickedIndex);
    }

    public RecipeAdapter(RecipeItemClickListener listener) {
        mRecipeItemClickListener = listener;
    }

    public void setRecipeData(ArrayList<RecipeItem> recipesIn, Context context) {
        mRecipeItems = recipesIn;
        mContext = context;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recipe_listing_item, parent, false);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);

        Log.i(TAG, "Aniket1, in onCreateViewHolder");

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.RecyclerViewHolder holder, int position) {
        holder.textRecyclerView.setText(mRecipeItems.get(position).getName());

//        holder.textServingsRecyclerView.setText(mRecipeItems.get(position).getServings());

        String imageUrl = mRecipeItems.get(position).getImage();

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Uri buitUri = Uri.parse(imageUrl).buildUpon().build();
            Picasso.with(mContext)
                    .load(buitUri)
                    .into(holder.imageRecyclerView);
        } else {
            Picasso.with(mContext)
                    .load(CakeConstants.DEFAULT_RECIPE_LISTING_IMAGE)
                    .into(holder.imageRecyclerView);
        }
    }

    @Override
    public int getItemCount() {
        return mRecipeItems != null ? mRecipeItems.size() : 0;
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textRecyclerView;
        ImageView imageRecyclerView;
//        TextView textServingsRecyclerView;


        public RecyclerViewHolder(View itemView) {
            super(itemView);

            textRecyclerView = itemView.findViewById(R.id.recipe_listing_item_title);
            imageRecyclerView = itemView.findViewById(R.id.recipe_thumbnail);
//            textServingsRecyclerView = itemView.findViewById(R.id.recipe_listing_item_servings);

            imageRecyclerView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mRecipeItemClickListener.onRecipeItemClick(mRecipeItems.get(clickedPosition));
        }

    }
}
