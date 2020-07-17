package com.example.hw2.vediodatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.hw2.database.TodoListEntity;

import java.util.List;

@Dao
public interface videoDao {
    @Insert
    long addVideo(vedioEntity entity);
}
