package com.m68476521.mike.baking.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.m68476521.mike.baking.R;
import com.m68476521.mike.baking.data.TaskContract;
import com.m68476521.mike.baking.data.TaskModelLoader;
import com.m68476521.mike.baking.databinding.ActivityRecipeStepDetailBinding;
import com.squareup.picasso.Picasso;

/**
 * This fragment is used to handle step detail for the recipe
 */

public class RecipeStepDetailFragment extends Fragment implements ExoPlayer.EventListener,
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = RecipeStepDetailFragment.class.getSimpleName();
    private static final String ARG_RECIPE_ID = "recipe_steps";
    private static final String ARG_RECIPE_POSITION = "recipe_position";
    private static final String EXO_PLAYER_STATE_TIME = "last_time_played";

    private Context context;
    private int stepNumber;
    private String recipeId;

    private SimpleExoPlayer exoPlayer;
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;
    private Long timeElapsedPlayed;
    //    private ActivityRecipeStepDetailBinding Binding;
    private ActivityRecipeStepDetailBinding Binding;
    private Cursor cursor;
    private static final int TASK_LOADER_ID = 2;

    private boolean isViewCreated = false;

    public static RecipeStepDetailFragment newInstance(int position, String recipeId) {
        Bundle args = new Bundle();

        args.putInt(ARG_RECIPE_POSITION, position);
        args.putString(ARG_RECIPE_ID, recipeId);
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
        recipeId = getArguments().getString(ARG_RECIPE_ID);
        stepNumber = getArguments().getInt(ARG_RECIPE_POSITION);

        getActivity().getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this).forceLoad();

        if (savedInstanceState != null) {
            timeElapsedPlayed = savedInstanceState.getLong(EXO_PLAYER_STATE_TIME);
        }
        initializeMediaSession();
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart: " + stepNumber);
        super.onStart();
        if (exoPlayer == null && cursor != null) {
            initializePlayer(getVideo(stepNumber));
        }
    }

    @Override
    public void onDestroy() {
        releasePlayer();
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        mediaSession.setActive(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        //TODO this is been calling twice
        //releasePlayer();
        if (exoPlayer == null && cursor != null) {
            initializePlayer(getVideo(stepNumber));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onSTOP");
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Binding = DataBindingUtil.inflate(inflater, R.layout.activity_recipe_step_detail, container,
                false);
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);

        if (!tabletSize && getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            assert Binding.btnNextStep != null;
            Binding.btnNextStep.setOnClickListener((view -> onNext()));
            assert Binding.btnPreviousStep != null;
            Binding.btnPreviousStep.setOnClickListener(view -> onPrevious());
        } else if (tabletSize) {
            assert Binding.btnNextStep != null;
            Binding.btnNextStep.setVisibility(View.GONE);
            assert Binding.btnPreviousStep != null;
            Binding.btnPreviousStep.setVisibility(View.GONE);
        }
        Binding.playerView.setDefaultArtwork(BitmapFactory.decodeResource
                (getResources(), R.drawable.backimage));
        isViewCreated = true;
        if (cursor != null) {
            setBinding(stepNumber);
        }
        return Binding.getRoot();
    }

    private void initializePlayer(Uri mediaUri) {
        releasePlayer();

        if (exoPlayer == null) {
            try {
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                // TODO check this deprecated method
                exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
                Binding.playerView.setPlayer(exoPlayer);
                exoPlayer.addListener(this);
                String userAgent = Util.getUserAgent(context, "ClassicalMusicQuiz");
                MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                        context, userAgent), new DefaultExtractorsFactory(), null, null);
                exoPlayer.prepare(mediaSource);
                exoPlayer.setPlayWhenReady(true);
                playedLastTime();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void playedLastTime() {
        if (timeElapsedPlayed != null) {
            exoPlayer.seekTo(timeElapsedPlayed);
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (exoPlayer != null) {
//            mExoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Snackbar snackbar = Snackbar
                .make(Binding.getRoot(), R.string.video_not_available, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    @Override
    public void onPositionDiscontinuity() {
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == Player.STATE_READY) && playWhenReady) {
            Log.d(TAG, "onPlayerStateChanged: PLAYING");
            stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    exoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == Player.STATE_READY)) {
            Log.d(TAG, "onPlayerStateChanged: PAUSED");
            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    exoPlayer.getCurrentPosition(), 1f);
        }
        mediaSession.setPlaybackState(stateBuilder.build());
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (exoPlayer != null) {
            outState.putLong(EXO_PLAYER_STATE_TIME, exoPlayer.getCurrentPosition());
        }
        super.onSaveInstanceState(outState);
    }

    private void initializeMediaSession() {
        // Create a MediaSessionCompat.
        mediaSession = new MediaSessionCompat(getActivity(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mediaSession.setPlaybackState(stateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mediaSession.setActive(true);

    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            Log.d(TAG, "PLAY");
            exoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            Log.d(TAG, "PAUSE");
            exoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            exoPlayer.seekTo(0);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new TaskModelLoader(getActivity(), 1, recipeId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursor = data;

        if (isViewCreated) {
            setBinding(stepNumber);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private Uri getVideo(int position) {
        cursor.moveToPosition(position);
        int indexVideo = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_STEP_VIDEO);
        String video = cursor.getString(indexVideo);

        if (TextUtils.isEmpty(video)) {
            video = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_STEP_IMAGE));
            if (TextUtils.isEmpty(video)) {
                boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
                if (!tabletSize) {
                    Snackbar snackbar = Snackbar
                            .make(Binding.getRoot(), R.string.video_not_available, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                return null;
            }
            String format = video.substring(video.length() - 4);
            Log.d(TAG, format);
            //TODO check for more image formats
            if (format.equals(".png") || format.equals("jpeg")) {
                Binding.playerView.setVisibility(View.INVISIBLE);
                Binding.imageViewBackground.setVisibility(View.VISIBLE);
                Picasso.with(getActivity())
                        .load(video)
                        .placeholder(R.mipmap.ic_launcher)
                        .into(Binding.imageViewBackground);
                return null;
            }
        }

        if (Binding.playerView.getVisibility() == View.INVISIBLE) {
            Binding.imageViewBackground.setVisibility(View.INVISIBLE);
            Binding.playerView.setVisibility(View.VISIBLE);
        }
        return Uri.parse(video);
    }

    private void setBinding(int currentPagePosition) {
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        cursor.moveToPosition(currentPagePosition);

        int indexSortDesc = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_STEP_SORT_DESC);
        String sortDesc = cursor.getString(indexSortDesc);

        int indexDesc = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_STEP_DESC);
        String desc = cursor.getString(indexDesc);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (!tabletSize) {
                assert Binding.toolbar != null;
                Binding.toolbar.setTitle(R.string.app_name);
                Binding.toolbar.setNavigationOnClickListener(view -> getActivity().onBackPressed());
            }
            assert Binding.shortDescription != null;
            Binding.shortDescription.setText(sortDesc);
            assert Binding.description != null;
            Binding.description.setText(desc);
        } else {
            if (!tabletSize) {

                Binding.getRoot().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
            } else {
                assert Binding.shortDescription != null;
                Binding.shortDescription.setText(sortDesc);
                assert Binding.description != null;
                Binding.description.setText(desc);
            }
        }
        initializePlayer(getVideo(stepNumber));
    }

    private void onPrevious() {
        if (stepNumber >= 1) {
            stepNumber = stepNumber - 1;
            setBinding(stepNumber);
        }
    }

    private void onNext() {
        if (stepNumber < cursor.getCount() - 1) {
            stepNumber = stepNumber + 1;
            setBinding(stepNumber);
        }
    }
}
