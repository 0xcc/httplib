package mike.httplib.performers;

import android.os.Build;

public class HttpPerformerFactory {
	
	private static final int GINGERBREAD_SDK_NUM = 9;
	public static HttpPerformer createHttpStack() {
        int runtimeSDKApi = Build.VERSION.SDK_INT;
        if (runtimeSDKApi >= GINGERBREAD_SDK_NUM) {
            return new HttpUrlConnPerformer();
        }

        return new HttpClientPerformer();
    }
}
