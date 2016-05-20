package mike.httplib.request;

import mike.httplib.base.Request;
import mike.httplib.base.Response;

public class StringRequest extends Request<String> {
    public StringRequest(HttpMethod method, String url, RequestListener<String> listener) {
        super(method, url, listener);
    }

    @Override
    public String parseResponse(Response response) {
    	byte[] b=response.getRawData();
        return new String(response.getRawData());
    }
}
