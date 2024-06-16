package com.example.shoppinglist;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface ItemDAO {

    @Insert
    void insertAll(Item... items);

    @Query("SELECT * FROM Item")
    List<Item> getAll();

    @Delete
    void delete(Item item);


}