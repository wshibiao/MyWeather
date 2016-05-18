package com.wshibiao.myweather.ui.choosecity;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wshibiao.myweather.R;
import com.wshibiao.myweather.data.bean.City;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wsb on 2016/4/26.
 */
public class CityRecyclerViewAdapter extends RecyclerView.Adapter<CityRecyclerViewAdapter.MyViewHolder> {
//    private onRecyclerViewItemClickListener listener;
//    private List<City> cityList;
//
//    public CityRecyclerViewAdapter(List<City> cityList) {
//
//        this.cityList=(cityList!=null?cityList:new ArrayList<City>());
//    }
//
//
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, final int position) {
//        City city=cityList.get(position);
//        holder.city.setText(city.getCity()+"-");
//        holder.district.setText(city.getDistrict());
//        holder.province.setText(city.getProvince() + "-");
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onItemClick(v,position);
//            }
//        });
//
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder{
//        private CardView cardView;
//        private TextView province;
//        private TextView city;
//        private TextView district;
//        public MyViewHolder(View view){
//            super(view);
//            cardView=(CardView)view.findViewById(R.id.cardView);
//            province=(TextView)view.findViewById(R.id.province);
//            city=(TextView)view.findViewById(R.id.city);
//            district=(TextView)view.findViewById(R.id.district);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//       return cityList.size();
//    }
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city,null);
//        MyViewHolder vh=new MyViewHolder(view);
//        return vh;
//    }
//
//    public interface onRecyclerViewItemClickListener{
//        void onItemClick(View view,int position);
//    }
//
//    public void setOnItemClickListener(onRecyclerViewItemClickListener listener){
//        this.listener=listener;
//    }
private OnRecyclerViewItemClickListener mOnItemClickListener;
    private List<City> dataset;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        private TextView province;
        private TextView city;
        private TextView district;
        public MyViewHolder(View view){
            super(view);
            cardView=(CardView)view.findViewById(R.id.cardView);
            province=(TextView)view.findViewById(R.id.province);
            city=(TextView)view.findViewById(R.id.city);
            district=(TextView)view.findViewById(R.id.district);
        }
    }
    public CityRecyclerViewAdapter(Context context,List<City> cityLists){
        mContext=context;
        dataset = (cityLists!=null?cityLists:new ArrayList<City>());
    }
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup,int viewType){
        View view= LayoutInflater.from(mContext)
                .inflate(R.layout.item_city, viewGroup, false);
        MyViewHolder vh=new MyViewHolder(view);
        return  vh;

    }
    public void onBindViewHolder(MyViewHolder viewHolder,final int position){
        City city=dataset.get(position);
        viewHolder.city.setText(city.getCity()+"-");
        viewHolder.district.setText(city.getDistrict());
        viewHolder.province.setText(city.getProvince()+"-");
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position);
            }
        });

    }

    public int getItemCount(){
        return dataset.size();
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int pos);
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
