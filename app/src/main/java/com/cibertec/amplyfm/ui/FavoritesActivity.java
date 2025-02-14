package com.cibertec.amplyfm.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.cibertec.amplyfm.AppDatabase;
import com.cibertec.amplyfm.R;
import com.cibertec.amplyfm.adapters.SinglePageAdapter;
import com.cibertec.amplyfm.models.FavoriteTracks.FavoriteItem;
import com.cibertec.amplyfm.models.Youtube.Id;
import com.cibertec.amplyfm.models.Youtube.Item;
import com.cibertec.amplyfm.models.Youtube.YoutubeUrl;
import com.cibertec.amplyfm.network.YoutubeVideoIdService;
import com.cibertec.amplyfm.ui.fragments.FavoritesFragment;
import com.cibertec.amplyfm.utils.Constants;
import com.google.android.material.tabs.TabLayout;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.preference.PowerPreference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavoritesActivity extends AppCompatActivity implements FavoritesFragment.OnListFragmentInteractionListener{


    List<FavoriteItem> items  = new ArrayList<>();

    @BindView(R.id.tabl_favorites)
    TabLayout mTabLayout;
    @BindView(R.id.vp_favorites)
    ViewPager mViewPager;



    //MainPagerAdapter mAdapter;
    @BindView(R.id.youtube_player_view_favorites)
    YouTubePlayerView youTubePlayerView;

    private  FavoritesFragment.OnListFragmentInteractionListener interactionListener;

    private Boolean AUTO_PLAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        ButterKnife.bind(this);
        getLifecycle().addObserver(youTubePlayerView);

        loadItems();

        AUTO_PLAY = PowerPreference.getDefaultFile().getBoolean("auto_play", false);

    }

    public void loadItems() {

        items = AppDatabase.getInstance(this).favoriteDao().getAll();
        final SinglePageAdapter adapter = new SinglePageAdapter
                (getSupportFragmentManager(),
                        mTabLayout.getTabCount(), items.toArray(new FavoriteItem[items.size()]));

        mViewPager.setAdapter(adapter);
    }



    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    public void onListFragmentInteraction(FavoriteItem model) {
        Toast.makeText(FavoritesActivity.this,
                model.getName(), Toast.LENGTH_SHORT).show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_YT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeVideoIdService youtubeVideoIdService =  retrofit.create(YoutubeVideoIdService.class);

        String songName = model.getName();
        String searchTerm =model.getArtist() + " " + songName;

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



    // menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.item_favorites:
                loadItems();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
