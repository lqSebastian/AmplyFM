package com.cibertec.amplyfm.adapters;

import android.os.AsyncTask;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cibertec.amplyfm.R;
import com.cibertec.amplyfm.models.Image.ImageResponse;
import com.cibertec.amplyfm.models.Image.Value;
import com.cibertec.amplyfm.models.Track;
import com.cibertec.amplyfm.models.TrackInfoResponse;
import com.cibertec.amplyfm.network.GetImage;
import com.cibertec.amplyfm.network.GetTrackInfo;
import com.cibertec.amplyfm.ui.fragments.TopTracksFragment;
import com.cibertec.amplyfm.utils.Constants;
import com.cibertec.amplyfm.utils.DurationConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrackRecyclerViewAdapter extends RecyclerView.Adapter<TrackRecyclerViewAdapter.ViewHolder> {

    // trackList para llenar el fragment
    private final Track[] trackList;
    private final TopTracksFragment.OnListFragmentInteractionListener interactionListener;
    public int position;
    static Retrofit retrofitBing;

    static Retrofit retrofitLastFM;
    static String imageUrl;

    private AsyncTask mTask;

    Call<TrackInfoResponse> trackInfoCall;
    Call<ImageResponse> imageResponseCall;
    List<Integer> currentCalls = new ArrayList<>();
    private Boolean CANCEL_ALL = false;

    public TrackRecyclerViewAdapter(Track[] items, TopTracksFragment.OnListFragmentInteractionListener listener) {
        try {
            if (trackInfoCall != null && trackInfoCall.isExecuted()) {
                trackInfoCall.cancel();
            }
            if (imageResponseCall != null && imageResponseCall.isExecuted()) {
                imageResponseCall.cancel();
            }

            //cancelar todos los async por si se hacen busquedas seguidas en corto plazo
            mTask.cancel(true);
        } catch (Exception e) {
        }
        trackList = items;
        interactionListener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get the layout and inflate

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.track_prototype, parent, false);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        retrofitLastFM = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        retrofitBing = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_BING)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        return new ViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Track dm = trackList[position];

        holder.trackItem = dm;

        holder.trackNameView.setText(dm.getName());
        holder.playCountView.setText(String.valueOf(dm.getPlaycount()));


        if (dm.getImgUrl() != null) {

            holder.trackItem.setAlbum(dm.getAlbum());

            String albumTitle;
            try {
                albumTitle = dm.getAlbum().getTitle();

            } catch (Exception e) {
                albumTitle = "";
            }
            holder.albumView.setText(albumTitle);

            String duration;
            try {
                duration = DurationConverter.getDurationInMinutesText(Long.parseLong(dm.getDuration()));

            } catch (Exception e) {
                duration = dm.getDuration();
            }
            holder.durationView.setText(duration);

            if (holder.imageView.getDrawable() == null && dm.getImgUrl() != null) {
                Glide.with(holder.imageView.getContext())
                        .load(dm.getImgUrl())
                        .into(holder.imageView);
                holder.progressBar.setVisibility(View.GONE);
            }


        } else {
            mTask = new LongOperation(position, trackList).execute();

        }

        holder.mView.setOnClickListener(v -> {
            if (null != interactionListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                interactionListener.onListFragmentInteraction(holder.trackItem);
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            setPosition(holder.getAdapterPosition());
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return trackList.length;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Track getItem(int position) {
        return trackList[position];
    }

    public void updateTrackList(Track dm, int pos) {
        trackList[pos] = dm;
        this.notifyDataSetChanged();
    }


    // Class for prototyping fields we're going to fill
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public final View mView;
        public final TextView trackNameView;
        public final TextView playCountView;
        public final TextView albumView;
        public final ImageView imageView;
        public final TextView durationView;
        public final ProgressBar progressBar;
        public Track trackItem;

        public ViewHolder(View view) {
            super(view);

            mView = view;
            albumView = (TextView) view.findViewById(R.id.tv_track_album);
            trackNameView = (TextView) view.findViewById(R.id.tv_track_name);
            playCountView = view.findViewById(R.id.tv_plays);
            imageView = view.findViewById(R.id.iv_track);
            durationView = (TextView) view.findViewById(R.id.tv_duration);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);

            view.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0, 1, 1, "Añadir a favoritos");
            menu.add(0, 2, 0, "Ver letra");
        }
    }

    private class LongOperation extends AsyncTask<String, Void, String> {
        int positionAsync;
        Track[] tracksAsync;

        Track dm;

        public LongOperation(int position, Track[] tracksAsync) {
            this.positionAsync = position;
            this.tracksAsync = tracksAsync;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (currentCalls.contains(positionAsync) || CANCEL_ALL) {
                    return "false";
                } else {
                    currentCalls.add(positionAsync);
                }

                dm = tracksAsync[positionAsync];
                GetTrackInfo getTrackInfo = retrofitLastFM.create(GetTrackInfo.class);
                trackInfoCall = getTrackInfo.getTrackInfo(dm.getArtist().getName(), dm.getName(), Constants.API_KEY);

                TrackInfoResponse trackInfoResponse = trackInfoCall.execute().body();
                dm = trackInfoResponse.getTrack();
                String albumTitle;
                try {
                    albumTitle = dm.getAlbum().getTitle();

                } catch (Exception e) {
                    albumTitle = "";
                }

                GetImage getImage = retrofitBing.create(GetImage.class);

                imageResponseCall = getImage.getImage(dm.getArtist().getName() + " " + albumTitle);
                try {
                    ImageResponse imageResponse = imageResponseCall.execute().body();
                    List<Value> items = imageResponse.getValue();
                    imageUrl = items.get(0).getThumbnailUrl();

                } catch (final java.net.SocketTimeoutException e) {
                    ImageResponse imageResponse = imageResponseCall.execute().body();
                    List<Value> items = imageResponse.getValue();
                    imageUrl = items.get(0).getThumbnailUrl();

                    e.printStackTrace();
                }
                dm.setImgUrl(imageUrl);

                tracksAsync[positionAsync] = dm;

                Log.d("N° DE ITEM REQUEST", String.valueOf(positionAsync));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return imageUrl;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null && result.equals("false")) {

            } else {
                updateTrackList(trackList[positionAsync], positionAsync);

            }

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    public void onDestroy() {
        try {
            if (trackInfoCall != null && trackInfoCall.isExecuted()) {
                trackInfoCall.cancel();
            }
            if (imageResponseCall != null && imageResponseCall.isExecuted()) {
                imageResponseCall.cancel();
            }
            CANCEL_ALL = true;

            //cancelar todos los async por si se hacen busquedas seguidas en corto plazo
            mTask.cancel(true);
        } catch (Exception e) {
        }
    }


}




