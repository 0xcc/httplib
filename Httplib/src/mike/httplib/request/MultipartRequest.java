package mike.httplib.request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.util.Log;

import mike.httplib.base.Request;
import mike.httplib.base.Response;
import mike.httplib.entity.MultipartEntity;

public class MultipartRequest extends Request<String> {

	MultipartEntity mMultiPartEntity=new MultipartEntity();
	
	public MultipartRequest(HttpMethod method,
			String url,
			RequestListener<String> listener) {
		super(method, url, listener);
		// TODO Auto-generated constructor stub
	}

	public MultipartEntity getMultipartEntity(){
		return mMultiPartEntity;
	}
	
	@Override
    public byte[] getBody() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            // 将MultipartEntity中的参数写入到bos中
            mMultiPartEntity.writeTo(bos);
        } catch (IOException e) {
            Log.e("", "IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }
	
	
	@Override
	public String parseResponse(Response response) {
		 if (response != null && response.getRawData() != null) {
	            return new String(response.getRawData());
	      }

	     return "";
	 }
}
