package com.cibertec.amplyfm.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cibertec.amplyfm.R;
import com.cibertec.amplyfm.models.Artist;
import com.cibertec.amplyfm.models.Image.ImageResponse;
import com.cibertec.amplyfm.models.Image.Value;
import com.cibertec.amplyfm.network.GetImage;
import com.cibertec.amplyfm.ui.fragments.ArtistInfoFragment;
import com.cibertec.amplyfm.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArtistInfoRecyclerViewAdapter  extends RecyclerView.Adapter<ArtistInfoRecyclerViewAdapter.ViewHolder> {

    private final Artist[] artistsList;
    private final ArtistInfoFragment.OnListFragmentInteractionListener interactionListener;
    Retrofit retrofit ;
    String imageUrl;

    public ArtistInfoRecyclerViewAdapter(Artist[] items, ArtistInfoFragment.OnListFragmentInteractionListener interactionListener) {
        this.artistsList = items;
        this.interactionListener = interactionListener;
    }


    @NonNull
    @Override
    public ArtistInfoRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // get the layout and inflate

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.artist_info_prototype, parent, false);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_BING)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return new ArtistInfoRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Artist dm = artistsList[position];
        holder.artistItem = dm;
        holder.artistNameView.setText(dm.getName());
        holder.listenersView.setText(String.valueOf(dm.getStats().getListeners()));
        holder.bioView.setText(dm.getBio().getSummary());

        GetImage getImage = retrofit.create(GetImage.class);
            Call<ImageResponse> imageResponseCall  = getImage.getImage(dm.getName() + " band");

        imageResponseCall.enqueue(new Callback<ImageResponse>() {
                @Override
                public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                    if (response.isSuccessful()) {
                        ImageResponse imageResponse = response.body();
                        List<Value> items = imageResponse.getValue();
                        imageUrl = items.get(0).getThumbnailUrl();

                        Log.d("IMAGE URL :","" + imageUrl);

                        Glide.with(holder.artistImageView.getContext()) // context del img
                                .load(imageUrl)
                                .into(holder.artistImageView);
                    }

                }
                @Override
                public void onFailure(Call<ImageResponse> call, Throwable t) {

                }

        });



        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != interactionListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    interactionListener.onListFragmentInteraction(holder.artistItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return artistsList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView artistNameView;
        public final TextView listenersView;
        public final ImageView artistImageView;
        public final TextView bioView;
        public Artist artistItem;

        public ViewHolder(View view) {
            super(view);

            mView = view;
            artistNameView = (TextView) view.findViewById(R.id.tv_artist_name);
            listenersView = (TextView) view.findViewById(R.id.tv_artist_listeners_count);
            artistImageView = view.findViewById(R.id.iv_artist_picture);
            bioView = view.findViewById(R.id.et_artist_bio);
        }
    }
}
