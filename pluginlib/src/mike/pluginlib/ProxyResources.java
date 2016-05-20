package mike.pluginlib;

import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class ProxyResources extends Resources {

	Resources hostResources;
	public ProxyResources(AssetManager assets, DisplayMetrics metrics,
			Configuration config,Resources hostRes) {
		super(assets, metrics, config);
		this.hostResources=hostRes;
		// TODO Auto-generated constructor stub
	}
	@Override
	public void getValue(int id, TypedValue outValue, boolean resolveRefs)
			throws NotFoundException {
		try{
			super.getValue(id, outValue, resolveRefs);
		}catch(NotFoundException e){
			hostResources.getValue(id, outValue, resolveRefs);
		}
		
	}
	@Override
	public void getValue(String name, TypedValue outValue, boolean resolveRefs)
			throws NotFoundException {
		try{
			super.getValue(name, outValue, resolveRefs);
		}catch(NotFoundException e){
			hostResources.getValue(name, outValue, resolveRefs);
		}
		
	}

	
}
