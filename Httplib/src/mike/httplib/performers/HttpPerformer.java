package mike.httplib.performers;

import java.io.IOException;

import mike.httplib.base.Request;
import mike.httplib.base.Response;

public interface HttpPerformer {
	public Response performRequest(Request<?> request) throws IOException;

}
