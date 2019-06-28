package com.cibertec.amplyfm.ui.fragments;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cibertec.amplyfm.AppDatabase;
import com.cibertec.amplyfm.R;
import com.cibertec.amplyfm.adapters.TrackRecyclerViewAdapter;
import com.cibertec.amplyfm.models.FavoriteTracks.FavoriteItem;
import com.cibertec.amplyfm.models.Lyrics.Lyrics;
import com.cibertec.amplyfm.models.TopTracksResponse;
import com.cibertec.amplyfm.models.Track;
import com.cibertec.amplyfm.network.GetLyrics;
import com.cibertec.amplyfm.network.TopArtistTracksService;
import com.cibertec.amplyfm.ui.MainActivity;
import com.cibertec.amplyfm.ui.dialogs.LyricsDialog;
import com.cibertec.amplyfm.utils.Constants;
import com.cibertec.amplyfm.utils.DialogFactory;
import com.cibertec.amplyfm.utils.ImageSaver;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TopTracksFragment extends Fragment {

    private static final String KEY_MODEL = "KEY_MODEL";

    private Track[] trackList;
    private OnListFragmentInteractionListener interactionListener;
    RecyclerView recyclerView;
    public TopTracksFragment(){

    }

    /**
     * Receive the model list
     */
    public static TopTracksFragment newInstance(Track[] trackList) {
        TopTracksFragment fragment = new TopTracksFragment();
        Bundle args = new Bundle();
        args.putParcelableArray(KEY_MODEL, trackList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
            throw new RuntimeException("You must to send a trackList ");
        }
        trackList = (Track[]) getArguments().getParcelableArray(KEY_MODEL);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =   inflater.inflate(R.layout.fragment_top_artist_tracks, container, false);

        Context context = view.getContext();
         recyclerView = (RecyclerView) view;

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new TrackRecyclerViewAdapter(trackList, interactionListener));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // activity must implement OnListFragmentInteractionListener
        if (context instanceof OnListFragmentInteractionListener) {
            interactionListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Track item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = -1;
        try {
            position = ((TrackRecyclerViewAdapter)recyclerView.getAdapter()).getPosition();
        } catch (Exception e) {
            Log.d("??????", e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        int itemId = item.getItemId();
        Track trackItem;
        switch (item.getItemId()) {
            case 1:
                 trackItem =  trackList[position];

                // save the image in Storage
                // i saved the img url in album class as seen in TrackRVAdapter

        if ( SaveFavorite(trackItem)){
                DialogFactory.success_toast(getContext(),trackItem.getName() + " - añadido a favoritos").show();

            }
                break;
            case 2:
                DialogFactory.loading_toast( getActivity(),"Obteniendo letra").show();

                trackItem =  trackList[position];
                Retrofit retrofit  = new Retrofit.Builder()
                        .baseUrl(Constants.BASE_URL_OVH)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                GetLyrics getLyrics =  retrofit.create(GetLyrics.class);
                Call<Lyrics> searchLyrics = getLyrics.getLyrics(MainActivity.CURRENT_ARTIST ,trackItem.getName());

                searchLyrics.enqueue(new Callback<Lyrics>() {
                    @Override
                    public void onResponse(Call<Lyrics> call, Response<Lyrics> response) {
                        Lyrics lyrics = response.body();

                        if (lyrics == null){
                            DialogFactory.warning_toast(getActivity(),"No se encontró letra para esta canción").show();
                        }else{
                            LyricsDialog lyricsDialog = LyricsDialog.newInstance(lyrics.getLyrics(),"Letra de " + trackItem.getName());
                            lyricsDialog.setCancelable(false);
                            lyricsDialog.show(getFragmentManager(),"LyricsDialog");
                        }
                    }

                    @Override
                    public void onFailure(Call<Lyrics> call, Throwable t) {
                        DialogFactory.warning_toast(getActivity(),"No se encontró letra para esta canción").show();
                    }
                });

                break;
        }
        return true;
    }

    public boolean SaveFavorite(Track trackItem){

        try {
            String trackId;

            if(trackItem.getMbid() == null){
                trackId = UUID.randomUUID().toString();
             }
            else {
                trackId = trackItem.getMbid() + ".png";
            }

                if(trackItem.getUrl() != "") {
                    Glide.with(getActivity())
                            .asBitmap()
                            .load(trackItem.getUrl())
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    new ImageSaver(getActivity()).
                                            setFileName(trackId).
                                            setDirectoryName("AmplyFMLocalImages").
                                            save(resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                }
                            });
            }

            String albumName = "-";
        if(trackItem.getAlbum() != null){
           albumName = trackItem.getAlbum().getTitle();

            }


        FavoriteItem favoriteItem = new FavoriteItem(trackId, trackItem.getName(),
                albumName,trackItem.getPlaycount(),trackItem.getDuration(), MainActivity.CURRENT_ARTIST,trackId);

        AppDatabase.getInstance(getActivity()).favoriteDao().insertFavorites(favoriteItem);

        }catch (Exception e){

            DialogFactory.error_toast(getActivity(),"Ocurrió un error al guardar").show();
            e.printStackTrace();
            return  false;
        }

        return  true;
    }
}

