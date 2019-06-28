package com.cibertec.amplyfm.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.cibertec.amplyfm.models.FavoriteTracks.FavoriteItem;
import java.util.List;

@Dao
public interface FavoriteDao {

    @Query("select * from FavoriteItem")
    List<FavoriteItem> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavorites(FavoriteItem... favoriteItems);

    @Delete
    void deleteFavorites(FavoriteItem... favoriteItems);

}
