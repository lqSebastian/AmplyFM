package com.cibertec.amplyfm;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cibertec.amplyfm.dao.FavoriteDao;
import com.cibertec.amplyfm.models.FavoriteTracks.FavoriteItem;
import com.cibertec.amplyfm.models.Track;

@Database(entities = {FavoriteItem.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {


    public abstract FavoriteDao favoriteDao();

    private static AppDatabase appDatabase = null;

    public static AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room
                    .databaseBuilder(context, AppDatabase.class, "favorites.db")
                    .allowMainThreadQueries()
                    .build();
        }
        return appDatabase;
    }
}
