package com.shoker.ahkamquran;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.fragment.app.Fragment;

import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shoker.ahkamquran.dummy.DummyContent;

import java.lang.reflect.Field;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private int position;
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

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            position = getArguments().getInt(ARG_ITEM_ID);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(getResources().getStringArray(R.array.titles_array)[position]);
            }
            audioManager =  (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);


        if (position != -1) {
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder(getResources().getStringArray(R.array.details_array)[position]);
            try {
                Class<R.array> res = R.array.class;
                Field field = res.getField("id_" + position);
                int number_of_links = getResources().getIntArray(field.getInt(null))[0];
                Log.d("tracking" ,"number of links "+ number_of_links);
                int x=1;
                for(int i =1;i<=number_of_links;i++) {
                    int start = getResources().getIntArray(field.getInt(null))[x];
                    int end = getResources().getIntArray(field.getInt(null))[++x];
                    Log.d("tracking" ,"start :"+start +" end : "+end);
                    customTextView(stringBuilder, start, end,position,i);
                    x+=1;

                }
            }catch (Exception e){

            }
            TextView textView = rootView.findViewById(R.id.item_detail);
            textView.setText(stringBuilder);
            textView.setMovementMethod(LinkMovementMethod.getInstance());

        }

        return rootView;
    }

    private void customTextView(SpannableStringBuilder spanTxt, int start , int end, final int position, final int linknumber ){
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                try {
                    Class<R.raw> res = R.raw.class;
                    Field field = res.getField("mp"+String.valueOf(position) + linknumber);
                    releaseMediaPlayer();
                    int result = audioManager.requestAudioFocus(audioFocusChangeListener,AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                    if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                        mediaPlayer = MediaPlayer.create(getContext(), field.getInt(null));
                        mediaPlayer.start();
                        mediaPlayer.setOnCompletionListener(CompletionListener);
                    }
                }catch (Exception e){
                    Log.d("Error","Error in customTextView>ItemDerailFragment");
                }
            }
        },start,end,0);
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            audioManager.abandonAudioFocus(audioFocusChangeListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }
}
