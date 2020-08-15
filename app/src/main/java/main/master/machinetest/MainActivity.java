package main.master.machinetest;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import main.master.machinetest.Networkcheck.ConnectionDetector;
import main.master.machinetest.Retrofit.GSONModelClass.Data;
import main.master.machinetest.Retrofit.GSONModelClass.MainParent;
import main.master.machinetest.Retrofit.GSONModelClass.Record;
import main.master.machinetest.Retrofit.RestApi.RequestInterface;
import main.master.machinetest.Room.DatabaseClient;
import main.master.machinetest.Room.Funding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private DataAdapter retrofitAdapter;
    private RecyclerView recyclerView;
    List<Record> recordslist = new ArrayList<>();
    List<Funding> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init
        recyclerView = findViewById(R.id.recycler);



        //checking if internet available
        // if yes then will hit rest api and show in recycleview and store in roomdb
        // if no then will look in to roomdb if data exist then show from room db or popup the toast
        if (!ConnectionDetector.networkStatus(getApplicationContext())) {

            //room operation in backround thread
            AsyncTask.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    //checing if tasklist in roomdb is empty or not
                    taskList = DatabaseClient
                            .getInstance(getApplicationContext())
                            .getAppDatabase()
                            .fundingDao()
                            .getcheck();

                            //if it is empty
                            if(taskList.isEmpty()){
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Enable Internet Connection ..RoomDB is Empty", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            //if it has something
                            else{
                                //getting the data from room db and show in recycleview
                                GetData gt = new GetData();
                                gt.execute();

                            }
                }
            });
        }
        // if internet connection is available then will hit rest api and show in recycleview and store in roomdb
        else {
            fetchJSON();
        }





    }

    private void fetchJSON(){

        //calling our rest api
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RequestInterface.JSONURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface api = retrofit.create(RequestInterface.class);

        //Call with our MainParent Model
        Call<MainParent> call = api.getString();

        // using Callback which gives as onResponse or onFailure
        call.enqueue(new Callback<MainParent>() {
            @Override
            public void onResponse(Call<MainParent> call, Response<MainParent> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        Log.i("onSuccess", response.body().toString());
                        //MainParent - getData() - into Model Data
                        Data data = response.body().getData();

                        //Data - getRecords() - into Model Record
                        List<Record> records = data.getRecords();

                        //List<Record> records pass to recycleview Adapter
                        retrofitAdapter = new DataAdapter(getApplicationContext(),records);
                        recyclerView.setAdapter(retrofitAdapter);
                        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));


                        //checking if data is already inserted in Room DB
                        AsyncTask.execute(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                taskList = DatabaseClient
                                        .getInstance(getApplicationContext())
                                        .getAppDatabase()
                                        .fundingDao()
                                        .getcheck();

                                //if it is empty then only we will add
                                if(taskList.isEmpty()) {
                                    //inserting in to DB
                                  new  AsyncTaskFunding(records).execute();
                                }
                            }
                        });

                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");
                    }
                }
            }

            @Override
            public void onFailure(Call<MainParent> call, Throwable t) {

            }
        });
    }



    //insert in to Room DB
    public class AsyncTaskFunding extends AsyncTask<String,String,String>{

        List<Record> record;

        AsyncTaskFunding(List<Record> record){
            this.record = record;
        }
        @Override
        protected String doInBackground(String... params) {

            Log.d( "doInBackgrounsize", String.valueOf(record.size()));
            //setting my  List<Record> to Room Model Funding
            for(int i =0 ; i<record.size() ; i++){
                Funding funding = new Funding();
                funding.setTitle(record.get(i).getTitle());
                funding.setShortDescription(record.get(i).getShortDescription());
                funding.setImg(record.get(i).getMainImageURL());

                //adding to database
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .fundingDao()
                        .insert(funding);

            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
          //   Toast.makeText(getApplicationContext(), "Inserted", Toast.LENGTH_LONG).show();
        }
    }

    //retrive from Room DB
    class GetData extends AsyncTask<Void, Void, List<Funding>> {

        @Override
        protected List<Funding> doInBackground(Void... voids) {
            //getting all data from room db in list
            List<Funding> taskList = DatabaseClient
                    .getInstance(getApplicationContext())
                    .getAppDatabase()
                    .fundingDao()
                    .getAll();
            return taskList;
        }

        @Override
        protected void onPostExecute(List<Funding> tasks) {
            super.onPostExecute(tasks);

            //List task data set to Record model  to show in recycleview
            for (int j = 0; j < tasks.size(); j++) {
                Funding funding = tasks.get(j);

                Record record = new Record();
                //setting into model class for recycleview
                record.setMainImageURL(funding.getImg());
                record.setTitle(funding.getTitle());
                record.setShortDescription(funding.getShortDescription());

                recordslist.add(record);
            }


            retrofitAdapter = new DataAdapter(getApplicationContext(),recordslist);
            recyclerView.setAdapter(retrofitAdapter);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));

        }
    }



}
