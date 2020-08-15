package main.master.machinetest.Room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Funding.class}, version = 1)
public abstract class FundingDatabase extends RoomDatabase {
    public abstract FundingDao fundingDao();
}
