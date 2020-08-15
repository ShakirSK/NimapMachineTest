package main.master.machinetest.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface FundingDao {


    @Query("SELECT * FROM Funding")
    List<Funding> getAll();

    @Query("SELECT * FROM Funding ORDER BY id LIMIT 1")
    List<Funding> getcheck();


    @Insert
    void insert(Funding funding);

    @Delete
    void delete(Funding funding);

    @Update
    void update(Funding funding);


}
