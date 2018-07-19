package com.example.a32150.a2017110902;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a32150.a2017110902.DataClass.ZooInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 32150 on 2017/11/9.
 */

public class MyAdapter extends BaseAdapter implements Filterable{

    List<ZooInfo> zooInfo;//有資料的
    ArrayList<ZooInfo> orgzooInfo;
    LayoutInflater inflator;

    Context context;

    public MyAdapter(Context context, List<ZooInfo> zooInfo)  {
        this.context = context;
        this.zooInfo = zooInfo;
        inflator = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return zooInfo.size();
    }

    @Override
    public ZooInfo getItem(int i) {
        return zooInfo.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        inflator = LayoutInflater.from(context);
        //if(view == null)    {
            view = inflator.inflate(R.layout.myitem, viewGroup, false);//解出Layout 解壓器,消耗資源
            holder = new ViewHolder();
            holder.index = (TextView)view.findViewById(R.id.index);
            holder.loc = (TextView)view.findViewById(R.id.loc);
            holder.city = (TextView)view.findViewById(R.id.city);
            holder.img = (ImageView)view.findViewById(R.id.imageView);
            holder.limit = (TextView)view.findViewById(R.id.limit);
            holder.coordinate = (TextView)view.findViewById(R.id.longitude);
            //holder.longtitude = (TextView)view.findViewById(R.id.longtitude);
            //holder.latitude = (TextView)view.findViewById(R.id.latitude);
            view.setTag(holder);//要加,不然listview滑動會當掉
            holder.index.setText((i+1)+" : ");
            holder.loc.setText(zooInfo.get(i).Address);
            holder.city.setText(zooInfo.get(i).CityName);
            holder.limit.setText(zooInfo.get(i).limit);
            holder.limit.setTextColor(Color.parseColor("#CC0000"));
            String lon=zooInfo.get(i).Longitude.replace(".", "°");
            lon=lon.concat("′E");
            String lat=zooInfo.get(i).Latitude.replace(".", "°");
            lat=lat.concat("′N");
            holder.coordinate.setText("("+lon+" , "+lat+")");
            holder.coordinate.setTextColor(Color.parseColor("#00AA00"));
            //holder.longtitude.setText(zooInfo.get(i).Longitude);
            //holder.latitude.setText(zooInfo.get(i).Latitude);
        //}
//        else    {
//            holder = (ViewHolder) view.getTag();
//        }
        return view;
    }

    @Override
    public Filter getFilter() {
        Filter filter= new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                charSequence = charSequence.toString();
                FilterResults result = new FilterResults();
                if(orgzooInfo == null){//空資料
                    synchronized (this){
                        orgzooInfo = new ArrayList<ZooInfo>(zooInfo);
                        // orgzooInfo 沒有資料，會複製一份item的過來.
                    }
                }
                if(charSequence != null && charSequence.toString().length()>0){
                    List<ZooInfo> filteredItem = new ArrayList<ZooInfo>();
                    //Set<ZooInfo> t_result = new TreeSet<>();
                    for(int i=0;i<orgzooInfo.size();i++){
                        int k=0;
                        String addr=orgzooInfo.get(i).Address;
                        String city=orgzooInfo.get(i).CityName;
                        String limit=orgzooInfo.get(i).limit;
                        String str = addr+" "+city+" "+limit;
                        //String[] tokens = charSequence.toString().split(" ");

                        //for(int j=i; j<tokens.length; j++)  {
                            if(str.contains(charSequence)) {
                                //if(filteredItem.get(i).Address!=orgzooInfo.get(i).Address &&
                                  //      filteredItem.get(i).CityName!=orgzooInfo.get(i).CityName &&
                                    //    filteredItem.get(i).limit!=orgzooInfo.get(i).limit)
                                filteredItem.add(orgzooInfo.get(i));

                            }
                        //}


                        //if(str.contains(charSequence)){
                            //Log.d("Str", i+":"+str);
                            //Log.d("charSequence", i+":"+charSequence);
                            //filteredItem.add(orgzooInfo.get(i));//address比對到的加進list
                        //}
                    }
                    result.count = filteredItem.size();
                    result.values = filteredItem;
                }else{
                    synchronized (this){
                        List<ZooInfo> list = new ArrayList<ZooInfo>(orgzooInfo);
                        result.values = list;
                        result.count = list.size();
                    }
                }
                //Log.d("Count", ""+result.count);
                //Log.d("result", result.values.toString());
                return result;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                zooInfo = (ArrayList<ZooInfo>)filterResults.values;//裝filter出來的資料
                //for(int i=0;i<zooInfo.size();i++)
                    //Log.d("Publish", (i+1)+":"+zooInfo.get(i).Address);
                if(filterResults.count>0){
                    notifyDataSetChanged();
                }   else{
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    static class ViewHolder
    {
        TextView loc;
        ImageView img;
        TextView city;
        TextView limit;
        TextView coordinate;
        TextView index;
        //TextView latitude;
    }
}