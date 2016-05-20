package mike.httplib.config;

import org.apache.http.conn.ssl.SSLSocketFactory;

public class HttpClientConfig extends HttpConfig {
	
	private static HttpClientConfig sConfig=new HttpClientConfig();
	
	SSLSocketFactory mSslSocketFactory;
	
	private HttpClientConfig(){
	}
	
	public static HttpClientConfig getConfig() {
        return sConfig;
    }
	
	public void setHttpConfig(SSLSocketFactory sslSocketFactory){
		mSslSocketFactory=sslSocketFactory;
	}
	
	public SSLSocketFactory getSocketFactory() {
        return mSslSocketFactory;
    }

}
