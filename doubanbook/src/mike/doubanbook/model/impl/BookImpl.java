package mike.doubanbook.model.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;

import mike.doubanbook.model.BookMDL;
import mike.doubanbook.model.interfaces.IBook;
import mike.doubanbook.presenter.interfaces.ICompleteListener;
import mike.doubanbook.tools.Utils;

public class BookImpl implements IBook {

	OkHttpClient okClient=new OkHttpClient();
	  
	
	
	@Override
	public void loadBookByIsbn(String isbn,final ICompleteListener<BookMDL> listener) {
		
		Request.Builder builder=new Builder();
		String url=DoubanApis.getBookIsbnApiUrl(isbn);
		builder.url(url);
		  
		CacheControl cacheControl=new CacheControl.Builder().noCache().build();
		builder.cacheControl(cacheControl);
		
		final Request request=builder.build();
		
		
		final Handler h=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				 Response response=(Response)msg.obj;
				 if(response.isSuccessful()){
					 String result= Utils.getResponseBody(response);
					 Gson gson=new Gson();
					 JsonReader reader = new JsonReader(new StringReader(result));
					 try{
						
						 //JsonElement jsonElement= {
						 reader.setLenient(true);
						 
						 BookMDL mdl=(BookMDL)gson.fromJson(reader, BookMDL.class);
						 listener.onSuccess(mdl);
					 }catch(Exception e){
						 e.printStackTrace();
					 }finally{
						 try {
							reader.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					 }
				 }else{
					 listener.onError(response.message());
				 }
				 
			}
			
		};
		
		Runnable r=new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Response response= okClient.newCall(request).execute();
					Message msg=Message.obtain();
					msg.obj=response;
					h.sendMessage(msg);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		new Thread(r).start();
		
	}

	@Override
	public void searchByTitle(String title,ICompleteListener< List<BookMDL>> listener) {
		// TODO Auto-generated method stub
		 
	}

	@Override
	public void searchByAuthor(String author,ICompleteListener< List<BookMDL> > listener) {
		 
	}
	

}
