package main.master.machinetest.Retrofit.GSONModelClass;



import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("TotalRecords")
    @Expose
    private Integer totalRecords;
    @SerializedName("Records")
    @Expose
    private List<Record> records = null;

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

}