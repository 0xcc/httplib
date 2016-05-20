package mike.httplib.base;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;

public class Response extends BasicHttpResponse {

	private Exception mError;
	
	public byte[] rawData=new byte[0];
	
	public Response(StatusLine statusLine){
		super(statusLine);
	}
	
	public Response(ProtocolVersion protocol,Exception e){
		//ProtocolVersion protocolVersion=new ProtocolVersion("http", major, minor)
		super(protocol, 1, e.getMessage());
		mError=e;
	}
	
	
	public Response(ProtocolVersion ver, int code, String reason) {
		super(ver, code, reason);
	}
	
	public byte[] getRawData(){
		return rawData;
	}
	
	 @Override
	    public void setEntity(HttpEntity entity) {
	        super.setEntity(entity);
	        rawData = entityToBytes(getEntity());
	    }
	
	
	public int getStatusCode(){
		return getStatusLine().getStatusCode();
	}
	
	public String getMessage(){
		return  getStatusLine().getReasonPhrase();
	}
	
	private byte[] entityToBytes(HttpEntity entity){
		try{
			return EntityUtils.toByteArray(entity);
		}catch(IOException e){
			e.printStackTrace();
		}
		return new byte[0];
	}
	
	public boolean isInnerError(){
		return mError==null? false:true;
	}
	
	public Exception getException(){
		return mError;
	}
}
