package com.serkantken.zhome.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.serkantken.zhome.models.AppModel;

import java.util.List;

@Dao
public interface RecentAppsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AppModel appModel);

    @Query("SELECT * FROM application ORDER BY id DESC")
    List<AppModel> getAll();

    @Query("UPDATE application SET appName = :appName, packageName = :packageName WHERE ID = :id")
    void update(int id, String appName, String packageName);

    @Delete
    void delete(AppModel appModel);

    @Query("DELETE FROM application")
    void deleteAll();
}
