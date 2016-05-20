package mike.plugin.model.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLEncoder;

import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;



import android.net.Uri;
import android.os.Environment;
import android.sax.StartElementListener;
import android.util.Log;
import android.util.PrintStreamPrinter;


import mike.plugin.app.MyApp;
import mike.plugin.model.IWeatherModel;
import mike.plugin.model.entity.Weather;
import mike.plugin.presenter.IOnWeatherListener;
import mike.plugin.util.VolleyRequest;

/**
 * Created by Administrator on 16-5-6.
 */
public class WeatherModelImpl implements IWeatherModel {
    @Override
    public void loadWeather(String cityNo, final IOnWeatherListener listener) {

        try {
        	final String url="http://www.weather.com.cn/data/sk/" + cityNo + ".html";
        	
        	Runnable r=new Runnable() {
				
				@Override
				public void run() {
					
						 OkHttpClient okHttpClient=new OkHttpClient();
			             Request.Builder requestBuilder=new Request.Builder();
			             requestBuilder.url(url);
			             Call call= okHttpClient.newCall(requestBuilder.build());
			             okhttp3.Response response=null;
			             try{
			            	 response= call.execute();
						 }catch(Exception e){
							 e.printStackTrace();
						 }
			             if (response.isSuccessful()){
			                 ResponseBody responseBody= response.body();
			                 InputStream inputStream=responseBody.byteStream();
			                 //InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
	
			                 byte[] buffer=new byte[1024];
			                 int len=0;
			                 StringBuilder result=new StringBuilder();
			                 try {
								while ((len=inputStream.read(buffer))!=-1){
								     result.append(new String(buffer));
								 }
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	
			                 String value=result.toString();
			                 Gson gson=new Gson();
			                 Weather weather=(Weather)gson.fromJson(value, Weather.class);
			                 listener.onSuccess(weather);
			                 Log.i("value",value);
			             }
					 
					 
				}
			};
        	
        	 
        	
        	new Thread(r).start();
        	
        	/*
            Request request= VolleyRequest.newInstance().newGsonRequest(url, Weather.class, new Response.Listener<Weather>() {
                @Override
                public void onResponse(Weather weather) {
                    if (weather!=null){
                        listener.onSuccess(weather);
                    }else{
                        listener.onError();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                	
                
                	
                	String f=Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"plugins"+File.separator+"error.txt";
                	
                	File file=new File(f);
                	file.deleteOnExit();
                	try {
						file.createNewFile();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                	
                	try {
						PrintStream p=new PrintStream(file);
						error.printStackTrace(p);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	 
                	error.printStackTrace(System.out);
                	//PrintStreamPrinter p=new PrintStreamPrinter(pw);
                	
                	
                	
                	
                    listener.onError();
                }
            });
            */
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
