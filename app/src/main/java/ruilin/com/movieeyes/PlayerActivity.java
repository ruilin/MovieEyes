package ruilin.com.movieeyes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import ruilin.com.movieeyes.widget.UniversalVideoView.UniversalMediaController;
import ruilin.com.movieeyes.widget.UniversalVideoView.UniversalVideoView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class PlayerActivity extends AppCompatActivity implements UniversalVideoView.VideoViewCallback {
    private final String TAG = PlayerActivity.class.getSimpleName();
    private final static String EXTRA_NAME_TITLE = "title";
    private final static String EXTRA_NAME_URL = "url";

    private UniversalVideoView mVideoView;
    private UniversalMediaController mMediaController;

    private String url;
    private String title;

    public static void start(Activity act, String title, String url) {
        Intent intent = new Intent(act, PlayerActivity.class);
        intent.putExtra(EXTRA_NAME_TITLE, title);
        intent.putExtra(EXTRA_NAME_URL, url);
        act.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Intent intent = getIntent();
        title = intent.getStringExtra(EXTRA_NAME_TITLE);
        url = intent.getStringExtra(EXTRA_NAME_URL);

        mVideoView = (UniversalVideoView) findViewById(R.id.videoView);
        mMediaController = (UniversalMediaController) findViewById(R.id.media_controller);
        mVideoView.setVideoViewCallback(this);
        mVideoView.setOnCompletionListener(new IjkMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {
                close();
            }
        });
        mMediaController.setTitle(title);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoPath(url);
        mVideoView.requestFocus();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void close() {
        if (mVideoView != null) {
            mVideoView.stopPlayback();
        }
        finish();
    }


    @Override
    public void onScaleChange(boolean isFullscreen) {
    }

    @Override
    public void onPause(IMediaPlayer mediaPlayer) {
        Log.d(TAG, "onPause UniversalVideoView callback");
    }

    @Override
    public void onStart(IMediaPlayer mediaPlayer) {
        Log.e(TAG, "onStart UniversalVideoView callback");
    }

    @Override
    public void onBufferingStart(IMediaPlayer mediaPlayer) {
        Log.e(TAG, "onBufferingStart UniversalVideoView callback");
    }

    @Override
    public void onBufferingEnd(IMediaPlayer mediaPlayer) {
        Log.d(TAG, "onBufferingEnd UniversalVideoView callback");
    }
}
