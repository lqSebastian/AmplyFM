package com.cibertec.amplyfm.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cibertec.amplyfm.R;
import com.cibertec.amplyfm.adapters.SimilarArtistsRecyclerViewAdapter;
import com.cibertec.amplyfm.models.SimilarArtist;

public class SimilarArtistsFragment extends Fragment {

    private static final String KEY_MODEL = "KEY_MODEL";

    private SimilarArtist[] similarArtists;

    private OnListFragmentInteractionListener interactionListener;

    public SimilarArtistsFragment(){

    }
    /**
     * Receive the model list
     */
    public static SimilarArtistsFragment newInstance(SimilarArtist[] similarArtists) {
        SimilarArtistsFragment fragment = new SimilarArtistsFragment();
        Bundle args = new Bundle();
        args.putParcelableArray(KEY_MODEL, similarArtists);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
            throw new RuntimeException("You must to send a trackList ");
        }
        similarArtists = (SimilarArtist[]) getArguments().getParcelableArray(KEY_MODEL);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =   inflater.inflate(R.layout.fragment_top_artist_tracks, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new GridLayoutManager(context,2));
        recyclerView.setAdapter(new SimilarArtistsRecyclerViewAdapter(similarArtists, interactionListener));
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // activity must implement OnListFragmentInteractionListener
        if (context instanceof SimilarArtistsFragment.OnListFragmentInteractionListener) {
            interactionListener = (SimilarArtistsFragment.OnListFragmentInteractionListener) context;
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


    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(SimilarArtist item);
    }

}
