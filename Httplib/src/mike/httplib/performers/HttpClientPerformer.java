package mike.httplib.performers;

import java.io.IOException;
import java.util.Map;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.net.http.AndroidHttpClient;

import mike.httplib.base.Request;
import mike.httplib.base.Response;
import mike.httplib.config.HttpClientConfig;

public class HttpClientPerformer implements HttpPerformer {
	
	HttpClientConfig mConfig=HttpClientConfig.getConfig();
	
	HttpClient mHttpClient=AndroidHttpClient.newInstance(mConfig.userAgent);
	
	
	
	@Override
	public Response performRequest(Request<?> request) throws IOException {
		
		try{
			HttpUriRequest httpRequest=createHttpRequest(request);
			
			setConnectionParams(httpRequest);
			addHeaders(httpRequest, request.getHeaders());
			
			//是否https 类型
			configHttps(request);
			//走起
			HttpResponse response = mHttpClient.execute(httpRequest);
			/* 重定之类的向不处理
			if(response.getStatusLine().getStatusCode()==301){
				//重定向
				String newUrl= response.getHeaders("Location").toString();
				
				//response=mHttpClient.
			}
			*/
			Response rawResponse=new Response(response.getStatusLine());
			
			rawResponse.setEntity(response.getEntity());
			
			return rawResponse;
			
		}catch(IOException e){
			throw e;
		}catch(Exception e){
			e.printStackTrace();
			//throws IOException, ClientProtocolException
		
		}
		
		return null;
	}
	
	
	private void configHttps(Request<?> request){
		SSLSocketFactory sslSocketFactory=mConfig.getSocketFactory();
		if(request.isHttps()&& sslSocketFactory!=null){
			Scheme sch=new Scheme("https", sslSocketFactory, 443);
			mHttpClient.getConnectionManager().getSchemeRegistry().register(sch);
		}
	}
	
	private static void addHeaders(HttpUriRequest httpRequest, Map<String, String> headers){
		for(String key:headers.keySet()){
			httpRequest.setHeader(key, headers.get(key));
		}
	}
	
	//设置链接超时
	private void setConnectionParams(HttpUriRequest httpUriRequest){
		HttpParams httpParams = httpUriRequest.getParams();
		//socket connect时的 链接超时
		HttpConnectionParams.setConnectionTimeout(httpParams, mConfig.connTimeOut);
		//请求超时
		//ocket receive的 超时 
		HttpConnectionParams.setSoTimeout(httpParams, mConfig.soTimeOut);
	}
	
	static HttpUriRequest createHttpRequest(Request<?> request){
		HttpUriRequest httpUriRequest = null;
		switch(request.getHttpMethod()){
		case GET:
				httpUriRequest=new HttpGet(request.getUrl());
			break;
		case DELETE:
				httpUriRequest=new HttpDelete(request.getUrl());
			break;
		case POST:
				httpUriRequest=new HttpPost(request.getUrl());
				//form 表当形式
				httpUriRequest.addHeader(Request.HEADER_CONTENT_TYPE,request.getBodyContentType());
				setEntityIfNonEmptyBody((HttpPost)httpUriRequest, request);
			break;
		case PUT:
			httpUriRequest = new HttpPut(request.getUrl());
			httpUriRequest.addHeader(Request.HEADER_CONTENT_TYPE, request.getBodyContentType());
			setEntityIfNonEmptyBody((HttpPut) httpUriRequest, request);
			break;
		 default:
             throw new IllegalStateException("Unknown request method.");
		}
		return httpUriRequest;
		
	}
	
	private static void setEntityIfNonEmptyBody(HttpEntityEnclosingRequestBase httpRequest,
            Request<?> request){
		byte[] body=request.getBody();
		
		if(body!=null){
			HttpEntity entity=new ByteArrayEntity(body);
			httpRequest.setEntity(entity);
		}
	}

}
