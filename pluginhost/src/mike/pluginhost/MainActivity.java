package mike.pluginhost;

import java.io.File;

import mike.pluginlib.PluginManager;
import mike.pluginlib.consts.Constants;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends /*Activity*/ ActionBarActivity {

	PluginManager pluginManager;
	Intent intent=new Intent();
    Handler h=new Handler(){
    	
    	
    };
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        PluginManager.init(getApplicationContext());

        pluginManager=PluginManager.getInstance();
        
        //this.getAssets().list(arg0)
        
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                //sdcard0/emulator/myplugin.apk
                //插件路径
                String pluginApkPath= Environment.getExternalStorageDirectory()+ File.separator+"plugins"+File.separator+"myplugin2.apk";
                String pluginClazz="mike.plugin.ui.activity.WeatherActivity";
                String pluginPackage="mike.plugin";
                Log.i("", "### 插件路径 : " + pluginApkPath);

                File file=new File(pluginApkPath);
                if (file.exists()) {
                    Log.d("", "### apk存在");
                } else {
                    Log.d("", "### no apk存在");
                }
                
                PluginManager.getInstance().loadApk(pluginApkPath,MainActivity.this.getResources());
                //PluginManager.getInstance().getPluginApk(pluginApkPath).resources.get
                
                
                intent.putExtra(Constants.PLUGIN_CLASS_NAME,pluginClazz);
                intent.putExtra(Constants.PACKAGE_NAME, pluginPackage);
                try {
                	h.post(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							try{
								//MainActivity.this.getResources().getDrawable(id)
								pluginManager.startActivity(intent);
							}catch(Exception e){
								e.printStackTrace();
							}
						}
                		
                	});
                    
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        },1000);
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
