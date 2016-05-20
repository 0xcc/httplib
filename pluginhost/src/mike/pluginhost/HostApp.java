package mike.pluginhost;



import android.app.Application;

public class HostApp extends Application implements Thread.UncaughtExceptionHandler  {
	
	@Override
    public void onCreate() {
        super.onCreate();
        
        Thread.currentThread().setUncaughtExceptionHandler(this);
    }
	
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
    }
}
