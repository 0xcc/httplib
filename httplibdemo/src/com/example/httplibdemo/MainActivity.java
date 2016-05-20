package com.example.httplibdemo;

import java.util.LinkedList;

import mike.httplib.base.Request.HttpMethod;
import mike.httplib.base.Request.RequestListener;
import mike.httplib.core.MyHttplib;
import mike.httplib.core.RequestQueue;
import mike.httplib.request.StringRequest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {

	RequestQueue mQueue=MyHttplib.newRequestQueue();
	TextView mResultTv;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mResultTv = (TextView) findViewById(R.id.mResultTv);
        sendStringRequest();
    }
    
    @SuppressLint("NewApi") private void sendStringRequest() {
        StringRequest request = new StringRequest(HttpMethod.GET, "http://www.douban.com/",
                new RequestListener<String>() {

                    @Override
                    public void onComplete(int stCode, String response, String errMsg) {
                        mResultTv.setText(Html.fromHtml(response));
                    }
                    
                    @Override
                    public void onRequestInnerError(Exception e){
                    	e.printStackTrace();
                    }
                    
                });

        mQueue.addRequest(request);
       
         
        ViewServer.get(this).addWindow(this);  
    }
    
    @Override  
    public void onDestroy() {  
        super.onDestroy();  
        ViewServer.get(this).removeWindow(this);  
    }  
  
    @Override  
    public void onResume() {  
        super.onResume();  
        ViewServer.get(this).setFocusedWindow(this);  
    }  
}
