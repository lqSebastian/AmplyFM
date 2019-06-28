package com.cibertec.amplyfm.ui.fragments;

import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cibertec.amplyfm.AppDatabase;
import com.cibertec.amplyfm.R;
import com.cibertec.amplyfm.adapters.FavoritesRecyclerViewAdapter;
import com.cibertec.amplyfm.models.FavoriteTracks.FavoriteItem;
import com.cibertec.amplyfm.models.Track;
import com.cibertec.amplyfm.ui.FavoritesActivity;
import com.cibertec.amplyfm.utils.DialogFactory;

public class FavoritesFragment extends Fragment {

    private static final String KEY_MODEL = "KEY_MODEL";

    private FavoriteItem[] favoriteItems;
    private OnListFragmentInteractionListener interactionListener;
    RecyclerView recyclerView;

    public FavoritesFragment() {
    }
    /**
     * Receive the model list
     */
    public static FavoritesFragment newInstance(FavoriteItem[] items) {
        FavoritesFragment fragment = new FavoritesFragment();
        Bundle args = new Bundle();
        args.putParcelableArray(KEY_MODEL, items);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
            throw new RuntimeException("You must to send a favoriteList ");
        }
        favoriteItems = (FavoriteItem[]) getArguments().getParcelableArray(KEY_MODEL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_artist_tracks, container, false);

        Context context = view.getContext();
        recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new FavoritesRecyclerViewAdapter(favoriteItems, interactionListener));
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


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = -1;
        try {
            position = ((FavoritesRecyclerViewAdapter) recyclerView.getAdapter()).getPosition();
        } catch (Exception e) {
            Log.d("??????", e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        int itemId = item.getItemId();
        FavoriteItem favoriteItem;
        switch (itemId) {
            case 3:
                favoriteItem = favoriteItems[position];
                try {
                    AppDatabase.getInstance(getActivity()).favoriteDao().deleteFavorites(favoriteItem);
                } catch (Exception e) {
                    DialogFactory.warning_toast(getContext(), "Ocurrió un error al eliminar");
                    e.printStackTrace();
                    break;
                }
                DialogFactory.success_toast(getContext(), favoriteItem.getName() + " - eliminado").show();
                // refresh Activity
                ((FavoritesActivity) getActivity()).loadItems();
                break;
        }
        return true;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(FavoriteItem item);
    }


}

