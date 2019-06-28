package com.cibertec.amplyfm.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.cibertec.amplyfm.R;
import com.cibertec.amplyfm.models.Image.ImageResponse;
import com.cibertec.amplyfm.models.Image.Value;
import com.cibertec.amplyfm.models.SimilarArtist;
import com.cibertec.amplyfm.network.GetImage;
import com.cibertec.amplyfm.ui.fragments.SimilarArtistsFragment;
import com.cibertec.amplyfm.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SimilarArtistsRecyclerViewAdapter extends RecyclerView.Adapter<SimilarArtistsRecyclerViewAdapter.ViewHolder> {

    private final SimilarArtist[] similarArtistsList;
    private final SimilarArtistsFragment.OnListFragmentInteractionListener interactionListener;
    Retrofit retrofit ;
    String imageUrl;


    public SimilarArtistsRecyclerViewAdapter(SimilarArtist[] items, SimilarArtistsFragment.OnListFragmentInteractionListener interactionListener) {
        this.similarArtistsList = items;
        this.interactionListener = interactionListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get the layout and inflate
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.similar_artist_prototype, parent, false);
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_BING)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        SimilarArtist dm = similarArtistsList[position];
        holder.similarArtistItem = dm;
        holder.artistNameView.setText(dm.getName());

        GetImage getImage = retrofit.create(GetImage.class);
        Call<ImageResponse> imageResponseCall  = getImage.getImage(dm.getName() + "band");
        imageResponseCall.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                if (response.isSuccessful()) {
                    try {

                        ImageResponse imageResponse = response.body();
                        List<Value> items = imageResponse.getValue();
                        imageUrl = items.get(0).getThumbnailUrl();

                        Glide.with(holder.imageView.getContext())
                                .load(imageUrl)
                                .into(holder.imageView);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {

            }

        });

        holder.mView.setOnClickListener(v -> {
            if (null != interactionListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                interactionListener.onListFragmentInteraction(holder.similarArtistItem);
            }
        });
    }



    @Override
    public int getItemCount() {
        return similarArtistsList.length;
    }


    // Class for prototyping fields we're going to fill
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView artistNameView;
        public final ImageView imageView;
        public SimilarArtist similarArtistItem;

        public ViewHolder(View view) {
            super(view);

            mView = view;
            artistNameView = (TextView) view.findViewById(R.id.tv_similar_artist_name);
            imageView = view.findViewById(R.id.iv_similar_artist_image);
        }
    }
}
