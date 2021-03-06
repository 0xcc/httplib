package mike.httplib.config;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;



public class HttpUrlConnConfig extends HttpConfig {
	
	private static HttpUrlConnConfig sConfig = new HttpUrlConnConfig();
	
	private SSLSocketFactory mSslSocketFactory = null;
    private HostnameVerifier mHostnameVerifier = null;
    
    private HttpUrlConnConfig(){
    }
    
    public static HttpUrlConnConfig getConfig() {
        return sConfig;
    }
    
    public void setHttpsConfig(SSLSocketFactory sslSocketFactory,
            HostnameVerifier hostnameVerifier) {
        mSslSocketFactory = sslSocketFactory;
        mHostnameVerifier = hostnameVerifier;
    }
    
    public HostnameVerifier getHostnameVerifier() {
        return mHostnameVerifier;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return mSslSocketFactory;
    }

}
