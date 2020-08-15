package main.master.machinetest;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import main.master.machinetest.Retrofit.GSONModelClass.Record;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<Record>  dataModelArrayList;

    public DataAdapter(Context ctx, List<Record> dataModelArrayList){

        inflater = LayoutInflater.from(ctx);
        this.dataModelArrayList = dataModelArrayList;
    }

    @Override
    public DataAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.model, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(DataAdapter.MyViewHolder holder, int position) {

        Picasso.get().load(dataModelArrayList.get(position).getMainImageURL()).into(holder.iv);
        holder.title.setText(dataModelArrayList.get(position).getTitle());
        holder.shortDescription.setText(dataModelArrayList.get(position).getShortDescription());
        }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView shortDescription, title,totalvalue,colvalue;
        ImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);

            shortDescription = (TextView) itemView.findViewById(R.id.shortDescription);
            title= (TextView) itemView.findViewById(R.id.title);
            iv = (ImageView) itemView.findViewById(R.id.iv);
        }

    }
}