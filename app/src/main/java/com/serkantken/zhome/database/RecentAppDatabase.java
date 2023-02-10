package com.serkantken.zhome.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.serkantken.zhome.models.AppModel;

@Database(entities = AppModel.class, version = 1)
public abstract class RecentAppDatabase extends RoomDatabase {
    public abstract RecentAppsDAO recentAppsDAO();

    private static RecentAppDatabase database;
    private static final String DATABASE_NAME = "recentApplication";

    public synchronized static RecentAppDatabase getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(
                    context.getApplicationContext(),
                    RecentAppDatabase.class,
                    DATABASE_NAME
            ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }
}
