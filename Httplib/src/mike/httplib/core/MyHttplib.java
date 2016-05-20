package mike.httplib.core;

import mike.httplib.performers.HttpPerformer;

public class MyHttplib {
	
	public static RequestQueue newRequestQueue() {
        return newRequestQueue(RequestQueue.DEFAULT_CORE_NUMS);
    }
	public static RequestQueue newRequestQueue(int coreNums) {
        return newRequestQueue(coreNums, null);
    }
	
	public static RequestQueue newRequestQueue(int coreNums, HttpPerformer httpStack) {
        RequestQueue queue = new RequestQueue(Math.max(0, coreNums), httpStack);
        queue.start();
        return queue;
    }
}
