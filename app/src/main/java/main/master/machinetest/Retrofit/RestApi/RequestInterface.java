package main.master.machinetest.Retrofit.RestApi;

import main.master.machinetest.Retrofit.GSONModelClass.MainParent;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RequestInterface {

        String JSONURL = "http://test.chatongo.in/";

        @GET("/testdata.json")
        Call<MainParent> getString();
    }