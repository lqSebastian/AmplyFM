package com.cibertec.amplyfm.adapters;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cibertec.amplyfm.R;
import com.cibertec.amplyfm.models.Album;
import com.cibertec.amplyfm.models.FavoriteTracks.FavoriteItem;
import com.cibertec.amplyfm.models.Track;
import com.cibertec.amplyfm.ui.fragments.FavoritesFragment;
import com.cibertec.amplyfm.ui.fragments.TopTracksFragment;
import com.cibertec.amplyfm.utils.DurationConverter;
import com.cibertec.amplyfm.utils.ImageSaver;

public class FavoritesRecyclerViewAdapter   extends RecyclerView.Adapter<FavoritesRecyclerViewAdapter.ViewHolder>{

    private final FavoriteItem[] favoriteItems;
    private final FavoritesFragment.OnListFragmentInteractionListener interactionListener;
    public int position;

    public FavoritesRecyclerViewAdapter(FavoriteItem[] favoriteItems, FavoritesFragment.OnListFragmentInteractionListener interactionListener) {
        this.favoriteItems = favoriteItems;
        this.interactionListener = interactionListener;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.track_prototype, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( FavoritesRecyclerViewAdapter.ViewHolder holder, int position) {
        FavoriteItem dm = favoriteItems[position];
        holder.favoriteItem = dm;
        holder.trackNameView.setText(String.format("%s - %s", dm.getArtist(), dm.getName()));
        holder.playCountView.setText(String.valueOf(dm.getPlaycount()));
        holder.albumView.setText(dm.getAlbum());
        holder.durationView.setText(dm.getDuration());

        Bitmap bitmap = new ImageSaver(holder.imageView.getContext()).
                setFileName(dm.getImageDir()).
                setDirectoryName("AmplyFMLocalImages").
                load();

        holder.imageView.setImageBitmap(bitmap);
        holder.mView.setOnClickListener(v -> {
            if (null != interactionListener) {
               // Album album = new Album();
                //album.setTitle(dm.getAlbum());
                //Track(String duration, String mbid, String name, String playcount, Album album)
               // Track track = new Track(dm.getDuration(),String.valueOf(dm.getId()),dm.getName(),dm.getPlaycount() ,album);
                interactionListener.onListFragmentInteraction(holder.favoriteItem);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getAdapterPosition());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteItems.length;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public class ViewHolder  extends RecyclerView.ViewHolder implements  View.OnCreateContextMenuListener {
        public final View mView;
        public final TextView trackNameView;
        public final TextView playCountView;
        public final TextView albumView;
        public final TextView durationView;
        public final ImageView imageView;
        public FavoriteItem favoriteItem;

        public ViewHolder(View view) {
            super(view);

            mView = view;

            imageView = view.findViewById(R.id.iv_track);
            albumView = (TextView) view.findViewById(R.id.tv_track_album);
            trackNameView = (TextView) view.findViewById(R.id.tv_track_name);
            playCountView = view.findViewById(R.id.tv_plays);
            durationView = (TextView) view.findViewById(R.id.tv_duration);

            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0,3 , 1, "Eliminar");
        }
    }
}
