package mike.httplib.core;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import org.apache.http.ProtocolVersion;

import android.os.Message;
import android.util.Log;

import mike.httplib.base.Request;
import mike.httplib.base.Response;
import mike.httplib.cache.ICache;
import mike.httplib.cache.LruMemCache;
import mike.httplib.performers.HttpPerformer;

public class NetworkExecutorThread extends Thread {

	private BlockingQueue<Request<?>> mRequestQueue;
	
	private HttpPerformer mHttpPerformer;
	private boolean isStop = false;
	
	private static ResponseDeliver mResponseDelivery = new ResponseDeliver();
	private static ICache<String, Response> mReqCache = new LruMemCache();
	
	RequestQueue.NetworkExecutorHandler msgHandler;
	int threadindex;
	
	public NetworkExecutorThread(BlockingQueue<Request<?>> mRequestQueue,
			HttpPerformer mHttpPerformer,
			RequestQueue.NetworkExecutorHandler handler,
			int threadindex) {
		
		this.mRequestQueue = mRequestQueue;
        this.mHttpPerformer = mHttpPerformer;
        this.msgHandler=handler;
        this.threadindex=threadindex;
	}

	@Override
	public void run() {
		 Response response = null;
		 Request<?> request=null;;
		try{
			
			while(!isStop){
				request=null;;
				 try{
					 request=mRequestQueue.take();
				 }catch(InterruptedException e){
					 continue;
				 }
				   
				 if(request.isCanceled()){
					 Log.d("### ", "### 取消执行了");
					 continue;
				 }
				
				 if(isUseCache(request)){
					 response = mReqCache.get(request.getUrl());
				 }else{
					 response = mHttpPerformer.performRequest(request);
				 }
				 
				 if (request.shouldCache() && isSuccess(response)) {
                     mReqCache.put(request.getUrl(), response);
                 }
				 
				 mResponseDelivery.deliveryResponse(request, response);
			}
		}catch(IOException e){
			//重启线程
			
			boolean isHttps=request.isHttps();
			ProtocolVersion p=new ProtocolVersion(isHttps?"https":"http", 1, 1);
			response=new Response(p,e);
			mResponseDelivery.deliveryResponse(request, response);
			
			if(msgHandler!=null){
				Message msg=Message.obtain();
				msg.arg1=threadindex;
				msg.obj=RequestQueue.NetworkExecutorMessage.ThreadDie;
				msgHandler.sendMessage(msg);
			}
			Log.i("IOException", e.getMessage());
			e.printStackTrace();
			
			//throw new Exception(e);
		
		}catch(Exception e){
			e.printStackTrace();
			//throw e;
		}
	}
	
	 private boolean isSuccess(Response response) {
	        return response != null && response.getStatusCode() == 200;
	    }

	    private boolean isUseCache(Request<?> request) {
	        return request.shouldCache() && mReqCache.get(request.getUrl()) != null;
	    }
	
	  public void quit() {
	        isStop = true;
	        interrupt();
	    }
}
