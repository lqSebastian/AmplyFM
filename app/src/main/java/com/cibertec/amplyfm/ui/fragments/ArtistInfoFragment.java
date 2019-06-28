package com.cibertec.amplyfm.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cibertec.amplyfm.R;
import com.cibertec.amplyfm.adapters.ArtistInfoRecyclerViewAdapter;
import com.cibertec.amplyfm.models.Artist;

public class ArtistInfoFragment extends Fragment {

    private static final String KEY_MODEL = "KEY_MODEL";



    private Artist[] artistList;
    private OnListFragmentInteractionListener interactionListener;

    public ArtistInfoFragment(){

    }

    /**
     * Receive the model list
     */
    public static ArtistInfoFragment newInstance(Artist[] artistList) {
        ArtistInfoFragment fragment = new ArtistInfoFragment();
        Bundle args = new Bundle();
        args.putParcelableArray(KEY_MODEL, artistList);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
            throw new RuntimeException("You must to send an artistList ");
        }
        artistList = (Artist[]) getArguments().getParcelableArray(KEY_MODEL);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =   inflater.inflate(R.layout.fragment_artist_info, container, false);

         Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new ArtistInfoRecyclerViewAdapter(artistList, interactionListener));
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


    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Artist item);
    }
}
