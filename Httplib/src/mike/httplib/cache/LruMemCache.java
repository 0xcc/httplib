package mike.httplib.cache;

import android.support.v4.util.LruCache;
import mike.httplib.base.Response;

public class LruMemCache implements ICache<String, Response>  {
	
	private LruCache<String,Response> mResponseCache;
	
	public LruMemCache(){
		// 计算可使用的最大内存
		//maxMemory 返回总内存字节数量
		//maxMemroy/1024 = k
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		
		//cacheSize M 为单位 
		final int cacheSize = maxMemory / 8;
		
		mResponseCache = new LruCache<String, Response>(cacheSize) {
            @Override
            protected int sizeOf(String key, Response response) {
                return response.rawData.length / 1024;
            }
        };
	
	}

	@Override
	public Response get(String key) {
		return mResponseCache.get(key);
	}

	@Override
	public void put(String key, Response value) {
		mResponseCache.put(key, value);
		
	}

	@Override
	public void remove(String key) {
		mResponseCache.remove(key);		
	}

}
