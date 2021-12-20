package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class adapter extends RecyclerView.Adapter<adapter.ViewHolder> {
    LayoutInflater layoutInflater;
    Context context;
    ArrayList<weatherModel> data;
    adapter(Context context,ArrayList<weatherModel> data){
        this.context=context;
        this.data=data;
    }

    @NonNull
    @Override
    public adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        weatherModel model=data.get(position);
        holder.wind.setText(model.getWind()+" Km/h");
        holder.temp.setText(model.getTemp()+" °C");

        Picasso.with(context).load("https:".concat(model.getIcon())).into(holder.icon);//TODO
        SimpleDateFormat ip=new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat op=new SimpleDateFormat("hh-mm aa");

        Date t= null;
        try {
            t = ip.parse(model.getTime());
            holder.time.setText(op.format(t));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView temp,time,wind;
        ImageView icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time=itemView.findViewById(R.id.time);
            temp=itemView.findViewById(R.id.temp_c);
            wind=itemView.findViewById(R.id.wind_kmp);
            icon=itemView.findViewById(R.id.icon_condition_hr);
        }
    }
}
