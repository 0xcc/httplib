package mike.httplib.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import mike.httplib.base.Request;
import mike.httplib.performers.HttpPerformer;
import mike.httplib.performers.HttpPerformerFactory;

public class RequestQueue {

	public enum NetworkExecutorMessage{
		ThreadDie;
	}

	public class NetworkExecutorHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			if(msg.obj==NetworkExecutorMessage.ThreadDie){
				int index=msg.arg1;
				if(index<RequestQueue.this.mDispatchers.length){
					RequestQueue.this.reStart(index);
				}
			}
		}
		
	}
	
	NetworkExecutorHandler handler;
	
	private BlockingQueue< Request<?> > mRequestQueue=new PriorityBlockingQueue<Request<?>>();
	private AtomicInteger mSerialNumGenerator = new AtomicInteger(0);
	 
	//默认线程数量
	public static int DEFAULT_CORE_NUMS = Runtime.getRuntime().availableProcessors() + 1;
	
	private int mDispatcherNums = DEFAULT_CORE_NUMS;
	
	private NetworkExecutorThread[] mDispatchers = null;
	
	private HttpPerformer mHttpPerformer;
	
	protected RequestQueue(int coreNums, HttpPerformer httpStack) {
        mDispatcherNums = coreNums;
        mHttpPerformer = httpStack != null ? httpStack : HttpPerformerFactory.createHttpStack();
    }
	
	private final void startNetworkExecutors() {
		if(handler==null){
			handler=new NetworkExecutorHandler();
		}
        mDispatchers = new NetworkExecutorThread[mDispatcherNums];
        for (int i = 0; i < mDispatcherNums; i++) {
            mDispatchers[i] = new NetworkExecutorThread(mRequestQueue, mHttpPerformer,handler,i);
            mDispatchers[i].start();
        }
    }
	
	
	
	//线程死亡，重启
	public void reStart(int i){
		if(i<mDispatchers.length){
			mDispatchers[i] = new NetworkExecutorThread(mRequestQueue,mHttpPerformer,handler,i);
			mDispatchers[i].start();
		}
	}
	
	 public void start() {
	        stop();
	        startNetworkExecutors();
	 }
	 
	  public void stop() {
	        if (mDispatchers != null && mDispatchers.length > 0) {
	            for (int i = 0; i < mDispatchers.length; i++) {
	                mDispatchers[i].quit();
	            }
	        }
	   }
	 
	  
	  public void addRequest(Request<?> request) {
	        if (!mRequestQueue.contains(request)) {
	            request.setSerialNumber(this.generateSerialNumber());
	            mRequestQueue.add(request);
	        } else {
	            Log.d("", "### 请求队列中已经含有");
	        }
	    }

	    public void clear() {
	        mRequestQueue.clear();
	    }

	    public BlockingQueue<Request<?>> getAllRequests() {
	        return mRequestQueue;
	    }

	    private int generateSerialNumber() {
	        return mSerialNumGenerator.incrementAndGet();
	    }	
}
