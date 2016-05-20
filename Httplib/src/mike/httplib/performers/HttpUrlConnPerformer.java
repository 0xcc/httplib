package mike.httplib.performers;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;



import mike.httplib.base.Request;
import mike.httplib.base.Request.HttpMethod;
import mike.httplib.base.Response;
import mike.httplib.config.HttpUrlConnConfig;

public class HttpUrlConnPerformer implements HttpPerformer {
	
	HttpUrlConnConfig mConfig = HttpUrlConnConfig.getConfig();

	@Override
	public Response performRequest(Request<?> request) throws IOException {
		
		HttpURLConnection urlConnection = null;
		
		try{
			urlConnection = createUrlConnection(request.getUrl());
			// 设置headers
            setRequestHeaders(urlConnection, request);
            
         // 设置Body参数
            setRequestParams(urlConnection, request);
            
         // https 配置
            configHttps(request);
            return fetchResponse(urlConnection);
		}catch(IOException e){
			throw e;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(urlConnection!=null){
				urlConnection.disconnect();
			}
		}
		return null;
	}
	
    private void configHttps(Request<?> request) {
        if (request.isHttps()) {
            SSLSocketFactory sslFactory = mConfig.getSslSocketFactory();
            // 配置https
            if (sslFactory != null) {
                HttpsURLConnection.setDefaultSSLSocketFactory(sslFactory);
                HttpsURLConnection.setDefaultHostnameVerifier(mConfig.getHostnameVerifier());
            }

        }
    }
	
	protected void setRequestParams(HttpURLConnection connection, Request<?> request)
			throws ProtocolException, IOException{
		
		HttpMethod method=request.getHttpMethod();
		connection.setRequestMethod(method.toString());
		
		byte[] body=request.getBody();
		 
		if(body!=null ){
			connection.setDoInput(true);
			connection.addRequestProperty(Request.HEADER_CONTENT_TYPE, request.getBodyContentType());
			DataOutputStream dataOutputStream=new DataOutputStream(connection.getOutputStream());
			dataOutputStream.write(body);
			dataOutputStream.close();
		}
		
	}
	
	private void setRequestHeaders(HttpURLConnection connection, Request<?> request){
		Set<String> headersKeys=request.getHeaders().keySet();
		for(String name:headersKeys){
			connection.addRequestProperty(name, request.getHeaders().get(name));
		}
		
	}
	
	//没有设置代理。。
	private HttpURLConnection createUrlConnection(String url) throws IOException {
		URL newUrl=new URL(url);
		URLConnection urlConnection=newUrl.openConnection();
		
		urlConnection.setConnectTimeout(mConfig.connTimeOut);
		urlConnection.setReadTimeout(mConfig.soTimeOut);
		//必须读取数据
		urlConnection.setDoInput(true);
		urlConnection.setUseCaches(false);
		return (HttpURLConnection) urlConnection;
		
	}
	
	private Response fetchResponse(HttpURLConnection connection) throws IOException{
		
		ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
		int responseCode = connection.getResponseCode();
		if (responseCode == -1) {
            throw new IOException("Could not retrieve response code from HttpUrlConnection.");
        }
		 
		// 状态行数据
        StatusLine responseStatus = new BasicStatusLine(protocolVersion,
                connection.getResponseCode(), connection.getResponseMessage());
        Response response = new Response(responseStatus);
        
        //设置response body
        response.setEntity(entityFromURLConnwction(connection));
        
        //设置response headers
        addHeadersToResponse(response, connection);
        return response;
	}
	
	private HttpEntity entityFromURLConnwction(HttpURLConnection connection) {
        BasicHttpEntity entity = new BasicHttpEntity();
        InputStream inputStream = null;
        try {
            inputStream = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            inputStream = connection.getErrorStream();
        }

        // TODO : GZIP 
        entity.setContent(inputStream);
        entity.setContentLength(connection.getContentLength());
        entity.setContentEncoding(connection.getContentEncoding());
        entity.setContentType(connection.getContentType());
        
        return entity;
    }
	
	private void addHeadersToResponse(BasicHttpResponse response, HttpURLConnection connection) {
        for (Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
            if (header.getKey() != null) {
                Header h = new BasicHeader(header.getKey(), header.getValue().get(0));
                response.addHeader(h);
            }
        }
    }

}
