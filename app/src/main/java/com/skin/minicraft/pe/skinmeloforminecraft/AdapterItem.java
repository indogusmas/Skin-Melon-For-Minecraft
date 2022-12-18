package com.skin.minicraft.pe.skinmeloforminecraft;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.bumptech.glide.Glide;
import com.skin.minicraft.pe.skinmeloforminecraft.databinding.ItemAdsBinding;
import com.skin.minicraft.pe.skinmeloforminecraft.databinding.ItemSkinBinding;

import java.util.ArrayList;
import java.util.List;


public class AdapterItem extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ItemSkin> items = new ArrayList<>();
    private int TYPE_DATA = 1;
    private int TYPE_OTHER = 0;
    private OnClikcListener onClikcListener;

    public AdapterItem(List<ItemSkin> items, OnClikcListener onClikcListener) {
        this.items = items;
        this.onClikcListener = onClikcListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_DATA) {
            return new ViewHolderItem(ItemSkinBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new ViewHolderAds(ItemAdsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_DATA) {
            ViewHolderItem viewHolderItem = (ViewHolderItem) holder;
            viewHolderItem.bind(items.get(position));
        } else {
            ViewHolderAds viewHolderAds = (ViewHolderAds) holder;
            viewHolderAds.createNativeAd();
        }
    }

    public void setItems(List<ItemSkin> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getId() == 0) {
            return TYPE_OTHER;
        } else {
            return TYPE_DATA;
        }
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public class ViewHolderItem extends RecyclerView.ViewHolder {

        private ItemSkinBinding mbinding;

        public ViewHolderItem(ItemSkinBinding binding) {
            super(binding.getRoot());
            this.mbinding = binding;
        }

        public void bind(ItemSkin item) {
            Log.d(getClass().getSimpleName(), "bind: " + item.getSkinView());
            Glide.with(mbinding.getRoot())
                    .load(item.getSkinView())
                    .into(mbinding.image);
            mbinding.tvName.setText(item.getName());
            mbinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClikcListener.onClickItem(item);
                }
            });
        }
    }

    interface OnClikcListener {
        void onClickItem(ItemSkin item);
    }

    public class ViewHolderAds extends RecyclerView.ViewHolder {

        private ItemAdsBinding mbinding;
        private MaxNativeAdLoader nativeAdLoader;
        private MaxAd nativeAd;


        public ViewHolderAds(ItemAdsBinding binding) {
            super(binding.getRoot());
            this.mbinding = binding;
        }

        void createNativeAd()
        {
            String id = BuildConfig.DEBUG ? "text" : mbinding.getRoot().getContext().getString(R.string.applovin_native);
            nativeAdLoader = new MaxNativeAdLoader( id, mbinding.getRoot().getContext() );
            nativeAdLoader.setNativeAdListener( new MaxNativeAdListener()
            {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad)
                {
                    // Clean up any pre-existing native ad to prevent memory leaks.
                    if ( nativeAd != null )
                    {
                        nativeAdLoader.destroy( nativeAd );
                    }

                    // Save ad for cleanup.
                    nativeAd = ad;

                    // Add ad view to view.
                    mbinding.frameAds.removeAllViews();
                    mbinding.frameAds.addView( nativeAdView );
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error)
                {
                    // We recommend retrying with exponentially higher delays up to a maximum delay
                }

                @Override
                public void onNativeAdClicked(final MaxAd ad)
                {
                    // Optional click callback
                }
            } );

            nativeAdLoader.loadAd();
        }


    }
}