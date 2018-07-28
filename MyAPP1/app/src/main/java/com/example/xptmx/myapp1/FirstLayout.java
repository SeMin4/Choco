package com.example.xptmx.myapp1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class FirstLayout extends AppCompatActivity{

    ListView listView;
    MyListAdapter myListAdapter;
    ArrayList<list_item> list_itemArrayList;
    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.first_layout);

        listView = findViewById(R.id.note);
        list_itemArrayList = new ArrayList<list_item>();
        myListAdapter = new MyListAdapter(FirstLayout.this, list_itemArrayList);
        listView.setAdapter(myListAdapter);

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),FirstLayout.class);
                intent.putExtra("create_date",list_itemArrayList.get(position).getCreate_date());
                intent.putExtra("location_id",list_itemArrayList.get(position).getLocation_id());
                intent.putExtra("location_name",list_itemArrayList.get(position).getLocation_name());
                intent.putExtra("md101_sn",list_itemArrayList.get(position).getMd101_sn());
                intent.putExtra("msg",list_itemArrayList.get(position).getContent());
                startActivity(intent);

            }
        });*/
    }
}
