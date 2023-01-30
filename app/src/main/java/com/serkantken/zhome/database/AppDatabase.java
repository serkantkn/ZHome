package com.serkantken.zhome.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.serkantken.zhome.models.AppModel;

@Database(entities = AppModel.class, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MainDAO mainDAO();

    private static AppDatabase database;
    private static final String DATABASE_NAME = "application";

    public synchronized static AppDatabase getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    DATABASE_NAME
            ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }
}
