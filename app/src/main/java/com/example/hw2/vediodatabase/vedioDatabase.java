package com.example.hw2.vediodatabase;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {vedioEntity.class}, version = 1,exportSchema = false)
@TypeConverters(vedioDataConverter.class)
public abstract class vedioDatabase extends RoomDatabase {
    private static volatile vedioDatabase INSTANCE;

    public abstract videoDao tovedioDao();

    public vedioDatabase(){

    }

    public static vedioDatabase inst(Context context){
        if(INSTANCE == null){
            synchronized (vedioDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), vedioDatabase.class,"video_data").build();
                }
            }
        }
        return INSTANCE;
    }
}
