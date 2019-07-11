package com.example.bluetoothgetzyme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyDataActivity extends AppCompatActivity {

    String data;

    RecyclerView verticalRecyclerView;

    ArrayList<VerticalModel> myArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data);



        Intent in = getIntent();
        data = in.getStringExtra("KEY_STRING");
        data = data.replaceAll("\\n","");

        verticalRecyclerView = (RecyclerView)findViewById(R.id.verticalRecyclerView);

        verticalRecyclerView.setHasFixedSize(true);
        verticalRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));


        //.........Make Vertical Adapter.............//

        try {

            JSONObject jsonObject = new JSONObject(data);

            int listCount = jsonObject.length();


            myArrayList = new ArrayList<>();
            for(int i=1;i<=listCount;i++) {
                String myData = jsonObject.getString(String.valueOf(i));
                VerticalModel verticalModel = new VerticalModel(myData);
                myArrayList.add(verticalModel);
            }

            VerticalRecyclerViewAdapter myAdapter = new VerticalRecyclerViewAdapter(MyDataActivity.this,
                    myArrayList);
            verticalRecyclerView.setAdapter(myAdapter);


        }catch (Exception e){
            e.printStackTrace();
            Log.e("ABCD",e+"");


        }

    }
}
