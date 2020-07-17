package com.example.hw2.vediodatabase;

import androidx.room.TypeConverter;

import java.util.Date;

public class vedioDataConverter {
    @TypeConverter
    public Date fromTimeStamp(long ts) {
        return new Date(ts);
    }

    @TypeConverter
    public long toTimeStamp(Date date) {
        return date.getTime();
    }
}
