package main.master.machinetest.Room;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Funding implements Serializable {


    @PrimaryKey(autoGenerate = true)
    private int id;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @ColumnInfo(name = "img")
    private String img;


    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "shortDescription")
    private String shortDescription;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }




}
