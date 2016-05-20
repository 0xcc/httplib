package mike.httplib.request;


import mike.httplib.base.Request;
import mike.httplib.base.Response;

import org.json.JSONObject;

public class JsonRequest extends Request<JSONObject> {

	public JsonRequest(HttpMethod method, String url,
			mike.httplib.base.Request.RequestListener<JSONObject> listener) {
		super(method, url, listener);
	}

	@Override
	public JSONObject parseResponse(Response response) {
		String jsonString =new String(response.getRawData());
		try{
			return new JSONObject(jsonString);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
