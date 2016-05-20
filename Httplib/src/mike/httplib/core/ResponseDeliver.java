package mike.httplib.core;

import java.util.concurrent.Executor;

import mike.httplib.base.Request;
import mike.httplib.base.Response;

import android.os.Handler;
import android.os.Looper;

public class ResponseDeliver implements Executor{
	
	//���̴߳���
	Handler handler=new Handler(Looper.getMainLooper());
	
	public void deliveryResponse(final Request<?> request, final Response response)  {
        Runnable respRunnable = new Runnable() {

            @Override
            public void run()  {
            	//���߳���ִ����
            	request.deliverResponse(response);
               // request.deliveryResponse(response);
            }
        };

        execute(respRunnable);
    }

	@Override
	public void execute(Runnable command) {
		
		handler.post(command);
	}
	

}
