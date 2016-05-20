package mike.httplib.base;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.utils.URLEncodedUtils;

import android.R.menu;

public abstract class Request<T> implements Comparable< Request<T> > {
	
	public static interface RequestListener<T>{
		public void onComplete(int stCode, T response, String errMsg);
		public void onRequestInnerError(Exception e);
	}
	
	public static enum HttpMethod{
		GET("GET"),
		POST("POST"),
		PUT("PUT"),
		DELETE("DELETE");
		
		private String mHttpMethod = "";
		private HttpMethod(String method){
			this.mHttpMethod=method;
		} 
		
		@Override
		public String toString(){
			return mHttpMethod;
		}
	}
	
	public static enum Priority {
        LOW,
        NORMAL,
        HIGN,
        IMMEDIATE
    }
	
	public static final String DEFAULT_PARAMS_ENCODING="UTF-8";
	
	public static String HEADER_CONTENT_TYPE="Content-Type";
	
	protected int mSerialNum=0;
	
	protected Priority mPriority=Priority.NORMAL;
	
	protected boolean isCancel=false;
	
	protected boolean mShouldCache=true;
	
	protected RequestListener<T> mRequestListener;
	
	private String mUrl;
	
	HttpMethod mHttpMethod=HttpMethod.GET;
	
	private Map<String, String> mHeaders=new HashMap<String, String>();
	
	private Map<String,String> mBodyParams=new HashMap<String, String>();
	
	
	public Request(HttpMethod method,String url,RequestListener<T> listener){
		this.mHttpMethod=method;
		this.mUrl=url;
		this.mRequestListener=listener;
	}
	
	public void addHeader(String name,String value){
		mHeaders.put(name, value);
	}
	
	public abstract T parseResponse(Response response);
	
	
	public final void deliverResponse(Response response){
		if(response.isInnerError()){
			if(mRequestListener!=null){
				mRequestListener.onRequestInnerError(response.getException());
			}
			return ;
		}
		
		T result=parseResponse(response);
		if(mRequestListener!=null){
			int stCode= (response!=null) ? response.getStatusCode():-1;
			String msg= (response!=null) ? response.getMessage(): "unknow error";
			mRequestListener.onComplete(stCode, result, msg);
		}
	}
	
	public String getUrl() {
        return mUrl;
    }

    public RequestListener<T> getRequestListener() {
        return mRequestListener;
    }

    public int getSerialNumber() {
        return mSerialNum;
    }

    public void setSerialNumber(int mSerialNum) {
        this.mSerialNum = mSerialNum;
    }

    public Priority getPriority() {
        return mPriority;
    }

    public void setPriority(Priority mPriority) {
        this.mPriority = mPriority;
    }
    
    protected String getParamsEncoding(){
    	return DEFAULT_PARAMS_ENCODING;
    }
    
    public String getBodyContentType() {
        return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
    }
    
    public HttpMethod getHttpMethod() {
        return mHttpMethod;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    public Map<String, String> getParams() {
        return mBodyParams;
    }

    
    public boolean isHttps() {
        return mUrl.startsWith("https");
    }
    
    
    public void setShouldCache(boolean shouldCache) {
        this.mShouldCache = shouldCache;
    }

    public boolean shouldCache() {
        return mShouldCache;
    }
    
    public void cancel() {
        isCancel = true;
    }

    public boolean isCanceled() {
        return isCancel;
    }
    
    //参数 合成  name=value 的形式
    public byte[] getBody(){
    	Map<String, String> params = getParams();
    	
    	if(params!=null && params.size()>0){
    		return encodeParameters(params, getParamsEncoding());
    	}
    	return null;
    }
    
    private byte[] encodeParameters(Map<String, String> params, String paramsEncoding){
    	StringBuilder encodedParams = new StringBuilder();
    	
    	try{
    		for(Map.Entry<String, String> entry: params.entrySet()){
    			encodedParams.append(URLEncoder.encode(entry.getKey(),paramsEncoding));
    			encodedParams.append("=");
    			encodedParams.append(URLEncoder.encode(entry.getValue(),paramsEncoding));
    		}
    		return encodedParams.toString().getBytes();
    	}catch(UnsupportedEncodingException e){
    		throw new RuntimeException("Encoding not supported: " + paramsEncoding, e);
    	}
    }
	

	@Override
	public int compareTo(Request<T> another) {
		Priority myPriority = this.getPriority();
        Priority anotherPriority = another.getPriority();
        // 如果优先级相等,那么按照添加到队列的序列号顺序来执行
        return myPriority.equals(anotherPriority) ? this.getSerialNumber()
                - another.getSerialNumber()
                : myPriority.ordinal() - anotherPriority.ordinal();
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mHeaders == null) ? 0 : mHeaders.hashCode());
        result = prime * result + ((mHttpMethod == null) ? 0 : mHttpMethod.hashCode());
        result = prime * result + ((mBodyParams == null) ? 0 : mBodyParams.hashCode());
        result = prime * result + ((mPriority == null) ? 0 : mPriority.hashCode());
        result = prime * result + (mShouldCache ? 1231 : 1237);
        result = prime * result + ((mUrl == null) ? 0 : mUrl.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Request<?> other = (Request<?>) obj;
        if (mHeaders == null) {
            if (other.mHeaders != null)
                return false;
        } else if (!mHeaders.equals(other.mHeaders))
            return false;
        if (mHttpMethod != other.mHttpMethod)
            return false;
        if (mBodyParams == null) {
            if (other.mBodyParams != null)
                return false;
        } else if (!mBodyParams.equals(other.mBodyParams))
            return false;
        if (mPriority != other.mPriority)
            return false;
        if (mShouldCache != other.mShouldCache)
            return false;
        if (mUrl == null) {
            if (other.mUrl != null)
                return false;
        } else if (!mUrl.equals(other.mUrl))
            return false;
        return true;
    }
	
	
}
