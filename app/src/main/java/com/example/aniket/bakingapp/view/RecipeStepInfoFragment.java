package com.example.aniket.bakingapp.view;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aniket.bakingapp.R;
import com.example.aniket.bakingapp.data.CakeConstants;
import com.example.aniket.bakingapp.pojo.RecipeItem;
import com.example.aniket.bakingapp.pojo.Step;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aniket on 12/21/17.
 */

public class RecipeStepInfoFragment extends Fragment {
    private Handler mHandler;
    private DefaultBandwidthMeter mDefaultBandwidthMeter;
    private ArrayList<Step> mSteps = new ArrayList<>();
    private ArrayList<RecipeItem> recipes = new ArrayList<>();
    private int mSelectedIndex;
    private String mRecipeName;
    private SimpleExoPlayerView mSimpleExoPlayerView;
    private SimpleExoPlayer mSimpleExoPlayer;
    private RequestedStepClickListener mStepRequestedClickListener;
    private Long mSavedPosition = C.TIME_UNSET;

    public RecipeStepInfoFragment() {
    }

    public interface RequestedStepClickListener {
        void onRecipeStepClick(List<Step> allSteps, int Index, String recipeName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView;
        mHandler = new Handler();
        mDefaultBandwidthMeter = new DefaultBandwidthMeter();

        mStepRequestedClickListener = (RecipeDetailActivity) getActivity();

        if (savedInstanceState == null) {
            mSteps = getArguments().getParcelableArrayList(CakeConstants.SELECTED_STEPS);
            if (mSteps == null) {
                recipes = getArguments().getParcelableArrayList(CakeConstants.SELECTED_RECIPES);
                mSteps = (ArrayList<Step>) recipes.get(0).getSteps();
                mSelectedIndex = 0;
            } else {
                mSelectedIndex = getArguments().getInt(CakeConstants.SELECTED_INDEX);
                mRecipeName = getArguments().getString(CakeConstants.SELECTED_STEP_RECIPE_TITLE);
                mSavedPosition = getArguments().getLong(CakeConstants.SELECTED_POSITION, C.TIME_UNSET);
            }
        } else {
            mSteps = savedInstanceState.getParcelableArrayList(CakeConstants.SELECTED_STEPS);
            mSelectedIndex = savedInstanceState.getInt(CakeConstants.SELECTED_INDEX);
            mRecipeName = savedInstanceState.getString(CakeConstants.SELECTED_STEP_RECIPE_TITLE);
            mSavedPosition = savedInstanceState.getLong(CakeConstants.SELECTED_POSITION, C.TIME_UNSET);
            System.out.println("Aniket3, mSavedPosition: " + mSavedPosition);
        }

        View rootView = inflater.inflate(R.layout.recipe_selected_step_fragment, container, false);
        textView = rootView.findViewById(R.id.recipe_step_detail_text);
        textView.setText(mSteps.get(mSelectedIndex).getDescription());

        mSimpleExoPlayerView = rootView.findViewById(R.id.video_player);
        mSimpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        String videoURL = mSteps.get(mSelectedIndex).getVideoURL();

        if (rootView.findViewWithTag("sw600dp-port-recipe_step_detail") != null) {
            mRecipeName = ((RecipeDetailActivity) getActivity()).recipeName;
//            ((RecipeDetailActivity) getActivity()).getSupportActionBar().setTitle(mRecipeName);
        }

        String imageURL = mSteps.get(mSelectedIndex).getThumbnailURL();
        if (imageURL != null) {
            Uri builtUri = Uri.parse(imageURL)
                    .buildUpon()
                    .build();
            ImageView thumbnail = rootView.findViewById(R.id.step_video_thumbnail);
            Picasso.with(getContext())
                    .load(builtUri)
                    .into(thumbnail);
        }

        if (!videoURL.isEmpty()) {


            initializePlayer(Uri.parse(mSteps.get(mSelectedIndex).getVideoURL()));

            if (rootView.findViewWithTag("sw600dp-land-recipe_step_detail") != null) {
                getActivity()
                        .findViewById(R.id.fragment_container2)
                        .setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
                mSimpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            } else if (isInLandscapeMode(getContext())) {
                textView.setVisibility(View.GONE);
            }
        } else {
            mSimpleExoPlayer = null;
            mSimpleExoPlayerView.setForeground(ContextCompat.getDrawable(
                    getContext(),
                    R.drawable.ic_visibility_off_white_36dp));
            mSimpleExoPlayerView.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
        }


        Button mPrevStep = rootView.findViewById(R.id.recipe_previous_step);
        Button mNextstep = rootView.findViewById(R.id.recipe_next_step);

        mPrevStep.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (mSteps.get(mSelectedIndex).getId() > 0) {
                    if (mSimpleExoPlayer != null) {
                        mSimpleExoPlayer.stop();
                    }
                    mStepRequestedClickListener.onRecipeStepClick(
                            mSteps, mSteps.get(mSelectedIndex).getId() - 1, mRecipeName);
                } else {
                    Toast.makeText(getActivity(),
                            "You already are in the First step of the recipe",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        mNextstep.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int lastIndex = mSteps.size() - 1;
                if (mSteps.get(mSelectedIndex).getId() < mSteps.get(lastIndex).getId()) {
                    if (mSimpleExoPlayer != null) {
                        mSimpleExoPlayer.stop();
                    }
                    mStepRequestedClickListener.onRecipeStepClick(
                            mSteps, mSteps.get(mSelectedIndex).getId() + 1, mRecipeName);
                } else {
                    Toast.makeText(getContext(),
                            "You already are in the Last step of the recipe",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });


        return rootView;
    }


    private void initializePlayer(Uri mediaUri) {
        if (mSimpleExoPlayer == null) {
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(mDefaultBandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(mHandler, videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();

            mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mSimpleExoPlayerView.setPlayer(mSimpleExoPlayer);

            String userAgent = Util.getUserAgent(getContext(), "Baking App");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(), null, null);
            mSimpleExoPlayer.prepare(mediaSource);
            mSimpleExoPlayer.seekTo(mSavedPosition);
            mSimpleExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList(CakeConstants.SELECTED_STEPS, mSteps);
        currentState.putInt(CakeConstants.SELECTED_INDEX, mSelectedIndex);
        currentState.putString(CakeConstants.SELECTED_STEP_RECIPE_TITLE, mRecipeName);
        currentState.putLong(CakeConstants.SELECTED_POSITION, mSavedPosition);
    }


    public boolean isInLandscapeMode(Context context) {
        return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mSimpleExoPlayer != null) {
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSimpleExoPlayer != null) {
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("Aniket3, onResume");
        String videoURL = mSteps.get(mSelectedIndex).getVideoURL();

        if (!videoURL.isEmpty()) {
            initializePlayer(Uri.parse(mSteps.get(mSelectedIndex).getVideoURL()));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mSimpleExoPlayer != null) {
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSimpleExoPlayer != null) {
            mSavedPosition = mSimpleExoPlayer.getCurrentPosition();
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
        }

    }

}