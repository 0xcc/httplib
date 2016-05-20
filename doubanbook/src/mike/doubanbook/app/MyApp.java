package mike.doubanbook.app;

import android.app.Application;

 

public class MyApp extends Application implements Thread.UncaughtExceptionHandler {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Thread.currentThread().setUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread arg0, Throwable arg1) {
		// TODO Auto-generated method stub
		arg1.printStackTrace();
		
	}
	
	

}
