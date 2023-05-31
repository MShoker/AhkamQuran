package com.shoker.ahkamquran;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

import java.util.List;

import static com.shoker.ahkamquran.ItemListActivity.interstitialAd;

public class TitlesAdapter extends RecyclerView.Adapter<TitlesAdapter.ViewHolder>{

        private  ItemListActivity mParentActivity;
        private  List<String> mValues;
        private  boolean mTwoPane;
        private int position;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position =(int)view.getTag();
                if (mTwoPane) {
                    if (interstitialAd.isLoaded()) {
                       interstitialAd.show();
                        interstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                interstitialAd.loadAd(new AdRequest.Builder().build());
                                reLoadFragmentForTwopan(position);
                                super.onAdClosed();
                            }
                        });
                    }else{
                        reLoadFragmentForTwopan(position);
                    }
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID,position);
                    context.startActivity(intent);
                }
            }
        };

    TitlesAdapter(ItemListActivity parent,List<String> items,boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position));
            Typeface font = Typeface.createFromAsset(holder.mIdView.getContext().getAssets(), "fonts/othman.otf");
            holder.mIdView.setTypeface(font,Typeface.BOLD);

            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(mOnClickListener);

        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.tv_title);

            }
        }

        private void reLoadFragmentForTwopan(int mposition){
            Bundle arguments = new Bundle();
            arguments.putInt(ItemDetailFragment.ARG_ITEM_ID,mposition);
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            mParentActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commitAllowingStateLoss();
        }

    }