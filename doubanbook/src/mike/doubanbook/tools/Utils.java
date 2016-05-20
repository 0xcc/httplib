package mike.doubanbook.tools;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;
import okhttp3.ResponseBody;
import android.content.Context;

public final class Utils {
	
	public static int px2dip(Context context, float pxValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(pxValue / scale +0.5f);
	}

	public static int dip2px(Context context, float pxValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(pxValue * scale +0.5f);
	}  	
	
	public  static String getResponseBody(Response response){
		 ResponseBody responseBody= response.body();
         InputStream inputStream=responseBody.byteStream();
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
		}finally{
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
         return result.toString();    
	}
	
}
