package com.example.administrator.mybaidunavi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    TextView te;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        te = (TextView) findViewById(R.id.te);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://www.sojson.com/open/api/weather/json.shtml?city=南昌")
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    JSONObject data = jsonObject.optJSONObject("data");
                    JSONObject data2 = data.optJSONObject("yesterday");
                    final String na = data2.getString("high");
                    Log.e("11111111", "" + na);
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            //耗时操作
                            mHandler.sendEmptyMessage(0);
                            Message msg =new Message();
                            msg.obj = na;//可以是基本类型，可以是对象，可以是List、map等
                            mHandler.sendMessage(msg);
                        }
                    }).start();
                    JSONArray list = data.optJSONArray("forecast");
                    for (int i = 0; i < list.length(); i++) {
                        String str = list.optJSONObject(i).toString();
                        Weatherbean bean = gson.fromJson(str, Weatherbean.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String data = (String)msg.obj;
                    te.setText(data);
                    break;
                default:
                    break;
            }
        }
    };
}
