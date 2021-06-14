package com.example.api_volley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.api_volley.Adapter.AdapterData;
import com.example.api_volley.Model.ModelData;
import com.example.api_volley.Util.AppController;
import com.example.api_volley.Util.ServerAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerview;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mManager;
    List<ModelData> mItems;
    Button btnInsert, btnDelete;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerviewTemp);
        btnInsert = (Button) findViewById(R.id.btn_insert);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        pd = new ProgressDialog(MainActivity.this);
        mItems = new ArrayList<>();


        mManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false);
        mRecyclerview.setLayoutManager(mManager);
        mAdapter = new AdapterData(MainActivity.this,mItems);
        mRecyclerview.setAdapter(mAdapter);
        loadJson();

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,InsertData.class);
                startActivity(intent);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent hapus = new Intent(MainActivity.this,Delete.class);
                startActivity(hapus);
            }
        });
    }

    private void loadJson()
    {
        pd.setMessage("Mengambil Data");
//        pd.setCancelable(false);
        pd.show();
//        System.out.println("Mengambil Data");

        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.GET, ServerAPI.URL_DATA,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println("Length :"+response.length());
                        pd.cancel();
                        Log.e("volley","response : " + response);
                        for(int i = 0 ; i < response.length(); i++)
                        {
                            try {
                                JSONObject data = response.getJSONObject(i);
                                ModelData md = new ModelData();
                                md.setUsername(data.getString("username"));
                                md.setGrup(data.getString("grup"));
                                md.setNama(data.getString("nama"));
                                md.setPassword(data.getString("password"));
                                mItems.add(md);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        System.out.println("Length :"+response.length());
                        mAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Log.d("volley", "error : " + error.getMessage());
                    }
                });

        AppController.getInstance().addToRequestQueue(reqData);
    }

}