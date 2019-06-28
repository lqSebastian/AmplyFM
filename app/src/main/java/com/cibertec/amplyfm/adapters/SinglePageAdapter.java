package com.cibertec.amplyfm.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cibertec.amplyfm.models.FavoriteTracks.FavoriteItem;
import com.cibertec.amplyfm.ui.fragments.FavoritesFragment;

public class SinglePageAdapter extends FragmentStatePagerAdapter {

    private int numTabs;
    private FavoriteItem[] favoriteItems;

    public SinglePageAdapter(FragmentManager fm, int numTabs,
                             FavoriteItem[] favoriteItems
                             ) {
        super(fm);
        this.numTabs = numTabs;
        this.favoriteItems = favoriteItems;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FavoritesFragment tab1 = FavoritesFragment.newInstance(favoriteItems);
                return tab1;
            default:
                throw new RuntimeException("Tab position invalid " + position);
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
