package com.shoker.ahkamquran;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import java.lang.reflect.Field;

public class WVJSI {
    private Activity activity;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener(){
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                mediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                releaseMediaPlayer();
            }
        }
    };

    private MediaPlayer.OnCompletionListener CompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };


    public WVJSI(Activity activity) {
        this.activity = activity;
        audioManager =  (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
    }

    public void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            audioManager.abandonAudioFocus(audioFocusChangeListener);
        }
    }



    @JavascriptInterface
    public void startTrack(String  i) {
        try {
            Class<R.raw> res = R.raw.class;
            Field field = res.getField(i);
            releaseMediaPlayer();
            int result = audioManager.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                mediaPlayer = MediaPlayer.create(activity, field.getInt(null));
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(CompletionListener);
            }
        } catch (Exception e) {
            Log.d("Error", "Error in customTextView>ItemDerailFragment");
        }

    }

}
