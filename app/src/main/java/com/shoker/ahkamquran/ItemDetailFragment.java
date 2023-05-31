package com.shoker.ahkamquran;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
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
    WVJSI wvjsi ;
    ProgressBar progressBar;


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
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        if (position != -1) {
            String s = "file:///android_asset/"+position+".html";
            progressBar = rootView.findViewById(R.id.progressbar);
            WebView webView = rootView.findViewById(R.id.item_detail);
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);

                    progressBar.setVisibility(View.VISIBLE);
                }
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url)
                {
                    view.loadUrl(url);
                    return true;
                }
                @Override
                public void onPageFinished(final WebView view, String url) {

                    progressBar.setVisibility(View.GONE);
                }
            } );
            webView.loadUrl(s);
            WebSettings settings = webView.getSettings();
            settings.setDefaultTextEncodingName("utf-8");
            settings.setJavaScriptEnabled(true);
            wvjsi = new WVJSI(getActivity());
            webView.addJavascriptInterface(wvjsi, "app");

        }
        return rootView;
    }


}

