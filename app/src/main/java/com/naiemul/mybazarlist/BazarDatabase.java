package com.naiemul.mybazarlist;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {BazarItem.class},version = 1)
public abstract class BazarDatabase extends RoomDatabase {

    public abstract BazarDao bazarDao();
    private  static volatile BazarDatabase instance;

    public static synchronized BazarDatabase getInstance(Context context){

        if (instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),
                    BazarDatabase.class,"bazar_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }

     return instance;
    }


}
