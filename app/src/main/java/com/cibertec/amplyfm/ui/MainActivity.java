package com.cibertec.amplyfm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.cibertec.amplyfm.R;
import com.cibertec.amplyfm.adapters.PagerAdapter;
import com.cibertec.amplyfm.models.Artist;
import com.cibertec.amplyfm.models.ArtistInfoResponse;
import com.cibertec.amplyfm.models.Search.Artistmatches;
import com.cibertec.amplyfm.models.Search.Results;
import com.cibertec.amplyfm.models.Search.SearchResults;
import com.cibertec.amplyfm.models.SimilarArtist;
import com.cibertec.amplyfm.models.SimilarArtistResponse;
import com.cibertec.amplyfm.models.TopTracks;
import com.cibertec.amplyfm.models.TopTracksResponse;
import com.cibertec.amplyfm.models.Track;
import com.cibertec.amplyfm.models.Youtube.Id;
import com.cibertec.amplyfm.models.Youtube.Item;
import com.cibertec.amplyfm.models.Youtube.YoutubeUrl;
import com.cibertec.amplyfm.network.GetArtistInfo;
import com.cibertec.amplyfm.network.SearchArtist;
import com.cibertec.amplyfm.network.TopArtistTracksService;
import com.cibertec.amplyfm.network.YoutubeVideoIdService;
import com.cibertec.amplyfm.ui.fragments.ArtistInfoFragment;
import com.cibertec.amplyfm.ui.fragments.SimilarArtistsFragment;
import com.cibertec.amplyfm.ui.fragments.TopTracksFragment;
import com.cibertec.amplyfm.utils.Constants;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.preference.PowerPreference;

import java.util.ArrayList;
import java.util.List;

import br.com.mauker.materialsearchview.MaterialSearchView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity  implements TopTracksFragment.OnListFragmentInteractionListener,
        SimilarArtistsFragment.OnListFragmentInteractionListener,
        ArtistInfoFragment.OnListFragmentInteractionListener {

    public static String CURRENT_ARTIST;

    @BindView(R.id.tabl_main)
    TabLayout mTabLayout;
    @BindView(R.id.vp_main)
    ViewPager mViewPager;

    @BindView(R.id.edt_search)
    EditText edt_search;

    //MainPagerAdapter mAdapter;
    @BindView(R.id.youtube_player_view)
    YouTubePlayerView youTubePlayerView;

    @BindView(R.id.progressBar_cyclic)
    ProgressBar progressBar;

    Retrofit retrofit;

     PagerAdapter adapter;

    MaterialSearchView searchView;

    //Preferences
    private Integer MAX_RESULTS;
    private Boolean AUTO_PLAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ButterKnife
        ButterKnife.bind(this);

        getLifecycle().addObserver(youTubePlayerView);

        setPreferences();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() < 1) {
                    return false;
                }
                SearchArtist searchArtist = retrofit.create(SearchArtist.class);
                Call<SearchResults> searchResultsCall = searchArtist.searchArtist(newText, Constants.API_KEY);
                searchResultsCall.enqueue(new Callback<SearchResults>() {
                    @Override
                    public void onResponse(Call<SearchResults> call, Response<SearchResults> response) {
                        if (response.isSuccessful()) {
                            SearchResults searchResults = response.body();
                            if (searchResults == null) {
                                return;
                            }

                            Results results = searchResults.getResults();
                            Artistmatches artistmatches = results.getArtistmatches();
                            List<Artist> artistList = artistmatches.getArtist();


                            List<String> suggestions = new ArrayList<>();

                            for (int i = 0; i < artistList.size(); i++) {
                                if (artistList.get(i).getName() == null) {
                                    continue;
                                }
                                suggestions.add(artistList.get(i).getName());

                            }
                            searchView.clearSuggestions();
                            searchView.addSuggestions(suggestions);
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchResults> call, Throwable t) {
                    }
                });


                return false;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (isValidSearch(query)) {
                    searchArtisTracks(query);
                } else {
                    //showNoResultsFoundToast();
                }
                looseSearchEditTextFocus();
                return false;
            }

        });

        searchView.setOnItemClickListener((parent, view, position, id) -> {
            // Do something when the suggestion list is clicked.
            String suggestion = searchView.getSuggestionAtPosition(position);

            searchView.setQuery(suggestion, false);
        });


    }


    @OnClick(R.id.edt_search)
    public void OnClickSearchView(View view) {
        edt_search.clearFocus();
        searchView.openSearch();
    }

    @Override
    public void onListFragmentInteraction(Track model) {
        // the user clicked on this item over the list
        Toast.makeText(MainActivity.this,
                model.getName(), Toast.LENGTH_LONG).show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_YT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeVideoIdService youtubeVideoIdService =  retrofit.create(YoutubeVideoIdService.class);

        String songName = model.getName();
        String searchTerm =model.getArtist().getName() + " " + songName;

        Log.d( "search query: " ,searchTerm);


        Call<YoutubeUrl> youtubeUrlCall  = youtubeVideoIdService.getVideoId(searchTerm,Constants.API_KEY_YT);

        youtubeUrlCall.enqueue(new Callback<YoutubeUrl>() {
            @Override
            public void onResponse(Call<YoutubeUrl> call, Response<YoutubeUrl> response) {
                if (response.isSuccessful()) {
                    YoutubeUrl youtubeUrl = response.body();
                    List<Item> items = youtubeUrl.getItems();
                    Id id = items.get(0).getId();

                    String videoId = id.getVideoId();
                    AUTO_PLAY = PowerPreference.getDefaultFile().getBoolean("auto_play", false);
                    if (AUTO_PLAY) {
                        youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> {
                            youTubePlayer.loadVideo(videoId, 0);
                        });
                    } else {
                        youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> {
                            youTubePlayer.cueVideo(videoId, 0);
                        });
                    }

                }

            }

            @Override
            public void onFailure(Call<YoutubeUrl> call, Throwable t) {

            }

        });
    }

    @Override
    public void onListFragmentInteraction(Artist item) {

    }

    @Override
    public void onListFragmentInteraction(SimilarArtist item) {
        searchArtisTracks(item.getName());

    }




    private String searchArtisTracks(final String query){
        edt_search.setText(query);
        MAX_RESULTS = Integer.valueOf(PowerPreference.getDefaultFile().getString("results_number", "10"));


        progressBar.setVisibility(View.VISIBLE);

        //------------  TOP TRACKS -----------//
        retrofit  = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TopArtistTracksService topArtistTracksService =  retrofit.create(TopArtistTracksService.class);
        Call<TopTracksResponse> searchTracks = topArtistTracksService.getTopTracks(query, MAX_RESULTS, Constants.API_KEY);

        searchTracks.enqueue(new Callback<TopTracksResponse>() {
            @Override
            public void onResponse(Call<TopTracksResponse> call, Response<TopTracksResponse> response) {
                if (response.isSuccessful()) {
                    TopTracksResponse topTracksResponse = response.body();

                    if( topTracksResponse.getTopTracks() == null){
                        Toast.makeText(MainActivity.this,
                                "Artista no encontrado" , Toast.LENGTH_LONG).show();
                        return ;
                    }

                    TopTracks topTracks = topTracksResponse.getTopTracks();
                    final List<Track> tracks  = topTracks.getTracks();

                     retrofit = new Retrofit.Builder()
                            .baseUrl(Constants.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    GetArtistInfo getArtistInfo = retrofit.create(GetArtistInfo.class);
                    String artistName = tracks.get(0).getArtist().getName();
                    CURRENT_ARTIST = artistName;

                    final Call<ArtistInfoResponse> artistInfoResponseCall = getArtistInfo.getArtistInfo(artistName,Constants.API_KEY);


                    artistInfoResponseCall.enqueue(new Callback<ArtistInfoResponse>() {
                        @Override
                        public void onResponse(Call<ArtistInfoResponse> call, Response<ArtistInfoResponse> response) {
                            if (response.isSuccessful()) {
                                ArtistInfoResponse artistInfoResponse = response.body();
                                Artist artist = artistInfoResponse.getArtist();

                                SimilarArtistResponse similarArtistResponse = artistInfoResponse.getArtist().getSimilar();
                                List<SimilarArtist> similarArtists = similarArtistResponse.getArtist();

                                adapter = new PagerAdapter
                                        (getSupportFragmentManager(),
                                                mTabLayout.getTabCount(), tracks.toArray(new Track[tracks.size()]),
                                                artist ,similarArtists.toArray(new SimilarArtist[similarArtists.size()]));

                                mViewPager.setAdapter(adapter);

                                progressBar.setVisibility(View.GONE);
                                mTabLayout.addOnTabSelectedListener(getOnTabSelectedListener(mViewPager));
                                // Listeners
                                mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
                                //  mTabLayout.addOnTabSelectedListener(getOnTabSelectedListener(mViewPager));
                            }
                        }

                        @Override
                        public void onFailure(Call<ArtistInfoResponse> call, Throwable t) {

                        }
                    });

                }
            }
            @Override
            public void onFailure(Call<TopTracksResponse> call, Throwable t) {

            }
        });
        return  "OK";
    }


    // validar si vacío
    private boolean isValidSearch(String search) {
        if (TextUtils.isEmpty(search))
            return false;
        return true;
    }

    //esconder teclado al buscar
    private void looseSearchEditTextFocus() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        edt_search.clearFocus();

        // only if you're using EditText

        /*InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        }*/
    }


    @NonNull
    private TabLayout.OnTabSelectedListener getOnTabSelectedListener(final ViewPager viewPager) {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Toast.makeText(MainActivity.this, "Tab selected " +  tab.getPosition(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        };
    }


    // menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorites:
                Intent intent = new Intent(this, FavoritesActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_config:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_exit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void displayMessage(String message){
        Snackbar.make(findViewById(R.id.rootView) , message,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (searchView.isOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    public void onReturned() {
        setPreferences();
    }

    public void setPreferences() {
        MAX_RESULTS = Integer.valueOf(PowerPreference.getDefaultFile().getString("results_number", "10"));
        AUTO_PLAY = PowerPreference.getDefaultFile().getBoolean("auto_play", false);
    }


}
