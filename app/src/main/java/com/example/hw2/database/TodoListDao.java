package com.example.hw2.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TodoListDao {
    @Query("SELECT * FROM douyin")
    List<TodoListEntity> loadAll();

    @Insert
    long addTodo(TodoListEntity entity);

    @Query("DELETE FROM douyin")
    void deleteAll();

    @Delete
    void deleteRecord(TodoListEntity... onetodo);

    @Query("SELECT * FROM douyin WHERE id = :qId")
    TodoListEntity getEntity(String qId);

    @Update
    void updateEntity(TodoListEntity... todos);

    @Query("SELECT * FROM douyin WHERE id = :qId")
    TodoListEntity getIfThePasswordIsRight(String qId);

    @Query("SELECT * FROM douyin WHERE id = :qId")
    TodoListEntity getIfTheIdIsUsed(String qId);

    @Query("SELECT * FROM douyin WHERE isCurrent =:is")
    TodoListEntity getCurrent(boolean is);
}
