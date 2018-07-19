package com.example.a32150.a2017110902;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.a32150.a2017110902.DataClass.DB;
import com.example.a32150.a2017110902.DataClass.MenuItemReceivedListener;
import com.example.a32150.a2017110902.DataClass.Zoo;
import com.example.a32150.a2017110902.DataClass.ZooDetail;
import com.example.a32150.a2017110902.DataClass.ZooInfo;
import com.example.a32150.a2017110902.Map.MapsActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity implements MenuItemReceivedListener {

    MyAdapter adapter;
    ListView lv;
    Zoo z;
    SearchView searchView;
    Set<String>cityName;
    int flag=0;
    Context context;
    private DB mDbHelper;

    public MainActivity() throws PackageManager.NameNotFoundException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        z = new Zoo();
        z.result = new ZooDetail();

        lv = (ListView) findViewById(R.id.listView);
        lv.setTextFilterEnabled(true);
        searchView = (SearchView)findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);// 關閉icon切換
        searchView.setFocusable(false); // 不要進畫面就跳出輸入鍵盤
        searchView.setQueryHint("關鍵字搜尋：");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

//        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
//        StringRequest request = new StringRequest("http://od.moi.gov.tw/api/v1/rest/datastore/A01010000C-000674-011",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Gson gson = new Gson();
//                        z = gson.fromJson(response, Zoo.class);
//                        adapter = new MyAdapter(MainActivity.this, z.result.records);
//                        adapter.zooInfo.remove(0);
//                        adapter.notifyDataSetChanged();
//                        lv.setAdapter(adapter);
//                        flag=1;
//                        ((MenuItemReceivedListener) context).onItemReceivedEvent();
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("ZOO", "Error:" + error.toString());
//                    }
//        });
//        queue.add(request);
//        queue.start();

        //=======================Get Data By Sqlite temporially=============================
        //複製檔案
        String destpath = getDatabasePath("project")+".db";//整個DB檔的完整路徑
        //Log.d("Dest Path", destpath);
        File f = new File(destpath);
        //Log.d("Dest Path", destpath.substring(0, destpath.length()-10));
        File dbdir = new File(destpath.substring(0, destpath.length()-10));//DB路徑,不包含DB名稱
        if (! f.exists())
        {
            InputStream is = getResources().openRawResource(R.raw.project);
            OutputStream os = null;
            try {
                os = new FileOutputStream(destpath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            int read;
            try {
                while ((read = is.read()) != -1)
                {
                    os.write(read);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //==========================================
        mDbHelper = new DB(this).open();
        Cursor c = mDbHelper.getAll();
        startManagingCursor(c);
        //int i=0;
        z.result.records = new ArrayList<>();
        if (c.moveToFirst()) {
            Log.d("Data", "OK");
            do {
                ZooInfo info = new ZooInfo();
                String CityName = info.CityName =  c.getString(c.getColumnIndex("CityName"));
                String RegionName  = info.RegionName = c.getString(c.getColumnIndex("RegionName"));
                String Address = info.Address = c.getString(c.getColumnIndex("Address"));
                String DeptNm = info.DeptNm = c.getString(c.getColumnIndex("DeptNm"));
                String BranchNm = info.BranchNm = c.getString(c.getColumnIndex("BranchNm"));
                String Longitude = info.Longitude =  c.getString(c.getColumnIndex("Longitude"));
                String Latitude = info.Latitude = c.getString(c.getColumnIndex("Latitude"));
                String direct = info.direct = c.getString(c.getColumnIndex("direct"));
                String limit = info.limit = c.getString(c.getColumnIndex("limit"));
                z.result.records.add(info);
                //i++;
                //String str = "第"+(i)+"筆 : "+CityName + " " + RegionName + " " + Address+" "+
                 //       DeptNm+" "+BranchNm+" "+Longitude+" "+Latitude+" "+direct+" "+limit;
                //Log.d("Data", str);
            } while (c.moveToNext());
        }   else    {
            Log.d("Data", "No Data !");
        }
        Log.d("Count",""+z.result.records.size());
        for(int k=0; k<z.result.records.size();k++) {
            ZooInfo info = z.result.records.get(k);
            Log.d("Data", "第"+(k+1)+"筆="+info.CityName+" "+info.RegionName+" "+info.Address+" "+info.DeptNm);
        }
        searchView.setVisibility(View.VISIBLE);
        adapter = new MyAdapter(MainActivity.this, z.result.records);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(listener);
        lv.setTextFilterEnabled(true);
    }

    public void GoCopyFile(String comepath, String gopath) throws IOException {
        try {
            File wantfile = new File(comepath);
            File newfile = new File(gopath);
            InputStream in = new FileInputStream(wantfile);
            OutputStream out = new FileOutputStream(newfile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            Log.e("copy file error", e.toString());
        }
    }

    //前往google地圖
    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String coordinate = ((TextView)view.findViewById(R.id.longitude)).getText().toString();
            String city = ((TextView)view.findViewById(R.id.city)).getText().toString();
            String loc = ((TextView)view.findViewById(R.id.loc)).getText().toString();
            coordinate = coordinate.replace('°', '.');
            String longitude = coordinate.substring(1,11);
            String latitude = coordinate.substring(16, 25);
            Log.d("DATA", "city="+city+",loc="+loc+", longitude="+longitude+", latitude="+latitude);
            Intent it = new Intent(MainActivity.this, MapsActivity.class);
            it.putExtra("latitude", latitude);
            it.putExtra("longitude", longitude);
            it.putExtra("city", city);
            it.putExtra("loc", loc);
            startActivity(it);
        }
    };

//    public void getItem(Menu menu)  {
//        MenuItem logoutMI;
//            for(String obj : cityName) {
//                Log.d("DATA", obj);
//                logoutMI = menu.add(Menu.NONE,1,1, obj);
//                logoutMI.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//                logoutMI.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//            }
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action, menu);
        Log.d("flag", ""+flag);
        //if(flag != 0)   {
//            MenuItem logoutMI;
//            for(String obj : cityName) {
//                Log.d("DATA", obj);
//                logoutMI = menu.add(Menu.NONE,1,1, obj);
//                logoutMI.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//                logoutMI.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//            }
        //}
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

//        MenuItem logoutMI;
//            for(String obj : cityName) {
//                Log.d("DATA", obj);
//                logoutMI = menu.add(Menu.NONE,1,1, obj);
//                logoutMI.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//                logoutMI.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//            }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onItemReceivedEvent() {
        searchView.setVisibility(View.VISIBLE);
        //get unique city name
        cityName = new TreeSet();
        for(int i=0; i<z.result.records.size();i++)
            cityName.add(z.result.records.get(i).CityName);
        for(String obj : cityName)
            Log.d("DATA", obj);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())    {
            case R.id.action:

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}