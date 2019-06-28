package com.cibertec.amplyfm.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cibertec.amplyfm.models.Artist;
import com.cibertec.amplyfm.models.SimilarArtist;
import com.cibertec.amplyfm.models.Track;
import com.cibertec.amplyfm.ui.fragments.ArtistInfoFragment;
import com.cibertec.amplyfm.ui.fragments.*;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int numTabs;
    private Track[] trackList;
    private Artist artist;
    private SimilarArtist[] similarArtistList;

    public PagerAdapter(FragmentManager fm, int numTabs,
                        Track[] trackList ,
                        Artist artist,
                        SimilarArtist[] similarArtistList) {
        super(fm);
        this.numTabs = numTabs;
        this.trackList = trackList;
        this.artist = artist;
        this.similarArtistList = similarArtistList;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TopTracksFragment tab1 = TopTracksFragment.newInstance(trackList);
                return tab1;
          case 1:
              Artist[] artists = { artist};
              ArtistInfoFragment tab2 = ArtistInfoFragment.newInstance(artists);
                return tab2;
            case 2:
                SimilarArtistsFragment tab3 = SimilarArtistsFragment.newInstance(similarArtistList);
                return tab3;
            default:
                throw new RuntimeException("Tab position invalid " + position);
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }

    public Track getTrack(int position){

        return  trackList[position];
    }

}
