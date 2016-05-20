package mike.plugin.ui.activity;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import mike.plugin.R;
import mike.plugin.model.entity.Weather;
import mike.plugin.model.entity.WeatherInfo;
import mike.plugin.presenter.IWeatherPresenter;
import mike.plugin.presenter.impl.WeatherPresenterImpl;
import mike.plugin.ui.view.IWeatherView;
import mike.plugin.util.VolleyRequest;
import mike.pluginlib.IAttachable;
import mike.pluginlib.IPluginable;
import mike.pluginlib.PluginActivity;



public class WeatherActivity extends PluginActivity  implements IWeatherView,View.OnClickListener {
    private Dialog loadingDialog;
    private EditText cityNOInput;
    private TextView city;
    private TextView cityNO;
    private TextView temp;
    private TextView wd;
    private TextView ws;
    private TextView sd;
    private TextView wse;
    private TextView time;
    private TextView njd;
    private ImageView imgview2;
    
    private IWeatherPresenter weatherPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        try{
        	setContentView(R.layout.activity_weather);
        	 
        	//VolleyRequest.buildRequest(getApplicationContext());
        }catch(Exception e){
        	e.printStackTrace();
        	//VolleyRequest.buildRequest(this);
        }
        
        
        //VolleyRequest.buildRequest(this);
        init();
    }

    private void init(){
        cityNOInput = (EditText)findViewById(R.id.et_city_no);
        city = findView(R.id.tv_city);
        cityNO = findView(R.id.tv_city_no);
        temp = findView(R.id.tv_temp);
        wd = findView(R.id.tv_WD);
        ws = findView(R.id.tv_WS);
        sd = findView(R.id.tv_SD);
        wse = findView(R.id.tv_WSE);
        time = findView(R.id.tv_time);
        njd = findView(R.id.tv_njd);
        imgview2=(ImageView)findViewById(R.id.imgview2);
        try {
        	Resources r=getResources();
        	
        	AssetManager assetManager=r.getAssets();
			InputStream inputStream= assetManager.open("images/t.png");
			
			Drawable drawable=Drawable.createFromStream(inputStream, "t.png");
			imgview2.setBackground(drawable);
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        findViewById(R.id.btn_go).setOnClickListener(this);

        weatherPresenter=new WeatherPresenterImpl(this);
        Resources res=this.getResources();
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setTitle("ads");
    }

    private  TextView findView(int resid){
        TextView view=(TextView)findViewById(resid);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_go:
            	try{
            		weatherPresenter.getWeather(cityNOInput.getText().toString().trim());
            	}catch(Exception e){
            		e.printStackTrace();
            	}
                break;
        }
    }


    @Override
    public void showLoading() {
        //loadingDialog.show();
    }

    @Override
    public void hideLoading() {
        //loadingDialog.dismiss();
    }

    @Override
    public void showError() {
        //Do something
        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setWeatherInfo(Weather weather) {
        WeatherInfo info = weather.getWeatherInfo();
        city.setText(info.getCity());
        cityNO.setText(info.getCityid());
        temp.setText(info.getTemp());
        wd.setText(info.getWD());
        ws.setText(info.getWS());
        sd.setText(info.getSD());
        wse.setText(info.getWS());
        time.setText(info.getTemp());
        njd.setText(info.getNjd());
    }
}
